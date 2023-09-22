package com.board.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.board.common.ResourceAndOriginName;
import com.board.dto.file.FileInfoDTO;
import com.board.dto.file.InsFileInfoDTO;
import com.board.enums.FileInfoParentTypeEnum;
import com.board.enums.FileStatusEnum;
import com.board.exception.FileSystemException;
import com.board.exception.NoDataFoundException;
import com.board.mapper.file.FileMapper;
import com.board.util.FileNamer;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {
    private final FileMapper fileMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private void checkIfFileFound(int fileInfoNo, FileInfoDTO fileInfo) {
        // 파일의 유효성 체크
        if (fileInfo == null) {
            throw new NoDataFoundException("file info no " + fileInfoNo + " 가 존재하지 않습니다.");
        }
    }

    private void initDirectory(Path dirPath) {
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new FileSystemException("디렉토리(" + dirPath.toString() + ") 생성 실패 ", e);
        }
    }

    @Transactional
    @Override
    public Integer insFile(MultipartFile file, int memberNo) {
        // 파일 저장
        if (file == null || file.isEmpty()) {
            throw new FileSystemException("빈 파일입니다.");
        }

        // 파일 확장자 파싱 & DB 저장용 파일 이름 생성
        String extension = FileNamer.parseExtension(file.getOriginalFilename());
        String fileName = FileNamer.retvRandomFileName();
        String originName = FileNamer.rmExtension(file.getOriginalFilename());

        // DB에 file info 임시 저장
        InsFileInfoDTO insFileInfoDTO = InsFileInfoDTO.builder()
                .originName(originName)
                .saveName(fileName)
                .savePath(uploadPath)
                .extension(extension)
                .size(file.getSize())
                .parentNo(null)
                .parentType(null)
                .authorNo(memberNo)
                .status(FileStatusEnum.TEMP)
                .build();

        fileMapper.insFileInfo(insFileInfoDTO);

        // 파일을 서버의 파일 시스템에 저장
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            initDirectory(root);
        }

        try {
            @Cleanup
            InputStream inputStream = file.getInputStream();

            Files.copy(inputStream, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioe) {
            throw new FileSystemException("파일 업로드 실패", ioe);
        }

        return insFileInfoDTO.getNo();
    }

    @Override
    public ResourceAndOriginName loadAsResource(int fileInfoNo) {
        // 필요한 정보 가져오기
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);

        // 파일 유효성 확인
        checkIfFileFound(fileInfoNo, fileInfo);

        if (fileInfo.getParentCnt() != 1) {
            throw new NoDataFoundException("부모가 존재하지 않습니다. fileInfo : " + fileInfo.toString());
        }

        String saveName = fileInfo.getSaveName(); // 파일 시스템에 저장된 이름
        String originName = fileInfo.getOriginName(); // 사용자가 업로드할 때 설정해둔 이름
        String ext = fileInfo.getExtension(); // 확장자명
        String originNameWithExtension = originName + "." + ext;
        String parentNoStr = fileInfo.getParentNo().toString();

        // 파일 시스템에 저장된 최종 경로
        Path filePath;
        if (parentNoStr == null) {
            filePath = Paths.get(uploadPath).resolve(saveName);
        } else {
            filePath = Paths.get(uploadPath).resolve(parentNoStr).resolve(saveName);
        }

        // Resource 가져와서 반환
        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new Exception("Resource가 존재하지 않습니다.");
            } else if (!resource.isReadable()) {
                throw new Exception("Resource를 읽어올 수 없습니다.");
            }

            return new ResourceAndOriginName(resource, originNameWithExtension);

        } catch (Exception e) {
            throw new FileSystemException("파일을 불러오는 데 실패했습니다.", e);
        }
    }

    @Transactional
    @Override
    public void deleteFileInfo(int fileInfoNo) {
        // file info 삭제
        fileMapper.deleteFileInfo(fileInfoNo);
    }

    @Transactional
    @Override
    public void deleteFile(int fileInfoNo) {
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);

        // 파일 유효성 확인
        checkIfFileFound(fileInfoNo, fileInfo);

        // 해당 file의 savePath 불러오기
        String savePath = fileInfo.getSavePath();
        String saveName = fileInfo.getSaveName();
        Path finalPath = Paths.get(savePath).resolve(saveName);

        // DB의 file info 삭제
        this.deleteFileInfo(fileInfoNo);

        // 파일 자체 삭제
        try {
            Files.delete(finalPath);
        } catch (IOException ioe) {
            throw new FileSystemException("파일 삭제 실패", ioe);
        }
    }

    @Transactional
    @Override
    public void changeFileStatus(int fileNo, String fileName, int parentNo,
            FileInfoParentTypeEnum fileInfoParentType) {

        // {uploadPath}/{parentNo}
        Path newPath = Paths.get(uploadPath).resolve(Integer.toString(parentNo));
        // file info의 정보 수정.
        fileMapper.changeFileStatus(fileNo, newPath.toString(), parentNo, fileInfoParentType);
    }

    @Override
    public void moveFile(String src, String dst, String fileName) throws IOException {
        // src 또는 dst가 null 이면 그냥 uploadPath 값을 사용한다
        Path srcPath = src == null ? Paths.get(uploadPath) : Paths.get(uploadPath).resolve(src);
        Path dstPath = dst == null ? Paths.get(uploadPath) : Paths.get(uploadPath).resolve(dst);

        // dstPath 가 존재하지 않으면 만들어준다
        if (!Files.exists(dstPath)) {
            initDirectory(dstPath);
        }

        // 만약 옮길 파일이 존재하지 않으면 함수 종료
        if (!Files.exists(srcPath.resolve(fileName))) {
            return;
        }

        // 파일 옮기기
        Files.move(srcPath.resolve(fileName), dstPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void recursiveDelete(String target) throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).resolve(target));
    }

}
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
import com.board.exception.FileUploadFailureException;
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

    private void initDirectory(Path dirPath) {
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new FileUploadFailureException("Could not create upload folder!", e);
        }
    }

    @Transactional
    @Override
    public Integer insFile(MultipartFile file, Integer memberNo) throws Exception {
        // 파일 저장
        if (file.isEmpty()) {
            throw new FileUploadFailureException("ERROR : File is empty.");
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

        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            initDirectory(root);
        }

        @Cleanup
        InputStream inputStream = file.getInputStream();

        Files.copy(inputStream, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return insFileInfoDTO.getNo();
    }

    @Override
    public boolean isDeleted(Integer fileInfoNo) {
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);

        if (fileInfo == null) {
            return true;
        } else if (fileInfo.getParentType() == FileInfoParentTypeEnum.POST) {
            // 부모가 삭제되었는지 확인
            return fileMapper.isParentPostDeleted(fileInfoNo);
        }

        return false;
    }

    @Override
    public ResourceAndOriginName loadAsResource(Integer fileInfoNo) throws Exception {
        // 필요한 정보 가져오기
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);
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
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return new ResourceAndOriginName(resource, originNameWithExtension);
        } else {
            throw new Exception();
        }
    }

    @Transactional
    @Override
    public void deleteFileInfo(Integer fileInfoNo) {
        // file info 삭제
        fileMapper.deleteFileInfo(fileInfoNo);
    }

    @Transactional
    @Override
    public void deleteFile(Integer fileInfoNo) throws Exception {
        // 해당 file의 savePath 불러오기
        String savePath = this.selectFileSavePath(fileInfoNo);
        String saveName = this.selectFileSaveName(fileInfoNo);
        Path finalPath = Paths.get(savePath).resolve(saveName);

        // DB의 file info 삭제
        this.deleteFileInfo(fileInfoNo);

        // 파일 자체 삭제
        try {
            Files.delete(finalPath);
        } catch (IOException ioe) {
            throw new RuntimeException("file delete failed", ioe);
        }
    }

    @Transactional
    @Override
    public void changeFileStatus(Integer fileNo, String fileName, Integer parentNo,
            FileInfoParentTypeEnum fileInfoParentType) {

        // {uploadPath}/{parentNo}
        Path newPath = Paths.get(uploadPath).resolve(parentNo.toString());
        // file info의 정보 수정.
        fileMapper.changeFileStatus(fileNo, newPath.toString(), parentNo, fileInfoParentType);
    }

    @Transactional
    @Override
    public String selectFileSaveName(Integer fileInfoNo) {
        return fileMapper.selectOne(fileInfoNo).getSaveName();
    }

    @Transactional
    @Override
    public String selectFileSavePath(Integer fileInfoNo) {
        return fileMapper.selectOne(fileInfoNo).getSavePath();
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
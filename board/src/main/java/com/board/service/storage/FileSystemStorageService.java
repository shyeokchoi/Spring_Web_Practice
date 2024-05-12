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
        if (file == null || file.isEmpty()) {
            throw new FileSystemException("빈 파일입니다.");
        }

        String extension = FileNamer.parseExtension(file.getOriginalFilename());
        String fileName = FileNamer.retvRandomFileName();
        String originName = FileNamer.rmExtension(file.getOriginalFilename());

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
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);
        checkIfFileFound(fileInfoNo, fileInfo);

        if (fileInfo.getParentCnt() != 1) {
            throw new NoDataFoundException("부모가 존재하지 않습니다. fileInfo : " + fileInfo.toString());
        }
        String saveName = fileInfo.getSaveName(); // 파일 시스템에 저장된 이름
        String originName = fileInfo.getOriginName(); // 사용자가 업로드할 때 설정해둔 이름
        String ext = fileInfo.getExtension(); // 확장자명
        String originNameWithExtension = originName + "." + ext;
        String parentNoStr = fileInfo.getParentNo().toString();

        Path filePath = Paths.get(uploadPath).resolve(parentNoStr).resolve(saveName);

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
        fileMapper.deleteFileInfo(fileInfoNo);
    }

    @Transactional
    @Override
    public void deleteFile(int fileInfoNo) {
        FileInfoDTO fileInfo = fileMapper.selectOne(fileInfoNo);
        checkIfFileFound(fileInfoNo, fileInfo);

        String savePath = fileInfo.getSavePath();
        String saveName = fileInfo.getSaveName();
        Path finalPath = Paths.get(savePath).resolve(saveName);
        deleteFileInfo(fileInfoNo);

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

        Path newPath = Paths.get(uploadPath).resolve(Integer.toString(parentNo));
        fileMapper.changeFileStatus(fileNo, newPath.toString(), parentNo, fileInfoParentType);
    }

    @Override
    public void moveFile(String src, String dst, String fileName) throws IOException {
        Path srcPath = src == null ? Paths.get(uploadPath) : Paths.get(uploadPath).resolve(src);
        Path dstPath = dst == null ? Paths.get(uploadPath) : Paths.get(uploadPath).resolve(dst);

        if (!Files.exists(srcPath.resolve(fileName))) {
            return;
        }
        if (!Files.exists(dstPath)) {
            initDirectory(dstPath);
        }
        Files.move(srcPath.resolve(fileName), dstPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void recursiveDelete(String target) throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).resolve(target));
    }
}
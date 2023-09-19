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
        // 파일 확장자 파싱 & DB 저장용 파일 이름 생성
        String extension = FileNamer.parseExtension(file.getOriginalFilename());
        String fileName = FileNamer.retvRandomFileName(extension);

        // DB에 file info 임시 저장
        InsFileInfoDTO insFileInfoDTO = InsFileInfoDTO.builder()
                .originName(file.getOriginalFilename())
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

        // 파일 저장
        if (file.isEmpty()) {
            throw new Exception("ERROR : File is empty.");
        }
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
    public Resource loadAsResource(Integer fileInfoNo) throws Exception {
        String fileName = fileMapper.selectFileSaveName(fileInfoNo);

        Path filePath = Paths.get(uploadPath).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
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
        Files.delete(finalPath);
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
        return fileMapper.selectFileSaveName(fileInfoNo);
    }

    @Transactional
    @Override
    public String selectFileSavePath(Integer fileInfoNo) {
        return fileMapper.selectFileSavePath(fileInfoNo);
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

        // 파일 옮기기
        Files.move(srcPath.resolve(fileName), dstPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void recursiveDelete(String target) throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).resolve(target));
    }

}
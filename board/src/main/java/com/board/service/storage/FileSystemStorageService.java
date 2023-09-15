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
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.file.InsFileInfoDTO;
import com.board.enums.FileStatusEnum;
import com.board.exception.FileUploadFailureException;
import com.board.mapper.file.FileMapper;
import com.board.util.FileNamer;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FileSystemStorageService implements StorageService {
    private final FileMapper fileMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void initDirectory() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new FileUploadFailureException("Could not create upload folder!");
        }
    }

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
            initDirectory();
        }

        @Cleanup
        InputStream inputStream = file.getInputStream();

        Files.copy(inputStream, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return insFileInfoDTO.getNo();
    }

    private Path load(String fileName) {
        return Paths.get(uploadPath).resolve(fileName);
    }

    @Override
    public Resource loadAsResource(Integer fileInfoNo) throws Exception {
        String fileName = fileMapper.selectFileSaveName(fileInfoNo);

        Path filePath = load(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new Exception();
        }
    }

    @Override
    public void deleteFile(Integer fileInfoNo) throws Exception {
        // file name 받아오기
        String fileName = fileMapper.selectFileSaveName(fileInfoNo);

        // file info 삭제
        fileMapper.deleteFileInfo(fileInfoNo);

        // 파일 삭제
        Path root = Paths.get(uploadPath);
        Files.delete(root.resolve(fileName));

    }

}
package com.board.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.file.InsFileInfoDTO;
import com.board.exception.FileUploadFailureException;
import com.board.mapper.file.FileMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

    private String parseExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return fileName.substring(lastIndex + 1);
    }

    @Override
    public void store(Integer parentNo, Integer currMemberNo, MultipartFile file) {
        // 파일 확장자 파싱 & DB 저장용 파일 이름 생성
        String extension = parseExtension(file.getOriginalFilename());
        String saveName = UUID.randomUUID().toString() + "." + extension;

        // 파일 저장
        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR : File is empty.");
            }
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                initDirectory();
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, root.resolve(saveName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new FileUploadFailureException("File copy failed");
            }
        } catch (Exception e) {
            throw new FileUploadFailureException("Could not store the file. Error: " + e.getMessage());
        }

        // DB에 file info 저장
        InsFileInfoDTO insFileInfoDTO = InsFileInfoDTO.builder()
                .originName(file.getOriginalFilename())
                .saveName(saveName)
                .savePath("/Users/wm-id002599/web_study_files")
                .extension(extension)
                .size((int) file.getSize()) // long을 int로 캐스팅해도 됨. 어차피 사이즈 5MB 제한.
                .parentNo(parentNo)
                .authorNo(currMemberNo)
                .build();

        fileMapper.insFileInfo(insFileInfoDTO);
    }

    private Path load(String filename) {
        return Paths.get(uploadPath).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileUploadFailureException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileUploadFailureException("Could not read file: " + filename);
        }
    }
}
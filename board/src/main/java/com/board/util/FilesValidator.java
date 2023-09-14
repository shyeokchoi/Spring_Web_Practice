package com.board.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.board.exception.InvalidFileUploadException;

@Component
public class FilesValidator {
    public void validateMultiparFileArr(MultipartFile[] files) {
        // 아무것도 업로드 하지 않은 경우
        if (files == null) {
            throw new InvalidFileUploadException("파일을 적어도 하나 업로드 해주세요.");
        }
        // 파일은 한 번에 최대 10개까지만 허용한다.
        if (files.length > 10) {
            throw new InvalidFileUploadException("파일이 너무 많습니다. 최대 10개까지만 허용합니다.");
        }

        // 화이트리스트로 허용된 확장자인지 확인
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                throw new InvalidFileUploadException("파일 이름을 읽어들일 수 없습니다 : null");
            }

            if (!(fileName.endsWith(".png") || fileName.endsWith(".mov") || fileName.endsWith(".docx"))) {
                throw new InvalidFileUploadException("파일 확장자는 .png, .mov, .docx 만 허용합니다. 현재 파일명: " + fileName);
            }
        }
    }

}

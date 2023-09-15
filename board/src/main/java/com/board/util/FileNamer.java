package com.board.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class FileNamer {
    public String parseExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return fileName.substring(lastIndex + 1);
    }

    public String nameFile(String originName) {
        String extension = parseExtension(originName);
        return UUID.randomUUID().toString() + "." + extension;
    }
}

package com.board.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class FileNamer {
    public static String parseExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return fileName.substring(lastIndex + 1);
    }

    public static String retvRandomFileName() {
        return UUID.randomUUID().toString();
    }

    public static String rmExtension(String originalFileName) {
        int lastIndex = originalFileName.lastIndexOf('.');
        return originalFileName.substring(0, lastIndex);
    }
}

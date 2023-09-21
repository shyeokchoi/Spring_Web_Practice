package com.board.exception;

public class FileSystemException extends RuntimeException {
    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSystemException(String message) {
        super(message);
    }
}

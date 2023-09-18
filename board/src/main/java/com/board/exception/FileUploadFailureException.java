package com.board.exception;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

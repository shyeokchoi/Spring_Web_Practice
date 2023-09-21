package com.board.exception;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadFailureException(String message) {
        super(message);
    }
}

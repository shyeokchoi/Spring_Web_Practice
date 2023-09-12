package com.board.exception;

public class NotSignedInException extends RuntimeException {
    public NotSignedInException(String message) {
        super(message);
    }
}

package com.board.exception;

public class AlreadySignedOutException extends RuntimeException {
    public AlreadySignedOutException(String message) {
        super(message);
    }
}

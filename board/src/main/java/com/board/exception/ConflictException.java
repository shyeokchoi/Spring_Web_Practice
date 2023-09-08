package com.board.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String conflictingFieldName) {
        super("중복: " + conflictingFieldName);
    }

}

package com.board.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String conflictingFieldName) {
        super("입력값이 중복됩니다: " + conflictingFieldName);
    }

}

package com.board.exception;

/**
 * 이미 존재하는 이메일로 가입 시도
 */
public class EmailConflictException extends ConflictException {
    public EmailConflictException() {
        super("중복된 이메일입니다.");
    }
}

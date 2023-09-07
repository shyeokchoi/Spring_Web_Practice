package com.board.exception;

/**
 * 이미 존재하는 아이디로 가입 시도
 */
public class IdConflictException extends ConflictException {
    public IdConflictException() {
        super("중복된 아이디입니다.");
    }
}

package com.board.exception;

/**
 * 로그인에 실패하는 경우
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg) {
        super(msg);
    }

}

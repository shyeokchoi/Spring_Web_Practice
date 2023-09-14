package com.board.exception;

/**
 * 권한이 없는 행동을 한 경우
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

}

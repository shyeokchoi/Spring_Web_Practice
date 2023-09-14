package com.board.exception;

/**
 * 어떤 자원이 이미 삭제되었을 때 발생하는 예외
 */
public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }

}

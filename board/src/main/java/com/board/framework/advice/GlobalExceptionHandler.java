package com.board.framework.advice;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.board.exception.ConflictException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 에러 메시지를 포함해 HTTP body를 만드는 함수
     * 
     * @param errMsg
     * @return 만들어진 body
     */
    private Map<String, String> buildErrBody(String errMsg) {
        Map<String, String> body = new HashMap<>();
        body.put("error message", errMsg);
        return body;
    }

    /**
     * 이미 존재하는 회원 정보로 가입하려고 하면 발생하는 예외
     * 
     * @param e
     * @return status code 409, 에러에 대한 메시지를 담고 있는 ResponseEntity
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrBody(e.getMessage()));
    }

    /**
     * 잘못된 형식을 입력했을 때 발생하는 예외
     * 
     * @param e
     * @return status code 400, 에러에 대한 메시지를 담고 있는 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrBody(e.getMessage()));
    }
}

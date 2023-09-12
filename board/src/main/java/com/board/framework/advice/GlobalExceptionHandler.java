package com.board.framework.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.board.exception.AuthenticationException;
import com.board.exception.NotSignedInException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 에러 메시지의 리스트를 받아서 HTTP body로 만드는 함수
     * 
     * @param errMsgList
     * @return 만들어진 HTTP body
     */
    private Map<String, List<String>> buildErrBody(List<String> errMsgList) {
        Map<String, List<String>> body = new HashMap<>();

        body.put("error", errMsgList);
        return body;
    }

    /**
     * 에러 메시지가 하나일 때,
     * 해당 에러 메시지를 받아서 HTTP body로 만드는 함수
     * 
     * @param errMsg
     * @return 만들어진 HTTP body
     */
    private Map<String, List<String>> buildErrBody(String errMsg) {
        List<String> errorList = new ArrayList<>(Collections.singletonList(errMsg));

        return buildErrBody(errorList);
    }

    /**
     * 이미 존재하는 회원 정보로 가입하려고 하면 발생하는 예외
     * 
     * @param e
     * 
     * @return status code 409
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, List<String>>> handleConflictException(DuplicateKeyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrBody(e.getMessage()));
    }

    /**
     * 잘못된 형식을 입력했을 때 발생하는 예외
     * 
     * @param e
     * @return status code 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errMsgList = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrBody(errMsgList));
    }

    /**
     * 아이디와 비밀번호를 틀렸거나 존재하지 않는 아이디로 회원가입을 시도할 때 발생하는 예외
     * 
     * @param e
     * @return status code 401
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(NotSignedInException.class)
    public ResponseEntity<Map<String, List<String>>> handleAlreadySignedOutException(NotSignedInException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrBody(e.getMessage()));
    }
}

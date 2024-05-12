package com.board.framework.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.board.exception.AuthenticationException;
import com.board.exception.FileSystemException;
import com.board.exception.NoDataFoundException;
import com.board.exception.NotSignedInException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String, List<String>> buildErrBody(List<String> errMsgList) {
        Map<String, List<String>> body = new HashMap<>();

        body.put("error", errMsgList);
        return body;
    }

    private Map<String, List<String>> buildErrBody(String errMsg) {
        List<String> errorList = new ArrayList<>(Collections.singletonList(errMsg));

        return buildErrBody(errorList);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, List<String>>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, List<String>>> handleConflictException(DuplicateKeyException e) {
        String msg = e.getMessage();
        String finalMsg;

        if (msg.contains("member_unique_id")) {
            finalMsg = "아이디가 중복됩니다.";
        } else if (msg.contains("member_unique_email")) {
            finalMsg = "이메일이 중복됩니다.";
        } else {
            finalMsg = "DuplicateKeyException 발생 : Unknown Field";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrBody(finalMsg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errMsgList = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrBody(errMsgList));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(NotSignedInException.class)
    public ResponseEntity<Map<String, List<String>>> handleAlreadySignedOutException(NotSignedInException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, List<String>>> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleNoDataFoundException(NoDataFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<Map<String, List<String>>> handleFileSystemException(
            FileSystemException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrBody(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleConstraintViolationException(
            ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrBody(e.getMessage()));
    }
}

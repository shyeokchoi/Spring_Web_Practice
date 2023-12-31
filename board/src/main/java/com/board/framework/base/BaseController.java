package com.board.framework.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
    protected <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    protected <T> ResponseEntity<T> ok() {
        return ok(null);
    }

    protected <T> ResponseEntity<T> nok(T body, HttpStatus httpStatus) {
        return new ResponseEntity<>(body, httpStatus);
    }

    protected <T> ResponseEntity<T> nok(HttpStatus httpStatus) {
        return nok(null, httpStatus);
    }
}
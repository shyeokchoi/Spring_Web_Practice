package com.board.framework.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.board.constant.FrkConstants;
import com.board.dto.ResponseDTO;

public class BaseController {

    private <T> ResponseEntity<ResponseDTO<T>> responseEntity(int result, String resultMessage, T data) {
        ResponseDTO<T> resp = new ResponseDTO<T>(result, resultMessage, data);
        return new ResponseEntity<ResponseDTO<T>>(resp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ResponseDTO<T>> ok(T body) {

        return responseEntity(FrkConstants.CD_OK, FrkConstants.SUCCESS, body);

    }

    protected <T> ResponseEntity<ResponseDTO<T>> nok(int code, String message, T body) {
        return responseEntity(code, message, body);
    }

    protected <T> ResponseEntity<ResponseDTO<T>> nok(int code, T body) {
        return nok(code, FrkConstants.FAIL, body);
    }

    protected ResponseEntity<ResponseDTO<Void>> ok() {

        return responseEntity(FrkConstants.CD_OK, FrkConstants.SUCCESS, null);

    }

    protected <T> ResponseEntity<ResponseDTO<T>> nok(T body) {
        return responseEntity(FrkConstants.CD_NOK, FrkConstants.FAIL, body);

    }

    protected <T> ResponseEntity<ResponseDTO<T>> nok(int code) {
        return nok(code, null);
    }

    protected ResponseEntity<ResponseDTO<Void>> nok() {
        return nok(FrkConstants.CD_NOK);
    }
}

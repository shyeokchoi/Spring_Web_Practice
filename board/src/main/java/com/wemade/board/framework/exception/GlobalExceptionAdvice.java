package com.wemade.board.framework.exception;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wemade.board.common.DTO.BaseResponseDTO;
import com.wemade.board.common.constant.FrkConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final String errMsg = "==> Error Message";

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        log.error("==> handleNoHandlerFoundException", ex);

        int errorCd = 404;
        BaseResponseDTO body = new BaseResponseDTO(errorCd, getMessage(errorCd + ""));

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> defaultException(WebRequest request, Exception e) {
        log.error(errMsg, e);
        int errorCd = FrkConstants.CD_INTERNAL_ERR;

        Object body = new BaseResponseDTO(errorCd, getMessage(errorCd + ""));
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> handleBadRequestException(WebRequest request, HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);

        Object body = new BaseResponseDTO(FrkConstants.CD_PARAM_ERR, e.getMessage());
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Object> handleConflict(WebRequest request, BaseException e) {
        log.error("==> Error Message. ", e);

        int errorCd = e.getResCode();
        String errorMsg = e.getResMsg();
        
        if (StringUtils.isEmpty(errorMsg)) {
            errorMsg = getMessage(errorCd + "", e.getParamArray());
        }
        
        Object body = new BaseResponseDTO(errorCd, errorMsg);
        
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.OK, request);
    }
    
    /**
     * 중복키 에러
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<Object> duplicateKeyException(WebRequest request, DuplicateKeyException e) {

        log.info("==> duplicateKeyException", e);
        
        int errorCd = FrkConstants.CD_DUPLICATED;
        Object body = new BaseResponseDTO(errorCd, getMessage(errorCd + ""));

        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.OK, request);
    }
    
    /**
     * requestBody를 json paring할때 발생하는 에러에 대한 처리.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorMessage = getMessage(FrkConstants.CD_PARAM_ERR + "");
        Object body = new BaseResponseDTO(FrkConstants.CD_PARAM_ERR, errorMessage);

        return handleExceptionInternal(e, body, headers, status, request);
    }
    
    /**
     * javax valid에 의한 유효성 검증 에러
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ObjectError objErr = e.getBindingResult().getAllErrors().get(0);
        String detailMsg = createDetailMessage(objErr);
        String errorMessage = getMessage(FrkConstants.CD_PARAM_ERR + "") + detailMsg;
        Object body = new BaseResponseDTO(FrkConstants.CD_PARAM_ERR, errorMessage);
        
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.OK, request);
    }
    
    /**
     * springframework validated에 의한 유효성 검증 에러
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(WebRequest request, ConstraintViolationException e) {

        log.info("==> ConstraintViolationExceptionException", e);

        int errorCd = FrkConstants.CD_PARAM_ERR;
        Object body = new BaseResponseDTO(errorCd, getMessage(errorCd + "") + e.getMessage());

        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.OK, request);
    }
    
    /**
     * javax valid에서 발생한 에러의 메세지 생성
     * @param objErr
     * @return
     */
    private String createDetailMessage(ObjectError objErr) {
        if (objErr instanceof FieldError) {
            FieldError fieldErr = (FieldError) objErr;
            return String.format("[Field : %s, Message : %s]", fieldErr.getField(), fieldErr.getDefaultMessage());
        } else {
            return String.format("[Object Name : %s, Message : %s]", objErr.getObjectName(),
                    objErr.getDefaultMessage());
        }
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}

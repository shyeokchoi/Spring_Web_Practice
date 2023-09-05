package com.wemade.board.framework.exception;

import com.wemade.board.common.constant.FrkConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1905122041950251207L;

    private int resCode;
	
	private String resMsg;

	public Object[] paramArray;

    public BaseException() {
        this(FrkConstants.CD_UNKNOWN);
    }

    public BaseException(int errCode) {
        this.resCode = errCode;
	}
    
    public BaseException(String message, int resCode) {
        super(message);
        this.resCode = resCode;
    }
    
    public BaseException(int resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }
    
    public BaseException(String message, int resCode, String resMsg) {
        super(message);
        this.resCode = resCode;
        this.resMsg = resMsg;
    }
    
    public BaseException(int resCode, Object[] paramArray) {
        this.resCode = resCode;
        this.paramArray = paramArray;
    }
    
    public BaseException(String message, int resCode, Object[] paramArray) {
        super(message);
        this.resCode = resCode;
        this.paramArray = paramArray;
    }

    public BaseException(Throwable cause, int resCode, String resMsg) {
        super(cause);
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public BaseException(Throwable cause, int resCode, Object[] paramArray) {
        super(cause);
        this.resCode = resCode;
        this.paramArray = paramArray;
    }
}

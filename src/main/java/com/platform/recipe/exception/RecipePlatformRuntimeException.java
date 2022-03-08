package com.platform.recipe.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipePlatformRuntimeException extends RuntimeException {
    private String errMsgKey;
    private String errorCode;

    public RecipePlatformRuntimeException(Throwable cause) {
        super(cause);
    }

    public RecipePlatformRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public RecipePlatformRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public RecipePlatformRuntimeException(ErrorCode errorCode) {
        super(errorCode.getErrMsgKey());
        this.errMsgKey = errorCode.getErrMsgKey();
        this.errorCode = errorCode.getErrCode();

    }

}

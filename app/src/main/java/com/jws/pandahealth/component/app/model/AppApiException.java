package com.jws.pandahealth.component.app.model;

/**
 * Created by codeest on 2016/8/4.
 */
public class AppApiException extends Exception{
    private String errorCode;
    public AppApiException(String msg)
    {
        super(msg);
    }

    public AppApiException(String errmsg, String errcode) {
        super(errmsg);
        errorCode = errcode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

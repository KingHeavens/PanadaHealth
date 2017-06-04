package com.jws.pandahealth.component.app.model.bean;

/**
 * Modify by jing.
 */

public class BaseInfo<T> {
    private String errmsg;
    private String status;
    private String errcode;
    private T object;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "BaseInfo{" +
                "errmsg='" + errmsg + '\'' +
                ", status='" + status + '\'' +
                ", errcode='" + errcode + '\'' +
                ", arrUserinfo=" + object +
                '}';
    }
}

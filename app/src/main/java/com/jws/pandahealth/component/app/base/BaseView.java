package com.jws.pandahealth.component.app.base;

/**
 * Created by codeest on 2016/8/2.
 * View基类
 */
public interface BaseView {
    /**
     *
     * @param msg
     */
    void showError(String msg);
    void noHttpError();

    void useNightMode(boolean isNight);

}

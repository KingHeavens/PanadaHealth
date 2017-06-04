package com.jws.pandahealth.component.app.base;

import android.content.Intent;

import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.model.AppApiException;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.services.ServicesConstant;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jws on 2016/8/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }

    /**
     * 用于处理错误的方法
     * 包括 网络异常
     * app自定义异常
     *
     * @param e
     */
    protected void handlerErrorException(Throwable e) {
        String code;
        String message;
        if (e instanceof HttpException) { //网络异常
            HttpException exception = (HttpException) e;
            code = String.valueOf(exception.code());
            message = exception.message();
            if (mView != null && "504".equals(code))
                mView.noHttpError();
            else {
                message = message + " code:" + code;
                if (mView != null)
                    mView.showError(message);
            }
        } else if (e instanceof AppApiException) {//app自定义异常
            AppApiException exception = (AppApiException) e;
            code = exception.getErrorCode();
            message = exception.getMessage();
            if (AppConfig.ERROR_TOEKN.equals(code)) {
                RxBusUtil.getDefault().post(ServicesConstant.ACCOUNT_REMOVED);
            } else {
                if (mView != null)
                    mView.showError(message);
            }
        } else {
            message = e.getMessage();
            if (mView != null)
                mView.showError(message);
        }
    }

}

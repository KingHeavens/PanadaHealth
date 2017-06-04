package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.presenter.contract.LoginContract;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.services.easemob.EaseMobLoginUtils;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhaijinjing on 2016/12/1.
 *
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter{
    EaseMobLoginUtils easeMobLogin;
    AppRetrofitHelper mRetrofitHelper;
    AppRealmHelper mBaseDbHelper;
    private UserInfo mUserInfo;

    @Inject
    public LoginPresenter(AppRetrofitHelper retrofitHelper, AppRealmHelper baseDbHelper, EaseMobLoginUtils easeMobLogin){
        this.mRetrofitHelper = retrofitHelper;
        this.mBaseDbHelper = baseDbHelper;
        this.easeMobLogin = easeMobLogin;
    }

    @Override
    public void login(String mobile,String pwd) {
        WeakHashMap<String, String> base = MyApplication.getHttpDataMap();
        base.put("email",mobile);
        base.put("pwd", pwd);
        Subscription subscribe = mRetrofitHelper.login(JsonUtil.mapToJsonString(base))
                .compose(RxUtil.<BaseInfo<UserInfo>>rxSchedulerHelper())
                .compose(RxUtil.<UserInfo>handleMyResult())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                       // mView.jump2MainOrFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }
                    @Override
                    public void onNext(UserInfo userInfo) {
                        getUserinfo(userInfo);
                    }
                });
        addSubscrebe(subscribe);
    }



    @Override
    public void getUserinfo(UserInfo info) {
        WeakHashMap<String, String> base = MyApplication.getHttpDataMap();
        base.put("token",info.token);
        mUserInfo = info;
        addSubscrebe(mRetrofitHelper.getuserinfo(JsonUtil.mapToJsonString(base))
                .compose(RxUtil.<BaseInfo<UserInfo>>rxSchedulerHelper())
                .compose(RxUtil.<UserInfo>handleMyResult())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        //mView.jump2MainOrFinish();
                    }
                    @Override
                    public void onError(Throwable e) {
                       handlerErrorException(e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                       //mBaseDbHelper.insertUserInfo(userInfo);
                        mUserInfo.userIcon = userInfo.userIcon;
                        mUserInfo.email = userInfo.email;
                        mUserInfo.userName = userInfo.userName;
                        mUserInfo.gender = userInfo.gender;
                        mUserInfo.born = userInfo.born;
                        mUserInfo.regionIds = userInfo.regionIds;
                        mUserInfo.regionName = userInfo.regionName;
                        easeMobLogin(mUserInfo);
                    }
                }) );
    }

    /**
     * 环信登录
     */
    private void easeMobLogin(UserInfo info) {
       easeMobLogin.login(info, new EaseMobLoginUtils.OnEaseMobLoginListener() {
           @Override
           public void onEaseMobLoginSuccess() {

               mBaseDbHelper.insertUserInfo(mUserInfo);
               if(mView != null)
                   mView.jump2MainOrFinish();
           }

           @Override
           public void onEaseMobLoginProgress(String progressMsg) {
               JLog.d(progressMsg);
           }

           @Override
           public void onEaseMobLoginFail(Throwable e) {

               // handlerErrorException(e);
               if(mView!=null)
                    mView.showError(e.getMessage());
           }

           @Override
           public void onAddSubscription(Subscription subscribe) {
                addSubscrebe(subscribe);
           }
       });
    }



}


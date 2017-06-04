package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.presenter.contract.UserRegContract;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.services.easemob.EaseMobLoginUtils;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/1.
 *
 */
public class UserRegPresenter extends RxPresenter<UserRegContract.View> implements UserRegContract.Persenter{

    private AppRetrofitHelper mRetrofitHelper;
    private WeakHashMap<String, String> map;
    private EaseMobLoginUtils mEaseMobLogin;

    @Inject
    public  UserRegPresenter(AppRetrofitHelper retrofitHelper, EaseMobLoginUtils easeMobLogin){
        this.mRetrofitHelper=retrofitHelper;
        this.mEaseMobLogin = easeMobLogin;
    }


    @Override
    public void reg(String emails,String pwd) {
        WeakHashMap<String, String> map = MyApplication.getHttpDataMap();
        map.put("email", emails);
        map.put("pwd", pwd);
        Subscription subscription=mRetrofitHelper.reg(JsonUtil.mapToJsonString(map))
                .compose(RxUtil.<BaseInfo<UserInfo>>rxSchedulerHelper())
                .compose(RxUtil.<UserInfo>handleMyResult())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo baseInfo) {
                        mView.regSeccess(baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                });
        addSubscrebe(subscription);
    }


}

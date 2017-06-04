package com.jws.pandahealth.component.app.presenter;

import android.util.Log;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.presenter.contract.LoadingContract;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/19.
 */

public class LoadingPresenter extends RxPresenter<LoadingContract.View> implements LoadingContract.Presenter{
    AppRetrofitHelper appRetrofitHelper;
    @Inject
    public LoadingPresenter(AppRetrofitHelper appRetrofitHelper){
        this.appRetrofitHelper=appRetrofitHelper;
    }
    @Override
    public void initApp() {
        WeakHashMap<String, String> map = MyApplication.getHttpDataMap();
        Subscription subscription = appRetrofitHelper.initApp(JsonUtil.mapToJsonString(map))
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.initAppSuccess(baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(subscription);
    }
}

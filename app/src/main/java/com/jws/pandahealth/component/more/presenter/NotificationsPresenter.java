package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.more.model.bean.Subscription;
import com.jws.pandahealth.component.more.presenter.contract.NotificationsContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/1/12.
 */

public class NotificationsPresenter extends RxPresenter<NotificationsContract.View> implements NotificationsContract.Presenter{

    MoreRetrofitHelper helper;

    @Inject
    public NotificationsPresenter(MoreRetrofitHelper helper){
        this.helper=helper;
    }


    @Override
    public void loadNotification(String token) {
        WeakHashMap map= MyApplication.getHttpDataMap();
        if(!token.equals("")) {
            map.put("token", token);
        }
        addSubscrebe(helper.notifications(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo<List<Subscription>>>rxSchedulerHelper())
        .compose(RxUtil.<List<Subscription>>handleMyResult()).subscribe(new Action1<List<Subscription>>() {
                    @Override
                    public void call(List<Subscription> subscriptions) {
                        mView.loadNotificationSuccess(subscriptions);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                }));
    }
}

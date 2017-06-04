package com.jws.pandahealth.component.services.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.presenter.contract.ChatDisplayContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by zhaijinjing on 2016/12/21.
 * for HistoryFragment page
 */

public class ChatDisplayPresenter extends RxPresenter<ChatDisplayContract.View> implements ChatDisplayContract.Presenter{

    private ServicesRetrofitHelper mRetrofitHelper;
    private boolean hasLoad;

    @Inject
    public ChatDisplayPresenter(ServicesRetrofitHelper retrofitHelper) {
        mRetrofitHelper = retrofitHelper;
    }
    @Override
    public void getChatList(String id) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("orderId",id);
        Subscription subscribe = mRetrofitHelper.getChatContentHistory(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<ChatInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<ChatInfo>>handleMyResult())
                .subscribe(new Subscriber<List<ChatInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(List<ChatInfo> list) {
                        if(!hasLoad){
                            mView.bindData(list);
                            hasLoad = true;
                        }else {
                            mView.onRefreshFinished(list);
                        }
                    }
                });
        addSubscrebe(subscribe);
    }
}

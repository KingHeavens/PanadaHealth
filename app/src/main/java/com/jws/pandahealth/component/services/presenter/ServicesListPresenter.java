package com.jws.pandahealth.component.services.presenter;

import android.text.TextUtils;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.contract.ServicesHistoryContract;
import com.jws.pandahealth.component.services.presenter.contract.ServicesListContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by zhaijinjing on 2016/12/21.
 * for HistoryFragment page
 */

public class ServicesListPresenter extends RxPresenter<ServicesListContract.View> implements ServicesListContract.Presenter{

    private ServicesRetrofitHelper mRetrofitHelper;
    private String mType;

    @Inject
    public ServicesListPresenter(ServicesRetrofitHelper retrofitHelper) {
        mRetrofitHelper = retrofitHelper;
    }
    @Override
    public void getHistoryList(String type) {
        mType = type;
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        if(TextUtils.isEmpty(mType))
            type = "1";
        httpDataMap.put("type", mType);
        Subscription subscribe = mRetrofitHelper.getChatHistoryList(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<DoctorChatInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DoctorChatInfo>>handleMyResult())
                .subscribe(new Subscriber<List<DoctorChatInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DoctorChatInfo> list) {
                        mView.onRefreshFinished(list);
                    }
                });
        addSubscrebe(subscribe);
    }

}

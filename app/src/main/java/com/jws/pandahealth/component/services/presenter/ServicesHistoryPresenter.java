package com.jws.pandahealth.component.services.presenter;

import android.text.TextUtils;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.contract.ServicesHistoryContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for HistoryFragment page
 */

public class ServicesHistoryPresenter extends RxPresenter<ServicesHistoryContract.View> implements ServicesHistoryContract.Presenter{

    private ServicesRetrofitHelper mRetrofitHelper;
    private String mType;

    @Inject
    public ServicesHistoryPresenter(ServicesRetrofitHelper retrofitHelper) {
        mRetrofitHelper = retrofitHelper;
        initLoginStatusListener();
    }

    private void initLoginStatusListener() {
        addSubscrebe(RxBusUtil.getDefault().toObservable(String.class).compose(RxUtil.<String>rxSchedulerHelper())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s.equals(AppConfig.LOGIN_SUCCESS)) {
                            mView.refreshData();
                        }else if(s.equals(AppConfig.LOGOUT_SUCCESS)){
                                mView.clearData();
                        }
                    }
                }));
    }
    @Override
    public void getHistoryList(String type) {
        //mType = type;

        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        if(TextUtils.isEmpty(type))
            type = "1";
        httpDataMap.put("type",type);
        final String finalType = type;
        Subscription subscribe = mRetrofitHelper.getChatHistoryList(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<DoctorChatInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DoctorChatInfo>>handleMyResult())
                .subscribe(new Subscriber<List<DoctorChatInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(List<DoctorChatInfo> list) {
                       // mView.bindData(list,list.size()>0?list.get(0).type:"1");
                        mView.bindData(list, finalType);
                    }
                });
        addSubscrebe(subscribe);
    }
}

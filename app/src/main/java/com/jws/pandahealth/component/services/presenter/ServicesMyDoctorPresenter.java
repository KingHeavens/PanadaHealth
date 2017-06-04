package com.jws.pandahealth.component.services.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;
import com.jws.pandahealth.component.services.presenter.contract.ServicesMyDoctorContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for MyDoctorFragment page
 */

public class ServicesMyDoctorPresenter extends RxPresenter<ServicesMyDoctorContract.View> implements ServicesMyDoctorContract.Presenter{
    private final ServicesRetrofitHelper mRetrofitHelper;
    private boolean hasInit = false;
    private List<MyDoctorInfo> mList;

    @Inject
    public ServicesMyDoctorPresenter(ServicesRetrofitHelper retrofitHelper) {
        mRetrofitHelper = retrofitHelper;
        initLoginStatusListener();
    }

    private void initLoginStatusListener() {
        addSubscrebe(RxBusUtil.getDefault().toObservable(String.class).compose(RxUtil.<String>rxSchedulerHelper())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s.equals(AppConfig.LOGIN_SUCCESS)) {
                            getMyDoctorDataFromNet();
                        }else if(s.equals(AppConfig.LOGOUT_SUCCESS)){
                            if(mList !=null){
                                mList.clear();
                                mView.bindData(mList);
                            }
                        }
                    }
                }));
    }
    @Override
    public void getMyDoctorDataFromNet() {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        Subscription subscribe = mRetrofitHelper.getMyDoctorList(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<MyDoctorInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<MyDoctorInfo>>handleMyResult())
                .subscribe(new Subscriber<List<MyDoctorInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(List<MyDoctorInfo> list) {
                        mList = list;
                        if(hasInit){
                            mView.onRefreshFinished(list);
                        }else {
                            mView.bindData(list);
                            hasInit = true;
                        }
                    }
                });
        addSubscrebe(subscribe);
    }
}

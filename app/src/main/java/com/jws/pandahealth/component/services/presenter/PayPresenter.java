package com.jws.pandahealth.component.services.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.OrderInfo;
import com.jws.pandahealth.component.services.presenter.contract.PayContract;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for ServicesFragment page
 */
public class PayPresenter extends RxPresenter<PayContract.View> implements PayContract.Presenter{

    private ServicesRetrofitHelper mRetrofitHelper;
    private AppRealmHelper mRealmHelper;

    @Inject
    public PayPresenter(ServicesRetrofitHelper retrofitHelper, AppRealmHelper realmHelper) {
        mRetrofitHelper = retrofitHelper;
        mRealmHelper = realmHelper;
    }



    @Override
    public void init(DoctorInfo info) {
        if(info == null){
            InitInfo initInitInfo = mRealmHelper.getInitInitInfo();
            mView.bindLayoutWithoutDoctorInfo(initInitInfo);
        }else
            mView.bindLayoutData(info);

    }

    @Override
    public void getOrder(String doctorId, String type) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("doctorId",doctorId);
        httpDataMap.put("type",type);
        Subscription subscribe = mRetrofitHelper.getOrder(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<OrderInfo>>rxSchedulerHelper())
                .compose(RxUtil.<OrderInfo>handleMyResult())
                .subscribe(new Subscriber<OrderInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(OrderInfo orderInfo) {
                        mView.toPayWebPage(orderInfo.orderId);
                    }
                });

        addSubscrebe(subscribe);
    }
}

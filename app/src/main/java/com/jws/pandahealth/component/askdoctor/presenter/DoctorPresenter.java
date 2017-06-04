package com.jws.pandahealth.component.askdoctor.presenter;

import android.text.TextUtils;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppApiException;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.presenter.contract.DoctorContract;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.model.bean.OrderInfo;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/26.
 */
public class DoctorPresenter extends RxPresenter<DoctorContract.View> implements DoctorContract.Presenter{
    private static final String TAG = "DoctorPresenter";
    private final ServicesRetrofitHelper mServicesRetrofitHelper;
    private final ServicesRealmHelper servicesRealmHelper;
    AskDoctorsRetrofitHelper askDoctorsRetrofitHelper;
    private String mOrderId;

    @Inject
    public DoctorPresenter(AskDoctorsRetrofitHelper askDoctorsRetrofitHelper
            , ServicesRetrofitHelper servicesRetrofitHelper
            , ServicesRealmHelper servicesRealmHelper) {
        this.askDoctorsRetrofitHelper = askDoctorsRetrofitHelper;
        this.mServicesRetrofitHelper = servicesRetrofitHelper;
        this.servicesRealmHelper = servicesRealmHelper;
        initListener();
    }

    private void initListener() {
        Subscription subscribe = RxBusUtil.getDefault().toObservable(ServicesConstant.class)
                .compose(RxUtil.<ServicesConstant>rxSchedulerHelper())
                .subscribe(new Subscriber<ServicesConstant>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ServicesConstant servicesConstant) {
                        JLog.e(TAG + " pay success");
                        String orderId = servicesConstant.getData(ServicesConstant.PAY_ORDER_ID);
                        if (TextUtils.isEmpty(mOrderId) || !mOrderId.equals(orderId))
                            return;
                        String hxId = servicesConstant.getData(ServicesConstant.PAY_DOCTOR_HXID);
                        if (servicesConstant.getPostType() == ServicesConstant.PAY_SUCCESS_BACK)
                            mView.onPaySuccess(hxId);
                        else if (servicesConstant.getPostType() == ServicesConstant.PAY_FAILED_BACK)
                            mView.onPayFailed();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void loadDoctorInfo(String id, String token) {
        WeakHashMap w = MyApplication.getHttpDataMap();
        w.put("doctorId", id);
        w.put("token", token);
        addSubscrebe(askDoctorsRetrofitHelper.findDoctor(JsonUtil.mapToJsonString(w))
                .compose(RxUtil.<BaseInfo<DoctorInfo>>rxSchedulerHelper())
                .compose(RxUtil.<DoctorInfo>handleMyResult())
                .subscribe(new Action1<DoctorInfo>() {
                    @Override
                    public void call(DoctorInfo listBaseInfo) {
                        mView.loadDoctorSuccess(listBaseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        handlerErrorException(throwable);
                    }
                }));
    }

    @Override
    public void getOrder(String doctorId, String type) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("doctorId", doctorId);
        httpDataMap.put("type", type);
        Subscription subscribe = mServicesRetrofitHelper.getOrder(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<OrderInfo>>rxSchedulerHelper())
                .compose(RxUtil.<OrderInfo>handleMyResult())
                .subscribe(new Subscriber<OrderInfo>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        if ("java.lang.NullPointerException".equals(e.toString())){
                            mView.getOrderError(MyApplication.getInstance().getString(R.string.server_exception));
                        }else
                            handlerErrorException(e);
                    }

                    @Override
                    public void onNext(OrderInfo orderInfo) {
                        if (orderInfo == null)
                            Observable.error(new AppApiException(MyApplication.getInstance().getString(R.string.get_order_info_fail)));
                        mOrderId = orderInfo.orderId;
                        mView.toPayWebPage(orderInfo.orderId, orderInfo.paypalUrl);
                    }
                });

        addSubscrebe(subscribe);
    }


    /**
     * 检查用户聊天余额
     *
     * @return 是否可以继续发消息 true:可以发消息 false:不可以
     */
    @Override
    public boolean checkUserCanSendMessage(String doctorId) {
        FriendInfo info = servicesRealmHelper.getFriendInfoByDoctorId(doctorId);
        if (info == null) {
            mView.onCheckResult(false, "");
            return false;
        }
        JLog.e("chat count in database:" + info.count);
        int count = 0;
        try {
            count = Integer.valueOf(info.count);
        } catch (NumberFormatException e) {
            JLog.e("checkUserCanSendMessage Exception:" + e.getMessage());
            count = 0;
        }
        if (!checkIsExpire(info.expirationTime)) {
            if (count <= 0) {
                mView.onCheckResult(false, info.hxId);
                return false;
            }
        } else {
            mView.onCheckResult(false, info.hxId);
            return false;
        }
        mView.onCheckResult(true, info.hxId);
        return true;
    }

    /**
     * 检验时间是否过期
     *
     * @return false 没过期  true 过期
     */
    private boolean checkIsExpire(String time) {
        long current = System.currentTimeMillis();
        long expire;
        try {
            expire = Long.parseLong(time);
        } catch (NumberFormatException e) {
            JLog.e("checkIsExpire Exception:" + e.getMessage());
            return true;
        }
        JLog.e("CURRENT:" + current + " exxpire:" + expire);
        if (current > expire * 1000)
            return true;
        return false;
    }
}

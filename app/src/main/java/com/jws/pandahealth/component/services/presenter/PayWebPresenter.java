package com.jws.pandahealth.component.services.presenter;

import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.presenter.contract.PayWebContract;
import com.jws.pandahealth.component.services.util.JLog;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for ServicesFragment page
 */

public class PayWebPresenter extends RxPresenter<PayWebContract.View> implements PayWebContract.Presenter {
    private static final String TAG = "PayWebPresenter";
    private final ServicesRealmHelper mRealmHelper;

    @Inject
    public PayWebPresenter(ServicesRealmHelper realmHelper) {
        this.mRealmHelper = realmHelper;
        initListener();
    }

    private void initListener() {

    }


    @Override
    public void init() {
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
                        if (ServicesConstant.PAY_SUCCESS == servicesConstant.getPostType() || ServicesConstant.PAY_FAILED == servicesConstant.getPostType()) {
                            JLog.e(TAG + " pay success");
                            String hxId = servicesConstant.getData(ServicesConstant.PAY_DOCTOR_HXID);
                            String orderId = servicesConstant.getData(ServicesConstant.PAY_ORDER_ID);
                            mView.closeCurrentPage(servicesConstant.getPostType(), hxId, orderId);
                        }
                    }
                });

    }

    /**
     * 更新好友信息
     *
     * @param info
     */
    @Override
    public void updateFriendInfo(FriendInfo info) {
        mRealmHelper.insertOrUpdateFriendInfo(info);
    }
}

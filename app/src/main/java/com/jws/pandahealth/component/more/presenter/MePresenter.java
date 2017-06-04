package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.more.presenter.contract.MeContract;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/27.
 */

public class MePresenter extends RxPresenter<MeContract.View> implements MeContract.Presenter {
    AppRealmHelper appRealmHelper;

    @Inject
    public MePresenter(AppRealmHelper appRealmHelper) {
        this.appRealmHelper = appRealmHelper;
    }

    @Override
    public UserInfo getCurrentUser() {
        return appRealmHelper.queryUserInfo();
    }

    @Override
    public void addReceiver() {
        addSubscrebe(RxBusUtil.getDefault().toObservable(String.class).compose(RxUtil.<String>rxSchedulerHelper())
                            .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            if (s.equals(AppConfig.LOGIN_SUCCESS) || s.equals(AppConfig.LOGOUT_SUCCESS) || s.equals(AppConfig.USER_UPDATE)) {
                                mView.initLogin();
                            }
                        }
                }));
    }
}

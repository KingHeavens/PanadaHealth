package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.MoreRealmHelper;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.more.presenter.contract.UserUpdateInfoContract;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.jws.pandahealth.component.MyApplication.getCurrentUser;
import static com.jws.pandahealth.component.MyApplication.getHttpDataMap;

/**
 * Created by Administrator on 2016/12/28.
 */

public class UserUpdateInfoPresenter extends RxPresenter<UserUpdateInfoContract.View> implements UserUpdateInfoContract.Presenter{
    MoreRetrofitHelper retrofitHelper;
    MoreRealmHelper realmHelper;
    @Inject
    public UserUpdateInfoPresenter(MoreRetrofitHelper retrofitHelper, MoreRealmHelper realHelper){
        this.retrofitHelper = retrofitHelper;
        this.realmHelper =realHelper;
    }
//1 000 000
    @Override
    public void updateName(final String userName) {
        WeakHashMap map = getHttpDataMap();
        map.put("userName",userName);
        addSubscrebe(retrofitHelper.updateUserInfo(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        JLog.e("baseInfo -->" + baseInfo);
                        realmHelper.realmUpdateUserName(userName);
                        JLog.e("getCurrentUser().userName -->" + getCurrentUser().userName);
//                        MyApplication.setCurrentUser(userInfo);
                        RxBusUtil.getDefault().post(AppConfig.USER_UPDATE); // 注册广播发送广播
                        mView.updateNameSuccess(baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        JLog.e("throwable -->" + throwable);
                        mView.noHttpError();
                    }
                }));
    }


    @Override
    public void updateGender(String gender) {
        realmHelper.realmUpdateUserGender(gender);
        mView.updateGenderSuccess(gender);
    }




}

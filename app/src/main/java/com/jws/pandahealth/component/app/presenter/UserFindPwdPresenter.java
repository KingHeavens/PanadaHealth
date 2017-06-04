package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.presenter.contract.UserFindPwdContract;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/30.
 */

public class UserFindPwdPresenter extends RxPresenter<UserFindPwdContract.View> implements UserFindPwdContract.Presenter{
      AppRetrofitHelper appRetrofitHelper;
    @Inject
    public UserFindPwdPresenter(AppRetrofitHelper appRetrofitHelper){
        this.appRetrofitHelper=appRetrofitHelper;
    }
    @Override
    public void findPWD(String email) {
        WeakHashMap map= MyApplication.getHttpDataMap();
        map.put("email",email);
        addSubscrebe(appRetrofitHelper.findpwd(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper()).subscribe(
                new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.findSuccess(baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                }
        ));
    }


}

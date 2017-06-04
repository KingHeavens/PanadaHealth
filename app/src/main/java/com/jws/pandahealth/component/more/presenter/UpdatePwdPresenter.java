package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.more.presenter.contract.UpdatePasswordContract;

import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/1/4.
 */

public class UpdatePwdPresenter extends RxPresenter<UpdatePasswordContract.View> implements UpdatePasswordContract.Presenter{
    MoreRetrofitHelper retrofitHelper;
    @Inject
    public UpdatePwdPresenter(MoreRetrofitHelper retrofitHelper){
        this.retrofitHelper=retrofitHelper;
    }

    @Override
    public void updatePwd(String token, String currPwd, String newPwd) {
        WeakHashMap map=MyApplication.getHttpDataMap();
        map.put("token",token);
        map.put("oldPwd",currPwd);
        map.put("newPwd",newPwd);
        addSubscrebe(retrofitHelper.updatePwd(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.updateSuccess( baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                }));
    }
}

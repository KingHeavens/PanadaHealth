package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.askdoctor.model.bean.UploadImageInfo;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.presenter.contract.ContinueContract;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/31.
 */

public class ContinuePresenter extends RxPresenter<ContinueContract.View> implements ContinueContract.Presenter{
    AppRetrofitHelper appRetrofitHelper;

    @Inject
    public ContinuePresenter(AppRetrofitHelper appRetrofitHelper){
        this.appRetrofitHelper=appRetrofitHelper;
    }
    @Override
    public void upload(String path) {
        File file = new File(path);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String,RequestBody> value=new HashMap<>();
        value.put("file[]\"; filename=\""+"img"+"",requestBody);
        appRetrofitHelper.upload(requestBody,MyApplication.getHttpDataMap()).compose(RxUtil.<BaseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleMyResult())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String rul) {
                        mView.uploadSuccess(rul);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        handlerErrorException(throwable);
                    }
                });
    }


    @Override
    public void continueUser(String token, String userIcon, String userName, String gender) {
        WeakHashMap map= MyApplication.getHttpDataMap();
        map.put("token",token);
        map.put("userIcon",userIcon);
        map.put("userName",userName);
        map.put("gender",gender);
        appRetrofitHelper.cuntinueUser(JsonUtil.mapToJsonString(map))
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.continueSuccess(baseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                });
    }
}

package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.MoreRealmHelper;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.more.presenter.contract.UserInfoContract;
import com.jws.pandahealth.component.services.util.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

import static com.jws.pandahealth.component.MyApplication.getCurrentUser;
import static com.jws.pandahealth.component.MyApplication.getHttpDataMap;

/**
 * Created by Administrator on 2016/12/27.
 */

public class UserInfoPresenter extends RxPresenter<UserInfoContract.View> implements UserInfoContract.Presenter{

    MoreRetrofitHelper retrofitHelper;
    MoreRealmHelper realmHelper;

    @Inject
    public UserInfoPresenter(MoreRealmHelper appRealmHelper, MoreRetrofitHelper appRetrofitHelper) {
        this.realmHelper = appRealmHelper;
        this.retrofitHelper = appRetrofitHelper;
    }


    @Override
    public void upLoadImg(String upFilePath) {
        //上传图片到服务和调用修改用户属性接口
        //成功 修改本地数据并更新UI
        //失败 提示
        JLog.e("upFilePath -->" + upFilePath);
        File file = new File(upFilePath);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String,RequestBody> value=new HashMap<>();
        value.put("file[]\"; filename=\""+"img"+"",requestBody);
        retrofitHelper.uploadImage(requestBody, MyApplication.getHttpDataMap()).compose(RxUtil.<BaseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleMyResult())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String rul) {
                        JLog.e("url -->" + rul);
                        realmHelper.realmUpdateUserIcon(rul);
                        mView.uploadImgSuccess();
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
    public void updateUserIcon() {
        WeakHashMap map = getHttpDataMap();
        map.put("userIcon",getCurrentUser().userIcon);
        addSubscrebe(retrofitHelper.updateUserInfo(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.updateUserIconSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.noHttpError();
                    }
                }));
    }

    @Override
    public void updateGender() {
        WeakHashMap map = getHttpDataMap();
        map.put("Gender",getCurrentUser().gender);
        addSubscrebe(retrofitHelper.updateUserInfo(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.updateGenderSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.noHttpError();
                    }
                }));
    }

    @Override
    public void updateRegion() {
        WeakHashMap map = getHttpDataMap();
        map.put("Region",getCurrentUser().regionIds);
        addSubscrebe(retrofitHelper.updateUserInfo(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.updateRegionSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.noHttpError();
                    }
                }));
    }

    @Override
    public void updateBorn(String born) {
        realmHelper.realmUpdateUserBorn(born);
        WeakHashMap map = getHttpDataMap();
        map.put("Born",getCurrentUser().born);
        addSubscrebe(retrofitHelper.updateUserInfo(JsonUtil.mapToJsonString(map)).compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .subscribe(new Action1<BaseInfo>() {
                    @Override
                    public void call(BaseInfo baseInfo) {
                        mView.updateBornSuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.noHttpError();
                    }
                }));
    }

}

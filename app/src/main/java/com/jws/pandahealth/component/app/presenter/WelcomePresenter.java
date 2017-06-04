package com.jws.pandahealth.component.app.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.app.presenter.contract.WelcomeContract;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;

import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

import static com.jws.pandahealth.component.MyApplication.getCurrentUser;
import static com.jws.pandahealth.component.MyApplication.getHttpDataMap;

/**
 * Created by codeest on 16/8/15.
 *
 */

public class WelcomePresenter extends RxPresenter<WelcomeContract.View> implements WelcomeContract.Presenter{

    private static final int COUNT_DOWN_TIME = 2200;
    private final ServicesRetrofitHelper mServicesRetrofitHelper;
    private final ServicesRealmHelper mServicesRealmHelper;
    private AppRetrofitHelper mRetrofitHelper;
    private AppRealmHelper mBaseDbHelper;

    @Inject
    public WelcomePresenter(AppRetrofitHelper mRetrofitHelper, ServicesRetrofitHelper servicesRetrofitHelper, AppRealmHelper baseDbHelper , ServicesRealmHelper servicesRealmHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        this.mBaseDbHelper = baseDbHelper;
        this.mServicesRetrofitHelper = servicesRetrofitHelper;
        this.mServicesRealmHelper = servicesRealmHelper;
    }

    @Override
    public void loadData() {
        loadInitData();
        loadFriendInfo();
        String configItem = mBaseDbHelper.getConfigItem(AppRealmHelper.CONGIF_ITEM_ISGUID);
        if(TextUtils.isEmpty(configItem)){
            toGuide();
        } else{
            startCountDown();
        }
    }

    private void toGuide() {
        Subscription rxSubscription = Observable.timer(COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mView.jump2Guide();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    /**
     * 加载好友列表信息
     */
    private void loadFriendInfo() {
        if(getCurrentUser() ==  null || TextUtils.isEmpty(getCurrentUser().token))
            return;
        WeakHashMap<String, String> httpDataMap = getHttpDataMap();
        Subscription subscribe = mServicesRetrofitHelper.getFriendUserInfo(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<FriendInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<FriendInfo>>handleMyResult())
                .subscribe(new Subscriber<List<FriendInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        mServicesRealmHelper.insertFriendInfoLists(friendInfos);
                    }
                });
        addSubscrebe(subscribe);
    }

    private void loadInitData() {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        Subscription rxSubscription = mRetrofitHelper.getInit(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<InitInfo>>rxSchedulerHelper())
                .compose(RxUtil.<InitInfo>handleMyResult())
                .subscribe(new Subscriber<InitInfo>() {
                    @Override
                    public void onCompleted() {
                        startCountDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                        Log.e("OkHttp", "initCashHandler fail " + e.getMessage());
                    }

                    @Override
                    public void onNext(InitInfo initInfo) {
                        mBaseDbHelper.insetOrUpdateInitData(initInfo);
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void startCountDown() {
        Subscription rxSubscription = Observable.timer(COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mView.jump2Main();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

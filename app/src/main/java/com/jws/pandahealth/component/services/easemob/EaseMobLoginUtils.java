package com.jws.pandahealth.component.services.easemob;

import android.text.TextUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.model.AppApiException;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import io.realm.exceptions.RealmException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zhaijinjing on 2016/12/29.
 * 环信登录相关操作
 *  1、获取好友列表
 *  2、登录环信
 */

public class EaseMobLoginUtils {

    private static final String TAG = "EaseMobLoginUtils";
    private ServicesRetrofitHelper mRetrofitHelper;
    private ServicesRealmHelper mRealmHelper;

    @Inject
    public EaseMobLoginUtils(ServicesRetrofitHelper retrofitHelper, ServicesRealmHelper realmHelper){
        mRetrofitHelper = retrofitHelper;
        mRealmHelper = realmHelper;
    }

    public void login(final UserInfo userInfo, final OnEaseMobLoginListener listener){
        httpGetUserFriend(listener, userInfo);
    }
    /**
     * 环信登录
     */
    private void easeMobLogin(final UserInfo userInfo , final OnEaseMobLoginListener listener) {
        String hxUserName = userInfo.hxId;
        String hxPwd = userInfo.hxPwd;
        final String nickName = userInfo.userName;
        if(TextUtils.isEmpty(hxUserName) || TextUtils.isEmpty(hxPwd)){
            String errMsg = "hxUserName or hxPwd isEmpty , Login operation has stopped!";
            listener.onEaseMobLoginFail(new AppApiException(errMsg));
            return;
        }
        EMClient.getInstance().login(hxUserName, hxPwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        nickName);
                if (!updatenick) {
                    listener.onEaseMobLoginProgress("easeMob update current user nick fail");
                }
                Observable.create(new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        listener.onEaseMobLoginSuccess();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                listener.onEaseMobLoginProgress("on Login Success , can enter MainActivity");
            }

            @Override
            public void onError(int i, String s) {
                JLog.e("easeMob log failed :" + s);
                listener.onEaseMobLoginFail(new AppApiException("easeMob log failed :" + s));
            }

            @Override
            public void onProgress(int i, String s) {
                JLog.e("on easeMob Login progress :" + i + "info:" + s);
            }
        });
    }

    /**
     * 获取好友列表并存储
     */
    private void httpGetUserFriend(final OnEaseMobLoginListener listener,final UserInfo info) {
        listener.onEaseMobLoginProgress("start get Friend list from net");
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("token",info.token);
        httpDataMap.put("orderId","1");
        Subscription subscribe = mRetrofitHelper.getFriendUserInfo(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<FriendInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<FriendInfo>>handleMyResult())
                .subscribe(new Subscriber<List<FriendInfo>>() {
                    @Override
                    public void onCompleted() {
                        listener.onEaseMobLoginProgress("save Friend list to Realm Success");
                        listener.onEaseMobLoginProgress("start easeMob login");
                        easeMobLogin(info,listener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof RealmException){
                            String errMsg = "insert FriendInfo fail: RealmException" + e.getMessage();
                            listener.onEaseMobLoginFail(new AppApiException(errMsg));
                        }else
                            listener.onEaseMobLoginFail(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        listener.onEaseMobLoginProgress("get Friend list from net Success");
                        listener.onEaseMobLoginProgress("start save Friend list to Realm");
                        mRealmHelper.insertFriendInfoLists(friendInfos);
                    }
                });
        listener.onAddSubscription(subscribe);
    }


    /**
     * 环信登录成功
     */
    public interface OnEaseMobLoginListener {
        void onEaseMobLoginSuccess();
        void onEaseMobLoginProgress(String progressMsg);
        void onEaseMobLoginFail(Throwable e);
        void onAddSubscription(Subscription subscribe);
    }



    /**
     * logout
     *
     * @param unbindDeviceToken
     *            whether you need unbind your device token
     * @param callback
     *            callback
     */
    public static void easeMobLogout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        JLog.d("logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                JLog.d("logout: onSuccess");
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                JLog.d(TAG + "logout: onError");
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    private static void endCall() {
        try {
           /* com.hyphenate.chat.EMCallManager emCallManager = EMClient.getInstance().callManager();
            // .endCall();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

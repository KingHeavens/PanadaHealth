package com.jws.pandahealth.component.app.presenter;

import android.view.View;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.presenter.contract.MainContract;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.util.ActiviyUtils;
import com.jws.pandahealth.component.services.util.JLog;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2016/12/19.
 *
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter{
    private final ServicesRealmHelper mRealmHelper;
    private final AppRealmHelper mAppRealmHelper;
    AppRetrofitHelper appRetrofitHelper;

    @Inject
    public MainPresenter(AppRetrofitHelper appRetrofitHelper, ServicesRealmHelper realmHelper,AppRealmHelper appRealmHelper){
        this.appRetrofitHelper = appRetrofitHelper;
        mRealmHelper = realmHelper;
        mAppRealmHelper = appRealmHelper;
    }

    @Override
    public void init() {
        initListener();
        initChatData();
    }

    @Override
    public void clearUser() {
        mAppRealmHelper.clearUser();
    }

    private void initChatData() {
        List<FriendInfo> allFriendInfo = mRealmHelper.getAllFriendInfo();
        int unreadMsgCount = 0;
        if(!AppConfig.IS_RELEASE && AppConfig.TEST_CHAT){
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(AppConfig.TEST_CHAT_ACCOUNT);
            if(conversation!=null)
                unreadMsgCount = conversation.getUnreadMsgCount();
            if (unreadMsgCount > 0) {
                mView.setServicesDotVisibility(View.VISIBLE);
            }
            return;
        }

        for(FriendInfo info: allFriendInfo){
            EMConversation conversation;
            conversation = EMClient.getInstance().chatManager().getConversation(info.hxId);
            if(conversation!=null)
                unreadMsgCount = conversation.getUnreadMsgCount();
            if (unreadMsgCount > 0){
                mView.setServicesDotVisibility(View.VISIBLE);
                break;
            }
        }
        JLog.e("unreadMsgCount:" + unreadMsgCount);
    }


    private void initListener() {
        Subscription subscribe = RxBusUtil.getDefault().toObservable(String.class)
                .compose(RxUtil.<String>rxSchedulerHelper())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if(ServicesConstant.RECEIVED_MSG.equals(s) && !ServicesConstant.CHAT_ACTIVITY.equals(ActiviyUtils.getCurrentActivity(MyApplication.getInstance().getApplicationContext())))
                            mView.setServicesDotVisibility(View.VISIBLE);
                        else if(ServicesConstant.CLEAR_UNREAD_MSG.equals(s))
                            mView.setServicesDotVisibility(View.GONE);
                        else if(AppConfig.LOGIN_SUCCESS.equals(s)){
                            JLog.e("main login succss");
                            initChatData();
                        }else if(AppConfig.LOGOUT_SUCCESS.equals(s))
                            mView.setServicesDotVisibility(View.GONE);
                    }
                });
        addSubscrebe(subscribe);
    }


}

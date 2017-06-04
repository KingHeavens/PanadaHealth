package com.jws.pandahealth.component.services.presenter;

import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.presenter.contract.ServicesContract;
import com.jws.pandahealth.component.services.util.ActiviyUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for ServicesFragment page
 */

public class ServicesPresenter extends RxPresenter<ServicesContract.View> implements ServicesContract.Presenter{

    private final ServicesRealmHelper mRealmHelper;

    @Inject
    public ServicesPresenter(ServicesRealmHelper realmHelper) {
        mRealmHelper = realmHelper;
    }

    @Override
    public void init() {
        initListener();
        initChatData();
    }

    private void initChatData() {
        int unreadMsgCount = 0;
        if(!AppConfig.IS_RELEASE && AppConfig.TEST_CHAT){
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(AppConfig.TEST_CHAT_ACCOUNT);
            if(conversation!=null)
                unreadMsgCount = conversation.getUnreadMsgCount();
            if (unreadMsgCount > 0) {
                mView.setFirstTabDotVisibility(View.VISIBLE);
            }
            return;
        }
        List<FriendInfo> allFriendInfo = mRealmHelper.getAllFriendInfo();
        for(FriendInfo info: allFriendInfo){
            EMConversation conversation;
            conversation = EMClient.getInstance().chatManager().getConversation(info.hxId);
            if(conversation!=null)
                unreadMsgCount = conversation.getUnreadMsgCount();
            if (unreadMsgCount > 0){
                mView.setFirstTabDotVisibility(View.VISIBLE);
                break;
            }
        }
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
                            mView.setFirstTabDotVisibility(View.VISIBLE);
                        else if(ServicesConstant.CLEAR_UNREAD_MSG.equals(s))
                            mView.setFirstTabDotVisibility(View.GONE);
                        else if(AppConfig.LOGIN_SUCCESS.equals(s)){
                            initChatData();
                        }else if(AppConfig.LOGOUT_SUCCESS.equals(s))
                            mView.setFirstTabDotVisibility(View.GONE);
                    }
                });
        addSubscrebe(subscribe);
    }


}

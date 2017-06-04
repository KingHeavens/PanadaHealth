package com.jws.pandahealth.component.services.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.ui.activity.MainActivity;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.contract.ServicesNewChatContract;
import com.jws.pandahealth.component.services.util.ActiviyUtils;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for NewChatFragment tab
 */

public class  ServicesNewChatPresenter extends RxPresenter<ServicesNewChatContract.View> implements ServicesNewChatContract.Presenter{
    private ServicesRetrofitHelper mRetrofitHelper;
    private List<DoctorChatInfo> mList;
    private boolean mHasUnReadMsg;

    @Inject
    public ServicesNewChatPresenter(ServicesRetrofitHelper retroFitHelper) {
        mRetrofitHelper = retroFitHelper;
        initMsgListener();
        initLoginStatusListener();
    }

    private void initLoginStatusListener() {
        addSubscrebe(RxBusUtil.getDefault().toObservable(String.class).compose(RxUtil.<String>rxSchedulerHelper())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s.equals(AppConfig.LOGIN_SUCCESS)) {
                            getDoctorChatListFromNet(false);
                        }else if(s.equals(AppConfig.LOGOUT_SUCCESS)){
                            if(mList!=null){
                                mList.clear();
                                mView.notifyView();
                            }
                        }
                    }
                }));
    }

    /**
     * 监听消息
     */
    private void initMsgListener() {
        Subscription subscribe = RxBusUtil.getDefault().toObservable(String.class)
                .compose(RxUtil.<String>rxSchedulerHelper())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        JLog.e("initMsgListener  call  " + s);
                        boolean b = false;
                        if (ServicesConstant.RECEIVED_MSG.equals(s))
                            if(!ActiviyUtils.getCurrentActivity(MyApplication.getInstance().getApplicationContext()).equals(ServicesConstant.CHAT_ACTIVITY))
                            {
                                parseUnReadMsg(mList);
                                b = true;
                            }
                        if(ServicesConstant.READ_MSG.equals(s)){
                            parseUnReadMsg(mList);
                            if(!mHasUnReadMsg){
                                RxBusUtil.getDefault().post(ServicesConstant.CLEAR_UNREAD_MSG);
                            }
                            b = true;
                        }
                        return b;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean)
                            mView.notifyView();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public List<DoctorChatInfo> getDoctorChatListFromNet(final boolean isRefresh) {
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        Subscription subscribe = mRetrofitHelper.getNewChatList(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo<List<DoctorChatInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DoctorChatInfo>>handleMyResult())
                .map(new Func1<List<DoctorChatInfo>, List<DoctorChatInfo>>() {
                    @Override
                    public List<DoctorChatInfo> call(List<DoctorChatInfo> list) {
                        return parseUnReadMsg(list);
                    }
                })
                .subscribe(new Subscriber<List<DoctorChatInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(List<DoctorChatInfo> list) {
                        mList = list;
                        if(isRefresh){
                            mView.onRefreshFinished(mList);
                        }else {
                            mView.bindData(mList);
                        }
                    }
                });
        addSubscrebe(subscribe);
        return mList;
    }



    /**
     * 检查是否有未读信息
     * @param list
     * @return
     */
    private List<DoctorChatInfo> parseUnReadMsg(List<DoctorChatInfo> list) {
        boolean flagFirst = false;
        for(DoctorChatInfo info : list){
            EMConversation conversation;
            if(!AppConfig.IS_RELEASE && AppConfig.TEST_CHAT)
            {
                if(!flagFirst){
                    conversation = EMClient.getInstance().chatManager().getConversation(AppConfig.TEST_CHAT_ACCOUNT);
                    flagFirst = true;
                }else
                    conversation = EMClient.getInstance().chatManager().getConversation(info.doctorHxId);
            }else{
                conversation = EMClient.getInstance().chatManager().getConversation(info.doctorHxId);
            }
            int unReadCount = 0;
            if(conversation !=null){
                unReadCount = conversation.getUnreadMsgCount();
            }
            info.hasUnReadMsg = unReadCount > 0;
            mHasUnReadMsg = unReadCount > 0;
        }
        return list;
    }

}

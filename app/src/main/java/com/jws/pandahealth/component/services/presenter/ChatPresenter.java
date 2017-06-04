package com.jws.pandahealth.component.services.presenter;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.presenter.contract.ChatContract;
import com.jws.pandahealth.component.services.util.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhaijinjing on 2016/12/21.
 * for ServicesFragment page
 */

public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter{

    private final ServicesRetrofitHelper mRetrofitHelper;
    private final ServicesRealmHelper mRealmHelper;
    private final AppRetrofitHelper mAppRetrofitHelper;
    private String mHxId;

    @Inject
    public ChatPresenter(ServicesRetrofitHelper retrofitHelper, ServicesRealmHelper realmHelper, AppRetrofitHelper appRetrofitHelper) {
        mRetrofitHelper = retrofitHelper;
        mRealmHelper = realmHelper;
        mAppRetrofitHelper = appRetrofitHelper;
    }

    @Override
    public void init(String hxid) {
        mHxId = hxid;
        FriendInfo info = mRealmHelper.getFriendInfoByHxId(hxid);
        checkUserCanSendMessage(hxid);
        if(info !=  null)
            mView.bindChatInfo(info);
        RxBusUtil.getDefault().post(ServicesConstant.READ_MSG);
    }

    @Override
    public void sendMessage(String toHxId,EMMessage message) {
        if(!checkUserCanSendMessage(toHxId))
            return;
        EMMessage.Type chatType = message.getType();
        if(chatType == EMMessage.Type.TXT) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            String content = body.getMessage();
            send(content, chatType, toHxId);
        }else if(chatType == EMMessage.Type.IMAGE){
            upLoadImg(chatType,toHxId,message);
        }
        //向环信服务器发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void upLoadImg(final EMMessage.Type chatType, final String toHxId, final EMMessage message) {
        EMImageMessageBody body = (EMImageMessageBody)message.getBody();
        String localUrl = body.getLocalUrl();
        JLog.e("upload:" + localUrl);
        if(TextUtils.isEmpty(localUrl)){
            JLog.e("get local img fail,stop upload");
            return;
        }
        File file = new File(localUrl);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String,RequestBody> value=new HashMap<>();
        value.put("file[]\"; filename=\""+"img"+"",requestBody);
        Subscription subscribe = mAppRetrofitHelper.upload(requestBody, MyApplication.getHttpDataMap())
                .compose(RxUtil.<BaseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleMyResult())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        String remoteUrl = url;
                        JLog.e("remoteUrl:" + remoteUrl);
                        send(remoteUrl,chatType,toHxId);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * 检查用户聊天余额
     * @return 是否可以继续发消息 true:可以发消息 false:不可以
     */
    public boolean checkUserCanSendMessage(String toHxId) {
        FriendInfo info = mRealmHelper.getFriendInfoByHxId(toHxId);
        //TODO need change
        if(info == null){
            mView.showError(MyApplication.getInstance().getString(R.string.get_doctor_info_fail));
            if(!AppConfig.IS_RELEASE) //调试
                return true;
            else
                return false;
        }
        JLog.e("chat count in database:" + info.count);
        int count = 0;
        try {
            count = Integer.valueOf(info.count);
        }catch (NumberFormatException e){
            JLog.e("checkUserCanSendMessage Exception:" + e.getMessage());
            count = 0;
        }
        if(!checkIsExpire(info.expirationTime)){
            if(count <= 0){
                mView.showDialog(MyApplication.getInstance().getString(R.string.chat_count_not_enough));
                return false;
            }
        }else{
            mView.showDialog(MyApplication.getInstance().getString(R.string.chat_time_not_enough));
            return false;
        }
        return true;
    }

    /**
     * 检验时间是否过期
     * @return  false 没过期  true 过期
     */
    private boolean checkIsExpire(String time){
        long current = System.currentTimeMillis();
        long expire;
        try {
            expire = Long.parseLong(time);
        }catch (NumberFormatException e){
            JLog.e("checkIsExpire Exception:" + e.getMessage());
            return true;
        }
        JLog.e("CURRENT:" + current + " exxpire:" + expire);
        if(current > expire * 1000)
            return true;
        return false;
    }

    /**
     * 网络传送消息
     * @param content
     * @param type
     * @param toHxId
     */
    private void send(String content, EMMessage.Type type, String toHxId) {
        mRealmHelper.minusFriendInfoChatCount(mHxId);
        String rType = "";
        if(EMMessage.Type.TXT == type)
            rType = "0";
        else if(EMMessage.Type.IMAGE == type){
            rType = "1";
        }else if(EMMessage.Type.VOICE == type)
            rType = "2";
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        httpDataMap.put("content",content);
        httpDataMap.put("type",rType);
        httpDataMap.put("to_hx_id",toHxId);
        Subscription subscribe = mRetrofitHelper.sendMessage(JsonUtil.mapToJsonString(httpDataMap))
                .compose(RxUtil.<BaseInfo>rxSchedulerHelper())
                .compose(RxUtil.handleBaseResult())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRealmHelper.addFriendInfoChatCount(mHxId);
                        if(e instanceof NumberFormatException) {
                            JLog.e("数量 + 1失败！");
                        }
                        handlerErrorException(e);
                    }

                    @Override
                    public void onNext(String status) {
                        JLog.e("消息发送成功");
                        //

                    }
                });
        addSubscrebe(subscribe);
    }


}

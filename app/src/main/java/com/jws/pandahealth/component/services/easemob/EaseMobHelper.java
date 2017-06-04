package com.jws.pandahealth.component.services.easemob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.BuildConfig;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.AppModule;
import com.jws.pandahealth.component.app.base.DaggerAppComponent;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.ui.activity.MainActivity;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRealmHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.ui.activity.DialogActivity;
import com.jws.pandahealth.component.services.util.ActiviyUtils;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhaijinjing on 2016/12/24.
 * easemob initCashHandler
 */

public class EaseMobHelper {

    private static final String TAG = "EaseMobHelper";
    private Context appContext;
    private EaseUI easeUI;
    private boolean isVideoCalling = false;
    private boolean isVoiceCalling = false;
    private EMMessageListener messageListener;
    private EMConnectionListener connectionListener;

    private EaseMobHelper() {

    }

    private static EaseMobHelper instence;

    public synchronized static EaseMobHelper getInstence() {
        if (instence == null)
            instence = new EaseMobHelper();
        return instence;
    }

    public void init(Context context) {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            //debug mode, you'd better set it to false, if you want release your App officially.
            if (BuildConfig.DEBUG)
                EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();

            setGlobalListeners();
        }
    }

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                JLog.e("global listener onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(ServicesConstant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(ServicesConstant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(ServicesConstant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register message event listener
        registerMessageListener();

    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        JLog.e("onUserException " + exception);
        /*Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(exception, true);
        appContext.startActivity(intent);*/
        RxBusUtil.getDefault().post(exception);
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    /**
     * register message event listener
     */
    private void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    JLog.e("onMessageReceived id : " + message.getMsgId());
                    RxBusUtil.getDefault().post(ServicesConstant.RECEIVED_MSG);
                    // in background, do not refresh UI, notify it in notification bar
                    //if (!easeUI.hasForegroundActivies() || !ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.CHAT_ACTIVITY)) {
                    getNotifier().onNewMsg(message);
                    //}
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    JLog.e(TAG + ": receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    JLog.e(String.format(TAG + ": Command：action:%s,message:%s", action, message.toString()));
                    switch (action) {
                        case "success"://支付成功
                            onPaySuccess(message);
                            break;
                        case "fail"://支付失败
                            onPayFailed(message);
                            break;
                    }
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * 支付失败 透传消息
     *
     * @param message
     */
    private void onPayFailed(EMMessage message) {
        String orderId = "";
        try {
            orderId = message.getStringAttribute("order_id");
        } catch (HyphenateException e) {
            e.printStackTrace();
            JLog.e(TAG + " HyphenateException:" + e.getMessage());
        }
        JLog.e(TAG + " onPayFailed==:" + orderId);
        ServicesConstant constantFailed = new ServicesConstant();
        if (ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.PAY_WEB_ACTIVITY)) {
            constantFailed.setPostType(ServicesConstant.PAY_FAILED);
            constantFailed.addData(ServicesConstant.PAY_ORDER_ID, orderId);
        } else if (ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.DOCTOR_ACTIVITY)) {
            constantFailed.setPostType(ServicesConstant.PAY_FAILED_BACK);
            constantFailed.addData(ServicesConstant.PAY_ORDER_ID, orderId);
        }
        RxBusUtil.getDefault().post(constantFailed);
    }

    /**
     * 支付成功 透传消息
     *
     * @param message
     */
    private void onPaySuccess(EMMessage message) {
        String orderId = "";
        final FriendInfo info = new FriendInfo();

        try {
            orderId = message.getStringAttribute("order_id");
            JLog.e(TAG + ": orderId=" + orderId);
            info.hxId = message.getStringAttribute("hxId");
            info.count = message.getStringAttribute("count");
            info.doctorId = message.getStringAttribute("doctorId");
            info.doctorIcon = message.getStringAttribute("doctorIcon");
            info.doctorName = message.getStringAttribute("doctorName");
            info.expirationTime = message.getStringAttribute("expirationTime");
            JLog.e(TAG + ": friendInfo=" + info.toString());
        } catch (HyphenateException e) {
            e.printStackTrace();
            JLog.e(TAG + "HyphenateException:" + e.getMessage());
        }
        if (!TextUtils.isEmpty(info.hxId))
            DaggerAppComponent.builder()
                    .appModule(new AppModule((MyApplication) appContext))
                    .build().servicesRealmHelper().insertOrUpdateFriendInfo(info);

        ServicesConstant constant = new ServicesConstant();

        if (ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.PAY_WEB_ACTIVITY)) {
            constant.setPostType(ServicesConstant.PAY_SUCCESS);
            constant.addData(ServicesConstant.PAY_ORDER_ID, orderId);
            constant.addData(ServicesConstant.PAY_DOCTOR_HXID, info.hxId);
        } else if (ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.DOCTOR_ACTIVITY)
                || ActiviyUtils.getCurrentActivity(appContext).equals(ServicesConstant.QUESTION_NEW_ACTIVITY)) {
            constant.setPostType(ServicesConstant.PAY_SUCCESS_BACK);
            constant.addData(ServicesConstant.PAY_ORDER_ID, orderId);
            constant.addData(ServicesConstant.PAY_DOCTOR_HXID, info.hxId);
        } else {
            JLog.e("打开Main");
            final String finalOrderId = orderId;
            Observable.timer(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    Intent it = new Intent(appContext, DialogActivity.class);
                    it.putExtra(DialogActivity.TYPE, DialogActivity.TYPE_PAY_SUCCESS);
                    it.putExtra(ServicesConstant.PAY_ORDER_ID, finalOrderId);
                    it.putExtra(ServicesConstant.PAY_DOCTOR_HXID, info.hxId);
                    it.putExtra(DialogActivity.MESSAGE, MyApplication.getInstance().getString(R.string.have_pay_success));
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    appContext.startActivity(it);
                }
            });
        }
        RxBusUtil.getDefault().post(constant);
    }

    /**
     * to set user's profile and avatar
     */
    private void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }

        });

        //set options
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {//使用扬声器播放语音 ?
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) { //消息 震动 ？
                if (message == null)
                    return false;
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {// 消息 声音？
                if (message == null)
                    return false;
                return true;
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {// 是否 展示通知 ？
                if (message == null)
                    return false;
                return true;
            }
        });

        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {

                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) { // pandaHealth暂不提供
                    //intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    //intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", ServicesConstant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == EMMessage.ChatType.GroupChat) {
                            intent.putExtra("chatType", ServicesConstant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", ServicesConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    /**
     * get EaseUser for chat update avatar and nickname
     *
     * @param username
     * @return
     */
    private EaseUser getUserInfo(String username) {
        UserInfo currentUser = MyApplication.getCurrentUser();
        EaseUser easeUser = new EaseUser(username);
        if (currentUser != null && username.equals(currentUser.hxId)) {
            easeUser.setAvatar(currentUser.userIcon);
            easeUser.setNick(currentUser.userName);
            return easeUser;
        }

        //get FriendUser Info from database
        ServicesRealmHelper realmHelper = new ServicesRealmHelper();
        FriendInfo friendInfo = realmHelper.getFriendInfoByHxId(username);
        if (friendInfo == null) {
            JLog.e("getUserInfo friend info is null");
        } else {
            easeUser.setAvatar(friendInfo.doctorIcon);
            easeUser.setNick(friendInfo.doctorName);
        }
        return easeUser;
    }

}

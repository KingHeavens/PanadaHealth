package com.jws.pandahealth.component.services.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.DoctorActivity;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.presenter.ChatPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ChatContract;
import com.jws.pandahealth.component.services.easemob.emoji.ui.ChatFragment;
import com.jws.pandahealth.component.services.util.AndroidBug5497Workaround;
import com.jws.pandahealth.component.services.util.DialogUtil;
import com.jws.pandahealth.component.services.util.JLog;

import butterknife.BindView;

public class ChatActivity extends BaseActivity<ChatPresenter> implements ChatContract.View, View.OnClickListener {
    private static final String TAG = "ChatActivity";
    @BindView(R.id.container)
    FrameLayout mContainer;

    private String mToChatUserName;
    private ChatFragment mChatFragment;
    private String mDoctorId;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.services_activity_chat;
    }

    @Override
    protected void initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决全屏模式下 设置adjustSize失效问题
            AndroidBug5497Workaround.assistActivity(this);
        }
        //get user id or group id
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        //use EaseChatFratFragment
        mChatFragment = new ChatFragment();
        //pass parameters to chat fragment
        mChatFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().add(R.id.container, mChatFragment).commit();

        mToChatUserName = extras.getString(EaseConstant.EXTRA_USER_ID);
        mPresenter.init(mToChatUserName);
    }

    @Override
    public void bindChatInfo(FriendInfo info) {
        mDoctorId = info.doctorId;
        setTitleName(!TextUtils.isEmpty(info.doctorName) ? info.doctorName : getString(R.string.chat), getString(R.string.chat_title_right_text));
    }

    @Override
    public void showDialog(String msg) {
        DialogUtil.showDialog(this, msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ChatActivity.this,DoctorActivity.class);
                it.putExtra("id",mDoctorId);
                startActivity(it);
                Dialog tag = (Dialog) v.getTag();
                tag.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog tag = (Dialog) v.getTag();
                tag.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    // title栏  右边功能按键
    public void clickTitleRight(View view) {
        enterDoctorDetailActivity();
    }


    /**
     * 进入医生详情页面
     */
    private void enterDoctorDetailActivity() {
        Intent it = new Intent(mContext, DoctorActivity.class);
        it.putExtra("id",mDoctorId);
        startActivity(it);
    }


    /**
     * 发消息
     * @param message
     */
    public void sendMessage(EMMessage message){
        mPresenter.sendMessage(mToChatUserName,message);
    }


    /**
     * 头像点击
     * @param hxId
     */
    public void onAvatarClick(String hxId) {

    }

    /**
     * 输入框点击事件
     */
    public void onInputMenuClick(View view) {
        mPresenter.checkUserCanSendMessage(mToChatUserName);
    }

    @Override
    public void showError(String msg) {
        JLog.e(TAG + ":" + msg);
        showToast(msg);
    }

    @Override
    public void noHttpError() {
        showToast(getString(R.string.http_no_net_tip));
    }



}

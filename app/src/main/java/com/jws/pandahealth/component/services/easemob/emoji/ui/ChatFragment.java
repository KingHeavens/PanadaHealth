package com.jws.pandahealth.component.services.easemob.emoji.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.util.DialogUtil;

/**
 * Created by zhaijinjing on 2016/12/24.
 *  ChatFragment support by easemob. component of {@link ChatActivity}
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private ChatActivity mActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (ChatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //隐藏标题栏
        hideTitleBar();
        //add easui example emoji
        //((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        setChatFragmentListener(this);
        inputMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onInputMenuClick(v);
            }
        });
    }

    @Override
    public void easeReSendMessage(final EMMessage message, EaseChatMessageList list) {
        DialogUtil.showDialog(getActivity(), getString(R.string.confirm_resend), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setStatus(EMMessage.Status.CREATE);
                mActivity.sendMessage(message);
                messageList.refresh();
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        });

    }

    @Override
    public void easeSendMessage(EMMessage message) {
        mActivity.sendMessage(message);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {//消息扩展属性

    }

    @Override
    public void onEnterToChatDetails() { //进入会话详情

    }

    @Override
    public void onAvatarClick(String username) {//用户头像点击事件
        mActivity.onAvatarClick(username);
    }

    @Override
    public void onAvatarLongClick(String username) {//

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {//消息气泡框点击事件
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {//

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {//扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {//设置自定义chatrow提供者
        return null;
    }



}

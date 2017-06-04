package com.jws.pandahealth.component.services.presenter.contract;

import com.hyphenate.chat.EMMessage;
import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;

/**
 * Created by zhaijinjing on 2016/12/24.
 *
 */

public interface ChatContract {
    interface View extends BaseView {
        /**
         * 绑定交谈信息
         * @param info
         */
        void bindChatInfo(FriendInfo info);

        /**
         * 显示Dialog
         * @param msg
         */
        void showDialog(String msg);
    }

    interface Presenter extends BasePresenter<ChatContract.View> {
        /**
         *  获取好友信息操作
         * @param hxid
         */
        void init(String hxid);

        /**
         * 发消息
         * @param content
         * @param type
         * @param toHxId
         */
        void sendMessage(String toHxId, EMMessage message);


    }
}


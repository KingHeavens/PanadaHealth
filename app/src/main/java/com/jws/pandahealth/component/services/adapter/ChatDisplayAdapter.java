package com.jws.pandahealth.component.services.adapter;

import android.text.Spannable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.util.DateUtils;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DateUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.util.ChatTimeUtils;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.util.NumberUtils;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/26.
 *
 */

public class ChatDisplayAdapter extends BaseMultiItemQuickAdapter<ChatInfo,BaseViewHolder> {

    private final String mUserIcon;
    private final List<ChatInfo> chatInfos;

    public ChatDisplayAdapter(List<ChatInfo> data) {
        super(data);
        chatInfos = data;
        mUserIcon = MyApplication.getCurrentUser().userIcon;
        addItemType(ChatInfo.TYPE_SENT_PICTURE,R.layout.ease_row_sent_picture);
        addItemType(ChatInfo.TYPE_RECEIVED_PICTURE,R.layout.ease_row_received_picture);
        addItemType(ChatInfo.TYPE_SENT_MESSAGE,R.layout.ease_row_sent_message);
        addItemType(ChatInfo.TYPE_RECEIVED_MESSAGE,R.layout.ease_row_received_message);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatInfo doctorChatInfo) {

        switch(baseViewHolder.getItemViewType()){
            case ChatInfo.TYPE_SENT_MESSAGE:
                sentMessage(baseViewHolder,doctorChatInfo);
                break;
            case ChatInfo.TYPE_SENT_PICTURE:
                sentPic(baseViewHolder,doctorChatInfo);
                break;
            case ChatInfo.TYPE_RECEIVED_MESSAGE:
                receivedMessage(baseViewHolder,doctorChatInfo);
                break;
            case ChatInfo.TYPE_RECEIVED_PICTURE:
                receivedPic(baseViewHolder,doctorChatInfo);
                break;
        }
    }

    private void receivedPic(BaseViewHolder baseViewHolder, ChatInfo info) {

        baseViewHolder.setText(R.id.timestamp, DateUtil.formatTimesMMdd(info.created))
                .setVisible(R.id.progress_bar,false)
                .setVisible(R.id.percentage,false)
                .setVisible(R.id.timestamp,parseTimeVisiable(baseViewHolder,info));
        ImageLoaderUtil.loadUserImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_userhead),info.doctorIcon);
        ImageLoaderUtil.loadChatImage(mContext, (ImageView) baseViewHolder.getView(R.id.image),info.content);
    }

    private void receivedMessage(BaseViewHolder baseViewHolder, ChatInfo info) {
        baseViewHolder.setText(R.id.timestamp, DateUtil.formatTimesMMdd(info.created))
                .setVisible(R.id.timestamp,parseTimeVisiable(baseViewHolder,info));
        Spannable span = EaseSmileUtils.getSmiledText(mContext, info.content);
        TextView tvContent = baseViewHolder.getView(R.id.tv_chatcontent);
        // 设置内容
        tvContent.setText(span, TextView.BufferType.SPANNABLE);
        ImageLoaderUtil.loadUserImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_userhead),info.doctorIcon);
    }

    private void sentPic(BaseViewHolder baseViewHolder, ChatInfo info) {
        baseViewHolder.setText(R.id.timestamp, DateUtil.formatTimesMMdd(info.created))
                .setVisible(R.id.progress_bar,false)
                .setVisible(R.id.percentage,false)
                .setVisible(R.id.timestamp,parseTimeVisiable(baseViewHolder,info));
        ImageLoaderUtil.loadUserImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_userhead),mUserIcon);
        ImageLoaderUtil.loadChatImage(mContext, (ImageView) baseViewHolder.getView(R.id.image),info.content);
        baseViewHolder.getView(R.id.progress_bar).setVisibility(View.GONE);
        baseViewHolder.getView(R.id.percentage).setVisibility(View.GONE);
    }

    private void sentMessage(BaseViewHolder baseViewHolder, ChatInfo info) {
        baseViewHolder.setText(R.id.timestamp, DateUtil.formatTimesMMdd(info.created))
                .setVisible(R.id.progress_bar,false)
        .setVisible(R.id.timestamp,parseTimeVisiable(baseViewHolder,info));

        Spannable span = EaseSmileUtils.getSmiledText(mContext, info.content);
        TextView tvContent = baseViewHolder.getView(R.id.tv_chatcontent);
        // 设置内容
        tvContent.setText(span, TextView.BufferType.SPANNABLE);
        ImageLoaderUtil.loadUserImage(mContext, (ImageView) baseViewHolder.getView(R.id.iv_userhead),mUserIcon);
        baseViewHolder.getView(R.id.progress_bar).setVisibility(View.GONE);
    }

    private boolean parseTimeVisiable(BaseViewHolder baseViewHolder,ChatInfo info) {
        if(!ChatTimeUtils.closeEnough(info.created,chatInfos.get(baseViewHolder.getAdapterPosition()).created))
            return true;
        return false;
    }


}

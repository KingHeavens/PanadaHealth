package com.jws.pandahealth.component.services.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhaijinjing on 2016/12/28.
 *
 */
public class ChatInfo implements MultiItemEntity{
    public static final int TYPE_SENT_MESSAGE = 0;
    public static final int TYPE_RECEIVED_MESSAGE = 1;
    public static final int TYPE_SENT_PICTURE = 2;
    public static final int TYPE_RECEIVED_PICTURE = 3;
    public String type;//	消息类型：1图，2文字
    public String speaker;	//本条消息说话人：1我，2对方
    public String content;	//聊天内容
    public String created;	//聊天创建时间，时间戳
    public String doctorIcon;//医生头像

    @Override
    public int getItemType() {
        if("1".equals(type))
            return "1".equals(speaker) ? TYPE_SENT_PICTURE :TYPE_RECEIVED_PICTURE;
        else
            return "1".equals(speaker) ? TYPE_SENT_MESSAGE : TYPE_RECEIVED_MESSAGE;
    }
}

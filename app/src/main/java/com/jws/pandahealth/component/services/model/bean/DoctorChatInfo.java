package com.jws.pandahealth.component.services.model.bean;

import java.io.Serializable;

/**
 * Created by zhaijinjing on 2016/12/26.
 * Services 页面New Chat页面数据
 */

public class DoctorChatInfo implements Serializable{
    public String type; //服务类型，1-图文聊天，2-语音聊天，3-私人医生
    public String doctorName; //医生姓名
    public String doctorHxId; //医生换新Id
    public String servicePrice; //	单价，例：39
    public String serviceTextDuration; //	持续时长，显示文本，例：24 hours或month
    public String updated;  //	最后更新时间，时间戳
    public String doctorId;//TODO 医生Id need add
    public String orderId;//订单Id
    public boolean hasUnReadMsg;// 是否有未读消息
}

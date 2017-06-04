package com.jws.pandahealth.component.services.model.bean;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by zhaijinjing on 2016/12/28.
 * 好友信息
 */

@RealmClass
public class FriendInfo implements RealmModel{
    @PrimaryKey
    public String hxId;//	环信ID
    public String doctorName;//	医生名字
    public String doctorIcon;//	医生头像
    public String expirationTime;//	订单过期时间
    public String count; //	订单剩余次数
    public String doctorId;//医生Id

    @Override
    public String toString() {
        return "FriendInfo{" +
                "hxId='" + hxId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorIcon='" + doctorIcon + '\'' +
                ", expirationTime='" + expirationTime + '\'' +
                ", count='" + count + '\'' +
                ", doctorId='" + doctorId + '\'' +
                '}';
    }
}

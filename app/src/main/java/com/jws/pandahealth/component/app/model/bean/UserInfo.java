package com.jws.pandahealth.component.app.model.bean;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by zhaijinjing on 2016/12/1.
 *
 */
@RealmClass
public class UserInfo implements RealmModel{

    //基础属性
    @PrimaryKey
    public String uid;	//用户ID
    public String token;
    public String tokenValidity;//	令牌有效期截止，时间戳
    public String hxId;//	环信ID
    public String hxPwd;//	环信密码
    public String needImproveInfo;//	是否需要完善信息，0/1，若为0则跳到完善信息页面

    //扩展属性
    public String userIcon;//	用户头像url
    public String userName;//	姓名，50个字母内
    public String gender;//	性别：0女，1男
    public String born;//	生日，时间戳
    public String regionIds;//	所在地，数字id，若多级则用逗号隔开：431,532,567
    public String regionName;//	所在地，文字描述，若多级则用逗号隔开：China,Beijing
    public String email;

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", tokenValidity='" + tokenValidity + '\'' +
                ", hxId='" + hxId + '\'' +
                ", hxPwd='" + hxPwd + '\'' +
                ", needImproveInfo='" + needImproveInfo + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", born='" + born + '\'' +
                ", regionIds='" + regionIds + '\'' +
                ", regionName='" + regionName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

package com.jws.pandahealth.component.more.model;


import com.jws.pandahealth.component.RealmHelper;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;


/**
 * Created by ZJJ on 2016/12/23.
 *
 */

public class MoreRealmHelper extends RealmHelper {

    @Inject
    public MoreRealmHelper() {
        super();
    }

    public void updateUserInfoFriendInfoLists(final List<FriendInfo> friendInfos) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(FriendInfo info : friendInfos){
                    mRealm.copyToRealmOrUpdate(info);
                }
            }
        });
    }


    public void realmUpdateUserIcon(String userIcon){
        mRealm.beginTransaction();
        mRealm.where(UserInfo.class).findFirst().userIcon = userIcon;
        mRealm.commitTransaction();
    }

    public void realmUpdateUserName(String userName){
        mRealm.beginTransaction();
        mRealm.where(UserInfo.class).findFirst().userName = userName;
        mRealm.commitTransaction();
    }

    public void realmUpdateUserGender(String gender){
        mRealm.beginTransaction();
        mRealm.where(UserInfo.class).findFirst().gender = gender;
        mRealm.commitTransaction();
    }

    public void realmUpdateUserRegion(String regionIds,String regionName){
        JLog.e("regionIds-->" + regionIds + " | areaName-->" + regionName);
        mRealm.beginTransaction();
        mRealm.where(UserInfo.class).findFirst().regionIds = regionIds;
        mRealm.where(UserInfo.class).findFirst().regionName = regionName;
        mRealm.commitTransaction();
    }

    public void realmUpdateUserBorn(String born){
        mRealm.beginTransaction();
        mRealm.where(UserInfo.class).findFirst().born = born;
        mRealm.commitTransaction();
    }

//    public void insertFriendInfoLists(final List<FriendInfo> friendInfos) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                for(FriendInfo info : friendInfos){
//                    mRealm.copyToRealmOrUpdate(info);
//                }
//            }
//        });
//    }
//
//    /**
//     *  根据环信ID查找好友信息
//     * @param hxID
//     */
//    public FriendInfo getFriendInfoByHxId(String hxID) {
//        FriendInfo info = mRealm.where(FriendInfo.class).equalTo("hxId", hxID).findFirst();
//        return info;
//    }
//
//    /**
//     * 好友消息余额减一
//     */
//    public void minusFriendInfoChatCount(final String hxId) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                FriendInfo info = realm.where(FriendInfo.class).equalTo("hxId", hxId).findFirst();
//                int count;
//                count = Integer.valueOf(info.count);
//                count = count - 1;
//                info.count = String.valueOf(count);
//                realm.copyToRealmOrUpdate(info);
//            }
//        });
//    }
}

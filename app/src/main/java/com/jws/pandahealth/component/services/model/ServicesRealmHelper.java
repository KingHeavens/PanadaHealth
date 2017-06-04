package com.jws.pandahealth.component.services.model;



import com.jws.pandahealth.component.RealmHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;


/**
 * Created by ZJJ on 2016/12/23.
 *
 */

public class ServicesRealmHelper extends RealmHelper {
    public ServicesRealmHelper() {
        super();
    }

    public void insertFriendInfoLists(final List<FriendInfo> friendInfos) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(FriendInfo info : friendInfos){
                    mRealm.copyToRealmOrUpdate(info);
                }
            }
        });
    }

    /**
     *  根据环信ID查找好友信息
     * @param hxID
     */
    public FriendInfo getFriendInfoByHxId(String hxID) {
        FriendInfo info = mRealm.where(FriendInfo.class).equalTo("hxId", hxID).findFirst();
        return info;
    }

    /**
     * 根据医生ID查找好友信息
     * @param doctorId
     * @return
     */
    public FriendInfo getFriendInfoByDoctorId(String doctorId) {
        FriendInfo info = mRealm.where(FriendInfo.class).equalTo("doctorId", doctorId).findFirst();
        return info;
    }
    /**
     * 好友消息余额减一
     */
    public void minusFriendInfoChatCount(final String hxId) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FriendInfo info = realm.where(FriendInfo.class).equalTo("hxId", hxId).findFirst();
                int count;
                count = Integer.valueOf(info.count);
                count -= 1;
                info.count = String.valueOf(count);
                realm.copyToRealmOrUpdate(info);
            }
        });
    }
    /**
     * 好友消息余额+1
     */
    public void addFriendInfoChatCount(final String hxId) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FriendInfo info = realm.where(FriendInfo.class).equalTo("hxId", hxId).findFirst();
                int count;
                count = Integer.valueOf(info.count);
                count += 1;
                info.count = String.valueOf(count);
                realm.copyToRealmOrUpdate(info);
            }
        });
    }

    /**
     * 更新好友信息
     * @param info
     */
    public void insertOrUpdateFriendInfo(final FriendInfo info) {
        if(info == null){
            JLog.e("Friend info is  null;");
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(info);
            }
        });
    }

    public List<FriendInfo> getAllFriendInfo() {
        RealmResults<FriendInfo> infos = mRealm.where(FriendInfo.class).findAll();
        List<FriendInfo> list = new ArrayList<>();
        for (FriendInfo info : infos) {
            list.add(info);
        }
        return list;
    }


}

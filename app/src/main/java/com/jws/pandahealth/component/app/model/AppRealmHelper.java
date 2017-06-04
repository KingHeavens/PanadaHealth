package com.jws.pandahealth.component.app.model;

import com.jws.pandahealth.component.RealmHelper;
import com.jws.pandahealth.component.app.model.bean.ConfigInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

import io.realm.RealmResults;

/**
 * Created by codeest on 16/8/16.、
 */
public class AppRealmHelper extends RealmHelper {

    public AppRealmHelper() {
        super();
    }
   /* private static AppRealmHelper instance;
    public static synchronized AppRealmHelper getInstance() {
        if (instance == null) {
            instance = new AppRealmHelper();
        }
        return instance;
    }*/

    /**
     * 插入用户信息
     *
     * @param userInfo
     */
    public void insertUserInfo(UserInfo userInfo) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(userInfo);
        mRealm.commitTransaction();
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    public UserInfo queryUserInfo() {
        //RealmResults<UserInfo> userInfos = mRealm.where(UserInfo.class).findAll();
        UserInfo userInfo = mRealm.where(UserInfo.class).findFirst();
        //return userInfos.size() > 0 ? userInfos.get(0) : null;
        return userInfo;
    }

    /**
     * 获取用户Token
     *
     * @return
     */
    public String getToken() {
        UserInfo first = mRealm.where(UserInfo.class).findFirst();
        return first.token;
    }

    /**
     * 插入用户Token
     *
     * @param token
     */
    public void insertUserToken(String token) {
        mRealm.beginTransaction();
        UserInfo userInfo = mRealm.createObject(UserInfo.class);
        userInfo.token = token;
        mRealm.commitTransaction();
    }

    /**
     * 标记向导页面是否已经暂时
     */
    public static final String CONGIF_ITEM_ISGUID = "CONGIF_ITEM_ISGUID";
    /**
     * 向导页面已展示
     */
    public static final String CONGIF_ITEM_ISGUID_TRUE = "CONGIF_ITEM_ISGUID_TRUE";
    /**
     * 向导页面未展示
     */
    public static final String CONGIF_ITEM_ISGUID_FALSE = "CONGIF_ITEM_ISGUID_FALSE";

    /**
     * 插入或更新配置表
     */
    public void insertOrUpdateConfig(String item, String value) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.item = item;
        configInfo.value = value;
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(configInfo);
        mRealm.commitTransaction();
    }

    /**
     * 获取Config value
     */
    public String getConfigItem(String item) {
        ConfigInfo configInfo = mRealm.where(ConfigInfo.class).equalTo("item", item).findFirst();
        return configInfo != null ? configInfo.value : null;
    }

    /**
     * 更新初始化数据
     *
     * @param initInfo
     */
    public void insetOrUpdateInitData(InitInfo initInfo) {
        mRealm.beginTransaction();
        InitInfo first = mRealm.where(InitInfo.class).findFirst();
        if (first != null) {
            first.versions = initInfo.versions;
            first.quickAskPrice = initInfo.quickAskPrice;
            first.quickAskTime = initInfo.quickAskTime;
        } else {
            mRealm.copyToRealm(initInfo);
        }
        mRealm.commitTransaction();
    }

    /**
     * 查询快速提问价钱
     */
    public InitInfo askprice() {
        InitInfo first = mRealm.where(InitInfo.class).findFirst();
        return first;
    }


    public void clearUser() {
        mRealm.beginTransaction();
        RealmResults<UserInfo> realmResults = mRealm.where(UserInfo.class).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            realmResults.deleteFromRealm(i);
        }
        mRealm.commitTransaction();
    }

    /**
     * 获取初始化信息
     *
     * @return
     */
    public InitInfo getInitInitInfo() {
        InitInfo first = mRealm.where(InitInfo.class).findFirst();
        return first;
    }

    public void commitTransaction() {
        mRealm.commitTransaction();
    }
}

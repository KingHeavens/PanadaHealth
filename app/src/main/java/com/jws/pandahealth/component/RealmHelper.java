package com.jws.pandahealth.component;

import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyMigration;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 数据库
 */
public class RealmHelper {

    public static final String DB_NAME = "pandaHealth.realm";
    protected Realm mRealm;

    public RealmHelper() {

        mRealm = Realm.getInstance(new RealmConfiguration.Builder()
                //.name(DB_NAME)
                .schemaVersion(AppConfig.VERSION_CODE)
                .migration(MyMigration.getInstance())
                .build());
        //.deleteRealmIfMigrationNeeded()//升级删除数据
        // .encryptionKey(key)//加密
        // 算法获取
        //byte[] key = new byte[64];
        //new SecureRandom().nextBytes(key);
    }


}

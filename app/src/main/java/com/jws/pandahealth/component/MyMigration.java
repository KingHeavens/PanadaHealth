package com.jws.pandahealth.component;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by zhaijinjing on 2016/12/9.
 *
 */

public //升级数据库回调方法
class MyMigration implements RealmMigration {
    private MyMigration() {

    }

    private static MyMigration instance;
    public static synchronized MyMigration getInstance() {
        if (instance == null) {
            instance = new MyMigration();
        }
        return instance;
    }

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
//            RealmSchema schema = realm.getSchema();
//            if (oldVersion < 2) {
//                RealmObjectSchema personSchema = schema.get("Person");
//                // Combine 'firstName' and 'lastName' in a new field called 'fullName'
//                personSchema
//                        .addField("fullName", String.class, FieldAttribute.REQUIRED)
//                        .transform(new RealmObjectSchema.Function() {
//                            @Override
//                            public void apply(DynamicRealmObject obj) {
//                                obj.set("fullName", obj.getString("firstName") + " " + obj.getString("lastName"));
//                            }
//                        })
//                        .removeField("firstName")
//                        .removeField("lastName");
    }
}

package com.jws.pandahealth.component.app.model.bean;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass

public class ConfigInfo implements RealmModel{
    @PrimaryKey
    public String item;
    public String value;

}

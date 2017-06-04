package com.jws.pandahealth.component.app.model.bean;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by zhaijinjing on 2016/12/29.
 *
 */

@RealmClass
public class InitInfo implements RealmModel{
    public String versions;
    public String quickAskPrice;
    public String quickAskTime;
}

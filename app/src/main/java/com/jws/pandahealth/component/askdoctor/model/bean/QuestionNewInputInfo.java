package com.jws.pandahealth.component.askdoctor.model.bean;

import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Administrator on 2016/12/23.
 */
//@RealmClass
public class QuestionNewInputInfo {
//    @PrimaryKey
//    public int id;

    public String input;
    public ArrayList<ImageItem> items;

    @Override
    public String toString() {
        return "QuestionNewInputInfo{" +
                "input='" + input + '\'' +
                ", items=" + items +
                '}';
    }
}

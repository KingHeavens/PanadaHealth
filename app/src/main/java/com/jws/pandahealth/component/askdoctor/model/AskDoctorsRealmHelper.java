package com.jws.pandahealth.component.askdoctor.model;


import android.content.Context;
import android.graphics.Bitmap;

import com.jws.pandahealth.base.util.SharedPrefUtils;
import com.jws.pandahealth.component.askdoctor.model.bean.QuestionNewInputInfo;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;
import com.jws.pandahealth.component.RealmHelper;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;

/**
 * Created by Administrator on 2016/12/23.
 */

public class AskDoctorsRealmHelper extends RealmHelper {
    public AskDoctorsRealmHelper() {
        super();
    }

    public void saveQuestionNewInputInfo(Context context,String input, ArrayList<ImageItem> items) {
        SharedPrefUtils.saveString(context,"inputQuestionText",input);
        SharedPrefUtils.setDataList(context,"inputQuestionImageItems",items);
    }

    public QuestionNewInputInfo getQuestionNewInputInfo(Context context) {
        QuestionNewInputInfo info=new QuestionNewInputInfo();
        info.input=SharedPrefUtils.getString(context,"inputQuestionText","");
        info.items= SharedPrefUtils.getDataList(context,"inputQuestionImageItems");

        return info;
    }


}

package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRealmHelper;
import com.jws.pandahealth.component.askdoctor.presenter.contract.PhotoContract;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;

import javax.inject.Inject;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/22.
 */

public class PhotoPresenter extends RxPresenter<PhotoContract.View> implements PhotoContract.Presenter{
    AskDoctorsRealmHelper helper;
    @Inject
    public PhotoPresenter(AskDoctorsRealmHelper helper){
        this.helper=helper;
    }




}

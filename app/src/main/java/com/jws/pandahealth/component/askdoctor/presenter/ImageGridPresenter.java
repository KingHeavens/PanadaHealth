package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRealmHelper;
import com.jws.pandahealth.component.askdoctor.presenter.contract.ImageGridContract;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;

import javax.inject.Inject;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ImageGridPresenter extends RxPresenter<ImageGridContract.View> implements ImageGridContract.Presenter{
    @Inject
    public ImageGridPresenter(AskDoctorsRealmHelper helper){
    }
}

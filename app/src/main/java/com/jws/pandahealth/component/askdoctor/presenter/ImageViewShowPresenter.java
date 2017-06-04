package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.component.askdoctor.presenter.contract.ImageViewShowContract;
import com.jws.pandahealth.component.app.base.RxPresenter;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ImageViewShowPresenter extends RxPresenter<ImageViewShowContract.View> implements ImageViewShowContract.Presenter{
    @Inject
    public ImageViewShowPresenter(){

    }
}

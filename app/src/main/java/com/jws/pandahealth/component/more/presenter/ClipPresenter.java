package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.more.presenter.contract.ClipContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/28.
 */

public class ClipPresenter extends RxPresenter<ClipContract.View> implements ClipContract.Presenter{
    @Inject
    public ClipPresenter(){

    }
}

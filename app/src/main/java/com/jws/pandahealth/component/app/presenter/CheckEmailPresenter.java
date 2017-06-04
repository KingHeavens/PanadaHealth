package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.presenter.contract.CheckEmailContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/30.
 */

public class CheckEmailPresenter extends RxPresenter<CheckEmailContract.View> implements CheckEmailContract.Presenter{
    @Inject
    public CheckEmailPresenter(){

    }
}

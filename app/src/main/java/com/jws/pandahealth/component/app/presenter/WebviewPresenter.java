package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.presenter.contract.WebviewContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/1/4.
 */

public class WebviewPresenter extends RxPresenter<WebviewContract.View> implements WebviewContract.Presenter{
    @Inject
    public WebviewPresenter(){

    }
}

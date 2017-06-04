package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.askdoctor.presenter.contract.FindDoctorContract;
import com.jws.pandahealth.component.app.base.RxPresenter;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/23.
 */
public class FindDoctorPresenter extends RxPresenter<FindDoctorContract.View> implements FindDoctorContract.Presenter{

    AskDoctorsRetrofitHelper askDoctorsRetrofitHelper;
    @Inject
    public FindDoctorPresenter(AskDoctorsRetrofitHelper askDoctorsRetrofitHelper){
        this.askDoctorsRetrofitHelper=askDoctorsRetrofitHelper;
    }

}

package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.component.askdoctor.presenter.contract.AskDoctorFragmentContract;
import com.jws.pandahealth.component.app.base.RxPresenter;

import java.util.WeakHashMap;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/1.
 *
 */
public class AskDoctorFragmentPresenter extends RxPresenter<AskDoctorFragmentContract.View> implements AskDoctorFragmentContract.Persenter{

    private WeakHashMap<String, String> map;

    @Inject
    public AskDoctorFragmentPresenter( ){
    }


}

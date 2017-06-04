package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.more.presenter.contract.SettingContract;
import com.jws.pandahealth.component.services.util.JLog;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/1/3.
 *
 */

public class SettingPresenter extends RxPresenter<SettingContract.View> implements SettingContract.Presenter{
    AppRealmHelper realmHelper;
    @Inject
    public  SettingPresenter(AppRealmHelper realmHelper){
        this.realmHelper=realmHelper;
    }

    @Override
    public void clearUser() {
        realmHelper.clearUser();
    }
}

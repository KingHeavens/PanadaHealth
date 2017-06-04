package com.jws.pandahealth.component.app.presenter;

import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.AppRealmHelper;
import com.jws.pandahealth.component.app.presenter.contract.GuideContract;
import com.jws.pandahealth.component.RetrofitHelper;

import javax.inject.Inject;

/**
 * Created by zhaijinjing on 2016/12/2.
 *
 */

public class AppGuidePresenter extends RxPresenter<GuideContract.View> implements GuideContract.Presenter {
    RetrofitHelper mRetrofitHelper;
    AppRealmHelper mBaseDbHelper;
    @Inject
    public AppGuidePresenter(RetrofitHelper retrofitHelper,AppRealmHelper baseDbHelper){
        this.mRetrofitHelper = retrofitHelper;
        this.mBaseDbHelper = baseDbHelper;
    }
    @Override
    public void storgedGuideStatus() {
        mBaseDbHelper.insertOrUpdateConfig(AppRealmHelper.CONGIF_ITEM_ISGUID, AppRealmHelper.CONGIF_ITEM_ISGUID_TRUE);
        mView.jump2Main();
    }
}

package com.jws.pandahealth.component.askdoctor.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface PhotoContract {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
    }
}

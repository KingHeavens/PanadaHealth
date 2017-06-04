package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2017/1/3.
 */

public interface SettingContract {
    interface View extends BaseView{}
    interface Presenter extends BasePresenter<View>{
        void clearUser();
    }
}

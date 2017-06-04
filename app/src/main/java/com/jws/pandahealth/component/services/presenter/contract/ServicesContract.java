package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by zhaijinjing on 2016/12/21.
 *
 */

public interface ServicesContract {
    interface View extends BaseView{
        void setFirstTabDotVisibility(int visibility);
    }

    interface Presenter extends BasePresenter<View>{
        void init();
    }
}

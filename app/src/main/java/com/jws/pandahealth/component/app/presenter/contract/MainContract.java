package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 *
 */

public interface MainContract  {
    interface View extends BaseView{
        void exitBy2Click();
        void setServicesDotVisibility(int visibility);
    }
    interface Presenter extends BasePresenter<View>{
        void init();
        void clearUser();
    }
}

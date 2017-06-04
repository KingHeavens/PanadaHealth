package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by zhaijinjing on 2016/12/2.
 *
 */

public interface GuideContract {
    interface View extends BaseView{
        void jump2Main();
    }
    interface Presenter extends BasePresenter<GuideContract.View>{
        void storgedGuideStatus();
    }

}

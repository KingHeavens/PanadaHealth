package com.jws.pandahealth.component.askdoctor.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2016/12/1.
 */

public interface AskDoctorFragmentContract {
    interface View extends BaseView{
        void go2QuestionNow();
        void go2FindDoctor();
        void go2ChineseMedicines();
        void go2ChinaMedicalTourism();

    }

    interface Persenter extends BasePresenter<View> {

    }

}

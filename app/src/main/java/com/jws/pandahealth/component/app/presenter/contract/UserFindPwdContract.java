package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;

/**
 * Created by Administrator on 2016/12/30.
 */

public interface UserFindPwdContract {
    interface View extends BaseView{
        void findSuccess(BaseInfo baseInfo);
    }
    interface Presenter extends BasePresenter<View>{
        void findPWD(String email);
    }
}

package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

/**
 * Created by Administrator on 2016/12/28.
 */

public interface UserUpdateInfoContract {
    interface View extends BaseView{
        void updateNameSuccess(BaseInfo<UserInfo> u);
        void updateGenderSuccess(String gender);
    }
    interface Presenter extends BasePresenter<View>{
        void updateName(String nikename);
        void updateGender(String gender);
    }
}

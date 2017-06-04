package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

import java.util.WeakHashMap;

/**
 * Created by Administrator on 2016/12/1.
 */

public interface UserRegContract {
    interface View extends BaseView{
        void regSeccess(UserInfo UserInfo);
    }

    interface Persenter extends BasePresenter<View> {
        void reg(String email,String pwd);
    }

}

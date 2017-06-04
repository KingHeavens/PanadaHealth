package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

/**
 * Created by zhaijinjing on 2016/12/1.
 *
 */

public interface LoginContract {

    interface View extends BaseView{
        /**
         * 关闭本页面
         */
        void jump2MainOrFinish();
        /**
         * 跳转到注册页面,关闭本页面
         */
        void jump2RegPage();

        void inputFilter(String account,String pass);
    }

    interface Presenter extends BasePresenter<View>{
        /**
         * 登录操作
         */
        void login(String account,String pwd);

        void getUserinfo(UserInfo token);

    }
}

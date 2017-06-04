package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by zhaijinjing on 2016/12/2.
 *
 */

public interface LoadingContract {
    interface View extends BaseView{
        void goMian();
        void goGuide();
        /**
         * 成功获取初始化数据
         */
        void initAppSuccess(Object object);
    }

    interface Presenter extends BasePresenter<View>{
        void initApp();
    }

}

package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

/**
 * Created by Administrator on 2016/12/27.
 */

public interface MeContract {
    interface View extends BaseView {
        void initLogin();
    }

    interface Presenter extends BasePresenter<View> {
        UserInfo getCurrentUser();
        void addReceiver();
    }
}

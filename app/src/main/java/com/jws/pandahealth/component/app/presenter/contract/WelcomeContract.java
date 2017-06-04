package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by codeest on 16/8/15.
 */
public interface WelcomeContract {

    interface View extends BaseView {

        void showContent();

        void jump2Main();

        void jump2Login();

        void jump2Guide();
    }

    interface Presenter extends BasePresenter<View> {

        void loadData();

    }
}

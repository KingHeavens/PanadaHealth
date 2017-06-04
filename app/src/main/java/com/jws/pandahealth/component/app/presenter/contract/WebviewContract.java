package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2017/1/4.
 */

public interface WebviewContract {
    interface View extends BaseView{}
    interface Presenter extends BasePresenter<View>{}
}

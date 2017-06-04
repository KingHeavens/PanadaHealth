package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2016/12/28.
 */

public interface ClipContract {
    interface View extends BaseView{}
    interface Presenter extends BasePresenter<View>{}
}

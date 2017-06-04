package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;

/**
 * Created by Administrator on 2017/1/4.
 */

public interface UpdatePasswordContract {
    interface View extends BaseView{
        void updateSuccess(BaseInfo baseInfo);
    }
    interface Presenter extends BasePresenter<View>{
        void updatePwd(String token,String currPwd,String newPwd);
    }
}

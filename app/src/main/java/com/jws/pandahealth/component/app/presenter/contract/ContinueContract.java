package com.jws.pandahealth.component.app.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.askdoctor.model.bean.UploadImageInfo;

/**
 * Created by Administrator on 2016/12/31.
 */

public interface ContinueContract {
    interface View extends BaseView{
        void uploadSuccess(String url);
        void continueSuccess(BaseInfo baseInfo);
    }
    interface Presenter extends BasePresenter<View>{
        void upload(String path);
//        0女，1男
        void continueUser(String token,String userIcon,String userName,String gender);
    }
}

package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2016/12/27.
 */

public interface UserInfoContract {

    interface View extends BaseView{
        void setUserInfo();
        void updateGenderSuccess();
        void updateRegionSuccess();
        void updateBornSuccess();
        void uploadImgSuccess();
        void updateUserIconSuccess();

    }

    interface Presenter extends BasePresenter<View>{
        void upLoadImg(String upFile);
        void updateUserIcon();
        void updateGender();
        void updateRegion();
        void updateBorn(String born);
    }

}

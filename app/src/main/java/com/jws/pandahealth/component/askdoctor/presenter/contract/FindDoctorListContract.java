package com.jws.pandahealth.component.askdoctor.presenter.contract;

import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public interface FindDoctorListContract {
    interface View extends BaseView{
        void findDoctorSuccess(List<DoctorInfo>  baseInfo);
    }
    interface Presenter extends BasePresenter<View>{
        void findDoctors(int department, int level, int service,int page,String token);
    }
}

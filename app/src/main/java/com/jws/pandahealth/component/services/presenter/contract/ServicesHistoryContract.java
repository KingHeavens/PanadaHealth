package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/21.
 *
 */

public interface ServicesHistoryContract {
    interface View extends BaseView{
        void bindData(List<DoctorChatInfo> lists,String type);
        void refreshData();
        void clearData();
    }

    interface Presenter extends BasePresenter<View>{
        void getHistoryList(String type);
    }
}

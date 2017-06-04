package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/21.
 *
 */

public interface ServicesMyDoctorContract {
    interface View extends BaseView{
        /**
         * 绑定数据
         * @param lists
         */
        void bindData(List<MyDoctorInfo> lists);

        /**
         * 刷新数据
         */
        void onRefreshFinished(List<MyDoctorInfo> lists);
    }

    interface Presenter extends BasePresenter<View>{
        /**
         * 网络获取医生列表数据
         */
        void getMyDoctorDataFromNet();
    }
}

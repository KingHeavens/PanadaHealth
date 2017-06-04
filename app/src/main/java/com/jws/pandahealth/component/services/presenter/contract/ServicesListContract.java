package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;


import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/24.
 *
 */

public interface ServicesListContract {
    interface View extends BaseView {
        /**
         * 绑定数据
         * @param lists
         */
        void bindData(List<DoctorChatInfo> lists);

        /**
         * 刷新数据
         */
        void onRefreshFinished(List<DoctorChatInfo> lists);

    }

    interface Presenter extends BasePresenter<ServicesListContract.View> {
        /**
         * 获取历史记录
         * @param type
         */
        void getHistoryList(String type);
    }
}


package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/21.
 *
 */

public interface ServicesNewChatContract {
    interface View extends BaseView{
        /**
         * 绑定数据
         * @param lists
         */
        void bindData(List<DoctorChatInfo> lists);

        /**
         * 刷新数据
         */
        void onRefreshFinished(List<DoctorChatInfo> lists);

        /**
         * 更新数据
         */
        void notifyView();
    }

    interface Presenter extends BasePresenter<ServicesNewChatContract.View>{
        /**
         * 网络获取医生信息
         * @return
         */
        List<DoctorChatInfo> getDoctorChatListFromNet(boolean isRefresh);
    }
}

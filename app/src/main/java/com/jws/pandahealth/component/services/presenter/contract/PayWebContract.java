package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;

/**
 * Created by zhaijinjing on 2016/12/24.
 *
 */

public interface PayWebContract {
    interface View extends BaseView {
        /**
         * 关闭当前页面
         */
        void closeCurrentPage(int payStatus,String hxId,String orderId);
    }

    interface Presenter extends BasePresenter<PayWebContract.View> {
        void init();
        void updateFriendInfo(FriendInfo info);
    }
}


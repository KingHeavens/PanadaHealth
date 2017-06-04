package com.jws.pandahealth.component.services.presenter.contract;

import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.InitInfo;

/**
 * Created by zhaijinjing on 2016/12/21.
 *
 */
public interface PayContract {
    interface View extends BaseView {
        /**
         * 绑定无医生信息时的界面
         * @param info
         */
        void bindLayoutWithoutDoctorInfo(InitInfo info);

        /**
         * 绑定有医生信息时的界面
         * @param info
         */
        void bindLayoutData(DoctorInfo info);

        /**
         * 跳转到支付Web页面
         */
        void toPayWebPage(String orderId);


    }

    interface Presenter extends BasePresenter<View> {
        void init(DoctorInfo info);

        /**
         * 获取订单
         * @param doctorId
         * @param type
         */
        void getOrder(String doctorId,String type);
    }

}

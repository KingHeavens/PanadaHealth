package com.jws.pandahealth.component.askdoctor.presenter.contract;

import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;

/**
 * Created by Administrator on 2016/12/26.
 *
 */

public interface DoctorContract {
    interface View extends BaseView{
        void loadDoctorSuccess(DoctorInfo baseInfo);

        /**
         * 跳转到支付Web页面
         */
        void toPayWebPage(String orderId,String url);

        /**
         *支付成功
         */
        void onPaySuccess(String hxId);

        /**
         * 支付失败
         */
        void onPayFailed();

        /**
         * 检查是否有聊天余额
         * @param status
         * @param hxid
         */
        void onCheckResult(boolean status,String hxid);
        void getOrderError(String msg);
    }
    interface Presenter extends BasePresenter<View>{
        void loadDoctorInfo(String id, String token);
        /**
         * 获取订单
         * @param doctorId
         * @param type
         */
        void getOrder(String doctorId,String type);
        /**
         * 检查用户聊天余额
         * @return 是否可以继续发消息 true:可以发消息 false:不可以
         */
        boolean checkUserCanSendMessage(String hxId);
    }
}

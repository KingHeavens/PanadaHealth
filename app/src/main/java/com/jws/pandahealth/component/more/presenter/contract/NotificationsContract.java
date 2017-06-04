package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.more.model.bean.Subscription;

import java.util.List;


/**
 * Created by Administrator on 2017/1/12.
 */

public interface NotificationsContract {
    interface View extends BaseView{
        void loadNotificationSuccess(List<Subscription> subscriptions);
    }
    interface Presenter extends BasePresenter<View>{
        void loadNotification(String token);
    }
}

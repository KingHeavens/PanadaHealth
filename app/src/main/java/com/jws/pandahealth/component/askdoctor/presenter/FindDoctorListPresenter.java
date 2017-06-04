package com.jws.pandahealth.component.askdoctor.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.askdoctor.model.AskDoctorsRetrofitHelper;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.presenter.contract.FindDoctorListContract;

import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/23.
 */
public class FindDoctorListPresenter extends RxPresenter<FindDoctorListContract.View> implements FindDoctorListContract.Presenter {

    AskDoctorsRetrofitHelper askDoctorsRetrofitHelper;

    @Inject
    public FindDoctorListPresenter(AskDoctorsRetrofitHelper askDoctorsRetrofitHelper) {
        this.askDoctorsRetrofitHelper = askDoctorsRetrofitHelper;
    }

    @Override
    public void findDoctors(int department, int level, int service,int page,String token) {
        WeakHashMap w = MyApplication.getHttpDataMap();
        w.put("department", department + "");
        w.put("title", level + "");
        w.put("type", service + "");
        w.put("page", page + "");
        w.put("token", token );
        addSubscrebe(askDoctorsRetrofitHelper.findDoctors(JsonUtil.mapToJsonString(w))
                .compose(RxUtil.<BaseInfo<List<DoctorInfo>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DoctorInfo>>handleMyResult())
                .subscribe(new Action1<List<DoctorInfo>>() {
                    @Override
                    public void call(List<DoctorInfo> listBaseInfo) {
                        mView.findDoctorSuccess(listBaseInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handlerErrorException(throwable);
                    }
                }));
    }
}

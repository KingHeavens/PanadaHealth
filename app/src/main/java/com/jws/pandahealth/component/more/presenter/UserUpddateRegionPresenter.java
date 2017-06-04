package com.jws.pandahealth.component.more.presenter;

import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.RxPresenter;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.MoreRealmHelper;
import com.jws.pandahealth.component.more.model.MoreRetrofitHelper;
import com.jws.pandahealth.component.more.model.bean.RegionInfo;
import com.jws.pandahealth.component.more.presenter.contract.UserUpdateRegionContract;
import com.jws.pandahealth.component.services.util.JLog;
import java.util.List;
import java.util.WeakHashMap;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/28.
 */

public class UserUpddateRegionPresenter extends RxPresenter<UserUpdateRegionContract.View> implements UserUpdateRegionContract.Presenter{
    MoreRetrofitHelper retrofitHelper;
    MoreRealmHelper realmHelper;

    @Inject
    public UserUpddateRegionPresenter(MoreRetrofitHelper retrofitHelper, MoreRealmHelper realmHelper){
        this.retrofitHelper = retrofitHelper;
        this.realmHelper=realmHelper;
    }

    @Override
    public void getRegionList(String selectedCountryId) {
        WeakHashMap w = MyApplication.getHttpDataMap();
        w.put("fid",selectedCountryId == null ?"0":selectedCountryId);
        addSubscrebe(retrofitHelper.getRegionList(JsonUtil.mapToJsonString(w)).compose(RxUtil.<BaseInfo<List<RegionInfo>>>rxSchedulerHelper()).subscribe(new Action1<BaseInfo<List<RegionInfo>>>() {

            @Override
            public void call(BaseInfo<List<RegionInfo>> baseInfo) {
                JLog.e("baseInfo -->" + baseInfo);
//                UserInfo userInfo = MyApplication.getCurrentUser();
//                userInfo.userName = nickname;
//                MyApplication.setCurrentUser(userInfo);
//                RxBusUtil.getDefault().post(AppConfig.USER_UPDATE); // 注册广播发送广播
                mView.setRegionList(baseInfo);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mView.showError("");
            }
        }));
    }

    @Override
    public void saveLocalCityInfo(String selectedCountryId,String selectedCountryName,String selectedCityId,String selectedCityName){
        realmHelper.realmUpdateUserRegion(String.format("%s,%s",selectedCountryId,selectedCityId),String.format("%s %s",selectedCountryName,selectedCityName));
    }


}

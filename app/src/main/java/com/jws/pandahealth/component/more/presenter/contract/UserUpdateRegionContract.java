package com.jws.pandahealth.component.more.presenter.contract;

import com.jws.pandahealth.component.app.base.BasePresenter;
import com.jws.pandahealth.component.app.base.BaseView;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.bean.RegionInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public interface UserUpdateRegionContract {

    interface View extends BaseView{
        void setRegionList(BaseInfo<List<RegionInfo>> list);
    }
    interface Presenter extends BasePresenter<View>{
        void getRegionList(String id);
        void saveLocalCityInfo(String selectedCountryId,String selectedCountryName,String selectedCityId,String selectedCityName);

    }
}

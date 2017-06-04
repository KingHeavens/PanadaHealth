package com.jws.pandahealth.component.askdoctor.model;

import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface AskDoctorsApi {
    @FormUrlEncoded
    @POST("?s=Doctors/listing")
    Observable<BaseInfo<List<DoctorInfo>>> findDoctors(@Field("data") String data);

    @FormUrlEncoded
    @POST("?s=Doctors/detail")
    Observable<BaseInfo<DoctorInfo>> findDoctor(@Field("data") String data);
    @FormUrlEncoded
    @POST("?s=Service/quickAsk")
    Observable<BaseInfo> questionNew(@Field("data") String data);
}

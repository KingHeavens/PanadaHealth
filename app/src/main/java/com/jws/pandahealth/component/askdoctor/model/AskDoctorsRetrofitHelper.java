package com.jws.pandahealth.component.askdoctor.model;

import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/22.
 *
 */
public class AskDoctorsRetrofitHelper extends RetrofitHelper {
    private static AskDoctorsRetrofitHelper instance;
    public static AskDoctorsApi askDoctorsApi=null;
    public AskDoctorsRetrofitHelper(){
        super();
        askDoctorsApi=getAskDoctorsApi();
    }

    public static synchronized AskDoctorsRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new AskDoctorsRetrofitHelper();
        }
        return instance;
    }

    private static AskDoctorsApi getAskDoctorsApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getServerPath())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(AskDoctorsApi.class);
    }

    public  Observable<BaseInfo<List<DoctorInfo>>> findDoctors(String data){
        return askDoctorsApi.findDoctors(data);
    }

    public  Observable<BaseInfo<DoctorInfo>> findDoctor(String data){
        return askDoctorsApi.findDoctor(data);
    }
    public  Observable<BaseInfo> questionNew(String data){
        return askDoctorsApi.questionNew (data);
    }

}

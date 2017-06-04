package com.jws.pandahealth.component.more.model;

import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.bean.RegionInfo;
import com.jws.pandahealth.component.more.model.bean.Subscription;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/27.
 */
public class MoreRetrofitHelper extends RetrofitHelper {

    private static MoreRetrofitHelper instance;
    MoreApi moreApi =null;

    @Inject
    public MoreRetrofitHelper(){
        super();
        moreApi = getMoreApi();
    }


    public static synchronized MoreRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new MoreRetrofitHelper();
        }
        return instance;
    }

    private  MoreApi getMoreApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getServerPath())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(MoreApi.class);
    }

    public Observable<BaseInfo<String>> uploadImage(RequestBody params ,Map<String,String> map){
        return moreApi.uploadImage(params, map);
    }

    public Observable<BaseInfo> updateUserInfo(String data){
        return moreApi.updateUserInfo(data);
    }
    public Observable<BaseInfo> updatePwd(String data){
        return moreApi.updatePwd(data);
    }

    public Observable<BaseInfo<List<Subscription>>> notifications(String data){
        return moreApi.getNotifications(data);
    }
    public Observable<BaseInfo<List<RegionInfo>>> getRegionList(String data){
        return moreApi.getRegionList(data);
    }

}

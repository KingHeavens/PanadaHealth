package com.jws.pandahealth.component.app.model;

import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by codeest on 2016/8/3.
 * `
 */
public class AppRetrofitHelper extends RetrofitHelper {

    private static AppApis appApis = null;
    public AppRetrofitHelper() {
        super();
        appApis = getAppApis();
    }

    private static AppRetrofitHelper instance;
    public static synchronized AppRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new AppRetrofitHelper();
        }
        return instance;
    }

    private static AppApis getAppApis() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getServerPath())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(AppApis.class);
    }

    /**
     * 用户登录
     *
     */
    public Observable<BaseInfo<UserInfo>> login(String data){
        return appApis.login(data);
    }

    public Observable<BaseInfo<UserInfo>> getuserinfo(String data){
        return appApis.getuserinfo(data);
    }

    public Observable<BaseInfo<UserInfo>> reg(String mapjsonString) {
        return appApis.reg(mapjsonString);
    }

    public Observable<BaseInfo> findpwd(String mapjsonString) {
        return appApis.findpwd(mapjsonString);
    }
    public Observable<BaseInfo> initApp(String mapjsonString) {
        return appApis.initApp(mapjsonString);
    }
    public Observable<BaseInfo> cuntinueUser(String mapjsonString) {
        return appApis.cuntinueUser(mapjsonString);
    }

    public Observable<BaseInfo<String>> upload(RequestBody params ,Map<String,String> map){
//        return appApis.uploadImage(params,String.valueOf(AppConfig.VERSION_CODE), AppConfig.DEVICE_TYPE,AppConfig.DEVICE_CODE,
//                MD5Util.getMD5(AppConfig.DEVICE_CODE + DateUtil.getCurrentDate()
//                        + AppConfig.PUBLIC_KEY),MyApplication.getCurrentUser() != null ? MyApplication.getCurrentUser().token : "");
        return appApis.uploadImage(params, map);
    }


    /**
     * 初始化应用信息
     * @param data
     * @return
     */
    public Observable<BaseInfo<InitInfo>> getInit(String data) {
        return appApis.getinit(data);
    }
}

package com.jws.pandahealth.component.app.model;

import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by jws on 2016/8/2.
 *
 */
public interface AppApis{

    /**
     * 登录
     * @return
     *
     */
    @FormUrlEncoded
    @POST("?s=Accounts/login")
    Observable<BaseInfo<UserInfo>> login(@Field("data") String data);

    /**
     * 登录
     * @return
     *
     */
    @FormUrlEncoded
    @POST("?s=Accounts/get_info")
    Observable<BaseInfo<UserInfo>> getuserinfo(@Field("data") String data);


    /**
     * 注册
     * @return
     */
    @FormUrlEncoded
    @POST("/?s=Accounts/register")
    Observable<BaseInfo<UserInfo>> reg(@Field("data") String data);
    /**
     * 找回密码
     * @return
     */
    @FormUrlEncoded
    @POST("/?s=Accounts/reset_password")
    Observable<BaseInfo> findpwd(@Field("data") String data);
    /**
     * 初始化数据
     * @return
     */
    @FormUrlEncoded
    @POST("/?s=act/initCashHandler")
    Observable<BaseInfo> initApp(@Field("data") String data);

//    /**
//     * 上传图片
//     * @return
//     *
//     */
//    @FormUrlEncoded
//    @POST("?s=act/upload")
//    Observable<BaseInfo<UserInfo>> upload(@Field("data") String data);


    //     * 上传一张图片
    //     * @param description
    //     * @param imgs
    //     * @return
    //     */
//    @POST("?s=Upload/image")
//    Observable<BaseInfo<UploadImageInfo>> upload(@PartMap Map<String,RequestBody> params);
//    @Multipart
//    @POST("?s=Upload/image")
//    Observable<BaseInfo<UploadImageInfo>> uploadImage(@Part("file\"; filename=\"img\"")RequestBody imgs,@Part("ver") String ver
//            ,@Part("device_type") String device_type,@Part("device_code") String device_code,@Part("secret") String secret,@Part("token") String token);
    @Multipart
    @POST("?s=Upload/image")
    Observable<BaseInfo<String>> uploadImage(@Part("img\"; filename=\"file.png")RequestBody imgs, @PartMap  Map<String,String> data);

    @FormUrlEncoded
    @POST("?s=Accounts/set_userinfo")
    Observable<BaseInfo> cuntinueUser(@Field("data") String data);

    /**
     * 初始化应用
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Main/get_initialize")
    Observable<BaseInfo<InitInfo>> getinit(@Field("data") String data);
}

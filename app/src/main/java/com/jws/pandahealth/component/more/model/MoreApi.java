package com.jws.pandahealth.component.more.model;

import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.model.bean.RegionInfo;
import com.jws.pandahealth.component.more.model.bean.Subscription;

import java.util.List;
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
 * Created by Administrator on 2016/12/27.
 */

public interface MoreApi {
    //     * 上传图片
    //     * @param description
    //     * @param imgs
    //     * @return
    //     */
    @Multipart
    @POST("?s=Upload/image")
    Observable<BaseInfo<String>> uploadImage(@Part("img\"; filename=\"file.png")RequestBody imgs, @PartMap  Map<String,String> data);

    @FormUrlEncoded
    @POST("?s=Accounts/set_userinfo")
    Observable<BaseInfo> updateUserInfo(@Field("data") String data);

    @FormUrlEncoded
    @POST("?s=Accounts/reset_password")
    Observable<BaseInfo> updatePwd(@Field("data") String data);

    @FormUrlEncoded
    @POST("?s=Other/get_area")
    Observable<BaseInfo<List<RegionInfo>>> getRegionList(@Field("data") String data);
    @FormUrlEncoded
    @POST("?s=Other/get_subscription")
    Observable<BaseInfo<List<Subscription>>> getNotifications(@Field("data") String data);
}

package com.jws.pandahealth.component.services.model;

import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.RetrofitHelper;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;
import com.jws.pandahealth.component.services.model.bean.OrderInfo;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ZJJ on 2016/12/22.
 *
 */

public class ServicesRetrofitHelper extends RetrofitHelper {
    private static ServicesRetrofitHelper instance;
    public static ServicesApi servicesApi =null;
    public ServicesRetrofitHelper(){
        super();
        servicesApi =getServicesApi();
    }
    public static synchronized ServicesRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new ServicesRetrofitHelper();
        }
        return instance;
    }
    private static ServicesApi getServicesApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.getServerPath())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ServicesApi.class);
    }


    /**
     * 获取new chat 列表
     * @param data
     * @return
     */
    public Observable<BaseInfo<List<DoctorChatInfo>>> getNewChatList(String data){
        return servicesApi.getNewChatList(data);
    }

    /**
     * 获取My Doctor 列表
     * @param data
     * @return
     */
    public Observable<BaseInfo<List<MyDoctorInfo>>> getMyDoctorList(String data){
        return servicesApi.getMyDoctorList(data);
    }

    /**
     * 获取 ChatHistory列表
     * @param data
     * @return
     */
    public Observable<BaseInfo<List<DoctorChatInfo>>> getChatHistoryList(String data){
        return servicesApi.getChatHistoryList(data);
    }

    /**
     * 获取历史订单对话接口
     * @param data
     * @return
     */
    public Observable<BaseInfo<List<ChatInfo>>> getChatContentHistory(String data){
        return servicesApi.getChatContentHistory(data);
    }

    /**
     * 发送消息
     * @param data
     * @return
     */
    public Observable<BaseInfo> sendMessage(String data){
        return servicesApi.sendMessage(data);
    }

    /**
     * 获取好友列表
     * @param data
     * @return
     */
    public Observable<BaseInfo<List<FriendInfo>>> getFriendUserInfo(String data){
        return servicesApi.getFriendUserInfo(data);
    }

    /**
     * 支付
     * @param data
     * @return
     */
    public Observable<BaseInfo<OrderInfo>> getOrder(@Field("data") String data){
        return servicesApi.getOrder(data);
    }
}

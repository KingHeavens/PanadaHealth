package com.jws.pandahealth.component.services.model;

import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;
import com.jws.pandahealth.component.services.model.bean.OrderInfo;

import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ZJJ on 2016/12/22.
 *
 */

public interface ServicesApi {

    /**
     * 获取new chat 列表
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Service/myNew")
    Observable<BaseInfo<List<DoctorChatInfo>>> getNewChatList(@Field("data") String data);

    /**
     * 获取My Doctor 列表
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Service/myDoctor")
    Observable<BaseInfo<List<MyDoctorInfo>>> getMyDoctorList(@Field("data") String data);

    /**
     * 获取 ChatHistory列表
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Service/getHistoryText")
    Observable<BaseInfo<List<DoctorChatInfo>>> getChatHistoryList(@Field("data") String data);

    /**
     * 获取历史订单对话接口
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Service/getHistoryRecords")
    Observable<BaseInfo<List<ChatInfo>>> getChatContentHistory(@Field("data") String data);

    /**
     * 发送消息
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Chat/send_msg")
    Observable<BaseInfo> sendMessage(@Field("data") String data);

    /**
     * 获取好友列表
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Chat/getMyFriendListInfo")
    Observable<BaseInfo<List<FriendInfo>>> getFriendUserInfo(@Field("data") String data);

    /**
     * 支付
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("?s=Order/create")
    Observable<BaseInfo<OrderInfo>> getOrder(@Field("data") String data);
}

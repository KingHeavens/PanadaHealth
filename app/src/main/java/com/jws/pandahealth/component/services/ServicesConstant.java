package com.jws.pandahealth.component.services;

import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.BuildConfig;
import com.jws.pandahealth.component.AppConfig;

import java.util.HashMap;

/**
 * Created by zhaijinjing on 2016/12/24.
 * ServicesConstant
 */

public class ServicesConstant extends EaseConstant{

    public static final String TYPE_TEXT = "1";
    public static final String TYPE_VOICE_CALL = "2";
    public static final String TYPE_PRIVATE_DOCTOR = "3";
    public static final String SERVICES_TYPE = "services_type";
    public static final String SERVICES_DOCTOR_ID = "SERVICES_DOCTOR_ID";
    public static final String SERVICES_ORDER_ID = "services_order_id";
    public static final String RECEIVED_MSG = "received_msg";
    public static final String READ_MSG = "read_msg";
    /**
     * Pay about
     */
    public static final String PAY_WEB_URL = AppConfig.getServerPath() + "?s=Order/create";
    public static final String PAY_DOCTOR_HXID = "PAY_DOCTOR_HXID";
    public static final String PAY_ORDER_ID = "OrderId";
    public static final String PAY_WEB_ACTIVITY = BuildConfig.APPLICATION_ID + ".component.services.ui.activity.PayWebActivity";
    public static final String DOCTOR_ACTIVITY = BuildConfig.APPLICATION_ID + ".component.askdoctor.ui.activity.DoctorActivity";
    public static final String QUESTION_NEW_ACTIVITY = BuildConfig.APPLICATION_ID + ".component.askdoctor.ui.activity.QuestionNewActivity";
    public static final int REQUEST_PAY = 1000;
    public static final int PAY_SUCCESS  = 2001;
    public static final int PAY_FAILED = 2002;
    public static final int PAY_CANCEL = 2003;
    public static final int PAY_SUCCESS_BACK = 2004;
    public static final int PAY_FAILED_BACK = 2006;
    public static final String PAY_SUCCESS_RECEIVED = "PAY_SUCCESS_RECEIVED";
    public static final String PAY_SP = "PAY_SP";
    public static final String PAY_URL = "PAY_URL";
    public static final String CHAT_ACTIVITY = BuildConfig.APPLICATION_ID  + ".component.services.ui.activity.ChatActivity";
    public static final String CLEAR_UNREAD_MSG = "CLEAR_UNREAD_MSG";
    public static final String ACCOUNT_REMOVED = "ACCOUNT_REMOVED";
    public static final String ACCOUNT_CONFLICT = "ACCOUNT_CONFLICT";
    public static final String ACCOUNT_FORBIDDEN = "ACCOUNT_FORBIDDEN";

    private HashMap<String ,String> datas = new HashMap<>();
    private int postType;
    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public void addData(String key, String value){
        datas.put(key,value);
    }
    public String getData(String key){
        return datas.get(key);
    }
}

package com.jws.pandahealth.component.services.util;

import com.jws.pandahealth.component.services.model.bean.FriendInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaijinjing on 2017/1/7.
 *
 */

public class FriendInfoJsonUtil {
    private static final String TAG  = "FriendInfoJsonUtil";
    public static FriendInfo parseFriendInfo(String json){
        FriendInfo info = new FriendInfo();
        try {
            JSONObject jsonObject = new JSONObject(json);
            info.hxId = jsonObject.optString("hxId");
            info.doctorName = jsonObject.optString("doctorName");
            info.doctorIcon = jsonObject.optString("doctorIcon");
            info.expirationTime = jsonObject.optString("expirationTime");
            info.count = jsonObject.optString("count");
            info.doctorId = jsonObject.optString("doctorId");
        } catch (JSONException e) {
            e.printStackTrace();
            JLog.e(TAG + ": " + e.getMessage());
        }
        return info;
    }
}

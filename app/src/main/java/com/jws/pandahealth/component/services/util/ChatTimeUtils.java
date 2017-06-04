package com.jws.pandahealth.component.services.util;

import android.text.TextUtils;

/**
 * Created by zhaijinjing on 2017/1/12.
 *
 */

public class ChatTimeUtils {
    private static final String TAG = "ChatTimeUtils";


    public static boolean closeEnough(String timeStamp,String preStamp){
        if(TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(preStamp))
            return false;
        long now = 0L;
        long pre = 0L;
        try{
            now = Long.parseLong(timeStamp);
            pre = Long.parseLong(preStamp);
        }catch (NumberFormatException e){
            JLog.e(TAG + " NumberFormatException:" + e.getMessage());
        }
        return now - pre < 30000L;
    }
}

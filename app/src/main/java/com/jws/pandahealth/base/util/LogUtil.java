package com.jws.pandahealth.base.util;

import android.util.Log;

import com.jws.pandahealth.BuildConfig;
import com.jws.pandahealth.component.MyApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by codeest on 2016/8/3.
 */
public class LogUtil {

    private static boolean isDebug = BuildConfig.DEBUG;
    private static String TAG = getTag();

    public static void initLogger(){
        if(isDebug)
            Logger.init(TAG).setLogLevel(LogLevel.FULL);
        else
            Logger.init(TAG).setLogLevel(LogLevel.NONE);
    }

    public static String getTag(){
        String[] strs = MyApplication.getInstance()
                .getPackageName()
                .split("\\.");
        if(strs == null || strs.length == 0){
            return "android";
        }else{
            return strs[strs.length - 1];
        }
    }

    public static void e(Object o) {
        if(isDebug) {
            Log.e(TAG, String.valueOf(o));
        }
    }

    public static void e(String tag,Object o) {
        if(isDebug) {
            Log.e(tag, String.valueOf(o));
        }
    }

    public static void d(Object o) {
        if(isDebug) {
            Log.d(TAG, String.valueOf(o));
        }
    }

    public static void d(String tag,Object o) {
        if(isDebug) {
            Log.d(tag, String.valueOf(o));
        }
    }

    public static void json(String json)
    {
        if(isDebug) {
            Logger.json(json);
        }
    }

}

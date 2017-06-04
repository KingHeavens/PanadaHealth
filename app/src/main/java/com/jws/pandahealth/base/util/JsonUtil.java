package com.jws.pandahealth.base.util;

import android.util.Log;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2016/12/2.
 */

public class JsonUtil {
    public static String mapToJsonString(WeakHashMap map){
            try {
                JSONObject params2 = new JSONObject();
                if (map.isEmpty())
                    map = new WeakHashMap();
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    String value = (String) map.get(name);
                    params2.put(name, value);
                }
                Log.e("OkHttp","Request Param:" + params2.toString());
                return params2.toString();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
    }
}

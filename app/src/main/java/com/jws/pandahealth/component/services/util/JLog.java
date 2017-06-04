package com.jws.pandahealth.component.services.util;

import com.jws.pandahealth.base.util.LogUtil;

/**
 * Created by zhaijinjing on 2016/12/22.
 *
 */

public class JLog {
    private static final String TAG = "JLOG";

    public static void e(String log) {
        LogUtil.e(TAG, log);
    }

    public static void d(String log) {
        LogUtil.d(TAG, log);
    }
}

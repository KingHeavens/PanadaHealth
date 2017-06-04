package com.jws.pandahealth.base.util;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Administrator on 2016/12/22.
 */

public class SDCardUtils {
    /***
     * 获取SD卡cache目录
     *
     * @param context
     * @return
     */
    public static String getSDCachePath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /***
     * 获取SD卡File目录
     *
     * @param context
     * @return
     */
    public static String getSDFilePath(Context context, String fileType) {
//        if (hasSDCard()) {
//            return context.getExternalFilesDir(fileType).getPath();
//        } else {
            return context.getFilesDir().getPath();
//        }
    }

    /***
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }
}

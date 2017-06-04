package com.jws.pandahealth.base.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

public class DeviceIdUtil {

    private static String deviceId = null; //自定义设备ID

    /**
     * 获取当前设备deviceId
     */
    public static String getmDeviceID(Context context) {
        if (deviceId == null) {
            deviceId = SharedPrefUtils.getString(context, "deviceId", "");
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getCustomDeviceId(context).replaceAll(":","_");
                SharedPrefUtils.saveString(context, "deviceId", deviceId);
            }
        }
        return deviceId;
    }

    /**
     * 根据wifi mac 和imei和sn 生成 唯一deviceId
     */
    private static String getCustomDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
//        deviceId.append("a");// 渠道标志
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                LogUtil.e("getCustomDeviceId --> " + deviceId.toString());
                return deviceId.toString();
            }

            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                LogUtil.e("getCustomDeviceId --> " + deviceId.toString());
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                LogUtil.e("getCustomDeviceId --> " + deviceId.toString());
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("uuid");
                deviceId.append(uuid);
                LogUtil.e("getCustomDeviceId --> " + deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        LogUtil.e("getCustomDeviceId --> " + deviceId.toString());

        return deviceId.toString();
    }


    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        String uuid = SharedPrefUtils.getString(context, "uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SharedPrefUtils.saveString(context, "uuid", uuid);
        }
        return uuid;
    }

}
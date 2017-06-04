package com.jws.pandahealth.component.services.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.view.PayAskDialog;
import com.jws.pandahealth.component.askdoctor.view.PayDetailDialog;
import com.jws.pandahealth.component.services.widget.CommonDialog;
import com.jws.pandahealth.component.services.widget.ServicesPayDetailDialog;

/**
 * Created by zhaijinjing on 2017/1/5.
 */

public class DialogUtil {
    /**
     * Dialog  ok and cancel
     * @param activity
     * @param content
     * @param okClick
     * @param cancelClick
     */
    public static void showDialog(Activity activity, String content,
                                  View.OnClickListener okClick, View.OnClickListener cancelClick) {
        CommonDialog dialog = new CommonDialog(activity, CommonDialog.COMMON_DIALOG_NORAML);
        dialog.setContent(content);
        dialog.setOkOnClick(okClick);
        dialog.setCancleOnClick(cancelClick);
        dialog.show();
    }

    /**
     *Dialog  ok
     * @param activity
     * @param title
     * @param content
     * @param okClick
     */
    public static void showDialog(Activity activity,String title, String content,
                                  View.OnClickListener okClick) {
        CommonDialog dialog = new CommonDialog(activity, CommonDialog.COMMON_DIALOG_NORAML);
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.setOkOnClick(okClick);
        dialog.setCancleGone();
        dialog.show();
    }

    /**
     *Dialog notitle  ok and cancel
     * @param activity
     * @param content
     * @param okClick
     * @param cancelClick
     */
    public static void showNoTitleDialog(Activity activity, String content,
                                         View.OnClickListener okClick, View.OnClickListener cancelClick) {
        CommonDialog dialog = new CommonDialog(activity, CommonDialog.COMMON_DIALOG_NOTITLE);
        dialog.setTitle(content);
        dialog.setOkOnClick(okClick);
        dialog.setCancleOnClick(cancelClick);
        dialog.show();
    }

    /**
     *Dialog  ok
     * @param activity
     * @param content
     * @param okClick
     */
    public static void showNoTitleDialog(Activity activity, String content,
                                         View.OnClickListener okClick) {
        CommonDialog dialog = new CommonDialog(activity, CommonDialog.COMMON_DIALOG_NOTITLE);
        dialog.setTitle(content);
        dialog.setOkOnClick(okClick);
        dialog.setCancleGone();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 显示支付详情Dialog
     */
    public static void showPayDetailDialog(Activity activity, DoctorInfo info,String type,View.OnClickListener ok){
        ServicesPayDetailDialog dialog = new ServicesPayDetailDialog(activity);
        dialog.setDoctorInfo(info,type);
        dialog.setOnPayClickListener(ok);
        dialog.show();
    }
    /**
     * 显示支付详情Dialog
     */
    public static void showPayDialog(Activity activity, DoctorInfo info,String type,View.OnClickListener ok){
        PayDetailDialog dialog = new PayDetailDialog(activity);
        dialog.setDoctorInfo(info,type);
        dialog.setOnPayClickListener(ok);
        dialog.show();
    }
    /**
     * 显示支付详情Dialog
     */
    public static void showPayDialog(Activity activity, String money,String time,View.OnClickListener ok){
        PayAskDialog dialog = new PayAskDialog(activity,money,time);
        dialog.setOnPayClickListener(ok);
        dialog.show();
    }

}

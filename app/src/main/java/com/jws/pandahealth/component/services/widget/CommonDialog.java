package com.jws.pandahealth.component.services.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.jws.pandahealth.R;


public class CommonDialog extends Dialog {

    public static final int COMMON_DIALOG_NORAML = 0;//带标题
    public static final int COMMON_DIALOG_NOTITLE = 1;//不带标题
    public static final int COMMON_DIALOG_CUSTOME = 2;//不带标题
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvOk;
    private TextView mTvCancle;

    public CommonDialog(Context context, int style) {
        super(context, R.style.MyDialog);
        if (style == COMMON_DIALOG_NORAML) {
            setContentView(R.layout.view_common_dialog);
            mTvContent = (TextView) findViewById(R.id.content);
        } else if(style == COMMON_DIALOG_NOTITLE){
            setContentView(R.layout.view_common_dialog_notitle);
        }
        mTvTitle = (TextView) findViewById(R.id.title);
        mTvCancle = (TextView) findViewById(R.id.cancle);
        mTvOk = (TextView) findViewById(R.id.ok);
        mTvOk.setTag(CommonDialog.this);
        mTvCancle.setTag(CommonDialog.this);
    }


    public TextView getTitleView() {
        return mTvTitle;
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setTitle(String t) {
        mTvTitle.setText(t);
    }

    public void setContent(String c) {
        if (mTvContent != null)
            mTvContent.setText(c);
    }

    public void setOkContent(String c) {
        mTvOk.setText(c);
    }

    public void setCancleContent(String c) {
        mTvCancle.setText(c);
    }

    public void setOkOnClick(View.OnClickListener listener) {
        mTvOk.setOnClickListener(listener);
    }

    public void setCancleOnClick(View.OnClickListener listener) {
        mTvCancle.setOnClickListener(listener);
    }

    public void setCancleGone() {
        mTvOk.setBackgroundResource(R.drawable.about_me_dialog_ok_bg2);
        mTvCancle.setVisibility(View.GONE);
    }


}

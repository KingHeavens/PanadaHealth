package com.jws.pandahealth.component.services.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.ui.activity.MainActivity;
import com.jws.pandahealth.component.services.ServicesConstant;

/**
 * Created by kangzhiWork on 2016/6/29.
 *
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MESSAGE = "DialogActivityMESSAGE";
    public static final String TYPE = "DialogActivityt6ypw";
    public static final String TYPE_PAY_SUCCESS = "success pay";
    private TextView mContent;
    private TextView mCancle;
    private TextView mOk;
    private Intent mIntent;
    private String mType;
    private TextView mTvTitle;
    private String mMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_common_dialog);
        initView();
        initData();
    }


    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        mCancle = (TextView) findViewById(R.id.cancle);
        mOk = (TextView) findViewById(R.id.ok);

        mCancle.setOnClickListener(this);
        mOk.setOnClickListener(this);

        //mTvTitle.setVisibility(View.GONE);
    }

    private void initData() {
        mIntent = getIntent();
        mType = mIntent.getStringExtra(TYPE);
        mMsg = mIntent.getStringExtra(MESSAGE);
        mContent.setText(mMsg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle:
                onBackPressed();
                break;
            case R.id.ok:
                okMethod();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 点击OK按钮执行的操作
     */
    private void okMethod() {
        Intent paramIntent = getParamIntent(null, mType, getApplicationContext());
        startActivity(paramIntent);
    }

    /**
     * 给Intent填充数据
     * @param it
     * @param typeStr
     * @param context
     * @return
     */
    private Intent getParamIntent(Intent it,String typeStr,Context context) {
        if(it == null)
            it = new Intent();

        switch (typeStr){
            case TYPE_PAY_SUCCESS:
                it = new Intent(getApplicationContext(), MainActivity.class);
                it.putExtra("type",ServicesConstant.PAY_SUCCESS_RECEIVED);
                it.putExtra(ServicesConstant.PAY_ORDER_ID,mIntent.getStringExtra(ServicesConstant.PAY_ORDER_ID));
                it.putExtra(ServicesConstant.PAY_DOCTOR_HXID,mIntent.getStringExtra(ServicesConstant.PAY_DOCTOR_HXID));
                startActivity(it);
                break;
            default:
                break;
        }
        return it;
    }
}

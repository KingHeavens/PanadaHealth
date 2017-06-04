package com.jws.pandahealth.component.app.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.CheckEmailPresenter;
import com.jws.pandahealth.component.app.presenter.contract.CheckEmailContract;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/30.
 */

public class CheckEmailActivity extends BaseActivity<CheckEmailPresenter> implements CheckEmailContract.View,View.OnClickListener{

    @BindView(R.id.head_title_tv)
    TextView headTitleTv;

    @BindView(R.id.btn_gologin)
    Button btn_gologin;

    @BindView(R.id.superemail)
    TextView superemail;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.base_activity_checkemail;
    }

    @Override
    protected void initEventAndData() {
        headTitleTv.setText(getString(R.string.forgetpassword1));
        superemail.setOnClickListener(this);
        btn_gologin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.superemail){
            Intent i = new Intent(Intent.ACTION_SEND);
            // i.setType("text/plain"); //模拟器请使用这行
            i.setType("message/rfc822"); // 真机上使用这行
            i.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { getString(R.string.supportemails) });
            startActivity(Intent.createChooser(i,
                    "Select email application."));
        }else if(v.getId()==R.id.btn_gologin){
            finish();
        }
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }

}

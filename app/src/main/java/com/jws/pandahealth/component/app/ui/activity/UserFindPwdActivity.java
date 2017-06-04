package com.jws.pandahealth.component.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.presenter.UserFindPwdPresenter;
import com.jws.pandahealth.component.app.presenter.contract.UserFindPwdContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 *
 */

public class UserFindPwdActivity extends BaseActivity<UserFindPwdPresenter> implements UserFindPwdContract.View,View.OnClickListener{
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;

    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.btn_findpwd)
    Button btn_findpwd;

    @BindView(R.id.superemail)
    TextView superemail;
    @BindView(R.id.nevermind)
    TextView nevermind;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.base_activity_findpwd;
    }

    @Override
    protected void initEventAndData() {
        headTitleTv.setText(getString(R.string.forgetpassword1));
        superemail.setOnClickListener(this);
        nevermind.setOnClickListener(this);
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                inputFilter(mEtAccount.getText().toString().trim());
            }
        };
        mEtAccount.addTextChangedListener(textWatcher);

    }

    public void inputFilter(String account) {
        String check = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(account);
        if(matcher.matches()){
            btn_findpwd.setBackgroundColor(Color.parseColor("#99E93C4F"));
            btn_findpwd.setClickable(true);
            btn_findpwd.setEnabled(true);
        }else{
            btn_findpwd.setBackgroundColor(Color.parseColor("#FBD5D9"));
            btn_findpwd.setClickable(false);
            btn_findpwd.setEnabled(false);
        }
    }




    @OnClick(R.id.btn_findpwd)
    void onClick(){
        showDialog();
        mPresenter.findPWD(mEtAccount.getText().toString().trim());
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
        }else if(v.getId()==R.id.nevermind){
            finish();
        }
    }

    @Override
    public void noHttpError() {
        closeDialog();
        ToastUtil.show(R.string.http_no_net_tip);
    }

    @Override
    public void findSuccess(BaseInfo baseInfo) {
        closeDialog();
        if(baseInfo.getStatus().equals(AppConfig.DATA_SUCCESS)){
            Intent it=new Intent(this,CheckEmailActivity.class);
            finish();
        }else{
            showError(baseInfo.getErrmsg());
        }
    }
    @Override
    public void showError(String msg) {
        closeDialog();
        ToastUtil.show(msg);
    }

}

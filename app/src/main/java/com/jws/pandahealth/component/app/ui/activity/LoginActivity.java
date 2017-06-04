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
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.LoginPresenter;
import com.jws.pandahealth.component.app.presenter.contract.LoginContract;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View,View.OnClickListener{
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.findpass)
    TextView findpass;

    //登录成功修改appconfig.token   退出后滞空

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.base_activity_login;
    }

    @Override
    protected void initEventAndData() {
        headTitleTv.setText(getString(R.string.signin));
        findpass.setOnClickListener(this);
        signup.setOnClickListener(this);
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                    inputFilter(mEtAccount.getText().toString().trim(),mEtPwd.getText().toString().trim());
            }
        };
        mEtAccount.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);

    }

    //@Override
    public void inputFilter(String account, String pass) {
        if(account.trim().equals("") || pass.trim().equals("")){
            mBtnLogin.setBackgroundColor(Color.parseColor("#FBD5D9"));
            mBtnLogin.setClickable(false);
            mBtnLogin.setEnabled(false);
        }else{
            mBtnLogin.setBackgroundColor(Color.parseColor("#99E93C4F"));
            mBtnLogin.setClickable(true);
            mBtnLogin.setEnabled(true);
        }
    }

    public void jump2MainOrFinish() {
        showToast(getString(R.string.login_success));
        RxBusUtil.getDefault().post(AppConfig.LOGIN_SUCCESS);
        finish();
    }

    @Override
    public void jump2RegPage() {

    }

    @Override
    public void showError(final String msg) {
        closeDialog();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(msg);
            }
        });
        JLog.e(msg);
    }

    @OnClick(R.id.btn_login)
    void onClick(){
        String check = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mEtAccount.getText().toString().trim());
        if(!matcher.matches()){
            ToastUtil.shortShow(R.string.emaliinputgood);
            return;
        }
        showDialog();
        mPresenter.login(mEtAccount.getText().toString().trim(),mEtPwd.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signup){
            Intent it=new Intent(this,UserRegActivity.class);
            startActivity(it);
        }else if(v.getId()==R.id.findpass){
            Intent it=new Intent(this,WebViewActivity.class);
            it.putExtra("title",getString(R.string.forgetpassword1));
            it.putExtra("url","?s=Accounts/find_password");
            it.putExtra("isPost",true);
            startActivity(it);
        }
    }

    @Override
    public void noHttpError() {
        closeDialog();
        ToastUtil.show(R.string.http_no_net_tip);
    }
}

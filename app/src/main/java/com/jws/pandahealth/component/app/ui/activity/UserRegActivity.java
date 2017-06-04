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
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.presenter.UserRegPresenter;
import com.jws.pandahealth.component.app.presenter.contract.UserRegContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;


/**
 * Created by Administrator on 2016/12/1.
 */
public class UserRegActivity extends BaseActivity<UserRegPresenter> implements UserRegContract.View, View.OnClickListener {
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;

    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_reg)
    TextView btn_reg;

    @BindView(R.id.terms)
    TextView terms;
    @BindView(R.id.privacypolicy)
    TextView privacypolicy;
    @BindView(R.id.login)
    TextView login;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.base_activity_reg;
    }

    @Override
    protected void initEventAndData() {
        headTitleTv.setText(getString(R.string.signup));
        login.setOnClickListener(this);
        terms.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
        privacypolicy.setOnClickListener(this);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputFilter(mEtAccount.getText().toString().trim(), mEtPwd.getText().toString().trim());
            }
        };
        mEtAccount.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);

    }

    public void inputFilter(String account, String pass) {
        if(account.trim().equals("") || pass.trim().equals("")){
            btn_reg.setBackgroundColor(Color.parseColor("#FBD5D9"));
            btn_reg.setClickable(false);
            btn_reg.setEnabled(false);
        } else {
            btn_reg.setBackgroundColor(Color.parseColor("#99E93C4F"));
            btn_reg.setClickable(true);
            btn_reg.setEnabled(true);
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            finish();
        } else if (v.getId() == R.id.terms) {
            Intent it=new Intent(this,WebViewActivity.class);
            it.putExtra("title",getString(R.string.termsofuse));
            it.putExtra("url", AppConfig.TERMSOFUSEURL);
            startActivity(it);
            it.putExtra("isaddjava",false);
        } else if (v.getId() == R.id.privacypolicy) {
            Intent it=new Intent(this,WebViewActivity.class);
            it.putExtra("title",getString(R.string.privacypolicy));
            it.putExtra("url",AppConfig.PRIVACYURL);
            it.putExtra("isaddjava",false);
            startActivity(it);
        } else if(v.getId() == R.id.btn_reg){
            String check = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(mEtAccount.getText().toString().trim());
            if(!matcher.matches()){
                ToastUtil.shortShow(R.string.emaliinputgood);
                return;
            }
            showDialog();
            mPresenter.reg(mEtAccount.getText().toString().trim(), mEtPwd.getText().toString().trim());
        }
    }

    @Override
    public void noHttpError() {
        closeDialog();
        ToastUtil.show(R.string.http_no_net_tip);
    }

    @Override
    public void regSeccess(UserInfo userInfo) {
        closeDialog();
        Intent it=new Intent(this,ContinueUserMessageActivity.class);
        it.putExtra("token",userInfo.token);
        startActivity(it);
        ToastUtil.show(R.string.registrationsuccessful);
        finish();
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        ToastUtil.show(msg);
    }
}

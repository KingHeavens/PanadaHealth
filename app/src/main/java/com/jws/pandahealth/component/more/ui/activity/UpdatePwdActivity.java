package com.jws.pandahealth.component.more.ui.activity;

import android.view.View;
import android.widget.EditText;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.more.presenter.UpdatePwdPresenter;
import com.jws.pandahealth.component.more.presenter.contract.UpdatePasswordContract;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/4.
 */

public class UpdatePwdActivity extends BaseActivity<UpdatePwdPresenter> implements UpdatePasswordContract.View {
    @BindView(R.id.currentpassword)
    EditText currentpwd;
    @BindView(R.id.newpassword)
    EditText newpwd;
    @BindView(R.id.confirmpassword)
    EditText confirmpwd;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting_pwd;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.changepassword),getString(R.string.update));
    }

    private void clickTitleRifht(View v){
        String pwd1=currentpwd.getText().toString().trim();
        String pwd2=newpwd.getText().toString().trim();
        String pwd3=confirmpwd.getText().toString().trim();
        if(pwd1.length()<6 || pwd2.length()<6 || pwd3.length()<6){
            showToast(R.string.pwdlength6);
            return;
        }

        if(!pwd2.equals(pwd3)){
            showToast(R.string.newpwdandconfirmpwd);
            return;
        }

        if(pwd2.equals(pwd2)){
            showToast(R.string.newpwdandcurrpwd);
            return;
        }

        showDialog();
        mPresenter.updatePwd(MyApplication.getCurrentUser().token,pwd1,pwd2);
    }


    @Override
    public void updateSuccess(BaseInfo baseInfo) {
        closeDialog();
        if(baseInfo.getStatus().equals(AppConfig.DATA_SUCCESS)){
            showToast(R.string.changepassword_success);
            finish();
        }else{
            showError(baseInfo.getErrmsg());
        }
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        showToast(msg);
    }

    @Override
    public void noHttpError() {
        closeDialog();
        showToast(R.string.http_no_net_tip);
    }
}

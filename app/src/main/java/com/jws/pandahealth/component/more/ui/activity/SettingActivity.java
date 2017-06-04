package com.jws.pandahealth.component.more.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.EMCallBack;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.ui.activity.WebViewActivity;
import com.jws.pandahealth.component.more.presenter.SettingPresenter;
import com.jws.pandahealth.component.more.presenter.contract.SettingContract;
import com.jws.pandahealth.component.services.easemob.EaseMobLoginUtils;
import com.jws.pandahealth.component.services.util.JLog;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/3.
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View,View.OnClickListener{
    @BindView(R.id.logout)
    LinearLayout logout;
    @BindView(R.id.yourpassword)
    RelativeLayout yourpassword;
    @BindView(R.id.termsofuse)
    RelativeLayout termsofuse;
    @BindView(R.id.privacypolicy)
    RelativeLayout privacypolicy;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.setting));
        logout.setOnClickListener(this);
        yourpassword.setOnClickListener(this);
        termsofuse.setOnClickListener(this);
        privacypolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                mPresenter.clearUser();
                MyApplication.clearCurrentUser();
                EaseMobLoginUtils.easeMobLogout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        JLog.e("环信登出成功");
                    }

                    @Override
                    public void onError(int i, String s) {
                        JLog.e("环信登出失败" + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                RxBusUtil.getDefault().post(AppConfig.LOGOUT_SUCCESS);
                ToastUtil.show(R.string.logout_success);
                this.finish();
                break;

            case R.id.yourpassword:
                Intent it=new Intent(this,UpdatePwdActivity.class);
                startActivity(it);
                break;

            case R.id.termsofuse:
                Intent it2=new Intent(this,WebViewActivity.class);
                it2.putExtra("title",getString(R.string.termsofuse));
                it2.putExtra("url", AppConfig.TERMSOFUSEURL);
                it2.putExtra("isaddjava",false);
                startActivity(it2);
                break;

            case R.id.privacypolicy:
                Intent it1=new Intent(this,WebViewActivity.class);
                it1.putExtra("title",getString(R.string.privacypolicy));
                it1.putExtra("url",AppConfig.PRIVACYURL);
                it1.putExtra("isaddjava",false);
                startActivity(it1);
                break;

            default:
                break;
        }
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }
}

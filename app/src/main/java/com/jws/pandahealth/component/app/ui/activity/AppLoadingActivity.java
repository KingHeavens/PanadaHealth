package com.jws.pandahealth.component.app.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.SharedPrefUtils;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.LoadingPresenter;
import com.jws.pandahealth.component.app.presenter.LoginPresenter;
import com.jws.pandahealth.component.app.presenter.contract.LoadingContract;
import com.jws.pandahealth.component.app.presenter.contract.LoginContract;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;

/**
 * 启动界面
 *
 * @author luoming
 */

public class AppLoadingActivity extends BaseActivity<LoadingPresenter> implements LoadingContract.View {

    private final long loadingTime = 3000L;// Loading等待最小时间
    private boolean isAppInitFlag;//是否看过欢迎页面
    private String mType;
    private String mId;
    private String mCount;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_loading;
    }


    /**
     * schema
     */
    public void initEventAndData() {
//        初始化数据
//        mPresenter.initApp();
        initVariables();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAppInitFlag) {
                    goMian();
                } else {
                    goGuide();
                }
                SharedPrefUtils.saveBoolean(AppLoadingActivity.this, "AppInitFlag", true);
            }
        },loadingTime);


    }


    private void initVariables() {
        isAppInitFlag = SharedPrefUtils.getBoolean(this, "AppInitFlag", false);
    }

    // 主界面
    @Override
    public void goMian() {
        Intent it;
        it = new Intent(AppLoadingActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    // 引导界面
    // @Override
    public void goGuide() {
        Intent it = new Intent(this, AppGuideActivity.class);
        startActivity(it);
        finish();
    }


    @Override
    public void initAppSuccess(Object object) {

    }

    @Override
    public void showError(String msg) {

    }
    @Override
    public void noHttpError() {

    }

}

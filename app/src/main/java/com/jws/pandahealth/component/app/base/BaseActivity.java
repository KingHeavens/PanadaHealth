package com.jws.pandahealth.component.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.di.module.ActivityModule;
import com.jws.pandahealth.base.util.DisplayUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.di.ActivityComponent;
import com.jws.pandahealth.component.app.di.DaggerActivityComponent;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.app.ui.activity.MainActivity;
import com.jws.pandahealth.component.app.widget.Loadingdialog;
import com.jws.pandahealth.component.askdoctor.ui.activity.QuestionNewActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

import static com.jws.pandahealth.component.MyApplication.getCurrentUser;

/**
 * Created by codeest on 2016/8/2.
 * MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends SupportActivity implements BaseView{

    @Inject
    protected T mPresenter;
    protected Activity mContext;
    private Unbinder mUnBinder;
    private Loadingdialog dialog;
    @BindView(R.id.rl_title)@Nullable
    protected View rlTitle;
    @BindView(R.id.status_bar)@Nullable
    protected View statusBar;
    @BindView(R.id.head_title_tv)@Nullable
    protected TextView headTitleTv;
    @BindView(R.id.head_left_back_tv)@Nullable
    protected TextView headTitleBack;
    @BindView(R.id.head_right_tv) @Nullable
    protected TextView headRightTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
        MyApplication.getInstance().addActivity(this);
        //initCashHandler status bar
        DisplayUtil.adaptStatusBar(this,statusBar);
        initEventAndData();
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    protected ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
        if (mPresenter != null)
            mPresenter.detachView();
        mUnBinder.unbind();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    public  void hiddenKeyBoard(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    protected abstract void initInject();
    protected abstract int getLayout();
    protected abstract void initEventAndData();

//---------------------------dialog-------------------------
    /**
     * Dialog
     **/
    public void showDialog() {
        if (dialog == null)
            dialog = new Loadingdialog(this);

        if (!dialog.isShowing())
            dialog.show();
    }

    public void closeDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    protected Loadingdialog getLoadingDialog() {
        if (dialog == null)
            dialog = new Loadingdialog(this);
        return dialog;
    }

    protected void showToast(@StringRes int string){
        ToastUtil.shortShow(string);
    }
    protected void showToast(String string){
        ToastUtil.shortShow(string);
    }

//---------------------------title-------------------------
    public void setTitleName(String ... names){
        if(names.length == 0)
            return;
        if(names.length >0 ){
            headTitleTv.setText(names[0]);
        }
        if(names.length > 1){
            headRightTv.setText(names[1]);
            headRightTv.setVisibility(View.VISIBLE);
        }
    }

    // title栏 退出
    public void clickTitleLeft(View view) {
        onBackPressed();
    }

    /**
     * 退出
     **/
    // 硬按键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return false;
        }
        return false;
    }

    public void onBackPressed() {
        if (this instanceof MainActivity) {
            ((MainActivity) this).exitBy2Click();
        } else if (this instanceof QuestionNewActivity) {
            ((QuestionNewActivity) this).saveAskContent();
             finish();
        } else {
            finish();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.noanim, R.anim.exit_leftttoight);
    }

    /***
     * 此功能需登录才能操作
     */
    public boolean actionNeedLogin() {
        if (getCurrentUser() == null) {
            showToast(getString(R.string.action_need_login));
            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(it);
            return true;
        }
        return false;
    }

    /**
     * 此功能需登录才能操作 不提示
     * @return
     */
    public boolean actionNeedLoginNoToast() {
        if (getCurrentUser() == null) {
            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(it);
            return true;
        }
        return false;
    }
}
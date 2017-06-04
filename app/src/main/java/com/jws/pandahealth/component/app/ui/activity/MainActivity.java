package com.jws.pandahealth.component.app.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.MainPresenter;
import com.jws.pandahealth.component.app.presenter.contract.MainContract;
import com.jws.pandahealth.component.askdoctor.ui.fragment.HomeFragment;
import com.jws.pandahealth.component.more.ui.fragment.MoreFragment;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.easemob.EaseMobLoginUtils;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.ui.fragment.ServicesFragment;
import com.jws.pandahealth.component.services.util.DialogUtil;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import rx.Observer;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.askdoctors)
    TextView askDoctors;
    @BindView(R.id.service)
    TextView service;
    @BindView(R.id.more)
    TextView more;

    HomeFragment homeFragment;
    MoreFragment moreFragment;
    ServicesFragment servicesFragment;
    TextView[] mTabs;
    Fragment[] tabFragments = new Fragment[]{
            homeFragment,
            servicesFragment,
            moreFragment};
    int index;
    int currentTabIndex; // 当前fragment的index
    @BindView(R.id.tab2_dot)
    View tab2Dot;
    private boolean hasLoadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoginOtherDevicesListener();

    }

    private void initLoginOtherDevicesListener() {
        RxBusUtil.getDefault().toObservable(String.class)
                .compose(RxUtil.<String>rxSchedulerHelper())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        JLog.e("onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(String savedInstanceState) {
                        JLog.e("onNext" + savedInstanceState);

                        if(!hasLoadDialog){
                            hasLoadDialog = false;
                            if(TextUtils.isEmpty(savedInstanceState))
                                return;
                            //make sure activity will not in background if user is logged into another device or removed
                            if (ServicesConstant.ACCOUNT_REMOVED.equals(savedInstanceState)) {
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
                                if(index == 1){
                                    index = 0;
                                    changeCurrentPage();
                                }
                                DialogUtil.showNoTitleDialog(MainActivity.this, getString(R.string.your_accout_has_login_in_other_device), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hasLoadDialog = true;
                                        Dialog dialog = (Dialog) v.getTag();
                                        dialog.dismiss();
                                    }
                                });
                            } else if (ServicesConstant.ACCOUNT_CONFLICT.equals(savedInstanceState)) {
                                mPresenter.clearUser();
                                MyApplication.clearCurrentUser();
                                RxBusUtil.getDefault().post(AppConfig.LOGOUT_SUCCESS);
                                if(index == 1){
                                    index = 0;
                                    changeCurrentPage();
                                }
                                DialogUtil.showNoTitleDialog(MainActivity.this, getString(R.string.your_accout_has_login_in_other_device), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hasLoadDialog = true;
                                        Dialog dialog = (Dialog) v.getTag();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }

                    }
                });


    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.init();
        mTabs = new TextView[]{askDoctors, service, more};
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        homeFragment = new HomeFragment();
        servicesFragment = new ServicesFragment();

        moreFragment = new MoreFragment();
        tabFragments = new Fragment[]{homeFragment, servicesFragment, moreFragment};

        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .add(R.id.fragment_container, servicesFragment)
                .add(R.id.fragment_container, moreFragment)
                .hide(servicesFragment)
                //.show(dynamicFragment).commit();
                .hide(moreFragment).show(homeFragment).commit();
        onReceviePaySuccessListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onReceviePaySuccessListener();
    }

    private void onReceviePaySuccessListener(){
        String type = getIntent().getStringExtra("type");
        if (ServicesConstant.PAY_SUCCESS_RECEIVED.equals(type)) {
            String orderId = getIntent().getStringExtra(ServicesConstant.PAY_ORDER_ID);
            String doctorHxId = getIntent().getStringExtra(ServicesConstant.PAY_DOCTOR_HXID);
            Intent it = new Intent(mContext, ChatActivity.class);
            it.putExtra(EaseConstant.EXTRA_USER_ID, doctorHxId);
            it.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            startActivity(it);
        }
    }


    /**
     * Tab点击事件
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.askdoctors:
                index = 0;
                break;
            case R.id.service:
                if (actionNeedLoginNoToast())
                    return;
                index = 1;
                break;
            case R.id.more:
                index = 2;
                break;
            default:
                break;

        }
        changeCurrentPage();
    }

    private void changeCurrentPage() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(tabFragments[currentTabIndex]);
            if (!tabFragments[index].isAdded()) {
                trx.add(R.id.fragment_container, tabFragments[index]);
            }
            trx.show(tabFragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }


    /**
     * 双击退出函数
     **/
    private static Boolean isExit = false;

    @Override
    public void exitBy2Click() {
        if (!isExit) {
            isExit = true; // 准备退出
            ToastUtil.shortShow("Again according to exit the program");
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            //false，仅当activity为task根（即首个activity例如启动activity之类的）时才生效
            //true，无论什么时候都会生效，忽略上述条件
            //System.exit(0);
            finish();
        }
    }

    @Override
    public void setServicesDotVisibility(int visibility) {
        if(tab2Dot == null)
            return;
        tab2Dot.setVisibility(visibility);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }
}

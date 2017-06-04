package com.jws.pandahealth.component.app.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.di.module.FragmentModule;
import com.jws.pandahealth.base.util.DisplayUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.di.DaggerFragmentComponent;
import com.jws.pandahealth.component.app.di.FragmentComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by jws on 2016/8/2.
 * MVP Fragment基类
 */
public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment implements BaseView{

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

    @Inject
    protected T mPresenter;
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected boolean isInited = false;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    protected FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }

    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        initInject();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mUnBinder = ButterKnife.bind(this, view);
        //initCashHandler status bar
        DisplayUtil.adaptStatusBar(mContext,statusBar);
        if (savedInstanceState == null) {
            if (!isHidden()) {
                isInited = true;
                initEventAndData();
            }
        } else {
            if (!isSupportHidden()) {
                isInited = true;
                initEventAndData();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isInited && !hidden) {
            isInited = true;
            initEventAndData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    @Override
    public void useNightMode(boolean isNight) {

    }
    protected void showToast(@StringRes int string){
        ToastUtil.shortShow(string);
    }
    protected void showToast(String string){
        ToastUtil.shortShow(string);
    }
    protected abstract void initInject();
    protected abstract int getLayoutId();
    protected abstract void initEventAndData();

    public void setTitleText(String ... names){
        if(names.length == 0)
            return;

        if(names.length == 1){
            headTitleTv.setText(names[0]);
        }else if(names.length == 2){
            headTitleTv.setText(names[0]);
            headRightTv.setText(names[1]);
            headRightTv.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleBackGone(){
        headTitleBack.setVisibility(View.GONE);
    }

}
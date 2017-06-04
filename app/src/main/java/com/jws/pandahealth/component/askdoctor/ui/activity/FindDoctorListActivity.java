package com.jws.pandahealth.component.askdoctor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.IRefreshHeader;
import com.ajguan.library.State;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.LogUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.app.widget.CustomLoadMoreView;
import com.jws.pandahealth.component.app.widget.LoadingLayout;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.askdoctor.decoration.DividerItemDecoration;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.presenter.FindDoctorListPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.FindDoctorListContract;
import com.jws.pandahealth.component.askdoctor.view.ExpandItemView;
import com.jws.pandahealth.component.askdoctor.view.ExpandTabView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/24.
 */

public class FindDoctorListActivity extends BaseActivity<FindDoctorListPresenter> implements FindDoctorListContract.View {
    @BindView(R.id.expandtab_view)
    ExpandTabView expandtab_view;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.fugai)
    View fugai;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadinglayout;
    MyBaseQuickAdapter adapter;

    private int departmentIndex = 0;
    private int levelIndex = 0;
    private int serviceIndex = 0;

    int page = 1;

    private ExpandItemView department;
    private ExpandItemView level;
    private ExpandItemView service;
    private ArrayList<View> mViewArray = new ArrayList<View>();

    private ArrayList<DoctorInfo> doctorInfos = new ArrayList<DoctorInfo>();

    UserInfo userInfo;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_finddoctor_list;
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        expandtab_view.isLeftTopBack = false;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.findadoctor));
        headTitleBack.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressWarnings("static-access")
                    @Override
                    public void onClick(View arg0) {
                        expandtab_view.isLeftTopBack = false;
                        if (!expandtab_view.onPressBack()) {
                            onBackPressed();
                        }
                    }
                });
        userInfo = MyApplication.getCurrentUser();
        loadinglayout.showLoading();
        loadinglayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadinglayout.showLoading();
                mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, page, userInfo == null ? "" : userInfo.token);
            }
        });
        initTablayout();
        initEasyLayout();
        initData();
    }

    void initData() {
        if (getIntent().getBooleanExtra("isQuickIn", false)) {
            departmentIndex = 7;
            expandtab_view.setTitle(AppConfig.departmentValue[6], 0);
            department.setSelectItemIndex(6);
        } else {

            int index = getIntent().getIntExtra("departmentIndex", 0);
            departmentIndex = index + 1;
            department.setSelectItemIndex(index);
            expandtab_view.setTitle(AppConfig.departmentValue[index], 0);
        }

        mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, 1, userInfo == null ? "" : userInfo.token);
    }

    void initTablayout() {
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add(getString(R.string.finddoctor_tab1));
        mTextArray.add(getString(R.string.finddoctor_tab2));
        mTextArray.add(getString(R.string.finddoctor_tab3));
        department = new ExpandItemView(this, AppConfig.departmentValue);
        level = new ExpandItemView(this, AppConfig.levelValue);
        service = new ExpandItemView(this, AppConfig.serviceValue);
        mViewArray.add(department);
        mViewArray.add(level);
        mViewArray.add(service);
        expandtab_view.setValue(mTextArray, mViewArray);
        expandtab_view.setFugai(fugai);
        initTabListener();
        expandtab_view.setTextColorSelected(0);
    }

    void initEasyLayout() {
        easylayout.setEnableLoadMore(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        easylayout.setRefreshHeadView(new MeSimpleRefreshHeaderView(this));
        easylayout.addEasyEvent(
                new EasyRefreshLayout.EasyEvent() {
                    @Override
                    public void onLoadMore() {
                    }

                    @Override
                    public void onRefreshing() {
                        mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, page, userInfo == null ? "" : userInfo.token);
                    }
                }
        );
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(FindDoctorListActivity.this, DoctorActivity.class);
                it.putExtra("id", doctorInfos.get(i).doctorId);
                startActivity(it);
            }
        });
        adapter = new MyBaseQuickAdapter(doctorInfos);
        recyclerview.setAdapter(adapter);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, page, userInfo == null ? "" : userInfo.token);
            }
        });

    }

    class MyBaseQuickAdapter extends BaseQuickAdapter<DoctorInfo, BaseViewHolder> {
        public MyBaseQuickAdapter(ArrayList<DoctorInfo> doctorInfos) {
            super(R.layout.incold_finddoctor_item, doctorInfos);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, DoctorInfo o) {
            ImageLoaderUtil.loadUserImage(FindDoctorListActivity.this,
                    (ImageView) baseViewHolder.getView(R.id.photo), o.doctorIcon);
            baseViewHolder.setText(R.id.name, o.doctorName).setVisible(R.id.isauth, o.verified.equals("1"))
                    .setText(R.id.zhiwei, o.doctorTitle).setText(R.id.department, o.departmentName)
                    .setText(R.id.yiyuan, (o.hospitalName == null || o.hospitalName.equals("")) ? "hospital" : o.hospitalName).setText(R.id.message, o.doctorIntro);
        }

    }

    void initTabListener() {

        department.setOnSelectListener(new ExpandItemView.OnSelectListener() {

            @Override
            public void getValue(int posi) {
                showDialog();
                expandtab_view.onPressBack();
                departmentIndex = posi + 1;
                page = 1;
                expandtab_view.setTitle(AppConfig.departmentValue[posi], 0);
                mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, 1, userInfo == null ? "" : userInfo.token);
            }
        });
        level.setOnSelectListener(new ExpandItemView.OnSelectListener() {
            @Override
            public void getValue(int posi) {
                showDialog();
                expandtab_view.onPressBack();
                levelIndex = posi + 1;
                page = 1;
                expandtab_view.setTitle(AppConfig.levelValue[posi], 1);
                mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, 1, userInfo == null ? "" : userInfo.token);
                expandtab_view.setTextColorSelected(1);
            }
        });
        service.setOnSelectListener(new ExpandItemView.OnSelectListener() {
            @Override
            public void getValue(int posi) {
                showDialog();
                expandtab_view.onPressBack();
                serviceIndex = posi + 1;
                page = 1;
                expandtab_view.setTitle(AppConfig.serviceValue[posi], 2);
                mPresenter.findDoctors(departmentIndex, levelIndex, serviceIndex, 1, userInfo == null ? "" : userInfo.token);
                expandtab_view.setTextColorSelected(2);
            }
        });

    }

    @Override
    public void findDoctorSuccess(List<DoctorInfo> baseInfo) {
        closeDialog();
        easylayout.refreshComplete();
        adapter.loadMoreComplete();
        if (baseInfo.size() == 0) {
            adapter.loadMoreEnd();
            if (doctorInfos.size() == 0) {
                //recyclerview显示空数据
                loadinglayout.showEmpty();
            } else {
                ToastUtil.shortShow(getString(R.string.no_data));
            }
        } else {
            if (page == 1) {
                doctorInfos.clear();
            }
            page++;
            doctorInfos.addAll(baseInfo);
            adapter.notifyDataSetChanged();
            loadinglayout.showContent();

        }
    }

    @Override
    public void noHttpError() {
        closeDialog();
        adapter.loadMoreFail();
        easylayout.refreshComplete();
        if (doctorInfos.size() == 0) {
            loadinglayout.showError();
        }
        ToastUtil.shortShow(getString(R.string.http_no_net_tip));
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        easylayout.refreshComplete();
        adapter.loadMoreComplete();
        if (doctorInfos.size() == 0) {
            loadinglayout.showEmpty();
            //listview显示网络失败
        } else {
            ToastUtil.shortShow(msg);
        }

    }



}

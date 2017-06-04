package com.jws.pandahealth.component.services.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.bumptech.glide.load.model.ModelLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.askdoctor.ui.activity.DoctorActivity;
import com.jws.pandahealth.component.services.adapter.ServicesMyDoctorListAdapter;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;
import com.jws.pandahealth.component.services.presenter.ServicesMyDoctorPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ServicesMyDoctorContract;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.DividerItemDecoration;
import com.jws.pandahealth.component.services.widget.ServicesLoadingLayout;

import java.util.List;

import butterknife.BindView;


/**
 * Created by zhaijinjing on 2016/12/21.
 * services MyDoctor tab
 */

public class MyDoctorFragment extends BaseFragment<ServicesMyDoctorPresenter> implements ServicesMyDoctorContract.View {
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.refresh_view)
    EasyRefreshLayout mRefreshView;
    @BindView(R.id.loading_layout)
    ServicesLoadingLayout loadingLayout;
    private ServicesMyDoctorListAdapter mAdapter;

    public static MyDoctorFragment instance() {
        MyDoctorFragment view = new MyDoctorFragment();
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_refresh_recycle_layout;
    }

    @Override
    protected void initEventAndData() {
        initListener();
        loadingLayout.showLoading();
        //关闭加载更多
        mRefreshView.setRefreshHeadView(new MeSimpleRefreshHeaderView(getActivity()));
        mRefreshView.setEnableLoadMore(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List data = baseQuickAdapter.getData();
                MyDoctorInfo info = (MyDoctorInfo) data.get(i);

                enterDoctorDetailActivity(info.doctorId);
            }
        });
        mPresenter.getMyDoctorDataFromNet();
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getMyDoctorDataFromNet();
                loadingLayout.showLoading();
            }
        });
    }

    /**
     * 进入医生详情
     *
     * @param doctorId
     */
    private void enterDoctorDetailActivity(String doctorId) {
        Intent it = new Intent(mContext, DoctorActivity.class);
        it.putExtra("id", doctorId);
        startActivity(it);
    }

    private void initListener() {
        mRefreshView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                mPresenter.getMyDoctorDataFromNet();
            }
        });
    }

    @Override
    public void showError(String msg) {
        JLog.e(msg);
        loadingLayout.showErrorByText(msg);
    }

    @Override
    public void noHttpError() {
        loadingLayout.showNoNetwork();
    }

    @Override
    public void bindData(List<MyDoctorInfo> lists) {
        mAdapter = new ServicesMyDoctorListAdapter(getActivity(), lists);
        mRecycleView.setAdapter(mAdapter);
        loadingLayout.showContent();
        showNoData(lists);
    }

    @Override
    public void onRefreshFinished(List<MyDoctorInfo> lists) {
        loadingLayout.showContent();
        showNoData(lists);
        mAdapter.setNewData(lists);
        mRefreshView.refreshComplete();
    }

    private void showNoData(List lists){
        if(lists == null || lists.size()<=0)
            loadingLayout.showNoDoctor();
    }

}

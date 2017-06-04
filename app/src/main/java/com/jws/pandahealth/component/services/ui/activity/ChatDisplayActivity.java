package com.jws.pandahealth.component.services.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.askdoctor.ui.activity.DoctorActivity;
import com.jws.pandahealth.component.services.adapter.ChatDisplayAdapter;
import com.jws.pandahealth.component.services.model.bean.ChatInfo;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.ChatDisplayPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ChatDisplayContract;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.DividerItemDecoration;
import com.jws.pandahealth.component.services.widget.ServicesLoadingLayout;

import java.util.List;

import butterknife.BindView;

/**
 * 服务界面历史数据
 */
public class ChatDisplayActivity extends BaseActivity<ChatDisplayPresenter> implements ChatDisplayContract.View, View.OnClickListener {
    private static final String TAG = "ChatDisplayActivity";
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.refresh_view)
    EasyRefreshLayout mRefreshView;
    @BindView(R.id.loading_layout)
    ServicesLoadingLayout loadingLayout;
    public List<ChatInfo> doctorChatInfos;
    private ChatDisplayAdapter mAdapter;
    private String mOrderId;
    private String mDoctorId;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.common_acticity_list;
    }

    @Override
    protected void initEventAndData() {
        loadingLayout.showLoading();
        mOrderId = getIntent().getStringExtra("id");
        mDoctorId = getIntent().getStringExtra("doctorId");
        setTitleName(getIntent().getStringExtra("doctorName"),getString(R.string.doctor_profile));
        mPresenter.getChatList(mOrderId);
        initAdapterAndView();
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.showLoading();
                mPresenter.getChatList(mOrderId);
            }
        });
    }

    private void initAdapterAndView() {

        //关闭加载更多
        mRefreshView.setRefreshHeadView(new MeSimpleRefreshHeaderView(this));
        mRefreshView.setEnableLoadMore(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                /*List data = baseQuickAdapter.getData();
                ChatInfo info = (ChatInfo) data.get(i);
                enterDoctorDetailActivity(info.);*/
            }
        });
        mRefreshView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                mPresenter.getChatList(mOrderId);
            }
        });


    }

    /**
     * 进入医生详情
     *
     * @param doctorHxId
     */
    private void enterDoctorDetailActivity(String doctorHxId) {
        Intent it = new Intent(mContext, ChatActivity.class);
        it.putExtra("id", doctorHxId);
        startActivity(it);
    }


    @Override
    public void showError(String msg) {
        JLog.e(msg);
        if(msg.contains("java.lang.IllegalStateException: Expected BEGIN_ARRAY"))
            loadingLayout.showEmpty();
        else
            loadingLayout.showErrorByText(msg);
    }

    @Override
    public void noHttpError() {
        loadingLayout.showNoNetwork();
    }

    @Override
    public void bindData(List<ChatInfo> lists) {
        JLog.e(TAG + ":" + lists.size());
        loadingLayout.showContent();

        mAdapter = new ChatDisplayAdapter(lists);
        mRecycleView.setAdapter(mAdapter);
        showNoData(lists);
    }

    @Override
    public void onRefreshFinished(List<ChatInfo> lists) {
        showNoData(lists);
        this.doctorChatInfos = lists;
        mAdapter.setNewData(lists);
        mRefreshView.refreshComplete();
    }


    @Override
    public void onClick(View v) {

    }

    // title栏  右边功能按键
    public void clickTitleRight(View view) {
        Intent it = new Intent(mContext, DoctorActivity.class);
        it.putExtra("id", mDoctorId);
        startActivity(it);
    }

    private void showNoData(List lists){
        if(lists==null || lists.size()<=0)
            loadingLayout.showNoService();
    }

}
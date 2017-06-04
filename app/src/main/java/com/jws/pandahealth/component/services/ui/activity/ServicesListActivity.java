package com.jws.pandahealth.component.services.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.adapter.ServicesChatListAdapter;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.ServicesListPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ServicesListContract;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.DividerItemDecoration;
import com.jws.pandahealth.component.services.widget.ServicesLoadingLayout;

import java.util.List;

import butterknife.BindView;

/**
 * 服务界面历史数据
 */
public class ServicesListActivity extends BaseActivity<ServicesListPresenter> implements ServicesListContract.View {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.refresh_view)
    EasyRefreshLayout mRefreshView;
    @BindView(R.id.loading_layout)
    ServicesLoadingLayout loadingLayout;
    @BindView(R.id.services_activity_history_list)
    LinearLayout servicesActivityHistoryList;
    private List<DoctorChatInfo> doctorChatInfos;
    private ServicesChatListAdapter mAdapter;
    private String mType;

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
        setTitle();
        List<DoctorChatInfo> list = (List<DoctorChatInfo>) getIntent().getSerializableExtra("data");
        doctorChatInfos = list;
        initAdapterAndView();
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getHistoryList(mType);
                loadingLayout.showLoading();
            }
        });
    }

    private void setTitle() {
        String type = getIntent().getStringExtra("type");
        mType = type;
        switch (type) {
            case ServicesConstant.TYPE_TEXT:
                setTitleName(getString(R.string.textchat));
                break;
            case ServicesConstant.TYPE_VOICE_CALL:
                setTitleName(getString(R.string.services_voice_call));
                break;
            case ServicesConstant.TYPE_PRIVATE_DOCTOR:
                setTitleName(getString(R.string.private_doctor));
                break;
        }
    }

    private void initAdapterAndView() {
        //关闭加载更多
        mRefreshView.setRefreshHeadView(new MeSimpleRefreshHeaderView(this));
        mRefreshView.setEnableLoadMore(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List data = baseQuickAdapter.getData();
                DoctorChatInfo info = (DoctorChatInfo) data.get(i);
                enterChatDisplayActivity(info);
            }
        });
        mRefreshView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                mPresenter.getHistoryList(mType);
            }
        });
        bindData(doctorChatInfos);
    }

    /**
     * 进入医生详情
     *
     * @param
     */
    private void enterChatDisplayActivity(DoctorChatInfo info) {
        Intent it = new Intent(mContext, ChatDisplayActivity.class);
        it.putExtra("id", info.orderId);
        it.putExtra("doctorId", info.doctorId);
        it.putExtra("doctorName", info.doctorName);
        startActivity(it);
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
    public void bindData(List<DoctorChatInfo> lists) {
        mAdapter = new ServicesChatListAdapter(lists,false);
        mRecycleView.setAdapter(mAdapter);
        loadingLayout.showContent();
        showNoData(lists);
    }

    @Override
    public void onRefreshFinished(List<DoctorChatInfo> lists) {
        showNoData(lists);
        this.doctorChatInfos = lists;
        mAdapter.setNewData(lists);
        mRefreshView.refreshComplete();
    }


    private void showNoData(List lists){
        if(lists == null || lists.size()<=0)
            loadingLayout.showNoService();
    }

}
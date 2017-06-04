package com.jws.pandahealth.component.services.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.BuildConfig;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.services.adapter.ServicesChatListAdapter;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.ServicesNewChatPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ServicesNewChatContract;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.DividerItemDecoration;
import com.jws.pandahealth.component.services.widget.ServicesLoadingLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhaijinjing on 2016/12/21.
 * services new chat page
 */

public class NewChatFragment extends BaseFragment<ServicesNewChatPresenter> implements ServicesNewChatContract.View {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.refresh_view)
    EasyRefreshLayout mRefreshView;
    @BindView(R.id.loading_layout)
    ServicesLoadingLayout loadingLayout;
    private ServicesChatListAdapter mChatListAdapter;
    private int mPage = 0;

    public static NewChatFragment instance() {
        NewChatFragment view = new NewChatFragment();
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
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                List data = baseQuickAdapter.getData();
                DoctorChatInfo info = (DoctorChatInfo) data.get(position);
                if(!AppConfig.IS_RELEASE && AppConfig.TEST_CHAT && position == 0)
                    enterChatActivity(AppConfig.TEST_CHAT_ACCOUNT);
                else
                    enterChatActivity(info.doctorHxId);

            }
        });
        mPresenter.getDoctorChatListFromNet(false);
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getDoctorChatListFromNet(false);
                loadingLayout.showLoading();
            }
        });
    }

    /**
     * 进ChatActivity页面
     */
    private void enterChatActivity(String hxId) {
        Intent it = new Intent(mContext, ChatActivity.class);
        it.putExtra(EaseConstant.EXTRA_USER_ID, hxId);
        it.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        startActivity(it);
    }

    private void initListener() {
        mRefreshView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                mPresenter.getDoctorChatListFromNet(true);
            }
        });
    }

    @Override
    public void showError(String msg) {
        JLog.e(msg);
        showToast(msg);
        loadingLayout.showErrorByText(msg);
    }

    @Override
    public void noHttpError() {
        loadingLayout.showNoNetwork();
    }

    @Override
    public void bindData(List<DoctorChatInfo> lists) {
        mChatListAdapter = new ServicesChatListAdapter(lists);
        //设置监听
        mRecycleView.setAdapter(mChatListAdapter);
        loadingLayout.showContent();
        showNoData(lists);
        mRefreshView.refreshComplete();
    }
    @Override
    public void notifyView(){
        mChatListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRefreshFinished(List<DoctorChatInfo> lists) {
        showNoData(lists);
        mChatListAdapter.setNewData(lists);
        mRefreshView.refreshComplete();
    }

    private void showNoData(List lists){
        if(lists == null || lists.size()<=0)
            loadingLayout.showNoService();
    }


}

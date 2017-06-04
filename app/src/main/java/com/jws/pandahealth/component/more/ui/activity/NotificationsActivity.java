package com.jws.pandahealth.component.more.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.ui.activity.WebViewActivity;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.app.widget.LoadingLayout;
import com.jws.pandahealth.component.askdoctor.decoration.DividerItemDecoration;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.ui.activity.DoctorActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.FindDoctorListActivity;
import com.jws.pandahealth.component.askdoctor.utils.DateUtil;
import com.jws.pandahealth.component.more.model.bean.Subscription;
import com.jws.pandahealth.component.more.presenter.NotificationsPresenter;
import com.jws.pandahealth.component.more.presenter.contract.NotificationsContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/1/12.
 */

public class NotificationsActivity extends BaseActivity<NotificationsPresenter> implements NotificationsContract.View {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadinglayout;
    MyBaseQuickAdapter adapter;
    private ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_notification_list;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.notifications));
        loadinglayout.showLoading();
        loadinglayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadinglayout.showLoading();
                mPresenter.loadNotification(MyApplication.getCurrentUser() == null ? "" : MyApplication.getCurrentUser().token);
            }
        });
        initEasyLayout();
        mPresenter.loadNotification(MyApplication.getCurrentUser() == null ? "" : MyApplication.getCurrentUser().token);
    }

    void initEasyLayout() {
        easylayout.setEnableLoadMore(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        easylayout.addEasyEvent(
                new EasyRefreshLayout.EasyEvent() {
                    @Override
                    public void onLoadMore() {
                    }

                    @Override
                    public void onRefreshing() {
                        mPresenter.loadNotification(MyApplication.getCurrentUser() == null ? "" : MyApplication.getCurrentUser().token);
                    }
                }
        );
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(NotificationsActivity.this, WebViewActivity.class);
                it.putExtra("title", getString(R.string.notifications));
                it.putExtra("url", subscriptions.get(i).url);
                startActivity(it);
            }
        });
    }

    class MyBaseQuickAdapter extends BaseQuickAdapter<Subscription, BaseViewHolder> {
        public MyBaseQuickAdapter(ArrayList<Subscription> subscriptions) {
            super(R.layout.incold_notification_item, subscriptions);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Subscription o) {
            ImageLoaderUtil.loadAll(NotificationsActivity.this, o.imgUrl,
                    (ImageView) baseViewHolder.getView(R.id.icon));
            baseViewHolder.setText(R.id.name, o.title)
                    .setText(R.id.create, DateUtil.getTime(Long.parseLong(o.created)))
                    .setText(R.id.message, o.intro);
        }

    }


    @Override
    public void loadNotificationSuccess(List<Subscription> list) {
        closeDialog();
        if (list.size() == 0) {
            if (subscriptions.size() == 0) {
                //recyclerview显示空数据
                loadinglayout.showEmpty();
            } else {
                ToastUtil.shortShow(getString(R.string.no_data));
            }
        } else {
            subscriptions.clear();
            subscriptions.addAll(list);
            if (adapter == null) {
                adapter = new MyBaseQuickAdapter(subscriptions);
                recyclerview.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }


            loadinglayout.showContent();
            easylayout.refreshComplete();
        }
    }

    @Override
    public void noHttpError() {
        closeDialog();
        loadinglayout.showError();
        ToastUtil.shortShow(getString(R.string.http_no_net_tip));
    }

    @Override
    public void showError(String msg) {
        closeDialog();
        loadinglayout.showEmpty();
        //listview显示网络失败
        ToastUtil.shortShow(msg);
    }
}

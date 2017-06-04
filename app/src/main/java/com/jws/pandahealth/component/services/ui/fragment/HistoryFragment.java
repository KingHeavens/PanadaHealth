package com.jws.pandahealth.component.services.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.RxUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.app.widget.MeSimpleRefreshHeaderView;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.presenter.ServicesHistoryPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ServicesHistoryContract;
import com.jws.pandahealth.component.services.ui.activity.ServicesListActivity;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.ServicesLoadingLayout;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhaijinjing on 2016/12/21.
 * services history tab
 */

public class HistoryFragment extends BaseFragment<ServicesHistoryPresenter> implements ServicesHistoryContract.View, View.OnClickListener {
    @BindView(R.id.iv_text_icon)
    ImageView mIvTextIcon;
    @BindView(R.id.tv_text_text)
    TextView mTvTextText;
    @BindView(R.id.tv_text_count)
    TextView mTvTextCount;
    @BindView(R.id.ll_text)
    LinearLayout mLlText;
    @BindView(R.id.iv_phone_icon)
    ImageView mIvPhoneIcon;
    @BindView(R.id.tv_phone_text)
    TextView mTvPhoneText;
    @BindView(R.id.tv_phone_count)
    TextView mTvPhoneCount;
    @BindView(R.id.ll_phone)
    LinearLayout mLlPhone;
    @BindView(R.id.iv_private_icon)
    ImageView mIvPrivateIcon;
    @BindView(R.id.tv_private_text)
    TextView mTvPrivateText;
    @BindView(R.id.tv_private_count)
    TextView mTvPrivateCount;
    @BindView(R.id.ll_private)
    LinearLayout mLlPrivate;
    @BindView(R.id.ll_line1)
    LinearLayout mLlLine1;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.refresh_view)
    EasyRefreshLayout refreshView;
    @BindView(R.id.loading_layout)
    ServicesLoadingLayout loadingLayout;

    private List<DoctorChatInfo> textlLists;
    private List<DoctorChatInfo> voiceCallLists;
    private List<DoctorChatInfo> privateDoctorlLists;

    public static HistoryFragment instance() {
        HistoryFragment view = new HistoryFragment();
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.services_item_history;
    }

    @Override
    protected void initEventAndData() {
        mLlText.setOnClickListener(this);
        mLlPhone.setOnClickListener(this);
        mLlPrivate.setOnClickListener(this);
        getAllDataFromNet();
        loadingLayout.showLoading();
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.showLoading();
                getAllDataFromNet();
            }
        });

        refreshView.setRefreshHeadView(new MeSimpleRefreshHeaderView(getActivity()));
        refreshView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                getAllDataFromNet();
            }
        });


    }

    private void getAllDataFromNet() {
        mPresenter.getHistoryList(ServicesConstant.TYPE_TEXT);
        mPresenter.getHistoryList(ServicesConstant.TYPE_VOICE_CALL);
        mPresenter.getHistoryList(ServicesConstant.TYPE_PRIVATE_DOCTOR);
    }


    @Override
    public void showError(String msg) {
        showToast(msg);
        JLog.e(msg);
    }

    @Override
    public void noHttpError() {
        loadingLayout.showError();
        refreshView.refreshComplete();
        showToast(getString(R.string.http_no_net_tip));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_text:
                enterServicesListPage(textlLists, ServicesConstant.TYPE_TEXT);
                break;
            case R.id.ll_phone:
                enterServicesListPage(voiceCallLists, ServicesConstant.TYPE_VOICE_CALL);
                break;
            case R.id.ll_private:
                enterServicesListPage(privateDoctorlLists, ServicesConstant.TYPE_PRIVATE_DOCTOR);
                break;
        }
    }

    private void enterServicesListPage(List<DoctorChatInfo> lists, String type) {
        Intent it = new Intent(mContext, ServicesListActivity.class);
        it.putExtra("data", (Serializable) lists);
        it.putExtra("type", type);
        startActivity(it);
    }


    @Override
    public void bindData(List<DoctorChatInfo> lists, String type) {
        switch (type) {
            case ServicesConstant.TYPE_TEXT:
                textlLists = lists;
                setTextSelected(lists.size() > 0);
                mTvTextCount.setText(String.format("(%s)", parseServicesCount(lists.size())));
                break;
            case ServicesConstant.TYPE_VOICE_CALL:
                voiceCallLists = lists;
                setVoiceSelected(lists.size() > 0);
                mTvPhoneCount.setText(String.format("(%s)", parseServicesCount(lists.size())));
                break;
            case ServicesConstant.TYPE_PRIVATE_DOCTOR:
                privateDoctorlLists = lists;
                setPrivateDocSelected(lists.size() > 0);
                mTvPrivateCount.setText(String.format("(%s)", parseServicesCount(lists.size())));
                break;
        }
        loadingLayout.showContent();
        refreshView.refreshComplete();
    }

    @Override
    public void refreshData() {
        getAllDataFromNet();
    }

    @Override
    public void clearData() {
        if(textlLists!=null)
            textlLists.clear();
        if(voiceCallLists != null)
            voiceCallLists.clear();
        if(privateDoctorlLists!=null)
            privateDoctorlLists.clear();
        loadingLayout.showNoService();
    }

    private String parseServicesCount(int size) {
        if (size <= 0)
            return "0";
        return String.valueOf(size);
    }

    private void setTextSelected(boolean isSelected) {
        if (isSelected) {
            mIvTextIcon.setImageResource(R.mipmap.doctorprofile_textchar);
            mTvTextCount.setTextColor(getResources().getColor(R.color.services_seleted_color));
            mTvTextText.setTextColor(getResources().getColor(R.color.services_normal_color));
        } else {
            mIvTextIcon.setImageResource(R.mipmap.doctorprofile_textchar);
            mTvTextCount.setTextColor(getResources().getColor(R.color.services_normal_color));
            mTvTextText.setTextColor(getResources().getColor(R.color.services_normal_color));
        }
    }

    private void setVoiceSelected(boolean isSelected) {
        if (isSelected) {
            mIvPhoneIcon.setImageResource(R.mipmap.doctorprofile_voicecall);
            mTvPhoneCount.setTextColor(getResources().getColor(R.color.services_seleted_color));
            mTvPhoneText.setTextColor(getResources().getColor(R.color.services_normal_color));
        } else {
            mIvPhoneIcon.setImageResource(R.mipmap.doctorprofile_voicecall);
            mTvPhoneCount.setTextColor(getResources().getColor(R.color.services_normal_color));
            mTvPhoneText.setTextColor(getResources().getColor(R.color.services_normal_color));
        }
    }

    private void setPrivateDocSelected(boolean isSelected) {
        if (isSelected) {
            mIvPrivateIcon.setImageResource(R.mipmap.doctorprofile_privatedoctor);
            mTvPrivateCount.setTextColor(getResources().getColor(R.color.services_seleted_color));
            mTvPrivateText.setTextColor(getResources().getColor(R.color.services_normal_color));
        } else {
            mIvPrivateIcon.setImageResource(R.mipmap.doctorprofile_privatedoctor);
            mTvPrivateCount.setTextColor(getResources().getColor(R.color.services_normal_color));
            mTvPrivateText.setTextColor(getResources().getColor(R.color.services_normal_color));
        }
    }


}

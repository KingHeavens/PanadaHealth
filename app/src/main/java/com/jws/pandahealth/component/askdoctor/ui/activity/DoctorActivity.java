package com.jws.pandahealth.component.askdoctor.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.app.widget.LoadingLayout;
import com.jws.pandahealth.component.askdoctor.decoration.DividerItemDecoration;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.askdoctor.model.bean.ReviewInfo;
import com.jws.pandahealth.component.askdoctor.presenter.DoctorPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.DoctorContract;
import com.jws.pandahealth.component.askdoctor.utils.DateUtil;
import com.jws.pandahealth.component.askdoctor.utils.StringUtils;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.ui.activity.PayWebActivity;
import com.jws.pandahealth.component.services.util.DialogUtil;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/26.
 *
 */

public class DoctorActivity extends BaseActivity<DoctorPresenter> implements DoctorContract.View, View.OnClickListener {
    private static final String TAG = "DoctorActivity";
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    String paytype = "1";
    UserInfo userInfo;
    String id;
    List<ReviewInfo> reviewInfoList = new ArrayList<>();
    MyBaseQuickAdapter adapter;
    View headview;
    private DoctorInfo mDoctorInfo;
    IncoldView incoldView;
    private String mOrderId;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_doctor;
    }

    class IncoldView {
        @BindView(R.id.photo)
        ImageView photo;            //图标
        @BindView(R.id.isauth)
        ImageView isauth;           //是否认证
        @BindView(R.id.name)
        TextView name;              //姓名
        @BindView(R.id.zhiwei)
        TextView zhiwei;            //职位
        @BindView(R.id.department)
        TextView department;        //科室
        @BindView(R.id.yiyuan)
        TextView yiyuan;           //医院
        @BindView(R.id.textchat)
        LinearLayout textchat;    //图文资讯文字
        @BindView(R.id.textchatmoney)
        TextView textchatmoney;    //图文资讯价钱
        @BindView(R.id.privatedoctor)
        LinearLayout privatedoctor;//私人医生layout
        @BindView(R.id.privatedoctormoney)
        TextView privatedoctormoney;
        @BindView(R.id.message)//私人医生价钱
                TextView message;     //介绍
        @BindView(R.id.more)
        LinearLayout more;   // 更多按钮
        @BindView(R.id.empty)
        LinearLayout empty;   // 更多按钮

        public IncoldView(View v) {
            ButterKnife.bind(this, v);
        }
    }

    @Override
    protected void initEventAndData() {
        userInfo = MyApplication.getCurrentUser();
        id = getIntent().getStringExtra("id");
        setTitleName(getString(R.string.doctor_profile));

        loadingLayout.showLoading();
        loadingLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.showLoading();
                mPresenter.loadDoctorInfo(id, userInfo == null ? "" : userInfo.token);
            }
        });
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.loadDoctorInfo(id, userInfo == null ? "" : userInfo.token);
    }

    @Override
    public void loadDoctorSuccess(DoctorInfo baseInfo) {
        JLog.e(baseInfo.toString());
        mDoctorInfo = baseInfo;
        reviewInfoList.clear();
        if (headview == null) {
            adapter = new MyBaseQuickAdapter(reviewInfoList);
            headview = LayoutInflater.from(this).inflate(R.layout.incold_doctorinfo_header, null);
            incoldView = new IncoldView(headview);
            ImageLoaderUtil.loadUserImage(DoctorActivity.this,incoldView.photo, baseInfo.doctorIcon);//图标
            incoldView.isauth.setVisibility(baseInfo.verified.equals("0") ? View.GONE : View.INVISIBLE); //是否认证
            incoldView.name.setText(baseInfo.doctorName);              //姓名
            incoldView.zhiwei.setText(baseInfo.doctorTitle);            //职位
            incoldView.department.setText(baseInfo.departmentName);        //科室
            incoldView.yiyuan.setText(baseInfo.hospitalName);           //医院
            //图文资讯价钱
            if (baseInfo.serviceStatus.split(",")[0].equals("1")) {
                incoldView.textchatmoney.setText("$" + baseInfo.servicePrice.split(",")[0] + " / " + baseInfo.serviceTextDuration);
                incoldView.textchat.setOnClickListener(this);    //图文资讯
                incoldView.textchat.setEnabled(true);
                incoldView.textchat.setSelected(true);


            } else {
                incoldView.textchat.setEnabled(false);
                incoldView.textchatmoney.setText(getString(R.string.notopen));
                incoldView.textchatmoney.setEnabled(false);
                incoldView.textchat.setEnabled(false);
                for (int i = 0; i < incoldView.textchat.getChildCount(); i++) {
                    incoldView.textchat.getChildAt(i).setEnabled(false);
                }
            }
            if (baseInfo.serviceStatus.split(",")[1].equals("1")) {
                incoldView.privatedoctormoney.setText("$" + baseInfo.servicePrice.split(",")[1] + " / " + baseInfo.serviceTextDuration);
                incoldView.privatedoctor.setOnClickListener(this);    //图文资讯
                incoldView.privatedoctor.setEnabled(true);
            } else {
                incoldView.privatedoctor.setEnabled(false);
                incoldView.privatedoctormoney.setText(getString(R.string.notopen));
                for (int i = 0; i < incoldView.privatedoctor.getChildCount(); i++) {
                    incoldView.privatedoctor.getChildAt(i).setEnabled(false);
                }
            }
            if (paytype.equals("1")) {
                incoldView.textchat.setSelected(true);
                incoldView.privatedoctor.setSelected(false);
            } else if (paytype.equals("2")) {
                incoldView.privatedoctor.setSelected(true);
                incoldView.textchat.setSelected(false);
            }
            incoldView.message.setText(StringUtils.toDBC(baseInfo.doctorIntro));     //介绍
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int linecount=incoldView.message.getLineCount();
                    if(linecount>5){
                        incoldView.more.setVisibility(View.VISIBLE);
                        incoldView.more.setOnClickListener(DoctorActivity.this);   // 更多按钮
                    }else{
                        incoldView.more.setVisibility(View.GONE);
                    }
                }
            },1000);

            adapter.addHeaderView(headview);
            recyclerview.setAdapter(adapter);


            submit.setVisibility(View.VISIBLE);
            submit.setOnClickListener(this);
            submit.setText("$" + baseInfo.servicePrice.split(",")[0] + " / " + baseInfo.serviceTextDuration);
        }
        if (baseInfo.object.size() != 0) {
            reviewInfoList.addAll(baseInfo.object);
            incoldView.empty.setVisibility(View.GONE);
        } else {
            incoldView.empty.setVisibility(View.VISIBLE);
//            incoldView.empty.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    loadingLayout.showLoading();
//                    mPresenter.loadDoctorInfo(id,  userInfo == null ? "" : userInfo.token);
//                }
//            });
        }
        adapter.notifyDataSetChanged();
        loadingLayout.showContent();
        submit.setVisibility(View.VISIBLE);

    }


    boolean isclickMore = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textchat:
                paytype = "1";
                submit.setText("Text Chat $" + mDoctorInfo.servicePrice.split(",")[0] + " / " + mDoctorInfo.serviceTextDuration);
                incoldView.textchat.setSelected(true);
                incoldView.privatedoctor.setSelected(false);
                break;
            case R.id.privatedoctor:
                paytype = "2";
                submit.setText(
                        "Private Doctor $" + mDoctorInfo.servicePrice.split(",")[1] + " / " + mDoctorInfo.serviceTextDuration);
                incoldView.privatedoctor.setSelected(true);
                incoldView.textchat.setSelected(false);
                break;

            case R.id.submit:
                if(MyApplication.getCurrentUser()==null){
                    Intent it=new Intent(this, LoginActivity.class);
                    startActivity(it);
                    return;
                }else{
                    mDoctorInfo.doctorId = id;
                    mPresenter.checkUserCanSendMessage(mDoctorInfo.doctorId);
                }
                break;
            case R.id.more:
                if (!isclickMore) {
                    incoldView.message.setMaxLines(1000);
                } else {
                    incoldView.message.setMaxLines(5);
                }
                isclickMore = !isclickMore;
                break;
        }
    }


    class MyBaseQuickAdapter extends BaseQuickAdapter<ReviewInfo, BaseViewHolder> {
        public MyBaseQuickAdapter(List<ReviewInfo> doctorInfos) {
            super(R.layout.incold_doctor_reviews_item, doctorInfos);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ReviewInfo o) {
            ImageLoaderUtil.loadUserImage(DoctorActivity.this,
                    (ImageView) baseViewHolder.getView(R.id.photo), o.userIcon);
            baseViewHolder.setText(R.id.name, o.userName)
                    .setText(R.id.time, DateUtil.getTime(Long.parseLong(o.created))).setText(R.id.speak, o.content);
        }

    }


    @Override
    public void noHttpError() {
        if (headview == null) {
            loadingLayout.showError();
            submit.setVisibility(View.GONE);
        } else {
            loadingLayout.showContent();
            submit.setVisibility(View.VISIBLE);
        }
        closeDialog();
        ToastUtil.shortShow(getString(R.string.http_no_net_tip));
    }

    @Override
    public void showError(String msg) {
        loadingLayout.showContent();
        submit.setVisibility(View.VISIBLE);
        ToastUtil.shortShow(msg);
        closeDialog();
    }

    @Override
    public void toPayWebPage(String orderId, String url) {
        closeDialog();
        mOrderId = orderId;
        Intent it = new Intent(mContext, PayWebActivity.class);
        it.putExtra(ServicesConstant.SERVICES_DOCTOR_ID, orderId);
        it.putExtra(ServicesConstant.PAY_URL, url);
        startActivityForResult(it, ServicesConstant.REQUEST_PAY);
    }

    @Override
    public void onPaySuccess(final String hxId) {
        JLog.e(TAG + " onPaySuccess hxId:" + hxId);
        if (TextUtils.isEmpty(hxId))
            return;
        DialogUtil.showNoTitleDialog(this, getString(R.string.pay_success), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(hxId)) {
                    Intent it = new Intent(mContext, ChatActivity.class);
                    it.putExtra(EaseConstant.EXTRA_USER_ID, hxId);
                    it.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    startActivity(it);
                }
                finish();
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onPayFailed() {
        DialogUtil.showNoTitleDialog(this, getString(R.string.pay_fail), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = (Dialog) v.getTag();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onCheckResult(boolean status, final String hxid) {
        if(status){
            DialogUtil.showDialog(this, getString(R.string.still_can_chat), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, ChatActivity.class);
                    it.putExtra(EaseConstant.EXTRA_USER_ID, hxid);
                    it.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    startActivity(it);
                    Dialog dialog = (Dialog) v.getTag();
                    dialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = (Dialog) v.getTag();
                    dialog.dismiss();
                }
            });
        }else {
            DialogUtil.showPayDialog(this, mDoctorInfo, paytype, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                    mPresenter.getOrder(id, paytype);
                    Dialog dialog = (Dialog) v.getTag();
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void getOrderError(String msg) {
        closeDialog();
        showToast(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_SUCCESS) {
            String hxId = data.getStringExtra(ServicesConstant.PAY_DOCTOR_HXID);
            String orderId = data.getStringExtra(ServicesConstant.PAY_ORDER_ID);
            if (orderId == null || !orderId.equals(mOrderId))
                return;
            onPaySuccess(hxId);
        } else if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_FAILED) {
            String orderId = data.getStringExtra(ServicesConstant.PAY_ORDER_ID);
            if (orderId == null || !orderId.equals(mOrderId))
                return;
            onPayFailed();
        } else if (requestCode == ServicesConstant.REQUEST_PAY && resultCode == ServicesConstant.PAY_CANCEL) {
            DialogUtil.showNoTitleDialog(this, getString(R.string.pay_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = (Dialog) v.getTag();
                    dialog.dismiss();
                }
            });
        }
    }


}

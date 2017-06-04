package com.jws.pandahealth.component.services.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.InitInfo;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.presenter.PayPresenter;
import com.jws.pandahealth.component.services.presenter.contract.PayContract;
import com.jws.pandahealth.component.services.util.JLog;

import butterknife.BindView;

public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View,View.OnClickListener {

    @BindView(R.id.layout_doctor_info)
    RelativeLayout mLayoutDoctorInfo;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.iv_badge)
    ImageView mIvBadge;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_professor)
    TextView mTvProfessor;
    @BindView(R.id.ll_name)
    LinearLayout mLlName;
    @BindView(R.id.tv_office)
    TextView mTvOffice;
    @BindView(R.id.tv_hospital)
    TextView mTvHospital;
    @BindView(R.id.empty)
    View mEmpty;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_money_type)
    TextView mTvMoneyType;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.btn_pay)
    Button mBtnPay;
    @BindView(R.id.services_activity_pay)
    RelativeLayout mServicesActivityPay;
    private DoctorInfo mDoctorInfo;
    private String mType;
    private String mDoctorId;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.services_activity_pay;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.pay_page_title));

        mDoctorInfo = (DoctorInfo) getIntent().getSerializableExtra("userInfo");
        mPresenter.init(mDoctorInfo);
        mBtnPay.setOnClickListener(this);
    }

    @Override
    public void bindLayoutWithoutDoctorInfo(InitInfo info) {
        //TODO 获取
        mLayoutDoctorInfo.setVisibility(View.GONE);
        mTvAmount.setText(info.quickAskPrice);
        mTvTime.setText(info.quickAskTime);
        mType = ServicesConstant.TYPE_TEXT;
    }

    @Override
    public void bindLayoutData(DoctorInfo info) {
        mTvName.setText(info.doctorName);
        mTvProfessor.setText(info.doctorTitle);
        mTvOffice.setText(info.departmentName);
        mTvHospital.setText(info.hospitalName);
        mTvAmount.setText(info.servicePrice);
        mTvTime.setText(info.serviceTextDuration);
        mIvBadge.setVisibility("0".equals(info.verified) ? View.GONE : View.VISIBLE);
        mType = getIntent().getStringExtra(ServicesConstant.SERVICES_TYPE);
        if(TextUtils.isEmpty(mType)){
            showError(getString(R.string.no_pay_type));
        }
        if(TextUtils.isEmpty(info.doctorId)){
            showError(getString(R.string.no_doctor_id));
            mBtnPay.setEnabled(false);
        }else
            mDoctorId = info.doctorId;
    }



    @Override
    public void showError(String msg) {
        JLog.e(msg);
        showToast(msg);
    }

    @Override
    public void noHttpError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pay:
                mPresenter.getOrder(mDoctorId,mType);
                break;
        }
    }


    @Override
    public void toPayWebPage(String orderId) {
        Intent it = new Intent(mContext,PayWebActivity.class);
        it.putExtra(ServicesConstant.SERVICES_ORDER_ID,orderId);
        startActivity(it);
    }
}

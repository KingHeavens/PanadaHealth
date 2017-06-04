package com.jws.pandahealth.component.services.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.services.ServicesConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ServicesPayDetailDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.empty)
    View empty;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_money_type)
    TextView tvMoneyType;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_badge)
    ImageView ivBadge;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_professor)
    TextView tvProfessor;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_office)
    TextView tvOffice;
    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.services_activity_pay)
    RelativeLayout servicesActivityPay;
    private final Unbinder bind;

    public ServicesPayDetailDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.services_dialog_pay_detail);
        bind = ButterKnife.bind(this);
        close.setOnClickListener(this);
        btnPay.setTag(this);
        close.setTag(this);
    }

    public void setOnPayClickListener(View.OnClickListener listener){
        btnPay.setOnClickListener(listener);
    }

    public void setDoctorInfo(DoctorInfo info,String type){
        String[] price = info.servicePrice.split(",");
        tvName.setText(info.doctorName);
        tvProfessor.setText(info.doctorTitle);
        tvOffice.setText(info.departmentName);
        tvHospital.setText(info.hospitalName);
        tvTime.setText(info.serviceTextDuration);
        ivBadge.setVisibility("0".equals(info.verified) ? View.GONE : View.VISIBLE);
        switch (type){
            case ServicesConstant.TYPE_TEXT:
                tvType.setText(getContext().getString(R.string.textchat));
                if(price!=null && price.length > 0)
                    tvAmount.setText("$" + price[0]);
                break;
            case ServicesConstant.TYPE_VOICE_CALL:
                tvType.setText(getContext().getString(R.string.voice_call));
                if(price!=null && price.length > 1)
                    tvAmount.setText("$" + price[1]);
                break;
            case ServicesConstant.TYPE_PRIVATE_DOCTOR:
                tvType.setText(getContext().getString(R.string.private_doctor));
                if(price!=null && price.length > 2)
                    tvAmount.setText("$" + price[2]);
                break;
        }
        //
    }


    @Override
    public void dismiss() {
        super.dismiss();
        bind.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dismiss();
                break;
        }
    }
}

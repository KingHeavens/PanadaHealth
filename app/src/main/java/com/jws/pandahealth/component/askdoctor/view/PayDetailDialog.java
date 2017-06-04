package com.jws.pandahealth.component.askdoctor.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.util.NumberUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PayDetailDialog extends Dialog implements View.OnClickListener {


    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.tv_money_type)
    TextView tv_money_type;

    @BindView(R.id.services_activity_pay)
    RelativeLayout servicesActivityPay;
    private final Unbinder bind;

    public PayDetailDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.services_pay_dialog);
        bind = ButterKnife.bind(this);
        close.setOnClickListener(this);
        sure.setTag(this);
        close.setTag(this);
    }

    public void setOnPayClickListener(View.OnClickListener listener){
        sure.setOnClickListener(listener);
    }

    public void setDoctorInfo(DoctorInfo info,String type){
        String[] price = info.servicePrice.split(",");
        tv_time.setText(info.serviceTextDuration);
        tv_money_type.setText("$");
        switch (type){
            case ServicesConstant.TYPE_TEXT:
                tv_type.setText(getContext().getString(R.string.textchat));
                if(price!=null && price.length > 0)
                    tv_amount.setText(NumberUtils.subZeroAndDot(price[0]));
                break;
            case ServicesConstant.TYPE_VOICE_CALL:
                tv_type.setText(getContext().getString(R.string.voice_call));
                if(price!=null && price.length > 1)
                    tv_amount.setText(NumberUtils.subZeroAndDot(price[1]));
                break;
            case ServicesConstant.TYPE_PRIVATE_DOCTOR:
                tv_type.setText(getContext().getString(R.string.private_doctor));
                if(price!=null && price.length > 2)
                    tv_amount.setText(NumberUtils.subZeroAndDot(price[2]));
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

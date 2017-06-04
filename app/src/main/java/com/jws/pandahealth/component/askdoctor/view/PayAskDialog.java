package com.jws.pandahealth.component.askdoctor.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorInfo;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.util.NumberUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PayAskDialog extends Dialog implements View.OnClickListener {


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
    String money;
    String time;
    @BindView(R.id.services_activity_pay)
    RelativeLayout servicesActivityPay;
    private final Unbinder bind;

    public PayAskDialog(Context context, String money, String time) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.services_pay_dialog);
        this.money = money;
        this.time = time;
        bind = ButterKnife.bind(this);
        close.setOnClickListener(this);
        sure.setTag(this);
        close.setTag(this);

        tv_time.setText(time);
        tv_money_type.setText("$");
        tv_type.setText(getContext().getString(R.string.textchat));
        tv_amount.setText(money);
    }

    public void setOnPayClickListener(View.OnClickListener listener) {
        sure.setOnClickListener(listener);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        bind.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
        }
    }
}

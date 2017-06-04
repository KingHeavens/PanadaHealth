package com.jws.pandahealth.component.askdoctor.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.app.ui.activity.WebViewActivity;
import com.jws.pandahealth.component.askdoctor.presenter.AskDoctorFragmentPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.AskDoctorFragmentContract;
import com.jws.pandahealth.component.askdoctor.ui.activity.FindDoctorActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.FindDoctorListActivity;
import com.jws.pandahealth.component.askdoctor.ui.activity.QuestionNewActivity;
import com.jws.pandahealth.component.askdoctor.view.Gradient;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * askdoctors
 */
public class HomeFragment extends BaseFragment<AskDoctorFragmentPresenter> implements AskDoctorFragmentContract.View,View.OnClickListener{
    @BindView(R.id.questionnow)
    TextView questionnow;
    @BindView(R.id.finddoctor)
    TextView finddoctor;
    @BindView(R.id.chinesemedicines)
    TextView chinesemedicines;
    @BindView(R.id.chinesemedical)
    TextView chinesemedical;
    @BindView(R.id.gradient)
    Gradient gradient;
    Intent it;
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initEventAndData() {

        ArrayList<ImageView> views=new ArrayList<ImageView>();
        views.add(newImageView(R.mipmap.bg1));
        views.add(newImageView(R.mipmap.bg2));
        views.add(newImageView(R.mipmap.bg3));
        gradient.setImageViews(views);
        questionnow.setOnClickListener(this);
        finddoctor.setOnClickListener(this);
        chinesemedicines.setOnClickListener(this);
        chinesemedical.setOnClickListener(this);

    }

    protected ImageView newImageView(int resources){
        ImageView v=(ImageView) LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_iv_item,null);
        v.setBackgroundResource(resources);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.questionnow:go2QuestionNow();
            break;
            case R.id.finddoctor:go2FindDoctor();
            break;
            case R.id.chinesemedicines:go2ChineseMedicines();
            break;
            case R.id.chinesemedical:go2ChinaMedicalTourism();
            break;
        }
    }

    @Override
    public void go2QuestionNow() {
        it=new Intent();
        it.setClass(mActivity, QuestionNewActivity.class);
        startActivity(it);
    }

    @Override
    public void go2FindDoctor() {
        it=new Intent();
        it.setClass(mActivity, FindDoctorActivity.class);
        startActivity(it);
    }

    @Override
    public void go2ChineseMedicines() {
        it=new Intent();
        it.setClass(mActivity, FindDoctorListActivity.class);
        it.putExtra("isQuickIn",true);
        startActivity(it);
    }

    @Override
    public void go2ChinaMedicalTourism() {
        it=new Intent();
        it.setClass(mActivity, WebViewActivity.class);
        it.putExtra("title","China medical travelling");
        it.putExtra("url", AppConfig.CHINESEMEDICALURL);
        it.putExtra("isaddjava",false);
        startActivity(it);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }
}

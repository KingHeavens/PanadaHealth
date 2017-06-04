package com.jws.pandahealth.component.services.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Equivalence;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DateUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.model.bean.MyDoctorInfo;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/26.
 *
 */

public class ServicesMyDoctorListAdapter extends BaseQuickAdapter<MyDoctorInfo,BaseViewHolder>{
    private Activity mActivity;
    public ServicesMyDoctorListAdapter(Activity activity, List<MyDoctorInfo> data) {
        super(R.layout.services_item_my_doctor, data);
        this.mActivity = activity;

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyDoctorInfo info) {
        baseViewHolder.setText(R.id.tv_name,info.doctorName)
                .setText(R.id.tv_professor,info.doctorTitle)
                .setText(R.id.tv_office,info.departmentName)
                .setText(R.id.tv_hospital,info.hospitalName)
                .setText(R.id.tv_be_good_at,info.doctorIntro)
                .setVisible(R.id.iv_badge,"1".equals(info.verified));
        ImageView text =  baseViewHolder.getView(R.id.iv_text);
        ImageView voice =  baseViewHolder.getView(R.id.iv_phone);
        ImageView privateDoctor =  baseViewHolder.getView(R.id.iv_private_doctor);
        setImage(text,info,0);
        setImage(voice,info,1);
        setImage(privateDoctor,info,2);
        ImageLoaderUtil.loadUserImage(mActivity, (ImageView) baseViewHolder.getView(R.id.iv_avatar),info.doctorIcon);
    }

    private void setImage(ImageView text, MyDoctorInfo info, int i) {
        String[] split = info.serviceStatus.split(",");
        if(split.length >= i){
            if("0".equals(split[i])){
                if(i == 0)
                    text.setImageResource(R.mipmap.doctorprofile_textchar);
                else if(i == 1)
                    text.setImageResource(R.mipmap.doctorprofile_voicecall);
                else if(i == 2)
                    text.setImageResource(R.mipmap.doctorprofile_privatedoctor);
            }else{
                if(i == 0)
                    text.setImageResource(R.mipmap.doctorprofile_textchar_selected);
                else if(i == 1)
                    text.setImageResource(R.mipmap.doctorprofile_voicecall_seleted);
                else if(i == 2)
                    text.setImageResource(R.mipmap.doctorprofile_privatedoctor_selected);
            }

        }
    }


}

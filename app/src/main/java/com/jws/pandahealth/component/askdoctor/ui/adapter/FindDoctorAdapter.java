package com.jws.pandahealth.component.askdoctor.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorType;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */

public class FindDoctorAdapter extends BaseQuickAdapter<DoctorType, BaseViewHolder> {
    public FindDoctorAdapter(List<DoctorType> s) {
//  单个样式
    super(R.layout.incold_doctor_type_item, s);
//            多个样式  list的数据必须继承 MultiItemEntity 如class userinfo extends MultiItemEntity {}
//            super(s);
//            add(0,R.layout.item1);
//            add(1,R.layout.item2);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DoctorType doctorType) {
//            单个样式
        baseViewHolder.setText(R.id.tv, doctorType.typeName);
        ((ImageView) baseViewHolder.getView(R.id.iv)).setImageResource(doctorType.resources);
//            多个样式\
//            int type=baseViewHolder.getItemViewType();
//            if(type==0){
//                baseViewHolder.setText(R.id.tv, strings).addOnClickListener(R.id.tv).addOnLongClickListener(R.id.tv);
//            }else if(type==1){
//                baseViewHolder.setText(R.id.tv, strings).addOnClickListener(R.id.tv).addOnLongClickListener(R.id.tv);
//            }
    }
}

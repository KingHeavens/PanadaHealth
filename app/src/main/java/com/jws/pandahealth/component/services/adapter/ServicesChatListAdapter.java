package com.jws.pandahealth.component.services.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DateUtil;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.services.model.bean.DoctorChatInfo;
import com.jws.pandahealth.component.services.util.NumberUtils;

import java.util.List;

/**
 * Created by zhaijinjing on 2016/12/26.
 *
 */

public class ServicesChatListAdapter extends BaseQuickAdapter<DoctorChatInfo,BaseViewHolder>{

    private boolean mInService = true;

    public ServicesChatListAdapter(List<DoctorChatInfo> data) {
        super(R.layout.services_item_chat,data);
        mInService = true;
    }
    public ServicesChatListAdapter(List<DoctorChatInfo> data,boolean inServer) {
        super(R.layout.services_item_chat,data);
        this.mInService = inServer;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DoctorChatInfo doctorChatInfo) {
        baseViewHolder.setText(R.id.tv_title,parseChatType(doctorChatInfo.type))
                .setText(R.id.tv_content,parseContent(doctorChatInfo))
                .setText(R.id.tv_date, DateUtil.formatTimesMMdd(doctorChatInfo.updated))
                .setVisible(R.id.dot,doctorChatInfo.hasUnReadMsg)
                .setVisible(R.id.tv_status,mInService)
        .setImageResource(R.id.iv_avatar,parseAvatar(doctorChatInfo.type));
    }

    private int parseAvatar(String type) {
        int resource = 0;
        switch(type){
            case "1":
                resource = R.mipmap.doctorprofile_textchar_selected;
                break;
            case "2":
                resource = R.mipmap.doctorprofile_voicecall;
                break;
            case "3":
                resource = R.mipmap.doctorprofile_privatedoctor_selected;
                break;
        }
        return resource;
    }

    private String parseContent(DoctorChatInfo doctorChatInfo) {
        return  doctorChatInfo.doctorName + " $" + NumberUtils.subZeroAndDot(doctorChatInfo.servicePrice) + " / " + doctorChatInfo.serviceTextDuration;
    }

    private String parseChatType(String type) {
        String str = "";
        switch(type){
            case "1":
                str = MyApplication.getInstance().getString(R.string.textchat);
                break;
            case  "2":
                str = MyApplication.getInstance().getString(R.string.voice_chat);
                break;
            case "3":
                str = MyApplication.getInstance().getString(R.string.private_doctor);
                break;
        }
        return str;
    }
}

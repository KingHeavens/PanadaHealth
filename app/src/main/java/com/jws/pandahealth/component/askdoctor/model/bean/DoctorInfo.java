package com.jws.pandahealth.component.askdoctor.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 *
 */

public class DoctorInfo implements Serializable{
    public String doctorId;//
    public String doctorName;//	名字
    public String doctorIcon;//	头像
    public String doctorTitle;//	头衔
    public String departmentName;//	科室名
    public String doctorIntro;//	简介
    public String hospitalName;//	医院
    public String verified;//	医学认证图标显示状态，0-不显示，1-显示
    public String serviceStatus;//	（图文、电话、私人医生）服务状态，逗号隔开。0-未开通，1-开通。例：1,0,0
    public String servicePrice;//	（图文、电话、私人医生）服务价格，逗号隔开。例：39,0,1050
    public String serviceTextSurplus;//	剩余咨询次数，0次则提示购买
    public String serviceTextSurplusTime;//	咨询截止时间，时间戳
    public String serviceTextDuration;//	持续时长，显示文本，例：24 hours或month
    public List<ReviewInfo> object;

    @Override
    public String toString() {
        return "DoctorInfo{" +
                "doctorId='" + doctorId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorIcon='" + doctorIcon + '\'' +
                ", doctorTitle='" + doctorTitle + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", doctorIntro='" + doctorIntro + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", verified='" + verified + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceTextSurplus='" + serviceTextSurplus + '\'' +
                ", serviceTextSurplusTime='" + serviceTextSurplusTime + '\'' +
                ", serviceTextDuration='" + serviceTextDuration + '\'' +
                ", object=" + object +
                '}';
    }
}

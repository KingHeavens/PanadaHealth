package com.jws.pandahealth.component.services.model.bean;

/**
 * Created by zhaijinjing on 2016/12/26.
 * Services MyDoctor页面数据
 */

public class MyDoctorInfo {
    public String doctorId;  //医生ID
    public String doctorName; //名字
    public String doctorIcon; //头像
    public String doctorTitle; //头衔
    public String departmentName; //科室名
    public String doctorIntro; //简介
    public String hospitalName;//医院
    public String verified; //医学认证图标显示状态，0-不显示，1-显示
    public String serviceStatus; //（图文、电话、私人医生）服务状态，逗号隔开。0-未开通，1-开通。例：1,0,0
}

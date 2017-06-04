package com.jws.pandahealth.component.askdoctor.model.bean;

/**
 * Created by Administrator on 2016/12/23.
 */

public class DoctorType {
    public String typeName;
    public int resources;
    public DoctorType(String typeName,int resources){
        this.resources=resources;
        this.typeName=typeName;
    }
}

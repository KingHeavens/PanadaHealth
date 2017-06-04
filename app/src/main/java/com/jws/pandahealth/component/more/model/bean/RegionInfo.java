package com.jws.pandahealth.component.more.model.bean;

/**
 * 我的－地区选择
 * 
 * @author luoming
 *
 */
public class RegionInfo {

	public String id;
	public String areaName;
//	public String hasMore; //是否有更多
	public String isSelected;//是否选中



	public RegionInfo(String id,String regionName) {
		this.id = id;
		this.areaName = regionName;
	}


}

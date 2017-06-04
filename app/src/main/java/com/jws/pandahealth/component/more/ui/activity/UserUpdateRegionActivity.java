package com.jws.pandahealth.component.more.ui.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.askdoctor.decoration.DividerItemDecoration;
import com.jws.pandahealth.component.more.model.bean.RegionInfo;
import com.jws.pandahealth.component.more.presenter.UserUpddateRegionPresenter;
import com.jws.pandahealth.component.more.presenter.contract.UserUpdateRegionContract;
import com.jws.pandahealth.component.more.ui.adapter.RegionAdapter;
import com.jws.pandahealth.component.more.ui.adapter.RegionDividerItemDecoration;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * update userCity type 地区
 * @author luoming
 */
public class UserUpdateRegionActivity extends BaseActivity<UserUpddateRegionPresenter> implements
		UserUpdateRegionContract.View {

	@BindView(R.id.recyclerview)
	RecyclerView recyclerView;
	List<RegionInfo> regionInfoList;
    RegionAdapter regionAdapter;
    String selectedCountryId;
    String selectedCountryName;
    boolean isFirst;

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_userinfo_update_region;
	}

	@Override
	protected void initEventAndData() {
		setTitleName(getString(R.string.user_info_region));

        selectedCountryId =getIntent().getStringExtra("id");
        selectedCountryName =getIntent().getStringExtra("areaName");
        isFirst = TextUtils.isEmpty(selectedCountryId);

        regionInfoList = new ArrayList<>();
        regionAdapter = new RegionAdapter(this,regionInfoList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);//设置为垂直布局，这也是默认的
        recyclerView.setLayoutManager(layoutManager);       //设置布局管理器
		recyclerView.setAdapter(regionAdapter);//设置Adapter
        recyclerView.addItemDecoration(new RegionDividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)); //设置分隔线
        recyclerView.setItemAnimator( new DefaultItemAnimator());//设置增加或删除条目的动画
        regionAdapter.setOnItemClickListener(new RegionAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                RegionInfo regionInfo = regionInfoList.get(position);
                if(isFirst){
                    Intent it = new Intent(UserUpdateRegionActivity.this,UserUpdateRegionActivity.class);
                    it.putExtra("id",regionInfo.id);
                    it.putExtra("areaName",regionInfo.areaName);
                    startActivityForResult(it,0x001);
                }else{
                    //修改城市名称
                    mPresenter.saveLocalCityInfo(selectedCountryId,selectedCountryName,regionInfo.id,regionInfo.areaName);
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onLongClick(int position) {
                JLog.e("onLongClick-->点击" + position);
            }
        });

        showDialog();

        mPresenter.getRegionList(selectedCountryId);

		overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);

	}

	@Override
	public void setRegionList(BaseInfo<List<RegionInfo>> baseInfo) {
        closeDialog();
        if("1".equals(baseInfo.getStatus())){
            regionInfoList.clear();
            regionInfoList.addAll(baseInfo.getObject());
            regionAdapter.notifyDataSetChanged();
        }else{
            showToast(baseInfo.getErrmsg());
        }

	}

	@Override
	public void noHttpError() {
        closeDialog();
		showToast(getString(R.string.http_no_net_tip));
	}

	@Override
	public void showError(String msg) {
        closeDialog();
		showToast(msg);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK)
             return;

        if(requestCode == 0x001)
        {
            setResult(RESULT_OK);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

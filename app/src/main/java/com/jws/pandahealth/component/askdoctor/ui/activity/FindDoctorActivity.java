package com.jws.pandahealth.component.askdoctor.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.askdoctor.decoration.DividerGridItemDecoration;
import com.jws.pandahealth.component.askdoctor.model.bean.DoctorType;
import com.jws.pandahealth.component.askdoctor.presenter.FindDoctorPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.FindDoctorContract;
import com.jws.pandahealth.component.askdoctor.ui.adapter.FindDoctorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/23.
 */

public class FindDoctorActivity extends BaseActivity<FindDoctorPresenter> implements FindDoctorContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<DoctorType> types;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_finddoctor;
    }

    @Override
    protected void initEventAndData() {
        setTitleName(getString(R.string.findadoctor));

        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerview.addItemDecoration(new DividerGridItemDecoration(this,4));
        recyclerview.setAdapter(new FindDoctorAdapter(initList()));
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Intent it=new Intent(FindDoctorActivity.this,FindDoctorListActivity.class);
                it.putExtra("departmentIndex",i);
                startActivity(it);
            }
        });

    }

    List<DoctorType> initList() {
        types = new ArrayList<>();
        types.add(new DoctorType("Obstetrics", R.drawable.finddoctor01selector));
        types.add(new DoctorType("Pediatrics", R.drawable.finddoctor02selector));
        types.add(new DoctorType("Dermatology", R.drawable.finddoctor03selector));
        types.add(new DoctorType("Medicine", R.drawable.finddoctor04selector));
        types.add(new DoctorType("Andrology", R.drawable.finddoctor05selector));
        types.add(new DoctorType("Surgery", R.drawable.finddoctor06selector));
        types.add(new DoctorType("Chinese medicine", R.drawable.finddoctor07selector));
        types.add(new DoctorType("Orthopaedics", R.drawable.finddoctor08selector));
        types.add(new DoctorType("Psychology", R.drawable.finddoctor09selector));
        types.add(new DoctorType("Dental", R.drawable.finddocto10selector));
        types.add(new DoctorType("Ophthalmology", R.drawable.finddocto11selector));
        types.add(new DoctorType("Ear nose throat", R.drawable.finddocto12selector));
        types.add(new DoctorType("Cancer and treatment", R.drawable.finddocto13selector));
        types.add(new DoctorType("Plastic surgery", R.drawable.finddocto14selector));
        types.add(new DoctorType("Report interpretation", R.drawable.finddocto15selector));
        types.add(new DoctorType("Nutrition", R.drawable.finddocto16selector));
        return types;
    }
    @Override
    public void noHttpError() {

    }
    @Override
    public void showError(String msg) {

    }
}

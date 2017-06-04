package com.jws.pandahealth.component.more.ui.activity;


import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.model.bean.BaseInfo;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.util.KeyBoardUtil;
import com.jws.pandahealth.component.more.presenter.UserUpdateInfoPresenter;
import com.jws.pandahealth.component.more.presenter.contract.UserUpdateInfoContract;
import com.jws.pandahealth.component.more.utils.InputFilterUtil;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * update userInfo type:0用户名称 1性别
 *
 * @author luoming
 */
public class UserUpdateInfoActivity extends BaseActivity<UserUpdateInfoPresenter> implements
        UserUpdateInfoContract.View, OnClickListener {

    @BindView ( R.id.stub)
    ViewStub mStub;

    NameStubView nameStubView;
    GenderStubView genderStubView;

    public static final int NAME_TYPE = 0; // 修改昵称
    public static final int GENDER_TYPE = 1; // 修改性别
    int type;
    String data;
    Timer timer;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_userinfo_update_info;
    }

    @Override
    protected void initEventAndData() {

        type = getIntent().getIntExtra("type", -1);
        data = getIntent().getStringExtra("data");
        switch (type) {
            case NAME_TYPE:
                initNickNameView();
                break;

            case GENDER_TYPE:
                initGenderView();
                break;

            default:
                break;
        }

        overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);

    }

    /**
     * 修改昵称
     */
    private void initNickNameView() {

        setTitleName(getString(R.string.user_info_name),getString(R.string.user_update_title_btn_ok));

        mStub.setLayoutResource ( R.layout.activity_userinfo_update_username);
        View view = mStub.inflate ();
        nameStubView = new NameStubView(view);
        nameStubView.userNameEt.setFilters(new InputFilter[]{new InputFilterUtil(
                16)});

        if (!TextUtils.isEmpty(data)) {
            nameStubView.userNameEt.setText(data);
            nameStubView.userNameEt.setSelection(data.length());
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyBoardUtil.showKeyboard(UserUpdateInfoActivity.this);
            }
        }, 200);
    }

    /**
     * 修改性别
     */
    private void initGenderView() {
        setTitleName(getString(R.string.user_info_gender));
        mStub.setLayoutResource ( R.layout.activity_userinfo_update_gender);
        View view = mStub.inflate ();
        genderStubView = new GenderStubView(view);
        genderStubView.sexLayout1.setOnClickListener(this);
        genderStubView.sexLayout2.setOnClickListener(this);
        JLog.e("data -->" + data);
        changeSexUI(AppConfig.SEX_MEN_KEY.equals(data));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_sex_layout1) {
            goUpdateGender(AppConfig.SEX_MEN_KEY);
        } else if (view.getId() == R.id.update_sex_layout2) {
            goUpdateGender(AppConfig.SEX_WOMEN_KEY);
        }
    }

    // title栏  右边功能按键
    public void clickTitleRight(View view) {
        if(type != NAME_TYPE)
            return;
        goUpdateNickName();
    }

    /**
     * 修改昵称
     **/
    private void goUpdateNickName() {
        hiddenKeyBoard();
        String data = nameStubView.userNameEt.getText().toString();
        if (TextUtils.isEmpty(data)) {
            showToast(getString(R.string.updateuserinfo_inputnamenull));
            nameStubView.userNameEt.setFocusable(true);
            return;
        }
        showDialog();
        mPresenter.updateName(data);
    }

    /**
     * 修改性别
     **/
    private void goUpdateGender(String value) {
        if (value.equals(data)) {
            finish();
            return;
        }

        mPresenter.updateGender(value);
    }


    @Override
    public void updateNameSuccess(BaseInfo<UserInfo> u) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void updateGenderSuccess(String value) {
        changeSexUI(AppConfig.SEX_MEN_KEY.equals(value));
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void showError(String msg)
    {
        closeDialog();
        showToast(msg);
    }

    private void changeSexUI(boolean isBoy) {
        genderStubView.sexImg1.setEnabled(isBoy);
        genderStubView.sexImg2.setEnabled(!isBoy);
    }

    @Override
    public void noHttpError() {
        closeDialog();
        ToastUtil.shortShow(getString(R.string.http_no_net_tip));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
        hiddenKeyBoard();


    }

    /** stubView **/
    class NameStubView {
        @BindView(R.id.update_username_et)
        EditText userNameEt;
        public NameStubView(View view) {
            ButterKnife.bind(this, view);
        }
    }


    class GenderStubView {
        @BindView(R.id.update_sex_layout1)
        View sexLayout1;
        @BindView(R.id.update_sex_layout2)
        View sexLayout2;
        @BindView(R.id.update_sex_img1)
        ImageView sexImg1;
        @BindView(R.id.update_sex_img2)
        ImageView sexImg2;
        public GenderStubView(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
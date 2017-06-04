package com.jws.pandahealth.component.more.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.LogUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.app.ui.activity.LoginActivity;
import com.jws.pandahealth.component.app.ui.activity.WebViewActivity;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;
import com.jws.pandahealth.component.more.presenter.MePresenter;
import com.jws.pandahealth.component.more.presenter.contract.MeContract;
import com.jws.pandahealth.component.more.ui.activity.NotificationsActivity;
import com.jws.pandahealth.component.more.ui.activity.SettingActivity;
import com.jws.pandahealth.component.more.ui.activity.UserInfoActivity;
import com.jws.pandahealth.component.services.ui.activity.ChatActivity;
import com.jws.pandahealth.component.services.util.JLog;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

public class MoreFragment extends BaseFragment<MePresenter> implements MeContract.View, View.OnClickListener {

    @BindView(R.id.melayout)
    RelativeLayout me_layout;
    @BindView(R.id.notifications)
    RelativeLayout notifications;
    @BindView(R.id.cantactus)
    RelativeLayout cantactus;
    @BindView(R.id.aboutpandahealth)
    RelativeLayout aboutpandahealth;
    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    UserInfo userInfo;
    Intent it;
    @BindView(R.id.notifications_img)
    ImageView notificationsImg;
    @BindView(R.id.cantactus_img)
    ImageView cantactusImg;
    @BindView(R.id.aboutpandahealth_img)
    ImageView aboutpandahealthImg;
    @BindView(R.id.setting_img)
    ImageView settingImg;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initEventAndData() {
        setTitleBackGone();
        setTitleText(getString(R.string.moretitle));

        Logger.json("{\"status\":\"1\",\"errcode\":\"\",\"errmsg\":\"\",\"id\":\"5363183\",\"nickname\":\"\\u4eac\",\"sex\":\"male\",\"usericon\":\"http:\\/\\/img.yiliaohui.com\\/header\\/201612\\/58578d42cbd59.jpg\",\"articleCount\":\"1\",\"questionCount\":\"0\",\"answerCount\":\"0\",\"description\":\"\",\"followed\":\"0\"}\n");

        LogUtil.e("MoreFragment initEventAndData-->");
        //DisplayUtils.adaptStatusBar(mContext, centerAdaptStatusBar);
        userInfo = mPresenter.getCurrentUser();
        me_layout.setOnClickListener(this);
        notifications.setOnClickListener(this);
        cantactus.setOnClickListener(this);
        aboutpandahealth.setOnClickListener(this);
        setting.setOnClickListener(this);
        initLogin();
        mPresenter.addReceiver();
    }


    @Override
    public void initLogin() {
        userInfo = mPresenter.getCurrentUser();
        if (userInfo == null) {
            email.setText("");
            name.setText("Please "+getString(R.string.login));
            photo.setImageResource(R.mipmap.more_default_user);
        } else {
            JLog.e(userInfo.toString());
            email.setText(userInfo.email);
            name.setText(userInfo.userName);
            ImageLoaderUtil.loadUserImage(mActivity, photo, userInfo.userIcon);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.melayout:
                if (userInfo == null) {
                    it = new Intent(mActivity, LoginActivity.class);
                    startActivity(it);
                    return;
                }
                it = new Intent(mActivity, UserInfoActivity.class);
                startActivity(it);
                break;

            case R.id.notifications:
                it = new Intent(mActivity, NotificationsActivity.class);
                startActivity(it);
                break;

            case R.id.cantactus:
                if (userInfo == null) {
                    it = new Intent(mActivity, LoginActivity.class);
                    startActivity(it);
                } else {
                    Intent it = new Intent(mContext, ChatActivity.class);
                    it.putExtra(EaseConstant.EXTRA_USER_ID, AppConfig.PANDAHEALTH_CHATID);
                    it.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    startActivity(it);
                }
                break;

            case R.id.aboutpandahealth:
                Intent it2=new Intent(mActivity,WebViewActivity.class);
                it2.putExtra("title",getString(R.string.aboutpandahealth));
                it2.putExtra("url", AppConfig.ABOUT);
                it2.putExtra("isaddjava",false);
                startActivity(it2);
                break;

            case R.id.setting:
                if (userInfo == null) {
                    it = new Intent(mActivity, LoginActivity.class);
                    startActivity(it);
                } else {
                    Intent it = new Intent(mActivity, SettingActivity.class);
                    startActivity(it);
                }

                break;

            default:
                break;
        }
    }


    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }


}

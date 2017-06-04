package com.jws.pandahealth.component.app.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.WelcomePresenter;
import com.jws.pandahealth.component.app.presenter.contract.WelcomeContract;
import com.jws.pandahealth.component.services.util.JLog;

import butterknife.BindView;

/**
 * Created by codeest on 16/8/15.
 *
 */
public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeContract.View {
    @BindView(R.id.iv_welcome_bg)
    ImageView ivWelcomeBg;
    @BindView(R.id.tv_welcome_author)
    TextView tvWelcomeAuthor;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.loadData();
    }

    @Override
    public void showContent() {
        /*ImageLoaderUtil.load(this, welcomeBean.getImg(), ivWelcomeBg);
        ivWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        tvWelcomeAuthor.setText(welcomeBean.getText());*/
    }

    @Override
    public void jump2Main() {
        Intent it = new Intent(mContext, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void jump2Login() {
        Intent it = new Intent(mContext, LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void jump2Guide() {
        Intent it = new Intent(mContext,AppGuideActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void showError(String msg) {
        //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        JLog.e(msg);
    }


    @Override
    protected void onDestroy() {
        if(ivWelcomeBg != null)
            Glide.clear(ivWelcomeBg);
        super.onDestroy();
    }


    @Override
    public void noHttpError() {
        showToast(getString(R.string.http_no_net_tip));
    }

}

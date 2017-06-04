package com.jws.pandahealth.component.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DisplayUtil;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.AppGuidePresenter;
import com.jws.pandahealth.component.app.presenter.contract.GuideContract;
import com.jws.pandahealth.component.app.ui.adapter.ViewPageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首次安装欢迎界面
 *
 * @author luoming
 */
public class AppGuideActivity extends BaseActivity<AppGuidePresenter> implements GuideContract.View {

    @BindView(R.id.guide_start_btn)
    protected TextView mBtnStart;
    @BindView(R.id.status_bar2)
    View statusBar;
    private PagerAdapter mPagerAdapter;
    private ArrayList<View> views;
    private GestureDetector gestureDetector; // 用户滑动
    private int currentItem = 0; // 当前图片的索引号
    private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.base_activity_guide;
    }

    @Override
    protected void initEventAndData() {
        DisplayUtil.adaptStatusBar2(mContext,statusBar);
        initVariables();
        initView();
    }

    @Override
    public void jump2Main() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void showError(String msg) {

    }


    private void initVariables() {
        gestureDetector = new GestureDetector(new GuideViewTouch());
        DisplayMetrics dm = new DisplayMetrics();//获取分辨率
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        flaggingWidth = dm.widthPixels / 3;
    }


    private void initView() {
        ButterKnife.bind(this);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        views = new ArrayList<>();
        views.add(getItemView(R.mipmap.base_app_guide_item1));
        views.add(getItemView(R.mipmap.base_app_guide_item2));
        views.add(getItemView(R.mipmap.base_app_guide_item3));
        views.add(getItemView(R.mipmap.base_app_guide_item4));
        views.add(getItemView(R.mipmap.base_app_guide_item5));


        mPagerAdapter = new ViewPageAdapter(views);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (currentItem == views.size() - 1) {
                    mBtnStart.setVisibility(View.VISIBLE);//显示Button
                } else
                    mBtnStart.setVisibility(View.INVISIBLE);
            }

        });
    }


    private void loadData() {

    }

    @OnClick({R.id.guide_jump_next_tv, R.id.guide_start_btn})
    public void goToNext() {
        mPresenter.storgedGuideStatus();
    }

    private View getItemView(int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.base_item_guide, null);
        ImageView iv = (ImageView) view.findViewById(R.id.img);
        iv.setBackgroundResource(id);
        return view;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            event.setAction(MotionEvent.ACTION_CANCEL);
        return super.dispatchTouchEvent(event);
    }


    private class GuideViewTouch extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (currentItem == (mPagerAdapter.getCount() - 1)) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
                        - e2.getY())
                        && (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
                        .getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        goToNext();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void noHttpError() {

    }


}

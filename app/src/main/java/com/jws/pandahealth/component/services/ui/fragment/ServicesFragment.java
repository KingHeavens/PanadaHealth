package com.jws.pandahealth.component.services.ui.fragment;


import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DisplayUtil;
import com.jws.pandahealth.component.app.base.BaseFragment;
import com.jws.pandahealth.component.services.presenter.ServicesPresenter;
import com.jws.pandahealth.component.services.presenter.contract.ServicesContract;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.MovedControllerViewPager;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.HEAD;

/**
 *
 */
public class ServicesFragment extends BaseFragment<ServicesPresenter> implements ServicesContract.View, ViewPager.OnPageChangeListener {

    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip mTabs;
    @BindView(R.id.view_pager)
    MovedControllerViewPager mViewPager;

    private final static int VIEW_SIZE = 3;


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.services_fragment;
    }


    @Override
    protected void initEventAndData() {

        setTitleBackGone();
        setTitleText(getString(R.string.services));

        mViewPager.setOffscreenPageLimit(VIEW_SIZE);
        FragmentAdapter adapter = new FragmentAdapter(getFragmentManager());

        mViewPager.setAdapter(new FragmentAdapter(getFragmentManager()));

        adapter.notifyDataSetChanged();
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(this);
        mPresenter.init();
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
        JLog.e(msg);
    }

    @Override
    public void noHttpError() {
        showToast(getString(R.string.http_no_net_tip));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void setFirstTabDotVisibility(int visibility) {
        if(mTabs == null)
            return;
        if(View.VISIBLE == visibility)
            mTabs.showDot(0);
        else
            mTabs.hideDot(0);
    }
    public class FragmentAdapter extends FragmentStatePagerAdapter implements AdvancedPagerSlidingTabStrip.TipsProvider{

        private NewChatFragment mNewChatFragment;
        private MyDoctorFragment mMyDoctorFragment;
        private HistoryFragment mHistoryFragment;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case 0:
                        if (null == mNewChatFragment)
                            mNewChatFragment = NewChatFragment.instance();
                        return mNewChatFragment;

                    case 1:
                        if (null == mMyDoctorFragment)
                            mMyDoctorFragment = MyDoctorFragment.instance();
                        return mMyDoctorFragment;

                    case 2:
                        if (null == mHistoryFragment)
                            mHistoryFragment = HistoryFragment.instance();
                        return mHistoryFragment;

                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case 0:
                        return getString(R.string.services_tab_new);
                    case 1:
                        return getString(R.string.services_tab_my_doctor);
                    case 2:
                        return getString(R.string.services_tab_history);
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int[] getTipsRule(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                return new int[]{
                    RelativeLayout.ALIGN_PARENT_RIGHT
                };
            }

            return new int[0];
        }

        @Override
        public Margins getTipsMargins(int position) {
            return new Margins(0, DisplayUtil.dip2px(mContext,15),DisplayUtil.dip2px(mContext,25),0);
        }

        @Override
        public Drawable getTipsDrawable(int position) {
            return null;
        }
    }
}

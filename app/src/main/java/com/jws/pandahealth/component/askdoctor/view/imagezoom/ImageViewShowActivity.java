package com.jws.pandahealth.component.askdoctor.view.imagezoom;

import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.askdoctor.presenter.ImageViewShowPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.ImageViewShowContract;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.util.ImageLoaderUtil;

import java.util.List;

import butterknife.BindView;

public class ImageViewShowActivity extends BaseActivity<ImageViewShowPresenter> implements ImageViewShowContract.View{
	@BindView(R.id.my_pager)
	MyViewPager viewPager;
	private List<String> list;//图片地址集合
	private int pos; // 指定页码数
	@BindView(R.id.page)
	TextView page;//页码数控件
	@BindView(R.id.back)
	View back;//页码数控件

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_image_show;
	}

	@Override
	protected void initEventAndData() {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		initView();

		initData();

		overridePendingTransition(R.anim.enter_righttoleft, R.anim.noanim);
	}

	@Override
	public void showError(String msg) {

	}

	@Override
	public void noHttpError() {

	}


	private void initView() {

		back.setOnClickListener(new OnClickListener() {
																			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int pos) {
				page.setText(String.valueOf(pos + 1) + "/" + list.size());
			}

		});


	}

	private void initData() {
		list = getIntent().getStringArrayListExtra("list");
		pos = getIntent().getIntExtra("pos", 0);
		
		if (list == null || list.size() == 0) {
			ToastUtil.show(R.string.server_date_error);
			return;
		}
		page.setText(String.valueOf(pos + 1) +"/" + list.size());
		
		viewPager.setAdapter(new ImageAdapter(list));
		viewPager.setCurrentItem(pos);

	}


	public class ImageAdapter extends PagerAdapter {
		private List<String> list;

		public ImageAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {

			EasePhotoView photoView = new EasePhotoView(container.getContext());
			ImageLoaderUtil.load(ImageViewShowActivity.this,list.get(position), photoView );
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

}

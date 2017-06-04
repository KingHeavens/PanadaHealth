package com.jws.pandahealth.component.more.ui.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.askdoctor.utils.ImageUtils;
import com.jws.pandahealth.component.more.presenter.ClipPresenter;
import com.jws.pandahealth.component.more.presenter.contract.ClipContract;
import com.jws.pandahealth.component.more.utils.FileUtils;
import com.jws.pandahealth.component.more.view.ClipImageLayout;
import butterknife.BindView;

/**
 * 个人头像截图
 * 
 * @author luoming
 */
public class UserCenterClipActivity extends BaseActivity<ClipPresenter> implements ClipContract.View,
		View.OnClickListener {

	DisplayMetrics dm;

	@BindView(R.id.id_clipImageLayout)
	ClipImageLayout mClipImageLayout;

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_userinfo_clipimage;
	}

	@Override
	protected void initEventAndData() {
		String path = getIntent().getStringExtra("path");
		if (TextUtils.isEmpty(path) || !FileUtils.isFileExist(path)) {
			return;
		}
		// 用户头像截取
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Bitmap bitmap = ImageUtils.convertToBitmap(
				ImageUtils.compressImage(this, path), dm.widthPixels,
				dm.widthPixels);
		if (bitmap == null) {
			return;
		}
		path= ImageUtils.compressImage(this, path);
		mClipImageLayout.setBitmap(bitmap);

		findViewById(R.id.id_action_clip).setOnClickListener(this);
		findViewById(R.id.id_action_cancle).setOnClickListener(this);
	}

	@Override
	public void showError(String msg) {

	}
	@Override
	public void noHttpError() {
		ToastUtil.shortShow(getString(R.string.http_no_net_tip));
	}
	private void initView() {

	}

	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.id_action_clip) {
			goClipBitmap();
		} else if (id == R.id.id_action_cancle) {
			goCancle();
		}
	}

	private void goClipBitmap() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = ImageUtils.convertToBitmap(
						mClipImageLayout.clip(), dm.widthPixels / 3,
						dm.widthPixels / 3);
				String path = AppConfig.SD_CACHE_PATH
						+ System.currentTimeMillis() + ".jpg";
				ImageUtils.saveUserPhotoToSDCard(bitmap, path);
				if (bitmap != null) {
					bitmap.recycle();
				}
				Intent intent = new Intent();
				intent.putExtra("path", path);
				setResult(RESULT_OK, intent);
				finish();
			}
		}).start();
	}

	private void goCancle() {
		super.onBackPressed();
	}
	
	
	

}

package com.jws.pandahealth.component.askdoctor.view.photo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.RxBusUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.askdoctor.presenter.ImageGridPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.ImageGridContract;
import com.jws.pandahealth.component.askdoctor.view.photo.util.AlbumHelper;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.askdoctor.view.photo.util.BitmapCache;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageBucket;
import com.jws.pandahealth.component.askdoctor.view.photo.util.ImageItem;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * 图片的目录详细页
 * 
 * @author Administrator
 * 
 */

@SuppressLint("InflateParams") public class ImageGridActivity extends BaseActivity<ImageGridPresenter> implements ImageGridContract.View,OnClickListener {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private List<ImageBucket> contentList;
	@BindView(R.id.title)
	RelativeLayout title;
	private List<ImageItem> dataList;
	@BindView(R.id.gridview)
	GridView gridView;
	private ImageGridAdapter adapter;
	private AlbumHelper helper;
	@BindView(R.id.bt)
	Button bt;
	private int count;
	@BindView(R.id.tv_cancel)
	TextView tv_cancel;
	@BindView(R.id.tv_content)
	TextView tv_content;
	private PopupWindow pop = null;
	private ListView popListView = null;
	private BitmapCache cache = new BitmapCache();
	private int pos;
	private ArrayList<ImageItem> tList, sList;
	private int maxCount = 3;

	private Handler mHandler = new Handler(Looper.myLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.shortShow(getString(R.string.ask_select_bmp_maxstring)+maxCount+getString(R.string.ask_select_bmp_max1string));
				break;
			default:
				break;
			}
		}
	};
	
//	protected void onDestroy() {
//	        super.onDestroy();
//	       contentList.clear(); 
//	    	dataList.clear();  
//	};
	

	@SuppressWarnings("deprecation")
	private void initView() {
		sList = Bimp.getInstance().selectBitmap.get(pos);
		tList = Bimp.getInstance().tempSelectBitmap.get(pos);
//		sList=new ArrayList<>();
//		tList=new ArrayList<>();
//		for(ImageItem iv:Bimp.getInstance().selectBitmap.get(pos)){
//			ImageItem iv1=new ImageItem();
//			iv1.imageId=iv.imageId;
//			iv1.thumbnailPath=iv.thumbnailPath;
//			iv1.imagePath=iv.imagePath;
//			iv1.upLoadPath=iv.upLoadPath;
//			iv1.isSelected = iv.isSelected;
//			iv1.setBitmap(iv.getBitmap());
//			iv1.setType(iv.getType());
//			sList.add(iv1);
//		}
//		for(ImageItem iv:Bimp.getInstance().tempSelectBitmap.get(pos)){
//			ImageItem iv1=new ImageItem();
//			iv1.imageId=iv.imageId;
//			iv1.thumbnailPath=iv.thumbnailPath;
//			iv1.imagePath=iv.imagePath;
//			iv1.setBitmap(iv.getBitmap());
//			iv1.upLoadPath=iv.upLoadPath;
//			iv1.isSelected = iv.isSelected;
//			iv1.setType(iv.getType());
//			tList.add(iv1);
//		}
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

		if (sList.size() != 0) {
			count = sList.size();
			bt.setText(getString(R.string.work_success) + "(" + count + ")");
		}
		bt.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		tv_content.setOnClickListener(this);
		View popLayout = getLayoutInflater().inflate(R.layout.photo_pop, null);
		pop = new PopupWindow(popLayout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setAnimationStyle(R.style.PopupAnimation1);
		popListView = (ListView) popLayout.findViewById(R.id.lv_content);
		popListView.setAdapter(new ImageGridActivity.MyAdapter());
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				dataList = contentList.get(arg2).imageList;
				adapter.notifyDataSetChanged();
				pop.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			finish();
			overridePendingTransition(R.anim.activity_translate_in,
					R.anim.activity_translate_out);
			break;
		case R.id.bt:

			sList.addAll(tList);
			tList.clear();
//			Bimp.getInstance().selectBitmap.get(pos).clear();
//			Bimp.getInstance().selectBitmap.get(pos).addAll(sList);
//			JLog.e(Bimp.getInstance().selectBitmap.get(pos).size()+"----------------"+Bimp.getInstance().tempSelectBitmap.get(pos).size());
//			JLog.e(Bimp.getInstance().selectBitmap.get(pos).size()+"----------------"+Bimp.getInstance().tempSelectBitmap.get(pos).size());
			RxBusUtil.getDefault().post("addImageItem");
			finish();
			overridePendingTransition(R.anim.activity_translate_in,
					R.anim.activity_translate_out);
			break;
		case R.id.tv_content:
			pop.showAsDropDown(title);
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return contentList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.photo_pop_item,
						null);
			}

			TextView tv_content = (TextView) view.findViewById(R.id.tv_content);

			tv_content.setText(contentList.get(position).bucketName + "("
					+ contentList.get(position).count + ")");

			return view;
		}

	}

	class ImageGridAdapter extends BaseAdapter {

		BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap,
								  Object... params) {
				if (imageView != null && bitmap != null) {
					String url = (String) params[0];
					if (url != null && url.equals((String) imageView.getTag())) {
						((ImageView) imageView).setImageBitmap(bitmap);
					} else {
						// "callback, bmp not match"
					}
				} else {
					// "callback, bmp null"
				}
			}
		};

		@Override
		public int getCount() {
			int count = 0;
			if (dataList != null) {
				count = dataList.size();
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class Holder {
			private ImageView iv;
			private ImageView selected;
			private TextView text;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {

			final Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = getLayoutInflater().inflate(
						R.layout.photo_image_grid, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.selected = (ImageView) convertView
						.findViewById(R.id.isselected);
				holder.text = (TextView) convertView
						.findViewById(R.id.item_image_grid_text);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			
			
			final ImageItem item = dataList.get(position);
			item.isSelected = false;
			if (sList.size() != 0) {
				for (int i = 0; i < sList.size(); i++) {
					if (item.imageId.equals(sList.get(i).imageId)) {
						item.isSelected = true;
					} 
				}
			}
			
			if (tList.size() != 0) {
				for (int i = 0; i < tList.size(); i++) {
					if (item.imageId.equals(tList.get(i).imageId)) {
						item.isSelected = true;
					} 
				}
			}
			
			holder.iv.setTag(item.imagePath);
			cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
					callback);
			if (item.isSelected) {
				holder.selected.setVisibility(View.VISIBLE);
				holder.selected
						.setImageResource(R.mipmap.about_me_update_sex_press);
				holder.text.setBackgroundResource(R.drawable.photo_relatly_line);
			} else {
				holder.selected.setVisibility(View.INVISIBLE);
				holder.text.setBackgroundColor(ContextCompat.getColor(ImageGridActivity.this,android.R.color.transparent));
			}

			holder.iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (item.isSelected) {
						item.isSelected = !item.isSelected;
						holder.selected.setVisibility(View.INVISIBLE);
						item.setSelected(false);
						if (tList.contains(item)) {
							tList.remove(item);
						}else{
							Iterator<ImageItem> it = tList.iterator();
							while(it.hasNext())
							{
								if(it.next().imageId.equals(item.imageId))
								{
									it.remove();
									break;
								}
							}
						}

						if (sList.contains(item)) {
							sList.remove(item);
						}else{
							Iterator<ImageItem> it = sList.iterator();
							while(it.hasNext())
							{
								if(it.next().imageId.equals(item.imageId))
								{
									it.remove();
									break;
								}
							}
						}
						holder.text.setBackgroundColor(ContextCompat.getColor(ImageGridActivity.this,android.R.color.transparent));
						bt.setText(getString(R.string.work_success)
								+ "("
								+ (tList.size()+sList.size()) + ")");
					} else {
						int size =  tList.size();
						if (size <= maxCount - 1) {
							if (!item.isSelected) {
								item.isSelected = !item.isSelected;
								holder.selected.setVisibility(View.VISIBLE);
								item.setSelected(true);
								tList.add(item);
								holder.selected
										.setImageResource(R.mipmap.about_me_update_sex_press);
								holder.text
										.setBackgroundResource(R.drawable.photo_relatly_line);
								bt.setText(getString(R.string.work_success)
										+ "("
										+ ( tList.size()+sList.size()) + ")");
							}
						} else {
							Message message = Message.obtain(mHandler, 0);
							message.sendToTarget();
						}
					}
				}

			});

			return convertView;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (pop.isShowing()) {
			pop.dismiss();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected int getLayout() {
		return R.layout.photo_activity_image_grid;
	}

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected void initEventAndData() {
		pos = getIntent().getIntExtra("pos", 0);
		maxCount= getIntent().getIntExtra("maxCount", 0);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}
		ImageBucket bucket = new ImageBucket();
		bucket.bucketName = getString(R.string.ask_all_bip_string);
		bucket.imageList = dataList;
		bucket.count = bucket.imageList.size();
		contentList.add(0, bucket);

		initView();
	}

	@Override
	public void showError(String msg) {

	}

	@Override
	public void noHttpError() {

	}

		}

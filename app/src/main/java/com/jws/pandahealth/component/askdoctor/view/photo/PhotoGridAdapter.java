package com.jws.pandahealth.component.askdoctor.view.photo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DisplayUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;


public class PhotoGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int pos;
	private int maxCount;
	private int viewwidth;
	private int margin=0;
	public void setmargin(int margin){
		this.margin=margin;
	}
	public PhotoGridAdapter(Context context, int pos, int maxCount) {
		inflater = LayoutInflater.from(context);
		this.pos = pos;
		this.maxCount = maxCount;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		viewwidth = (wm.getDefaultDisplay().getWidth() - DisplayUtil.dip2px(context, maxCount*14+margin)) / maxCount;
	}

	public int getPos() {
		return pos;
	}

	public int getCount() {
		return Bimp.getInstance().selectBitmap.get(pos).size() == maxCount ? maxCount
				: Bimp.getInstance().selectBitmap.get(pos).size() + 1;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_gridview, parent,
					false);
			holder = new ViewHolder();
			holder.img1 = (ImageView) convertView
					.findViewById(R.id.item_image1);
			holder.img2 = (ImageView) convertView
					.findViewById(R.id.item_image2);
			holder.img3 = (ImageView) convertView
					.findViewById(R.id.item_image3);
			holder.itemLayout =  (RelativeLayout) convertView.findViewById(R.id.item_layout);
			AbsListView.LayoutParams fParams = new AbsListView.LayoutParams(
					viewwidth, viewwidth);
	
			holder.itemLayout.setLayoutParams(fParams);
//			holder.img1.setLayoutParams(fParams);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == Bimp.getInstance().selectBitmap.get(pos).size()) {
			holder.img1.setImageResource(R.drawable.photo_bt_add);
			holder.img2.setVisibility(View.GONE);
			holder.img3.setVisibility(View.GONE);
			if (position == maxCount) {
				holder.img1.setVisibility(View.GONE);
			}
		} else {
			holder.img1.setImageBitmap(Bimp.getInstance().selectBitmap.get(pos)
					.get(position).getBitmap());
			String path = Bimp.getInstance().selectBitmap.get(pos)
					.get(position).upLoadPath;
			if (!TextUtils.isEmpty(path)) {
				if (AppConfig.UPLOAD_PIC_FAILE.equals(path)) {// 上传失败
					holder.img2.setVisibility(View.VISIBLE);
					holder.img3
							.setBackgroundResource(R.mipmap.photo_indicator_icon_fail);
					holder.img3.setVisibility(View.VISIBLE);
				} else {// 上传成功
					holder.img2.setVisibility(View.GONE);
					holder.img3
							.setBackgroundResource(R.mipmap.about_me_update_sex_press);
					holder.img3.setVisibility(View.VISIBLE);
				}
			} else {// 正在上传
				holder.img2.setVisibility(View.VISIBLE);
				holder.img3.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView img1;
		ImageView img2;
		ImageView img3;
		RelativeLayout itemLayout;
	}

}
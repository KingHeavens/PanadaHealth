package com.jws.pandahealth.component.services.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jws.pandahealth.R;


/**
 * 
 */
public class ServicesLoadingLayout extends FrameLayout {

	private int emptyView, errorView, loadingView;
	private OnClickListener onRetryClickListener;
	private OnClickListener onEmptyClickListener;

	public ServicesLoadingLayout(Context context) {
		this(context, null);
	}

	public ServicesLoadingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ServicesLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ServicesLoadingLayout, 0, 0);
		try {
			emptyView = a.getResourceId(R.styleable.ServicesLoadingLayout_emptyView,
					R.layout.services_view_loading_empty);
			errorView = a.getResourceId(R.styleable.ServicesLoadingLayout_errorView,
					R.layout.services_view_loading_error);
			loadingView = a.getResourceId(
					R.styleable.ServicesLoadingLayout_loadingView,
					R.layout.services_view_loading);

			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(emptyView, this, true);
			inflater.inflate(errorView, this, true);
			inflater.inflate(loadingView, this, true);
		} finally {
			a.recycle();
		}
	}

	public void setEmptyTvContent(String content) {
		((TextView) findViewById(R.id.tv_empty_content)).setText(content);
	}
	public void setErrorTvContent(String content) {
		((TextView) findViewById(R.id.tv_error_content)).setText(content);
	}
	@Deprecated
	public void setEmptyBtnContent(String content) {
		//((TextView) findViewById(R.id.btn_empty_retry)).setText(content);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		for (int i = 0; i < getChildCount() - 1; i++) {
			getChildAt(i).setVisibility(GONE);
		}

		findViewById(R.id.empty).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != onRetryClickListener) {
							//onEmptyClickListener.onClick(v);
							onRetryClickListener.onClick(v);
						}
					}
				});

		findViewById(R.id.error).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != onRetryClickListener) {
							onRetryClickListener.onClick(v);
						}
					}
				});
		/*findViewById(R.id.btn_empty_retry).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != onEmptyClickListener) {
							onEmptyClickListener.onClick(v);
						}
					}
				});
		findViewById(R.id.btn_error_retry).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != onEmptyClickListener) {
							onEmptyClickListener.onClick(v);
						}
					}
				});*/

	}

	public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
		this.onRetryClickListener = onRetryClickListener;
	}
	
	public void setOnEmptyClickListener(OnClickListener onEmptyClickListener){
		this.onEmptyClickListener = onEmptyClickListener;
	}

	public void showEmpty() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (i == 0) {
				child.setVisibility(VISIBLE);
			} else {
				child.setVisibility(GONE);
			}
		}
	}

	public void showError() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (i == 1) {
				child.setVisibility(VISIBLE);
			} else {
				child.setVisibility(GONE);
			}
		}
	}


	public void showErrorByText(String text) {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (i == 1) {
				child.setVisibility(VISIBLE);
				((TextView)(child.findViewById(R.id.tv_error_content))).setText(getContext().getString(R.string.error));
				((TextView)(child.findViewById(R.id.tv_error_desc))).setText(text);
			} else {
				child.setVisibility(GONE);
			}
		}
	}
	public void showLoading() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (i == 2) {
				child.setVisibility(VISIBLE);
			} else {
				child.setVisibility(GONE);
			}
		}
	}

	public void showContent() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);
			if (i == 3) {
				child.setVisibility(VISIBLE);
			} else {
				child.setVisibility(GONE);
			}
		}
	}


	public void showNoDoctor(){
		ImageView ivEmpty = (ImageView) findViewById(R.id.empty_iv);
		ivEmpty.setImageResource(R.mipmap.app_loading_result_no_doctor);
		TextView  tvContent = (TextView) findViewById(R.id.tv_empty_content);
		tvContent.setText(R.string.layout_no_doctor_content);
		TextView tvDesc = (TextView) findViewById(R.id.tv_empty_desc);
		tvDesc.setText(R.string.layout_no_doctor_content_desc);
		showEmpty();
	}

	public void showNoService(){
		showEmpty();
	}

	public void showNoNetwork(){
		showError();
	}
}

package com.jws.pandahealth.component.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;


/**
 * 
 */
public class LoadingLayout extends FrameLayout {

	private int emptyView, errorView, loadingView;
	private OnClickListener onErrorClickListener;
	private OnClickListener onEmptyClickListener;

	public LoadingLayout(Context context) {
		this(context, null);
	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ServicesLoadingLayout, 0, 0);
		try {
			emptyView = a.getResourceId(R.styleable.ServicesLoadingLayout_emptyView,
					R.layout.view_loading_empty);
			errorView = a.getResourceId(R.styleable.ServicesLoadingLayout_errorView,
					R.layout.view_loading_error);
			loadingView = a.getResourceId(
					R.styleable.ServicesLoadingLayout_loadingView,
					R.layout.view_loading);
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater.inflate(emptyView, this, true);
			inflater.inflate(errorView, this, true);
			inflater.inflate(loadingView, this, true);
		} finally {
			a.recycle();
		}
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
						if (null != onEmptyClickListener) {
							onEmptyClickListener.onClick(v);
						}
					}
				});

		findViewById(R.id.error).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (null != onErrorClickListener) {
							onErrorClickListener.onClick(v);
						}
					}
				});

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
	

	public void setEmptyTvContent(String content) {
		((TextView) findViewById(R.id.tv_empty_content)).setText(content);
	}

//	public void setEmptyBtnContent(String content) {
//		((Button) findViewById(R.id.btn_empty_retry)).setText(content);
//	}

	public void setOnEmptyClickListener(OnClickListener onEmptyClickListener){
		this.onEmptyClickListener = onEmptyClickListener;
	}
	public void setOnErrorClickListener(OnClickListener onEmptyClickListener){
		this.onErrorClickListener = onEmptyClickListener;
	}
	public void setErrorTvContent(String text){
		((TextView)findViewById(R.id.tv_error_content)).setText(text);
	}
	
	 
	public void setEmptyBtnContentVisibility(int visibility){
		//((Button) findViewById(R.id.btn_empty_retry)).setVisibility(visibility);
	}
	
	
	public void setOnRetryClickListener(OnClickListener onErrorClickListener) {
		this.onErrorClickListener = onErrorClickListener;
		this.onEmptyClickListener = onErrorClickListener;
	}


	public void setEmptyImageVisiablity(int visiablity) {
		findViewById(R.id.empty_iv).setVisibility(visiablity);
	}
}

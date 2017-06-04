package com.jws.pandahealth.component.app.ui.adapter;


import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter
{
	
	private List<View> viewList;
	
	public ViewPageAdapter(List<View> viewList)
	{
		super();
		this.viewList = viewList;
		this.notifyDataSetChanged();
	}
	
	@Override
	public void destroyItem(View collection, int position, Object arg2)
	{
		((ViewPager) collection).removeView(viewList.get(position));
	}
	
	@Override
	public void finishUpdate(View arg0)
	{
		
	}
	
	@Override
	public int getCount()
	{
		return viewList.size();
	}
	
	@Override
	public Object instantiateItem(View arg0, int arg1)
	{
		((ViewPager) arg0).addView(viewList.get(arg1) , 0);
		return viewList.get(arg1);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == (object);
	}
	
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
	}
	
	@Override
	public Parcelable saveState()
	{
		return null;
	}
	
	@Override
	public void startUpdate(View arg0)
	{
		
	}
}

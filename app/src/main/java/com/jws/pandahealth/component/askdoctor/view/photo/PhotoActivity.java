package com.jws.pandahealth.component.askdoctor.view.photo;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.presenter.PhotoPresenter;
import com.jws.pandahealth.component.askdoctor.presenter.contract.PhotoContract;
import com.jws.pandahealth.component.askdoctor.view.photo.util.Bimp;
import com.jws.pandahealth.component.app.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class PhotoActivity extends BaseActivity<PhotoPresenter> implements PhotoContract.View {

    private ArrayList<View> listViews = null;
    @BindView(R.id.layout2_viewPager)
     ViewPager pager;
    private MyPageAdapter adapter;
    private int count;
    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();
    private RelativeLayout photo_relativeLayout;
    private int pos;
    @BindView(R.id.photo_bt_exit)
    Button photo_bt_exit;
    @BindView(R.id.photo_bt_del)
    Button photo_bt_del;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.photo_item;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }

    @Override
    protected void initEventAndData() {
        pos = getIntent().getIntExtra("pos", 0);
        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setBackgroundColor(0x70000000);

        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        photo_bt_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listViews.size() == 1) {
                    Bimp.getInstance().selectBitmap.get(pos).clear();
                    Bimp.getInstance().max.set(pos, 0);
                    finish();
                } else {
                    Bimp.getInstance().selectBitmap.get(pos).remove(count);
                    Bimp.getInstance().max.set(pos, Bimp.getInstance().max.get(pos) - 1);
                    pager.removeAllViews();
                    listViews.remove(count);
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        pager.addOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < Bimp.getInstance().selectBitmap.get(pos).size(); i++) {
            initListViews(Bimp.getInstance().selectBitmap.get(pos).get(i).getBitmap());
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);

        pager.setCurrentItem(getIntent().getIntExtra("ID", 0));
    }


    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            count = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    class MyPageAdapter extends PagerAdapter {
        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}

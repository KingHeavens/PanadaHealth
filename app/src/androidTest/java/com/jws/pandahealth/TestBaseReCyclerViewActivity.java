package com.jws.pandahealth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
//import butterknife.InjectView;

/**
 * Created by Administrator on 2016/12/8.
 *
 */

//    https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/%E9%A6%96%E9%A1%B5
public class TestBaseReCyclerViewActivity extends Activity {


    /*<?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:background="#E91E63"
    android:layout_height="match_parent">

    <com.ajguan.library.EasyRefreshLayout
    android:id="@+id/easylayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
    android:id="@+id/recyclerview"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    </android.support.v7.widget.RecyclerView>
    </com.ajguan.library.EasyRefreshLayout>

    </LinearLayout>
*/
/*

    @InjectView(R.id.recyclerview)
    android.support.v7.widget.RecyclerView recyclerview;

    @InjectView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    List<String> nums = new ArrayList<>();
    MyQuickAdapter mAdapter;

    boolean netWrokOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);
        easylayout.setEnableLoadMore(false);
        easylayout.autoRefresh(200);
//        swipe.setRefreshing(true);
        for (int i = 0; i < 10; i++) {
            nums.add(i + "");
        }
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyQuickAdapter(nums);
        mAdapter.setEnableLoadMore(true);   //打开加载
        mAdapter.setAutoLoadMoreSize(2);  //展示到倒数第几个时候开始预加载 默认1
        mAdapter.setLoadMoreView(new CustomLoadMoreView());   //设置自定义的loadingmoreview
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);  //添加列表加载动画
//        默认动画每个item只执行一次加载动画,如果想重复执行动画可以调用一下方法
        mAdapter.isFirstOnly(true);
//        自定义加载动画
//        mAdapter.openLoadAnimation(new BaseAnimation() {
//            @Override
//            public Animator[] getAnimators(View view) {
//                return new Animator[]{
//                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
//                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
//                };
//            }
//        });

//        设置空布局
//        mAdapter.setEmptyView(getView());


//        添加头部、尾部
        mAdapter.addHeaderView(getView());
//        mAdapter.addFooterView(getView());
//        删除指定头部、尾部
//
//        mAdapter.removeHeaderView(getView);
//        mAdapter.removeFooterView(getView);

//        删除所有头部、尾部
//        mAdapter.removeAllHeaderView();
//        mAdapter.removeAllFooterView();

//        如果是gridview，就要用gridviewlayoutmanager，这个方法是gridview的每一行或者列合并，
//        如：return  1 2 3  ,那么grid第一行是1个item，第二行是2个item，第三行是三个item
//        multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
//        @Override
//        public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
//            return data.get(position).getSpanSize();
//        }
//    });


        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(TestBaseReCyclerViewActivity.this, i + " view被点击了", Toast.LENGTH_LONG).show();
            }
        });
        recyclerview.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(TestBaseReCyclerViewActivity.this, i + " view被长按了", Toast.LENGTH_LONG).show();
            }
        });
// 需要在adapter里面设置一下点击的viwe
        recyclerview.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(TestBaseReCyclerViewActivity.this, i + "的子view" + view.getId() + "被点击了", Toast.LENGTH_LONG).show();
            }
        });
// 需要在adapter里面设置一下长按的viwe
        recyclerview.addOnItemTouchListener(new OnItemChildLongClickListener() {
            @Override
            public void onSimpleItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(TestBaseReCyclerViewActivity.this, i + "的子view" + view.getId() + "被长按了", Toast.LENGTH_LONG).show();
            }
        });

//        注意：在onLoadMoreRequested中添加数据要在mRecyclerView.post的Runnable中执行，
// 否则会出现Cannot call this method while RecyclerView is computing a layout or scrolling 异常。
//        因为添加adapter添加数据是ui操作，所以要放在view.post里面
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nums.size() >= 100 || nums.size() % 10 != 0) {
                            mAdapter.loadMoreEnd();
                        } else {
                            if (netWrokOk) {
                                int a = nums.size();
                                List<String> nums1 = new ArrayList<>();
                                for (int i = a + 1; i < a + 11; i++) {
                                    nums1.add(i + "");
                                }
                                nums.addAll(nums1);
                                mAdapter.addData(nums1);  //向adapter添加数据条目
                                mAdapter.loadMoreComplete();
                            } else {
                                Toast.makeText(TestBaseReCyclerViewActivity.this, "链接错误", Toast.LENGTH_LONG).show();
                                mAdapter.loadMoreFail();
                            }
                        }
                    }
                }, 2000);

            }
        });


        easylayout.addEasyEvent(
                new EasyRefreshLayout.EasyEvent() {
                    @Override
                    public void onLoadMore() {
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                final List<String> list = new ArrayList<>();
//                                for (int j = 0; j < 5; j++) {
//                                    list.add("this is  new load data >>>>" + new Date().toLocaleString());
//                                }
//
//                                //adapter.addData(list);
//
//                                easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
//                                    @Override
//                                    public void complete() {
//                                        adapter.getData().addAll(list);
//                                        adapter.notifyDataSetChanged();
//
//                                    }
//                                }, 500);
//
//                            }
//                        }, 2000);


                    }

                    @Override
                    public void onRefreshing() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                    停止刷新
//                                            swipe.setRefreshing(false);
                                            easylayout.refreshComplete();
                                            nums.clear();
                                            for (int i = 0; i < 10; i++) {
                                                nums.add(i + "");
                                            }
//                                            nums.add("加载中.....");
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerview.setAdapter(mAdapter);
                            easylayout.refreshComplete();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
////                                    停止刷新
//                                    swipe.setRefreshing(false);
//                                    nums.clear();
//                                    for (int i = 0; i < 10; i++) {
//                                        nums.add(i + "");
//                                    }
//                                    nums.add("加载中.....");
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });


    }


    class MyQuickAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MyQuickAdapter(List<String> s) {
//            单个样式
            super(R.layout.item, s);
//            多个样式   list的数据必须继承 MultiItemEntity     如    class userinfo extends MultiItemEntity {}
//            super(s);
//            add(0,R.layout.item1);
//            add(1,R.layout.item2);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String strings) {
//            单个样式
            baseViewHolder.setText(R.id.tv, strings).addOnClickListener(R.id.tv).addOnLongClickListener(R.id.tv);
//            多个样式
//            int type=baseViewHolder.getItemViewType();
//            if(type==0){
//                baseViewHolder.setText(R.id.tv, strings).addOnClickListener(R.id.tv).addOnLongClickListener(R.id.tv);
//            }else if(type==1){
//                baseViewHolder.setText(R.id.tv, strings).addOnClickListener(R.id.tv).addOnLongClickListener(R.id.tv);
//            }
        }
    }

    private View getItemView(int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_guide_item, null);
        view.setBackgroundResource(id);
        return view;
    }

    public View getView() {
        View v = LayoutInflater.from(this).inflate(R.layout.activity_guide, null);
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
        ArrayList views = new ArrayList<>();
        views.add(getItemView(R.drawable.sample_footer_loading));
        views.add(getItemView(R.drawable.sample_footer_loading));
        views.add(getItemView(R.drawable.sample_footer_loading));
        views.add(getItemView(R.drawable.sample_footer_loading));
        ViewPageAdapter mPagerAdapter = new ViewPageAdapter(views);
        mViewPager.setAdapter(mPagerAdapter);
        return v;
    }
*/

}

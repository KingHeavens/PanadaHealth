package com.jws.pandahealth.component.askdoctor.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.askdoctor.ui.adapter.TextAdapter;


public class ExpandItemView extends RelativeLayout implements ViewBaseAction {

    private ListView mListView;
    private String[] items;//显示字段
    private OnSelectListener mOnSelectListener;
    private TextAdapter adapter;
    private int selectItemIndex = -1;


    public ExpandItemView(Context context, String[] value) {
        super(context);
        this.items = value;
        init(context);
    }

    public ExpandItemView(Context context, String[] value, int selectItemIndex) {
        super(context);
        this.items = value;
        this.selectItemIndex = selectItemIndex;
        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        setBackgroundResource(android.R.color.white);
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new TextAdapter(context, items);
        adapter.setTextSize(17);
        if (selectItemIndex != -1) {
            adapter.setSelectedPositionNoNotify(selectItemIndex);
        }
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(View view, int position) {
                selectItemIndex=position;
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(position);
                }
            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(int position);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    public void setSelectItemIndex(int selectItemIndex){
        this.selectItemIndex=selectItemIndex;
        adapter.setSelectedPositionNoNotify(selectItemIndex);
    }

}

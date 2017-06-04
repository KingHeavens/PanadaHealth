package com.jws.pandahealth.component.askdoctor.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.ArrayList;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 *
 * @author yueyueniao
 */

public class ExpandTabView extends LinearLayout implements OnDismissListener {

    private LinearLayout selectedButton;
    private View line;
    private ArrayList<String> mTextArray = new ArrayList<String>();
    private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
    private ArrayList<LinearLayout> mToggleButton = new ArrayList<LinearLayout>();
    private Context mContext;
    private final int SMALL = 0;
    private int displayWidth;
    private int displayHeight;
    private PopupWindow popupWindow;
    private int selectPosition;
    public static boolean isLeftTopBack = false;
    private View fugai;

    public void setFugai(View fugai) {
        this.fugai = fugai;
    }

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String valueText, int position) {
        if (position < mToggleButton.size()) {
            ((TextView) ((LinearLayout) mToggleButton.get(position).getChildAt(0)).getChildAt(0)).setText(valueText);
        }
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < mToggleButton.size() && ((TextView) ((LinearLayout) mToggleButton.get(position).getChildAt(0)).getChildAt(0)).getText() != null) {
            return ((TextView) ((LinearLayout) mToggleButton.get(position).getChildAt(0)).getChildAt(0)).getText().toString();
        }
        return "";
    }

    /**
     * 设置tabitem的个数和初始值
     */
    public void setValue(ArrayList<String> textArray, ArrayList<View> viewArray) {
        if (mContext == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTextArray = textArray;
        int maxHeight = (int) (displayHeight * 0.6);
        for (int i = 0; i < viewArray.size(); i++) {
            final RelativeLayout r = new RelativeLayout(mContext);
//			@SuppressWarnings("deprecation")
            /**
             * 设置如果是搜地址，就屏幕的60%，如果男女和医院，就是自增高
             */
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, i == 0 ? maxHeight : RelativeLayout.LayoutParams.WRAP_CONTENT);
            rl.leftMargin = dip2px(mContext, 13);
            rl.rightMargin = dip2px(mContext, 13);
            r.addView(viewArray.get(i), rl);
            mViewArray.add(r);
            r.setTag(SMALL);
            LinearLayout tButton = (LinearLayout) inflater.inflate(R.layout.toggle_button, this, false);
            addView(tButton);
            mToggleButton.add(tButton);
            tButton.setTag(i);
            ((TextView) ((LinearLayout) tButton.getChildAt(0)).getChildAt(0)).setText(mTextArray.get(i));

            r.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    onPressBack();
                }
            });
            r.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            tButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // initPopupWindow();
                    LinearLayout tButton = (LinearLayout) view;
                    if (selectedButton == tButton) {
                        if (popupWindow.isShowing()) {
                            isLeftTopBack = false;
                            onPressBack();
                            return;
                        }
                    }

                    if (selectedButton != null) {
                        selectedButton.setSelected(false);
                        selectedButton.findViewById(R.id.line).setVisibility(GONE);
                    }
                    selectedButton = tButton;
                    selectPosition = (Integer) selectedButton.getTag();
                    selectedButton.setSelected(true);
                    selectedButton.findViewById(R.id.line).setVisibility(VISIBLE);
                    startAnimation();
                    if (mOnButtonClickListener != null && tButton.isSelected()) {
                        mOnButtonClickListener.onClick(selectPosition);
                    }
                }
            });
        }
    }

    private void startAnimation() {

        if (popupWindow == null) {
            popupWindow = new PopupWindow(mViewArray.get(selectPosition), displayWidth, LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(this);
            popupWindow.setOutsideTouchable(true);
        }

//        if (selectedButton.isSelected()) {
//            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
                backgroundAlpha(true);
//            }
//            else {
//                popupWindow.dismiss();
//                hideView();
//                backgroundAlpha(false);
//                selectedButton.setSelected(true);
//                backgroundAlpha(true);
//            }
//        } else {
//            if (popupWindow.isShowing()) {
//                popupWindow.dismiss();
//                backgroundAlpha(false);
//                hideView();
//            }
//        }
    }

    private void showPopup(int position) {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mViewArray.get(position)) {
            popupWindow.setContentView(mViewArray.get(position));
        }

        popupWindow.showAsDropDown(this, 0, 2);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        backgroundAlpha(false);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedButton != null) {
//				selectedButton.findViewById(R.id.line).setVisibility(GONE);
                selectedButton.setSelected(false);
            }
            return isLeftTopBack ? false : true;
        } else {
            return false;
        }

    }

    private void hideView() {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
            backgroundAlpha(false);
        }
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        mContext = context;
        displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void onDismiss() {
//        showPopup(selectPosition);
        selectedButton.setSelected(false);
        backgroundAlpha(false);
    }

    private OnButtonClickListener mOnButtonClickListener;

    /**
     * 设置tabitem的点击监听事件
     */
    public void setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    /**
     * 自定义tabitem点击回调接口
     */
    public interface OnButtonClickListener {
        public void onClick(int selectPosition);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(boolean show) {
        if (show) {
            fugai.setVisibility(VISIBLE);
        } else {
            fugai.setVisibility(GONE);
        }
    }


    public void setTextColorSelected(int posi) {
        ((TextView) mToggleButton.get(posi).findViewById(R.id.text)).setTextColor(Color.parseColor("#D02234"));
        ((ImageView) mToggleButton.get(posi).findViewById(R.id.open)).setImageResource(R.mipmap.green_down_action);
    }
}

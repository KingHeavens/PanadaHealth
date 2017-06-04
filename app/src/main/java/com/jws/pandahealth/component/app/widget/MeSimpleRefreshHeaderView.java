package com.jws.pandahealth.component.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ajguan.library.IRefreshHeader;
import com.ajguan.library.State;
import com.jws.pandahealth.R;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MeSimpleRefreshHeaderView extends FrameLayout implements IRefreshHeader {
    private Animation rotate_up;
    private Animation rotate_down;
    private Animation rotate_infinite;
    private TextView textView;
    private View arrowIcon;
    private View successIcon;
    private View loadingIcon;

    public MeSimpleRefreshHeaderView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MeSimpleRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.rotate_up = AnimationUtils.loadAnimation(context, com.ajguan.library.R.anim.rotate_up);
        this.rotate_down = AnimationUtils.loadAnimation(context, com.ajguan.library.R.anim.rotate_down);
        this.rotate_infinite = AnimationUtils.loadAnimation(context, com.ajguan.library.R.anim.rotate_infinite);
        inflate(context, com.ajguan.library.R.layout.default_refresh_header, this);
        this.textView = (TextView)this.findViewById(com.ajguan.library.R.id.text);
        this.arrowIcon = this.findViewById(com.ajguan.library.R.id.arrowIcon);
        this.successIcon = this.findViewById(com.ajguan.library.R.id.successIcon);
        this.loadingIcon = this.findViewById(com.ajguan.library.R.id.loadingIcon);
    }

    public void reset() {
        this.textView.setText(this.getResources().getText(R.string.easyrefreshlayout_header_reset));
        this.successIcon.setVisibility(INVISIBLE);
        this.arrowIcon.setVisibility(VISIBLE);
        this.arrowIcon.clearAnimation();
        this.loadingIcon.setVisibility(INVISIBLE);
        this.loadingIcon.clearAnimation();
    }

    public void pull() {
    }

    public void refreshing() {
        this.arrowIcon.setVisibility(INVISIBLE);
        this.loadingIcon.setVisibility(VISIBLE);
        this.textView.setText(this.getResources().getText(R.string.easyrefreshlayout_header_refreshing));
        this.arrowIcon.clearAnimation();
        this.loadingIcon.startAnimation(this.rotate_infinite);
    }

    public void onPositionChange(float currentPos, float lastPos, float refreshPos, boolean isTouch, State state) {
        if(currentPos < refreshPos && lastPos >= refreshPos) {
            Log.i("", ">>>>up");
            if(isTouch && state == State.PULL) {
                this.textView.setText(this.getResources().getText(R.string.easyrefreshlayout_header_pull));
                this.arrowIcon.clearAnimation();
                this.arrowIcon.startAnimation(this.rotate_down);
            }
        } else if(currentPos > refreshPos && lastPos <= refreshPos) {
            Log.i("", ">>>>down");
            if(isTouch && state == State.PULL) {
                this.textView.setText(this.getResources().getText(R.string.easyrefreshlayout_header_pull_over));
                this.arrowIcon.clearAnimation();
                this.arrowIcon.startAnimation(this.rotate_up);
            }
        }

    }

    public void complete() {
        this.loadingIcon.setVisibility(INVISIBLE);
        this.loadingIcon.clearAnimation();
        this.successIcon.setVisibility(VISIBLE);
        this.textView.setText(this.getResources().getText(R.string.easyrefreshlayout_header_completed));
    }
}
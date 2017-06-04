package com.jws.pandahealth.component.more.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jws.pandahealth.R;
import com.jws.pandahealth.component.app.util.GlideRoundTransform;


public class MoreUtil {

    public static void loadUserImage(Activity activity, ImageView imageView,
                                     String url) {
        if(!activity.isFinishing()){
            Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.more_default_user).transform(new GlideRoundTransform(activity)).into(imageView);
        }
    }

    public static String splitDateString(String date) {
        // 1942年
        return date.split(" ")[0];
    }


}

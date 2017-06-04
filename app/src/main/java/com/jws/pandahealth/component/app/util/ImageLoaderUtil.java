package com.jws.pandahealth.component.app.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.DisplayUtil;

/**
 * Glide 不仅是一个图片缓存，它支持 Gif、WebP、缩略图。甚至是 Video
 * 通过设置绑定生命周期，我们可以更加高效的使用Glide提供的方式进行绑定，这样可以更好的让加载图片的请求的生命周期动态管理起来
 * 高效的缓存策略
 *  A. 支持Memory和Disk图片缓存
 *  B. 其他加载器加载的是原图，Glide 缓存的是多种规格，也就意味着 Glide 会根据你 ImageView 的大小来缓存相应大小的图片尺寸
 *  C. 内存开销小
 默认的 Bitmap 格式是 RGB_565 格式，而 Picasso 默认的是 ARGB_8888 格式，这个内存开销要小一半。


 注意：
 1. 传入的context类型影响到Glide加载图片的优化程度
 2. Glide可以监视Activity的生命周期，在Activity销毁的时候自动取消等待中的请求。但是如果你使用Application context，你就失去了这种优化效果。
 */
public class ImageLoaderUtil {

    public static void load(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            if(!activity.isDestroyed())
                Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }else{
            if(!activity.isFinishing())
                Glide.with(activity).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }

    public static void loadAll(Context context, String url, ImageView iv) {    //不缓存，全部从网络加载
        Glide.with(context).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
    }
    public static void loadUserImage(Activity activity, ImageView imageView,
                                     String url) {
        if(!activity.isFinishing()){
            Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.more_default_user).transform(new GlideRoundTransform(activity)).into(imageView);
        }
    }
    public static void loadUserImage(Context activity, ImageView imageView,
                                     String url) {
            Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar).transform(new GlideRoundTransform(activity)).into(imageView);
    }
    public static void loadContinueImage(Activity activity, ImageView imageView,
                                     String url) {
        if(!activity.isFinishing()){
            Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.app_default_user).transform(new GlideRoundTransform(activity)).into(imageView);
        }
    }
    public static void loadImage(Context activity, ImageView imageView,
                                     String url) {
        Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(activity)).into(imageView);
    }
    public static void loadAll(Activity activity, String url, ImageView iv) {    //不缓存，全部从网络加载
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            if(!activity.isDestroyed())
                Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        }else{
            if(!activity.isFinishing())
                Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        }
    }




//------------------------整理glide------------------------
    /**
     *
     * @param context  最好放activity，fragment，不要放appliction
     * @param url
     * @param priority  优先级  IMMEDIATE, HIGH, NORMAL, LOW, priority,
     * @param thumbnail   加载缩略图  默认无 （1 直接加载原图）   0.1--1
     * @param anim   加载动画
     * @param iv
     * @param loadingImg  加载中图片
     * @param errorImg   加载失败图片
     *
     */
    public static void loadImg(Context context, String url,Priority priority, float thumbnail,int anim, ImageView iv,int loadingImg,int errorImg) {
        Glide.with(context).load(url).crossFade()
                .skipMemoryCache(true)
                .placeholder(loadingImg)
                .error(errorImg)
                .priority(priority)
                .animate(anim)
                .thumbnail(thumbnail)
                .into(iv);
    }


    public static void loadChatImage(Context mContext, ImageView imageView, String url) {
        Glide.with(mContext).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(mContext))
                .override(DisplayUtil.dip2px(mContext,160),DisplayUtil.dip2px(mContext,160))
                .into(imageView);
    }
}

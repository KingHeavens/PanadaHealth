package com.jws.pandahealth.component.app.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.JsonUtil;
import com.jws.pandahealth.component.AppConfig;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.app.presenter.WebviewPresenter;
import com.jws.pandahealth.component.app.presenter.contract.WebviewContract;
import com.jws.pandahealth.component.app.widget.ProgressWebView;
import com.jws.pandahealth.component.services.util.JLog;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.util.WeakHashMap;

/**
 * 公共网页加载activity
 * <p/>
 * url 加载网址 title 界面显示标题名称（可不传，默认截取网页title 如果截取为空显示医小护） isShare 是否支持分享
 * （可不传，默认支持）
 */
public class WebViewActivity extends BaseActivity<WebviewPresenter> implements WebviewContract.View {

    private static final String TAG = "WebViewActivity";
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private String title;// 界面标题
    private boolean isShare;// 是否支持分享 默认支持
    private String url;// 加载网址

    private ProgressWebView webView;
    private View mErrorView;
    private boolean isNotifyIn;

    private boolean isPost=false;
    private boolean isaddjava=false;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_webview_content;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void noHttpError() {

    }


    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {

        Intent it = getIntent();
        title = it.getStringExtra("title");
        url = it.getStringExtra("url");
        isPost = it.getBooleanExtra("isPost",false);
        isaddjava = it.getBooleanExtra("isaddjava",true);

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (TextUtils.isEmpty(title))
            title = getString(R.string.app_name);
        setTitleName(title);

        webView = (ProgressWebView) findViewById(R.id.web);
        initWebView(webView);
        webView.setTitleView(headTitleTv, title);
        mErrorView = findViewById(R.id.fail_layout);
        webView.setErrorView(mErrorView);
        // webView.setTitleView(mTextView);
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        if(!isPost) {
            if(isaddjava) {
                webView.addJavascriptInterface(new JsObject(), "jo");
            }
        }
        wSet.setRenderPriority(WebSettings.RenderPriority.HIGH);
        wSet.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        // 开启 DOM storage API 功能
        wSet.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        wSet.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME;
        // String cacheDirPath =
        // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        // 设置数据库缓存路径
        wSet.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        wSet.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        wSet.setAppCacheEnabled(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        /*** 支持 flash ***/
        String temp = "<html><body bgcolor=\"" + "black"
                + "\"> <br/><embed src=\"" + url + "\" width=\"" + "100%"
                + "\" height=\"" + "90%" + "\" scale=\"" + "noscale"
                + "\" type=\"" + "application/x-shockwave-flash"
                + "\"> </embed></body></html>";
        String mimeType = "text/html";
        String encoding = "utf-8";
        webView.loadDataWithBaseURL("null", temp, mimeType, encoding, "");
        if(isPost){
            WeakHashMap map= MyApplication.getHttpDataMap();
            String str = "device_type="+map.get("device_type")+"&" + "device_code="+map.get("device_code")+"&" + "secret="+map.get("secret")+"&" + "ver="+map.get("ver");
             byte[] b=str.getBytes();
                webView.postUrl(AppConfig.getServerPath()+ url,b );
            for (byte b1:b) {
                JLog.e(b1+"");
            }
        }else {
            webView.loadUrl(url);
        }

    }
    /**
     * 初始化WebView配置
     * @param webView webview
     */
    private void initWebView(WebView webView) {
        if(Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/

        webView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }
    class JsObject {
        @JavascriptInterface
        public String toString() {
            return "injectedObject";

        }
    }


    @Override
    public void onBackPressed() {
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        // 清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录

        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        Log.i(TAG, "delete file path=" + file.getAbsolutePath());
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }
}

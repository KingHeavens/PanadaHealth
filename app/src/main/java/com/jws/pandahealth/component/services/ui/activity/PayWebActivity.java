package com.jws.pandahealth.component.services.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.jws.pandahealth.R;
import com.jws.pandahealth.component.MyApplication;
import com.jws.pandahealth.component.app.base.BaseActivity;
import com.jws.pandahealth.component.services.ServicesConstant;
import com.jws.pandahealth.component.services.model.ServicesRetrofitHelper;
import com.jws.pandahealth.component.services.model.bean.FriendInfo;
import com.jws.pandahealth.component.services.presenter.PayWebPresenter;
import com.jws.pandahealth.component.services.presenter.contract.PayWebContract;
import com.jws.pandahealth.component.services.util.FriendInfoJsonUtil;
import com.jws.pandahealth.component.services.util.JLog;
import com.jws.pandahealth.component.services.widget.ProgressWebView;

import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import butterknife.BindView;

public class PayWebActivity extends BaseActivity<PayWebPresenter> implements PayWebContract.View {
    private static final String TAG = "PayWebActivity";
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    @Inject
    public ServicesRetrofitHelper mRetrofitHelper;
    @BindView(R.id.web_view)
    ProgressWebView webView;
    @BindView(R.id.pay_activity_web)
    LinearLayout payActivityWeb;


    @Override
    protected int getLayout() {
        return R.layout.pay_activity_web;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void initEventAndData() {
        mPresenter.init();
        setTitleName(getString(R.string.pay_web_page_title));

        String url = getIntent().getStringExtra(ServicesConstant.PAY_URL);

        /*// webView.setTitleView(mTextView);
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
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
        *//*webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });*//*
        *//*** 支持 flash ***//*
        String temp = "<html><body bgcolor=\"" + "black"
                + "\"> <br/><embed src=\"" + url + "\" width=\"" + "100%"
                + "\" height=\"" + "90%" + "\" scale=\"" + "noscale"
                + "\" type=\"" + "application/x-shockwave-flash"
                + "\"> </embed></body></html>";
        String mimeType = "text/html";
        String encoding = "utf-8";*/

       /* String id = getIntent().getStringExtra(ServicesConstant.SERVICES_DOCTOR_ID);
        String type = getIntent().getStringExtra(ServicesConstant.SERVICES_TYPE);
        WeakHashMap<String, String> httpDataMap = MyApplication.getHttpDataMap();
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : httpDataMap.entrySet()){
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        sb.append("doctorId=" + id);
        sb.append("&type=" + type);
        JLog.e(TAG + "postUrl: " + sb.toString());

        webView.postUrl(ServicesConstant.PAY_WEB_URL,sb.toString().getBytes());*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(this,"android");
        View inflate = View.inflate(mContext, R.layout.services_view_loading_error, null);
        webView.setErrorView(inflate);
        webView.setTitleView(headTitleTv,getString(R.string.pay_web_page_title));
        webView.setOnPageLoadListener(new ProgressWebView.OnPageLoadListener() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                JLog.e("errorCode:" + errorCode + "description:" + description + "failingUrl:" + failingUrl);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(failingUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void showError(String msg) {
        JLog.e(msg);
        showToast(msg);
    }

    @Override
    public void noHttpError() {
        JLog.e(getString(R.string.http_no_net_tip));
    }

    @Override
    public void closeCurrentPage(int payStatus,String hxId,String orderId) {
        JLog.e("closeCurrentPage" + "payStatus:" + payStatus + "hxId:" + hxId + "orderId:" +  orderId);
        Intent data = new Intent();
        if(ServicesConstant.PAY_SUCCESS == payStatus){
            data.putExtra(ServicesConstant.PAY_DOCTOR_HXID,hxId);
        }
        data.putExtra(ServicesConstant.PAY_ORDER_ID,orderId);
        setResult(payStatus, data);
        finish();
    }

    /**
     * 支付成功
     * @param orderId  订单号
     * @param friendInfo  好友信息（json）
     */
    @JavascriptInterface
    public void onPaySuccess(String orderId,String friendInfo){
        JLog.e("onPaySuccess: orderId:" + orderId + "friendInfo:" + friendInfo);
        FriendInfo info = FriendInfoJsonUtil.parseFriendInfo(friendInfo);
        mPresenter.updateFriendInfo(info);
        closeCurrentPage(ServicesConstant.PAY_SUCCESS,info.hxId,orderId);

    }

    /**
     * 支付失败
     */
    @JavascriptInterface
    public void onPayFail(String orderId){
        JLog.e("onPayFail");
        closeCurrentPage(ServicesConstant.PAY_FAILED,"",orderId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

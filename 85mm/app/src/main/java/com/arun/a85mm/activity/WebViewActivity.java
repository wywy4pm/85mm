package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.utils.StatusBarUtils;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private String url;
    private ImageView image_back;
    private TextView titleText;
    private boolean isFromSplash;
    private String title;

    public static void jumpToWebViewActivity(Context context, String webUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constant.INTENT_WEB_URL, webUrl);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToWebViewActivity(Context context, String webUrl, String... args) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constant.INTENT_WEB_URL, webUrl);
        if (args != null && args.length > 0) {
            if (args.length == 1) {
                if (Constant.STRING_TRUE.equals(args[0])) {
                    intent.putExtra(Constant.STRING_TRUE, true);
                }
            }
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initData();
        initView();
        initWebView();
    }

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            isFromSplash = getIntent().getExtras().getBoolean(Constant.STRING_TRUE);
        }
    }

    private void initView() {
        image_back = (ImageView) findViewById(R.id.image_back);
        titleText = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.webView);
        if (!isFromSplash) {
            setBack(image_back);
        } else {
            setSwipeBackEnable(false);
            image_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.jumpToMain(WebViewActivity.this);
                    finish();
                }
            });
        }
        setRight();
    }

    private void setRight() {
        ImageView image_right = (ImageView) findViewById(R.id.image_right);
        image_right.setVisibility(View.VISIBLE);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title)) {
                    onEvent(EventConstant.BTN_SHARE);
                    ShareWindow.show(WebViewActivity.this, title, url, url, "", eventStatisticsHelper);
                }
            }
        });
        setImageRight(image_right, R.mipmap.web_btn_share);
    }

    private void initWebView() {
        loadWebView(webView);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(Constant.INTENT_WEB_URL);
            if (eventStatisticsHelper != null) {
                eventStatisticsHelper.recordUserAction(this, EventConstant.OPEN_WEBVIEW, "", url);
            }
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
        }
    }

    private void loadWebView(WebView webView) {
        webView.setBackgroundColor(0);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /* Enable zooming */
        webSettings.setSupportZoom(false);
        webSettings.setSavePassword(false);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                WebViewActivity.this.title = title;
                titleText.setText(title);
            }
        };
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(webChromeClient);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFromSplash) {
                MainActivity.jumpToMain(this);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Log.i("tag", "url=" + url);
            Log.i("tag", "userAgent=" + userAgent);
            Log.i("tag", "contentDisposition=" + contentDisposition);
            Log.i("tag", "mimetype=" + mimetype);
            Log.i("tag", "contentLength=" + contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
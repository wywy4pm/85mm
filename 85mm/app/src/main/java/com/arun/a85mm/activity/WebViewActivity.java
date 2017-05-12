package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.utils.StatusBarUtils;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private String url;
    private ImageView image_back;
    private TextView titleText;
    //private ImageView image_right;

    public static void jumpToWebViewActivity(Context context, String webUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constant.INTENT_WEB_URL, webUrl);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initView();
        initWebView();
    }

    private void initView() {
        image_back = (ImageView) findViewById(R.id.image_back);
        titleText = (TextView) findViewById(R.id.title);
        //image_right = (ImageView) findViewById(R.id.image_right);
        webView = (WebView) findViewById(R.id.webView);
        setBack(image_back);
    }

    private void initWebView() {
        loadWebView(webView);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(Constant.INTENT_WEB_URL);
            if (!TextUtils.isEmpty(url)) {
                webView.postUrl(url, null);
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
                titleText.setText(title);
            }
        };
        webView.setWebChromeClient(webChromeClient);
    }

}
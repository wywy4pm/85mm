package com.arun.a85mm.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arun.a85mm.R;
import com.arun.a85mm.common.Constant;

/**
 * Created by wy on 2017/9/2.
 */

public class WebViewFragment extends BaseFragment {

    private View title_bar;
    private WebView webView;
    private String url;

    public static WebViewFragment getInstance(String loadUrl) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.INTENT_WEB_URL, loadUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        title_bar = findViewById(R.id.title_bar);
        title_bar.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.webView);
        initWebView();
    }

    @Override
    protected void initData() {

    }

    private void initWebView() {
        loadWebView(webView);
        if (getArguments() != null
                && getArguments().containsKey(Constant.INTENT_WEB_URL)
                && getArguments().getString(Constant.INTENT_WEB_URL) != null) {
            url = getArguments().getString(Constant.INTENT_WEB_URL);
            /*if (eventStatisticsHelper != null) {
                eventStatisticsHelper.recordUserAction(getActivity(), EventConstant.OPEN_WEBVIEW, "", url);
            }*/
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
            }
        };
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(webChromeClient);
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

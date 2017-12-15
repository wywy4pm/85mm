package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.ContactDialog;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GsonUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.google.gson.reflect.TypeToken;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private ImageView image_back;
    private TextView titleText;
    //private int isFromSplash;
    private String title;
    private int type;
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_SPLASH = 1;
    public static final int TYPE_MY_COIN = 2;

    public static void jumpToWebViewActivity(Context context, String webUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constant.INTENT_WEB_URL, webUrl);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToWebViewActivity(Context context, String webUrl, int type, String... args) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constant.INTENT_WEB_URL, webUrl);
        intent.putExtra(Constant.INTENT_WEB_TYPE, type);
        if (args != null && args.length > 0) {
            if (args.length == 1) {
                intent.putExtra(Constant.INTENT_WEB_ARGS, args[0]);
            }
        }
        context.startActivity(intent);
        if (context instanceof SplashActivity) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
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
            type = getIntent().getExtras().getInt(Constant.INTENT_WEB_TYPE);
        }
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        image_back = (ImageView) findViewById(R.id.image_back);
        titleText = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.webView);
        if (type != TYPE_SPLASH) {
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
        if (type == TYPE_MY_COIN) {
            setRightText("充值");
        } else if (type == TYPE_COMMON) {
            setRightImage();
        }

    }

    private void setRightText(final String text) {
        TextView text_right = (TextView) findViewById(R.id.text_right);
        text_right.setVisibility(View.VISIBLE);
        if (text_right.getLayoutParams() != null
                && text_right.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            text_right.getLayoutParams().height = DensityUtil.dp2px(this, 22);
            text_right.getLayoutParams().width = DensityUtil.dp2px(this, 46);
            ((RelativeLayout.LayoutParams) text_right.getLayoutParams())
                    .setMargins(DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10));
        }
        text_right.setVisibility(View.VISIBLE);
        text_right.setText(text);
        text_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        text_right.setBackgroundResource(R.drawable.shape_btn_reply);
        text_right.setTextColor(getResources().getColor(R.color.white));
        text_right.setGravity(Gravity.CENTER);
        text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(text) && text.equals("充值")) {
                    showContactDialog();
                }
            }
        });
    }

    private void showContactDialog() {
        ContactDialog contactDialog = new ContactDialog(this, R.style.CustomDialog);
        contactDialog.show();
    }

    private void setRightImage() {
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
            title = getIntent().getExtras().getString(Constant.INTENT_WEB_ARGS);
            if (eventStatisticsHelper != null) {
                eventStatisticsHelper.recordUserAction(this, EventConstant.OPEN_WEBVIEW, "", url);
            }
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
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

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (progressBar.getVisibility() == View.INVISIBLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(webChromeClient);
        webView.addJavascriptInterface(this, "mm");
    }

    @JavascriptInterface
    public void jumToAwardDetail(String titleName, String awardJson) {
        AwardBodyBean awardBodyBean = GsonUtils.fromJson(awardJson, new TypeToken<AwardBodyBean>() {
        }.getType());
        if (awardBodyBean != null) {
            AmountWorkActivity.jumpToAmountWork(this, AmountWorkActivity.TYPE_COMMON, titleName, awardBodyBean);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            if (type == TYPE_SPLASH) {
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
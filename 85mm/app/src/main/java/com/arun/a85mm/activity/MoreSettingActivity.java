package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.helper.ShareHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DataCleanManager;
import com.arun.a85mm.utils.OtherAppStartUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.SystemServiceUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layout_share;
    private RelativeLayout layout_clear;
    private TextView cache_size;
    //private ListView configListView;
    //private ConfigAdapter configAdapter;
    private List<String> texts = new ArrayList<>();
    private ImageView more_detail;

    public static void jumpToMoreSettingActivity(Context context) {
        Intent intent = new Intent(context, MoreSettingActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initView();
        initData();
    }

    private void initView() {
        layout_share = (RelativeLayout) findViewById(R.id.layout_share);
        layout_clear = (RelativeLayout) findViewById(R.id.layout_clear);
        cache_size = (TextView) findViewById(R.id.cache_size);
        more_detail = (ImageView) findViewById(R.id.more_detail);
        /*configListView = (ListView) findViewById(R.id.configListView);
        configAdapter = new ConfigAdapter(this, texts);
        configListView.setAdapter(configAdapter);*/

        layout_share.setOnClickListener(this);
        layout_clear.setOnClickListener(this);
        setTitle("更多");
        setBack();
        setCommonShow();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        cache_size.setText(DataCleanManager.getImageCacheSize(this));
        /*Object object = CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_CONFIG);
        if (object != null && object instanceof List) {
            texts.addAll((List<String>) object);
        }*/
        String moreImageUrl = SharedPreferencesUtils.getMoreImage(this);
        Glide.with(this).load(moreImageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(more_detail);
        //configAdapter.notifyDataSetChanged();
    }

    private void showShare() {
        ShareWindow.show(this, getString(R.string.share_title), getString(R.string.share_description), getString(R.string.share_url), "");
    }

    private void clearCache() {
        DataCleanManager.clearImageAllCache(this);
        cache_size.setText(DataCleanManager.getImageCacheSize(this));
        showTop("清除缓存成功");
    }

    public void openWeChat(View view) {
        OtherAppStartUtils.jumpToWeChat(this);
    }

    public void copyWeChat(View view) {
        SystemServiceUtils.copyText(this, getString(R.string.wechat_num));
        showTop("复制成功");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_share:
                showShare();
                break;
            case R.id.layout_clear:
                clearCache();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}

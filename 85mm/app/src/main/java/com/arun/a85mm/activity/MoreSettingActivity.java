package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ConfigAdapter;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DataCleanManager;
import com.arun.a85mm.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layout_share;
    private RelativeLayout layout_clear;
    private TextView cache_size;
    private ListView configListView;
    private ConfigAdapter configAdapter;
    private List<String> texts = new ArrayList<>();

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
        configListView = (ListView) findViewById(R.id.configListView);
        configAdapter = new ConfigAdapter(this, texts);
        configListView.setAdapter(configAdapter);

        layout_share.setOnClickListener(this);
        layout_clear.setOnClickListener(this);
        setBack();
        setCommonShow();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        cache_size.setText(DataCleanManager.getImageCacheSize(this));
        Object object = CacheUtils.getObject(this, CacheUtils.KEY_OBJECT_CONFIG);
        if (object != null && object instanceof List) {
            texts.addAll((List<String>) object);
        }
        configAdapter.notifyDataSetChanged();
    }

    private void showShare() {
        
    }

    private void clearCache() {
        DataCleanManager.clearImageAllCache(this);
        cache_size.setText(DataCleanManager.getCacheSize(this));
        showTop("清除缓存成功");
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
}

package com.arun.a85mm.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.bumptech.glide.Glide;

public class ArticleDetailActivity extends BaseActivity {

    private ImageView head_image;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        /*if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }*/
        initView();
        initData();
    }

    private void initView() {
        head_image = (ImageView) findViewById(R.id.head_image);
    }

    private void initData() {
        //Glide.with(this).load(url).centerCrop().into(head_image);
    }
}

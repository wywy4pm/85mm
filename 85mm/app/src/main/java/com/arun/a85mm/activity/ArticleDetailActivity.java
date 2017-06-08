package com.arun.a85mm.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.ArticleDetailAdapter;
import com.arun.a85mm.bean.ArticleDetailBean;
import com.arun.a85mm.bean.ArticleDetailResponse;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.presenter.ArticleActivityPresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyLinearLayoutManager;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView;
import com.arun.a85mm.view.CommonView2;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends BaseActivity implements CommonView2<ArticleDetailResponse.ArticleBean> {

    private RelativeLayout layout_detail;
    private ArticleActivityPresenter articleActivityPresenter;
    //private ImageView head_image;
    private String url = "";
    private int startX = 0;
    private int startY = 0;
    private String articleId;
    /*private TextView article_title;
    private RelativeLayout article_author;
    private ImageView author_head_image;
    private TextView author_name;
    private TextView article_create_time;*/
    private RecyclerView recyclerView;
    private ImageView back;
    private ArticleDetailAdapter articleDetailAdapter;
    private ArticleDetailResponse.ArticleBean articleBean;
    private List<ArticleDetailBean> articleDetails = new ArrayList<>();
    private boolean isAnimEndLoadUnSuccess = true;
    private ImageView imageView;

    public static void startArticleDetailActivity(Context context, String articleId, int x, int y, String headImageUrl) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(Constant.INTENT_ARTICLE_ID, articleId);
        intent.putExtra(Constant.INTENT_ARTICLE_IMAGE_POSITIONX, x);
        intent.putExtra(Constant.INTENT_ARTICLE_IMAGE_POSITIONY, y);
        intent.putExtra(Constant.INTENT_ARTICLE_HEAD_IMAGE, headImageUrl);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                articleId = getIntent().getExtras().getString(Constant.INTENT_ARTICLE_ID);
                url = getIntent().getExtras().getString(Constant.INTENT_ARTICLE_HEAD_IMAGE);
                startX = getIntent().getExtras().getInt(Constant.INTENT_ARTICLE_IMAGE_POSITIONX);
                startY = getIntent().getExtras().getInt(Constant.INTENT_ARTICLE_IMAGE_POSITIONY);
            }
        }
        initView();
        initData();
    }

    private void initView() {
        layout_detail = (RelativeLayout) findViewById(R.id.layout_detail);
        /*head_image = (ImageView) findViewById(R.id.head_image);
        article_title = (TextView) findViewById(R.id.article_title);
        article_author = (RelativeLayout) findViewById(R.id.article_author);
        author_head_image = (ImageView) findViewById(R.id.author_head_image);
        author_name = (TextView) findViewById(R.id.author_name);
        article_create_time = (TextView) findViewById(R.id.article_create_time);*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        back = (ImageView) findViewById(R.id.back);
        setBack(back);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 8) {//上滑
                    StatusBarUtils.setStatusBar(ArticleDetailActivity.this, true);
                } else if (dy < -8) {//下滑
                    StatusBarUtils.setStatusBar(ArticleDetailActivity.this, false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initData() {
        //Glide.with(this).load(url).centerCrop().into(head_image);
        showHeadTranslateAnimation();
        articleActivityPresenter = new ArticleActivityPresenter(this);
        articleActivityPresenter.attachView(this);
        requestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (articleActivityPresenter != null) {
            articleActivityPresenter.detachView();
        }
    }

    private void showHeadTranslateAnimation() {
        imageView = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.getScreenWidth(this), (int) (DensityUtil.getScreenWidth(this) * 0.6));
        imageView.setLayoutParams(params);
        imageView.setX(startX);
        imageView.setY(startX - DensityUtil.getStatusHeight(this));
        Glide.with(this).load(url).centerCrop().into(imageView);
        layout_detail.addView(imageView);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", startY - DensityUtil.getStatusHeight(this), 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (articleDetails != null && articleDetails.size() > 0) {
                    setLoadSuccessView();
                } else {
                    isAnimEndLoadUnSuccess = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(1000).start();
    }

    private void requestData() {
        if (articleActivityPresenter != null) {
            articleActivityPresenter.getArticleDetailsData(articleId);
        }
    }

    private void setLoadSuccessView() {
        articleDetailAdapter = new ArticleDetailAdapter(ArticleDetailActivity.this, articleDetails);
        recyclerView.setAdapter(articleDetailAdapter);
        layout_detail.removeView(imageView);
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void refresh(ArticleDetailResponse.ArticleBean data) {
        if (data != null) {
            articleBean = data;
            if (articleBean.contentComponents != null && articleBean.contentComponents.size() > 0 && articleBean.contentComponents.get(0) != null) {//添加头图
                ArticleDetailBean articleDetailBean = new ArticleDetailBean();
                articleDetailBean.componentType = Constant.ARTICLE_TYPE_FULL_IMAGE;
                articleDetailBean.imageUrl = url;
                articleDetails.add(articleDetailBean);
                //articleBean.contentComponents.remove(0);//去除多余头图
            }
            if (!TextUtils.isEmpty(articleBean.title)) {//添加大标题
                ArticleDetailBean articleDetailBean = new ArticleDetailBean();
                articleDetailBean.componentType = Constant.ARTICLE_TYPE_BIG_TITLE;
                articleDetailBean.text = articleBean.title;
                articleDetails.add(articleDetailBean);
            }
            if (!TextUtils.isEmpty(articleBean.author) && !TextUtils.isEmpty(articleBean.authorHeadImg)) {//添加作者部分
                ArticleDetailBean articleDetailBean = new ArticleDetailBean();
                articleDetailBean.componentType = Constant.ARTICLE_TYPE_AUTHOR;
                articleDetailBean.author = articleBean.author;
                articleDetailBean.authorHeadImg = articleBean.authorHeadImg;
                articleDetailBean.createTime = articleBean.createTime;
                articleDetails.add(articleDetailBean);
            }
            articleDetails.addAll(articleBean.contentComponents);
            if (!isAnimEndLoadUnSuccess) {
                setLoadSuccessView();
            }
        }
    }

    /*private void loadViewData() {
        if (articleBean != null) {
            if (head_image != null && head_image.getLayoutParams() != null) {
                head_image.getLayoutParams().height = (int) (DensityUtil.getScreenWidth(this) * 0.6);
            }
            Glide.with(this).load(url).centerCrop().into(head_image);
            article_title.setText(articleBean.title);
            if (!TextUtils.isEmpty(articleBean.title) && !TextUtils.isEmpty(articleBean.author)) {
                article_author.setVisibility(View.VISIBLE);
                author_name.setText(articleBean.author);
                Glide.with(this).load(articleBean.authorHeadImg).transform(new GlideCircleTransform(this));
                article_create_time.setText(articleBean.createTime);
            } else {
                article_author.setVisibility(View.GONE);
            }
        }
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ArticleDetailActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, R.anim.slide_out_right);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}

package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.ArticleDetailActivity;
import com.arun.a85mm.bean.ArticleListBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.utils.DensityUtil;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by WY on 2017/4/13.
 */
public class ArticleListAdapter extends RecyclerView.Adapter {

    private WeakReference<Context> contexts;
    private List<ArticleListBean> articles;
    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ArticleListAdapter(Context context, List<ArticleListBean> articles) {
        contexts = new WeakReference<>(context);
        this.articles = articles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.article_list_item, parent, false);
        return new ArticleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleHolder) {
            ArticleHolder articleHolder = (ArticleHolder) holder;
            //浏览文章
            if (eventListener != null) {
                if (position >= 4) {
                    ArticleListBean bean = articles.get(position - 4);
                    if (bean != null && !TextUtils.isEmpty(bean.id)) {
                        eventListener.onEvent(EventStatisticsHelper.createOneActionList(EventConstant.ARTICLE_BROWSE, bean.id, ""));
                    }
                }
                articleHolder.setData(contexts.get(), articles.get(position), eventListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    private static class ArticleHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private View divide;
        private ImageView article_image;
        private TextView article_title, article_detail;

        public ArticleHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.divide = itemView.findViewById(R.id.divide);
            this.article_image = (ImageView) itemView.findViewById(R.id.article_image);
            this.article_title = (TextView) itemView.findViewById(R.id.article_title);
            this.article_detail = (TextView) itemView.findViewById(R.id.article_detail);
        }

        private void setData(final Context context, final ArticleListBean articleListBean, final EventListener eventListener) {

            if (article_image.getLayoutParams() != null) {
                article_image.getLayoutParams().height = (int) (DensityUtil.getScreenWidth(context) * 0.56);
            }
            Glide.with(context).load(articleListBean.headImage).centerCrop().into(article_image);
            article_title.setText(articleListBean.title);
            article_detail.setText(articleListBean.brief);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击进入文章详情
                    if (eventListener != null) {
                        eventListener.onEvent(EventStatisticsHelper.createOneActionList(EventConstant.CLICK_TO_ARTICLE_DETAIL, articleListBean.id, ""));
                    }
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    //onImageClickCallBack.onClickImage(v, x, y, articleListBean.headImage);
                    ArticleDetailActivity.startArticleDetailActivity(context, articleListBean.id, x, y, articleListBean.headImage);
                }
            });
        }
    }
}

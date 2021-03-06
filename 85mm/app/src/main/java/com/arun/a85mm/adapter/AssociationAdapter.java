package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.activity.UserMainActivity;
import com.arun.a85mm.bean.CommentsBean;
import com.arun.a85mm.bean.CommunityTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/24.
 */

public class AssociationAdapter extends BaseRecyclerAdapter<WorkListBean> {
    public static final String DATA_TYPE_HEAD = "head";
    public static final String DATA_TYPE_CONTENT = "content";

    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_CONTENT = 1;

    private int screenWidth;
    private List<CommunityTagBean> tagsList;
    private EventStatisticsHelper eventStatisticsHelper;

    private static OnTagClick onTagClick;

    public interface OnTagClick {
        void click(int dataType);
    }

    public void setOnTagClick(OnTagClick onTagClick) {
        AssociationAdapter.onTagClick = onTagClick;
    }

    public AssociationAdapter(Context context, List<WorkListBean> list, List<CommunityTagBean> tagsList, EventStatisticsHelper eventStatisticsHelper) {
        super(context, list);
        screenWidth = DensityUtil.getScreenWidth(context);
        this.tagsList = tagsList;
        this.eventStatisticsHelper = eventStatisticsHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.association_head_tag, parent, false);
            return new HeadHolder(contexts.get(), itemView, tagsList);
        } else if (viewType == VIEW_TYPE_CONTENT) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_association_item, parent, false);
            return new AssociationHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {
            HeadHolder headHolder = (HeadHolder) holder;
            headHolder.setData(contexts.get(), tagsList);
        } else if (holder instanceof AssociationHolder) {
            AssociationHolder associationHolder = (AssociationHolder) holder;
            associationHolder.setData(contexts.get(), getItem(position), screenWidth, eventStatisticsHelper);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (DATA_TYPE_HEAD.equals(getItem(position).type)) {
            type = VIEW_TYPE_HEAD;
        } else if (DATA_TYPE_CONTENT.equals(getItem(position).type)) {
            type = VIEW_TYPE_CONTENT;
        }
        return type;
    }

    private static class HeadHolder extends RecyclerView.ViewHolder {
        /*public TextView tag_choice;
        public TextView tag_newest;
        public TextView tag_hottest;*/
        public LinearLayout layout_head_tags;

        private HeadHolder(final Context context, View itemView, final List<CommunityTagBean> tagsList) {
            super(itemView);
            /*this.tag_choice = (TextView) itemView.findViewById(R.id.tag_choice);
            this.tag_newest = (TextView) itemView.findViewById(R.id.tag_newest);
            this.tag_hottest = (TextView) itemView.findViewById(R.id.tag_hottest);*/
            this.layout_head_tags = (LinearLayout) itemView.findViewById(R.id.layout_head_tags);

            int dataType = SharedPreferencesUtils.getConfigInt(context, SharedPreferencesUtils.KEY_ASSOCIATION_TAG);
            if (dataType == 0) {
                dataType = 2;
            }
            if (tagsList != null && tagsList.size() > 0) {
                for (int i = 0; i < tagsList.size(); i++) {
                    final TextView tv = new TextView(context);
                    tv.setTag(i);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    tv.setTextColor(context.getResources().getColor(R.color.charcoalgrey));
                    tv.setPadding(DensityUtil.dp2px(context, 10), DensityUtil.dp2px(context, 5), DensityUtil.dp2px(context, 10), DensityUtil.dp2px(context, 5));
                    tv.setGravity(Gravity.CENTER);
                    tv.setBackgroundResource(R.drawable.selector_audit_flow_tags);
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv.setSelected(true);
                            tv.setTextColor(context.getResources().getColor(R.color.white));
                            resetSelect(context, tv.getTag());

                            if (onTagClick != null) {
                                onTagClick.click(tagsList.get(finalI).dataType);
                            }
                        }
                    });
                    tv.setText(tagsList.get(i).name);

                    if (dataType == tagsList.get(i).dataType) {
                        tv.setSelected(true);
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    layout_head_tags.addView(tv);
                    ((LinearLayout.LayoutParams) tv.getLayoutParams()).setMargins(0, 0, DensityUtil.dp2px(context, 10), 0);
                }
            }

        }

        private void setData(final Context context, final List<CommunityTagBean> tagsList) {
            int dataType = SharedPreferencesUtils.getConfigInt(context, SharedPreferencesUtils.KEY_ASSOCIATION_TAG);
            for (int i = 0; i < layout_head_tags.getChildCount(); i++) {
                TextView tv = (TextView) layout_head_tags.getChildAt(i);
                if (dataType == tagsList.get(i).dataType) {
                    tv.setSelected(true);
                    tv.setTextColor(context.getResources().getColor(R.color.white));

                    resetSelect(context, tv.getTag());
                }
            }
        }

        private void resetSelect(Context context, Object selectTag) {
            if (layout_head_tags.getChildCount() > 0) {
                for (int i = 0; i < layout_head_tags.getChildCount(); i++) {
                    TextView textView = (TextView) layout_head_tags.getChildAt(i);
                    if (textView != null && textView.getTag() != null
                            && !textView.getTag().equals(selectTag)
                            && textView.isSelected()) {
                        textView.setSelected(false);
                        textView.setTextColor(context.getResources().getColor(R.color.charcoalgrey));
                    }
                }
            }
        }
    }

    private static class AssociationHolder extends RecyclerView.ViewHolder {
        public ImageView author_image;
        public TextView author_name;
        private TextView author_create_time;
        private RelativeLayout work_list_item_author;
        private ImageView cover_Image;
        private TextView community_title;
        private TextView community_detail;
        private RelativeLayout comment_head;
        private LinearLayout layout_comment;
        private RelativeLayout layout_list_comment;
        private View itemView;
        private RelativeLayout layout_cover_Image;
        private TextView cover_count;
        private RelativeLayout shadow;
        private TextView comment_shadow;
        private ImageView comment_more;

        private AssociationHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.author_image = (ImageView) itemView.findViewById(R.id.author_image);
            this.author_name = (TextView) itemView.findViewById(R.id.author_name);
            this.author_create_time = (TextView) itemView.findViewById(R.id.author_create_time);
            this.work_list_item_author = (RelativeLayout) itemView.findViewById(R.id.work_list_item_author);
            this.cover_Image = (ImageView) itemView.findViewById(R.id.cover_Image);
            this.community_title = (TextView) itemView.findViewById(R.id.community_title);
            this.community_detail = (TextView) itemView.findViewById(R.id.community_detail);
            this.comment_head = (RelativeLayout) itemView.findViewById(R.id.comment_head);
            this.layout_comment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            this.layout_list_comment = (RelativeLayout) itemView.findViewById(R.id.layout_list_comment);
            this.layout_cover_Image = (RelativeLayout) itemView.findViewById(R.id.layout_cover_Image);
            this.cover_count = (TextView) itemView.findViewById(R.id.cover_count);
            this.shadow = (RelativeLayout) itemView.findViewById(R.id.shadow);
            this.comment_shadow = (TextView) itemView.findViewById(R.id.comment_shadow);
            this.comment_more = (ImageView) itemView.findViewById(R.id.comment_more);
        }

        private void setData(final Context context, final WorkListBean bean, int screenWidth, final EventStatisticsHelper eventStatisticsHelper) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Serializable> map = new HashMap<>();
                    map.put(UrlJumpHelper.WORK_ID, bean.id);
                    map.put(OneWorkActivity.KEY_TYPE, Constant.TYPE_COMMUNITY);
                    /*if (bean.productInfo != null) {
                        map.put(OneWorkActivity.KEY_AMOUNT, bean.productInfo);
                    }*/
                    /*FragmentCommonActivity.jumpToFragmentCommonActivity(context,
                            FragmentCommonActivity.FRAGMENT_ONE_WORK, bean.workTitle, map, FragmentCommonActivity.BACK_MODE_COM);*/

                    OneWorkActivity.jumpToOneWorkActivity(context, OneWorkActivity.FRAGMENT_ONE_WORK, bean.title, map, OneWorkActivity.BACK_MODE_COM);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogHelper.showBottom(context, Constant.TYPE_COMMUNITY, "", bean.id, bean.uid, eventStatisticsHelper);
                    return false;
                }
            });

            Glide.with(context).load(bean.authorHeadImg).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(context)).into(author_image);
            author_name.setText(bean.authorName);
            author_create_time.setText(bean.createTime);

            author_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserMainActivity.jumpToUserMain(context, bean.uid);
                }
            });
            author_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserMainActivity.jumpToUserMain(context, bean.uid);
                }
            });

            int imageHeight = 0;
            if (bean.coverWidth > 0 && !TextUtils.isEmpty(bean.coverUrl)) {
                layout_cover_Image.setVisibility(View.VISIBLE);
                if (bean.coverHeight < bean.coverWidth) {
                    if (ConfigHelper.isShowWImage == Constant.VALUE_SHOW_WIMAGE) {
                        imageHeight = screenWidth;
                    } else {
                        imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
                    }
                } else {
                    imageHeight = (bean.coverHeight * screenWidth) / bean.coverWidth;
                }
                if (imageHeight > 8192) {
                    imageHeight = 8192;
                }
                layout_cover_Image.getLayoutParams().height = imageHeight;
                cover_Image.getLayoutParams().height = imageHeight;
                cover_count.setText(String.valueOf(bean.totalImageNum));
                Glide.with(context)
                        .load(bean.coverUrl)
                        .placeholder(bean.backgroundColor)
                        .error(bean.backgroundColor)
                        .override(screenWidth, imageHeight)
                        .centerCrop()
                        .into(cover_Image);
            } else {
                layout_cover_Image.setVisibility(View.GONE);
            }

            community_title.setText(bean.title);
            community_detail.setText(bean.description);

            comment_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DialogHelper.showBottomSourceLink(context, "", bean.workId, eventStatisticsHelper, Constant.TYPE_COMMUNITY, bean.uid);
                    DialogHelper.showBottom(context, Constant.TYPE_COMMUNITY, "", bean.id, bean.uid, eventStatisticsHelper);
                }
            });
            if (bean.commentList != null && bean.commentList.size() > 0) {
                comment_shadow.setVisibility(View.VISIBLE);
                layout_comment.setVisibility(View.VISIBLE);
                layout_comment.removeAllViews();
                for (int i = 0; i < bean.commentList.size(); i++) {
                    if (bean.commentList.get(i) != null) {
                        final CommentsBean commentItem = bean.commentList.get(i);
                        View commentView = LayoutInflater.from(context).inflate(R.layout.list_commnet_item, layout_comment, false);
                        if (commentView.getLayoutParams() != null && commentView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                            if (i < bean.commentList.size() - 1) {
                                ((LinearLayout.LayoutParams) commentView.getLayoutParams()).setMargins(0, 0, 0, DensityUtil.dp2px(context, 12));
                            } else {
                                ((LinearLayout.LayoutParams) commentView.getLayoutParams()).setMargins(0, 0, 0, 0);
                            }
                        }
                        TextView author = (TextView) commentView.findViewById(R.id.comment_author);
                        TextView detail = (TextView) commentView.findViewById(R.id.comment_detail);
                        author.setText(commentItem.authorName + " : ");
                        detail.setText(commentItem.content);

                        if (!TextUtils.isEmpty(commentItem.authorId)) {
                            if (commentItem.authorId.equals(SharedPreferencesUtils.getUid(context))
                                    || "4".equals(SharedPreferencesUtils.getUid(context))) {
                                commentView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        DialogHelper.showDeleteCommentBottom(context, String.valueOf(commentItem.id));
                                        return false;
                                    }
                                });
                            }
                        }

                        commentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserMainActivity.jumpToUserMain(context, commentItem.authorId);
                            }
                        });

                        layout_comment.addView(commentView);
                    }
                }
            } else {
                comment_shadow.setVisibility(View.GONE);
                layout_comment.setVisibility(View.GONE);
            }
        }
    }

}

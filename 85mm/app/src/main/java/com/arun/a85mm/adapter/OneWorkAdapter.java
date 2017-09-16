package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.arun.a85mm.R;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.bean.CommentsBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.widget.AutoLineLinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/21.
 */

public class OneWorkAdapter extends BaseRecyclerAdapter<WorkListItemBean> {
    private static int screenWidth;
    private WorkListBean workListBean;
    private OnImageClick onImageClick;
    public static String DATA_TYPE_HEAD = "head";
    public static String DATA_TYPE_IMAGE = "image";
    public static String DATA_TYPE_DESCRIPTION = "description";
    public static String DATA_TYPE_COMMENTS = "comments";
    public static String DATA_TYPE_ADD_TAG = "add_tag";
    private static int VIEW_TYPE_HEAD = 0;
    private static int VIEW_TYPE_IMAGE = 1;
    private static int VIEW_TYPE_DESCRIPTION = 3;
    private static int VIEW_TYPE_COMMENTS = 4;
    private static int VIEW_TYPE_ADD_TAG = 5;
    private boolean isNoImage = false;

    public OneWorkAdapter(Context context, List<WorkListItemBean> list) {
        super(context, list);
        screenWidth = DensityUtil.getScreenWidth(context);
    }

    public void setWorkListBean(WorkListBean workListBean) {
        this.workListBean = workListBean;
    }

    public void setOnImageClick(OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }

    public OnTagWorkListener onTagWorkListener;

    public void setOnTagWorkListener(OnTagWorkListener onTagWorkListener) {
        this.onTagWorkListener = onTagWorkListener;
    }

    public void setNoImage(boolean isNoImage) {
        this.isNoImage = isNoImage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_one_work_author, parent, false);
            return new OneWorkAuthorHolder(itemView);
        } else if (viewType == VIEW_TYPE_IMAGE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_work_list_item, parent, false);
            return new WorkListItemHolder(itemView);
        } else if (viewType == VIEW_TYPE_DESCRIPTION) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_description, parent, false);
            return new DescriptionHolder(itemView);
        } else if (viewType == VIEW_TYPE_COMMENTS) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_one_work_comments, parent, false);
            return new CommentsHolder(contexts.get(), itemView);
        } else if (viewType == VIEW_TYPE_ADD_TAG) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_add_tag, parent, false);
            return new AddTagHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkListItemHolder) {
            WorkListItemHolder workListItemHolder = (WorkListItemHolder) holder;
            workListItemHolder.setData(contexts.get(), getItem(position), workListBean, onImageClick);
        } else if (holder instanceof OneWorkAuthorHolder) {
            OneWorkAuthorHolder oneWorkAuthorHolder = (OneWorkAuthorHolder) holder;
            oneWorkAuthorHolder.setData(contexts.get(), getItem(position));
        } else if (holder instanceof DescriptionHolder) {
            DescriptionHolder descriptionHolder = (DescriptionHolder) holder;
            descriptionHolder.setData(contexts.get(), getItem(position), isNoImage);
        } else if (holder instanceof CommentsHolder) {
            CommentsHolder commentsHolder = (CommentsHolder) holder;
            commentsHolder.setData(getItem(position).comments);
        } else if (holder instanceof AddTagHolder) {
            AddTagHolder addTagHolder = (AddTagHolder) holder;
            addTagHolder.setData(contexts.get(), getItem(position), onTagWorkListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (DATA_TYPE_HEAD.equals(getItem(position).type)) {
            type = VIEW_TYPE_HEAD;
        } else if (DATA_TYPE_IMAGE.equals(getItem(position).type)) {
            type = VIEW_TYPE_IMAGE;
        } else if (DATA_TYPE_DESCRIPTION.equals(getItem(position).type)) {
            type = VIEW_TYPE_DESCRIPTION;
        } else if (DATA_TYPE_COMMENTS.equals(getItem(position).type)) {
            type = VIEW_TYPE_COMMENTS;
        } else if (DATA_TYPE_ADD_TAG.equals(getItem(position).type)) {
            type = VIEW_TYPE_ADD_TAG;
        }
        return type;
    }

    private static class OneWorkAuthorHolder extends RecyclerView.ViewHolder {
        private ImageView author_image;
        private TextView author_name;
        private TextView author_create_time;

        private OneWorkAuthorHolder(View itemView) {
            super(itemView);
            author_image = (ImageView) itemView.findViewById(R.id.author_image);
            author_name = (TextView) itemView.findViewById(R.id.author_name);
            author_create_time = (TextView) itemView.findViewById(R.id.author_create_time);
        }

        private void setData(Context context, WorkListItemBean bean) {
            author_name.setText(bean.authorName);
            Glide.with(context).load(bean.authorHeadImg).centerCrop().bitmapTransform(new GlideCircleTransform(context)).into(author_image);
            author_create_time.setText(bean.createTime);
        }
    }

    private static class WorkListItemHolder extends RecyclerView.ViewHolder {
        private ImageView work_list_item_img;
        private RippleView rippleView;
        private View bg_line;

        private WorkListItemHolder(View rootView) {
            super(rootView);
            rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
            work_list_item_img = (ImageView) rootView.findViewById(R.id.work_list_item_img);
            bg_line = rootView.findViewById(R.id.bg_line);
            bg_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        private void setData(final Context context, final WorkListItemBean bean, final WorkListBean workListBean, final OnImageClick onImageClick) {

            int imageHeight = 0;
            if (TextUtils.isEmpty(bean.url)) {
                work_list_item_img.setVisibility(View.GONE);
            } else {
                if (bean.width > 0) {
                    work_list_item_img.setVisibility(View.VISIBLE);
                    if (bean.height < bean.width) {
                        imageHeight = screenWidth;
                    } else {
                        imageHeight = (bean.height * screenWidth) / bean.width;
                    }
                    if (imageHeight > 8192) {
                        imageHeight = 8192;
                    }
                    if (work_list_item_img.getLayoutParams() != null) {
                        work_list_item_img.getLayoutParams().height = imageHeight;
                    }
                    Glide.with(context).load(bean.url).centerCrop()
                            .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bean.isLoad = true;
                            return false;
                        }
                    }).override(screenWidth, imageHeight).into(work_list_item_img);
                }
            }

            if (bean.backgroundColor > 0) {
                rippleView.setRippleColor(bean.backgroundColor);
            }
            final int finalSaveImageHeight = imageHeight;
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isLoad) {
                        rippleView.setRippleDuration(300);
                        if (onImageClick != null) {
                            onImageClick.onCoverClick(workListBean.id, bean.url, workListBean.authorName);
                        }
                    } else {
                        rippleView.setRippleDuration(0);
                        Glide.with(context).load(bean.url).centerCrop()
                                .placeholder(bean.backgroundColor).error(bean.backgroundColor)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                bean.isLoad = true;
                                return false;
                            }
                        }).into(work_list_item_img);
                    }
                }
            });
            work_list_item_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rippleView.setRippleDuration(0);
                    if (onImageClick != null) {
                        onImageClick.onMoreLinkClick(workListBean.id, workListBean.sourceUrl);
                    }
                    return false;
                }
            });
        }
    }

    private static class DescriptionHolder extends RecyclerView.ViewHolder {
        private TextView community_title;
        private TextView community_detail;
        private View itemView;

        private DescriptionHolder(View itemView) {
            super(itemView);
            this.community_title = (TextView) itemView.findViewById(R.id.community_title);
            this.community_detail = (TextView) itemView.findViewById(R.id.community_detail);
            this.itemView = itemView;
        }

        public void setData(Context context, WorkListItemBean bean, boolean isNoImage) {
            if (itemView.getLayoutParams() instanceof RecyclerView.LayoutParams) {
                if (isNoImage) {
                    ((RecyclerView.LayoutParams) itemView.getLayoutParams()).setMargins(0, DensityUtil.getStatusHeight(context) + DensityUtil.dp2px(context, 40), 0, 0);
                } else {
                    ((RecyclerView.LayoutParams) itemView.getLayoutParams()).setMargins(0, 0, 0, 0);
                }
            }
            community_title.setText(bean.workTitle);
            community_detail.setText(bean.description);
        }
    }

    private static class CommentsHolder extends RecyclerView.ViewHolder {
        //private View itemView;
        private TextView comment_count;
        private RecyclerView comment_list;
        private CommentAdapter commentAdapter;
        private List<CommentsBean> commentList = new ArrayList<>();

        private CommentsHolder(Context context, View itemView) {
            super(itemView);
            //this.itemView = itemView;
            comment_count = (TextView) itemView.findViewById(R.id.comment_count);
            comment_list = (RecyclerView) itemView.findViewById(R.id.comment_list);
            commentAdapter = new CommentAdapter(context, commentList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            comment_list.setAdapter(commentAdapter);
            comment_list.setLayoutManager(linearLayoutManager);
        }

        private void setData(List<CommentsBean> comments) {
            if (comments != null) {
                commentList.clear();
                comment_count.setText("评论 " + comments.size());
                commentList.addAll(comments);
                commentAdapter.notifyDataSetChanged();
            } else {
                comment_count.setText("评论 " + 0);
            }
        }
    }

    private static class AddTagHolder extends RecyclerView.ViewHolder {
        private AutoLineLinearLayout layout_work_tags;
        private LinearLayout layout_user_tags;
        private AutoLineLinearLayout user_tags;

        private AddTagHolder(View itemView) {
            super(itemView);
            layout_work_tags = (AutoLineLinearLayout) itemView.findViewById(R.id.layout_work_tags);
            layout_user_tags = (LinearLayout) itemView.findViewById(R.id.layout_user_tags);
            user_tags = (AutoLineLinearLayout) itemView.findViewById(R.id.user_tags);
        }

        private void setData(final Context context, final WorkListItemBean bean, final OnTagWorkListener onTagWorkListener) {
            if (bean.workTags != null && bean.workTags.size() > 0) {
                layout_work_tags.setVisibility(View.VISIBLE);
                layout_work_tags.removeAllViews();
                for (int i = 0; i < bean.workTags.size(); i++) {
                    final String tagName = bean.workTags.get(i);
                    View itemView = LayoutInflater.from(context).inflate(R.layout.layout_work_tag_item_gray, layout_work_tags, false);
                    TextView work_tag_name = (TextView) itemView.findViewById(R.id.work_tag_name);
                    work_tag_name.setText(bean.workTags.get(i));
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> map = new HashMap<>();
                            map.put(TagWorkFragment.KEY_TAG_NAME, tagName);
                            FragmentCommonActivity.jumpToFragmentCommonActivity(context, FragmentCommonActivity.FRAGMENT_TAG_WORKS, tagName, map);
                        }
                    });
                    layout_work_tags.addView(itemView);
                }
            } else {
                layout_work_tags.setVisibility(View.GONE);
            }

            if (ConfigHelper.userTags != null && ConfigHelper.userTags.size() > 0) {
                layout_user_tags.setVisibility(View.VISIBLE);
                user_tags.removeAllViews();
                final List<UserTagBean> userTags = new ArrayList<>();
                userTags.addAll(ConfigHelper.userTags);
                for (int i = 0; i < userTags.size(); i++) {
                    final UserTagBean tagBean = userTags.get(i);
                    if (tagBean != null && tagBean.isShow == 1) {
                        tagBean.tagType = 0;
                        final TextView myTag = new TextView(context);
                        myTag.setTextColor(context.getResources().getColor(R.color.text_right_tips));
                        myTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        myTag.setText(tagBean.name);
                        myTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onTagWorkListener != null) {
                                    onTagWorkListener.onClickMyTag(tagBean, bean.id);
                                }
                                tagBean.tagType = tagBean.tagType == 0 ? 1 : 0;
                                myTag.setTextColor(context.getResources().getColor(tagBean.tagType == 1 ? R.color.more_yellow : R.color.text_right_tips));
                            }
                        });
                        user_tags.addView(myTag);
                    }
                }
            } else {
                layout_user_tags.setVisibility(View.GONE);
            }
        }
    }
}

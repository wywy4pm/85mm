package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.AuditActivity;
import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.bean.AuditInfoBean;
import com.arun.a85mm.bean.AuditItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.listener.EventListener;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.widget.AutoLineLinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/16.
 */

public class AuditListAdapter extends BaseRecyclerAdapter<AuditItemBean> {
    public static final String TYPE_AUDIT_HEAD = "audit_head";
    public static final String TYPE_AUDIT_LIST = "audit_list";
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    public EventListener eventListener;
    public List<AuditInfoBean.TagItemBean> tags;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public AuditListAdapter(Context context, List<AuditItemBean> list) {
        super(context, list);
    }

    public void setTags(List<AuditInfoBean.TagItemBean> tags) {
        this.tags = tags;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_audit_head, parent, false);
            return new AuditHeadHolder(contexts.get(), itemView, tags);
        } else if (viewType == TYPE_LIST) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_audit_work_item, parent, false);
            return new AuditHolder(contexts.get(), itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AuditHolder) {
            AuditHolder auditHolder = (AuditHolder) holder;
            auditHolder.setData(contexts.get(), getItem(position));
            if (eventListener != null) {
                if (position >= 6) {
                    AuditItemBean previousBean = list.get(position - 6);
                    if (!TextUtils.isEmpty(previousBean.workId)) {
                        Log.d("TAG", "POSITION = " + position);
                        eventListener.onEvent(EventConstant.WORK_BROWSE_AUDIT, previousBean.workId);
                    }
                }
            }
        } else if (holder instanceof AuditHeadHolder) {
            AuditHeadHolder auditHeadHolder = (AuditHeadHolder) holder;
            auditHeadHolder.setData(contexts.get());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (TYPE_AUDIT_HEAD.equals(list.get(position).type)) {
            type = TYPE_HEAD;
        } else if (TYPE_AUDIT_LIST.equals(list.get(position).type)) {
            type = TYPE_LIST;
        }
        return type;
    }

    private static class AuditHeadHolder extends RecyclerView.ViewHolder {
        private AutoLineLinearLayout layout_tags;
        private List<AuditInfoBean.TagItemBean> tags;

        private AuditHeadHolder(final Context context, View itemView, final List<AuditInfoBean.TagItemBean> tags) {
            super(itemView);
            layout_tags = (AutoLineLinearLayout) itemView.findViewById(R.id.layout_tags);

            this.tags = tags;
            String selectName = SharedPreferencesUtils.getConfigString(context, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG);
            if (tags != null) {
                for (int i = 0; i < tags.size(); i++) {
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
                            ((AuditActivity) context).setSearchName(tags.get(finalI).searchName);
                            ((AuditActivity) context).requestData();
                        }
                    });
                    tv.setText(tags.get(i).showName);

                    if ((!TextUtils.isEmpty(selectName) && selectName.equals(tags.get(i).searchName))
                            || (TextUtils.isEmpty(selectName) && i == 0)) {
                        tv.setSelected(true);
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    layout_tags.addView(tv);
                }
            }
        }

        private void setData(final Context context) {
            String selectName = SharedPreferencesUtils.getConfigString(context, SharedPreferencesUtils.KEY_AUDIT_SELECT_TAG);
            for (int i = 0; i < layout_tags.getChildCount(); i++) {
                TextView tv = (TextView) layout_tags.getChildAt(i);
                if ((!TextUtils.isEmpty(selectName) && selectName.equals(this.tags.get(i).searchName))
                        || (TextUtils.isEmpty(selectName) && i == 0)) {
                    tv.setSelected(true);
                    tv.setTextColor(context.getResources().getColor(R.color.white));

                    resetSelect(context, tv.getTag());
                }
            }
        }

        private void resetSelect(Context context, Object selectTag) {
            if (layout_tags.getChildCount() > 0) {
                for (int i = 0; i < layout_tags.getChildCount(); i++) {
                    TextView textView = (TextView) layout_tags.getChildAt(i);
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

    private static class AuditHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView work_image;
        private TextView work_count;
        private int widthHeight;

        private AuditHolder(Context context, View itemView) {
            super(itemView);
            this.itemView = itemView;
            work_image = (ImageView) itemView.findViewById(R.id.work_image);
            work_count = (TextView) itemView.findViewById(R.id.work_count);
            widthHeight = (DensityUtil.getScreenWidth(context)) / 2;
        }

        private void setData(final Context context, final AuditItemBean bean) {

            work_image.getLayoutParams().height = widthHeight;
            work_image.getLayoutParams().width = widthHeight;
            Glide.with(context)
                    .load(bean.coverUrl)
                    .placeholder(bean.backgroundColor)
                    .error(bean.backgroundColor)
                    .override(widthHeight, widthHeight)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(work_image);

            work_count.setText(bean.totalImageNum + "");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Serializable> map = new HashMap<>();
                    map.put(UrlJumpHelper.WORK_ID, bean.workId);
                    map.put(OneWorkActivity.KEY_TYPE, Constant.TYPE_AUDIT);
                   /* FragmentCommonActivity.jumpToFragmentCommonActivity(context,
                            FragmentCommonActivity.FRAGMENT_ONE_WORK, bean.workTitle, map, FragmentCommonActivity.BACK_MODE_COM);*/

                    OneWorkActivity.jumpToOneWorkActivity(context, OneWorkActivity.FRAGMENT_ONE_WORK, bean.workTitle, map, OneWorkActivity.BACK_MODE_COM);
                }
            });
        }
    }
}

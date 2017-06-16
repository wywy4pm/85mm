package com.arun.a85mm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.SendMessageActivity;
import com.arun.a85mm.bean.MessageItem;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wy on 2017/6/7.
 */

public class MessageAdapter extends BaseRecyclerAdapter<MessageItem> {

    private int msgType;
    private static final int TYPE_TOP = 1;
    private static final int TYPE_IMAGE = 2;

    public MessageAdapter(Context context, List<MessageItem> list, int msgType) {
        super(context, list);
        this.msgType = msgType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_msg_head, parent, false);
            return new MsgHeadHolder(itemView);
        } else if (viewType == TYPE_IMAGE) {
            View itemView = LayoutInflater.from(contexts.get()).inflate(R.layout.layout_single_iamge, parent, false);
            return new ImageHolder(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        MessageItem bean = getItem(position);
        if (bean != null) {
            if (Constant.MESSAGE_LIST_TOP.equals(bean.type)) {
                type = TYPE_TOP;
            } else if (Constant.MESSAGE_LIST_IMAGE.equals(bean.type)) {
                type = TYPE_IMAGE;
            }
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MsgHeadHolder) {
            MsgHeadHolder msgHeadHolder = (MsgHeadHolder) holder;
            msgHeadHolder.setData(contexts.get(), getItem(position), position, msgType);
        } else if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setData(contexts.get(), getItem(position), position);
        }
    }


    private static class MsgHeadHolder extends RecyclerView.ViewHolder {
        private TextView text_user_from;
        private TextView btn_reply;
        private TextView create_time;
        private TextView text_description;
        private View top_divide;
        private TextView from;

        private MsgHeadHolder(View rootView) {
            super(rootView);
            from = (TextView) rootView.findViewById(R.id.from);
            top_divide = rootView.findViewById(R.id.top_divide);
            text_user_from = (TextView) rootView.findViewById(R.id.text_user_from);
            btn_reply = (TextView) rootView.findViewById(R.id.btn_reply);
            create_time = (TextView) rootView.findViewById(R.id.create_time);
            text_description = (TextView) rootView.findViewById(R.id.text_description);
        }

        private void setData(final Context context, final MessageItem bean, int position, int msgType) {
            if (bean != null) {
                if (position == 0) {
                    top_divide.setVisibility(View.GONE);
                } else {
                    top_divide.setVisibility(View.VISIBLE);
                }
                if (msgType == 0) {//收件箱
                    from.setText("From:  ");
                    text_user_from.setText(bean.sender + "号用户");
                    btn_reply.setVisibility(View.VISIBLE);
                    btn_reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Map<String, String> map = new HashMap<>();
                            map.put(SendMessageFragment.KEY_SEND_UID, bean.sender);
                            FragmentCommonActivity.jumpToFragmentCommonActivity(context,
                                    FragmentCommonActivity.FRAGMENT_SEND_MESSAGE, "发私信", map);*/

                            SendMessageActivity.jumpToSendMessage(context, bean.sender);
                        }
                    });
                } else {//已发送
                    from.setText("To:  ");
                    text_user_from.setText(bean.receiver + "号用户");
                    btn_reply.setVisibility(View.GONE);
                }

                create_time.setText(bean.sendTime);
                text_description.setText(bean.content);
            }
        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView item_fullImage;

        private ImageHolder(View rootView) {
            super(rootView);
            item_fullImage = (ImageView) rootView.findViewById(R.id.item_fullImage);
        }

        private void setData(final Context context, final MessageItem bean, int position) {
            //setIsRecyclable(false);
            if (item_fullImage.getLayoutParams() != null) {
                if (item_fullImage.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                    ((RelativeLayout.LayoutParams) item_fullImage.getLayoutParams()).setMargins(DensityUtil.dp2px(context, 12), 0, DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 6));
                }
                int imageWidth = DensityUtil.getScreenWidth(context) - DensityUtil.dp2px(context, 12);
                int imageHeight = 0;
                if (bean.width > 0) {
                    imageHeight = (bean.height * imageWidth) / bean.width;
                    item_fullImage.setVisibility(View.VISIBLE);
                    item_fullImage.getLayoutParams().height = imageHeight;
                    item_fullImage.getLayoutParams().width = imageWidth;
                } else {
                    if (TextUtils.isEmpty(bean.imageUrl)) {
                        item_fullImage.setVisibility(View.GONE);
                    }
                }
                //Log.d("TAG", "item_fullImage = " + item_fullImage.toString() + "     imageUrl = " + bean.imageUrl);
                Glide.with(context).load(bean.imageUrl)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(imageWidth, imageHeight)
                        .centerCrop()
                        .into(item_fullImage);
            }
        }
    }
}

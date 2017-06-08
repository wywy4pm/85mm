package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UploadImageBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by wy on 2017/6/8.
 */

public class UploadImageAdapter extends BaseListAdapter<UploadImageBean> {
    public UploadImageAdapter(Context context, List<UploadImageBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UploadImageHolder uploadImageHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_upload_image, parent, false);
            uploadImageHolder = new UploadImageHolder(convertView);
            convertView.setTag(uploadImageHolder);
        } else {
            uploadImageHolder = (UploadImageHolder) convertView.getTag();
        }
        UploadImageBean bean = getItem(position);
        if (bean.isUpload) {
            uploadImageHolder.photo_close.setVisibility(View.VISIBLE);
        } else {
            uploadImageHolder.photo_close.setVisibility(View.GONE);
        }
        Glide.with(context).load(bean.imageUrl).centerCrop()
                .placeholder(R.mipmap.message_add_photo).error(R.mipmap.message_add_photo).into(uploadImageHolder.photo_close);
        return convertView;
    }


    public static class UploadImageHolder {
        public ImageView photo_close;
        public ImageView add_photo;

        public UploadImageHolder(View rootView) {
            this.photo_close = (ImageView) rootView.findViewById(R.id.photo_close);
            this.add_photo = (ImageView) rootView.findViewById(R.id.add_photo);
        }
    }
}

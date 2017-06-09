package com.arun.a85mm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.listener.ImagePickerListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by wy on 2017/6/8.
 */

public class UploadImageAdapter extends BaseListAdapter<UploadImageBean> {
    private ImagePickerListener imagePickerListener;

    public UploadImageAdapter(Context context, List<UploadImageBean> list) {
        super(context, list);
    }

    public void setImagePickerListener(ImagePickerListener imagePickerListener) {
        this.imagePickerListener = imagePickerListener;
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
            uploadImageHolder.add_photo.setVisibility(View.GONE);
            uploadImageHolder.image_picture.setVisibility(View.VISIBLE);

            Glide.with(context).load(bean.imageUrl).centerCrop().into(uploadImageHolder.image_picture);
            uploadImageHolder.photo_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imagePickerListener != null) {
                        imagePickerListener.removeSelect();
                    }
                }
            });
        } else {
            uploadImageHolder.photo_close.setVisibility(View.GONE);
            uploadImageHolder.add_photo.setVisibility(View.VISIBLE);
            uploadImageHolder.image_picture.setVisibility(View.GONE);

            uploadImageHolder.add_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imagePickerListener != null) {
                        imagePickerListener.openImagePicker();
                    }
                }
            });
        }
        return convertView;
    }


    private static class UploadImageHolder {
        private ImageView photo_close;
        private ImageView add_photo;
        private ImageView image_picture;

        private UploadImageHolder(View rootView) {
            this.photo_close = (ImageView) rootView.findViewById(R.id.photo_close);
            this.add_photo = (ImageView) rootView.findViewById(R.id.add_photo);
            this.image_picture = (ImageView) rootView.findViewById(R.id.image_picture);
        }
    }
}

package com.arun.a85mm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.listener.ImagePickerListener;
import com.arun.a85mm.utils.InputUtils;
import com.arun.a85mm.widget.GridViewForScrollView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/8.
 */

public class SendMessageFragment extends BaseFragment implements ImagePickerListener {
    private EditText reply_receiver;
    private EditText reply_description;
    private GridViewForScrollView gridView;
    private TextView send_msg;
    public static final String KEY_SEND_UID = "send_uid";
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;
    private static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        return R.layout.layout_send_message;
    }

    @Override
    protected void initView() {
        reply_receiver = (EditText) findViewById(R.id.reply_receiver);
        reply_description = (EditText) findViewById(R.id.reply_description);
        gridView = (GridViewForScrollView) findViewById(R.id.gridView);
        send_msg = (TextView) findViewById(R.id.send_msg);
        uploadImageAdapter = new UploadImageAdapter(getActivity(), images);
        uploadImageAdapter.setImagePickerListener(this);
        gridView.setAdapter(uploadImageAdapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        if (getArguments() != null) {
            Map<String, String> map = (Map<String, String>) getArguments().getSerializable(FragmentCommonActivity.EXTRAS);
            if (map != null) {
                String uid = map.get(KEY_SEND_UID);
                reply_receiver.setText(uid);
                InputUtils.setInputEditEnd(reply_receiver);
            }
        }
        images.add(new UploadImageBean(false, null));
        uploadImageAdapter.notifyDataSetChanged();
    }

    public void startPicturePicker() {
        Matisse.from(getActivity())
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(400)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            //Glide.with(this).load(mSelected.get(0)).centerCrop().into(image);
        }
    }

    @Override
    public void openImagePicker() {
        startPicturePicker();
    }

    @Override
    public void removeSelect(int position) {

    }
}

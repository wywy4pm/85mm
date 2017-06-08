package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.FragmentCommonActivity;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.widget.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2017/6/8.
 */

public class SendMessageFragment extends BaseFragment {
    private EditText reply_receiver;
    private EditText reply_description;
    private GridViewForScrollView gridView;
    private TextView send_msg;
    public static final String KEY_SEND_UID = "send_uid";
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;

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
            }
        }
        images.add(new UploadImageBean(false, ""));
        uploadImageAdapter.notifyDataSetChanged();
    }

}

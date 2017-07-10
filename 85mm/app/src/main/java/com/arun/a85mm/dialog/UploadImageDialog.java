package com.arun.a85mm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.utils.AlbumUtils;

/**
 * Created by wy on 2017/7/10.
 */

public class UploadImageDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int requestCode;

    public UploadImageDialog(Context context, int requestCode) {
        super(context);
        this.context = context;
        this.requestCode = requestCode;
    }

    public UploadImageDialog(Context context, int themeResId, int requestCode) {
        super(context, themeResId);
        this.context = context;
        this.requestCode = requestCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upload_image);
        initView();
    }

    private void initView() {
        TextView btn_album_upload = (TextView) findViewById(R.id.btn_album_upload);
        TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_album_upload.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_album_upload:
                AlbumUtils.openAlbum((Activity) context, requestCode);
                cancel();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
        }
    }
}

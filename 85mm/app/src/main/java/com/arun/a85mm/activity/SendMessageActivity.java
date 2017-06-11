package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.helper.OssUploadImageHelper;
import com.arun.a85mm.listener.ImagePickerListener;
import com.arun.a85mm.listener.UploadImageListener;
import com.arun.a85mm.matisse.Matisse;
import com.arun.a85mm.matisse.MimeType;
import com.arun.a85mm.matisse.engine.impl.GlideEngine;
import com.arun.a85mm.matisse.ui.MatisseActivity;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.InputUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.widget.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends BaseActivity implements ImagePickerListener {
    private EditText reply_receiver;
    private EditText reply_description;
    private GridViewForScrollView gridView;
    private TextView send_msg;
    public static final String KEY_SEND_UID = "send_uid";
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;
    private static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    //存放需要上传服务器的imageUrl
    private List<String> uploadImages = new ArrayList<>();

    public static void jumpToSendMessage(Context context, String uid) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        intent.putExtra(KEY_SEND_UID, uid);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        setContentView(R.layout.layout_send_message);
        initView();
        initData();
    }

    private void initView() {
        reply_receiver = (EditText) findViewById(R.id.reply_receiver);
        reply_description = (EditText) findViewById(R.id.reply_description);
        gridView = (GridViewForScrollView) findViewById(R.id.gridView);
        send_msg = (TextView) findViewById(R.id.send_msg);
        uploadImageAdapter = new UploadImageAdapter(this, images);
        uploadImageAdapter.setImagePickerListener(this);
        gridView.setAdapter(uploadImageAdapter);

        setTitle("发私信");
        setBack();
        setCommonShow();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            String uid = getIntent().getExtras().getString(KEY_SEND_UID);
            reply_receiver.setText(uid);
            InputUtils.setInputEditEnd(reply_receiver);
        }
        images.add(new UploadImageBean(false, null));
        uploadImageAdapter.notifyDataSetChanged();
    }

    public void startPicturePicker() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(400)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            /*mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            if (mSelected != null && mSelected.size() > 0) {
                for (int i = 0; i < mSelected.size(); i++) {
                    UploadImageBean bean = new UploadImageBean(true, mSelected.get(i));
                    images.add(bean);
                }
            }*/
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() != null) {
            mSelected = getIntent().getParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION);
            //File file = new File(FileUtils.getRealFilePathByUri(this, mSelected.get(0)));
            if(mSelected != null) {
                for (int i = 0; i < mSelected.size(); i++) {
                    OssUploadImageHelper.uploadImage(this,
                            FileUtils.getRealFilePathByUri(this, mSelected.get(i)),
                            new UploadImageListener() {
                                @Override
                                public void uploadSuccess(String imageUrl) {
                                    uploadImages.add(imageUrl);
                                }
                            });
                }
                if (mSelected.size() > 0) {
                    if (images != null && images.size() > 0) {
                        images.remove(images.size() - 1);
                    }
                    for (int i = 0; i < mSelected.size(); i++) {
                        if (images.size() < 9) {
                            UploadImageBean bean = new UploadImageBean(true, mSelected.get(i), this);
                            images.add(bean);
                        }
                    }
                    if (images.size() < 9) {
                        images.add(new UploadImageBean(false, null));
                    }
                    uploadImageAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    @Override
    public void openImagePicker() {
        startPicturePicker();
    }

    @Override
    public void removeSelect(int position) {
        if (images != null && images.size() > 0 && position <= images.size() - 1) {
            UploadImageBean bean = images.get(position);
            if (bean != null && !TextUtils.isEmpty(bean.imageUrl)) {
                for (int i = 0; i < uploadImages.size(); i++) {
                    if (bean.imageUrl.equals(uploadImages.get(i))) {
                        uploadImages.remove(i);
                        break;
                    }
                }
            }

            images.remove(position);
            if (getSelectCount(images) == 8) {
                images.add(new UploadImageBean(false, null));
            }
            uploadImageAdapter.notifyDataSetChanged();
        }
    }

    private int getSelectCount(List<UploadImageBean> images) {
        int count = 0;
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).isUpload) {
                count += 1;
            }
        }
        return count;
    }
}

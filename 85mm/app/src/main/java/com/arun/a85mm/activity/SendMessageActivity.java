package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.bean.request.MsgImgRequest;
import com.arun.a85mm.event.UpdateMesDotEvent;
import com.arun.a85mm.event.UpdateSendMsg;
import com.arun.a85mm.helper.OssUploadImageHelper;
import com.arun.a85mm.listener.ImagePickerListener;
import com.arun.a85mm.listener.UploadImageListener;
import com.arun.a85mm.matisse.Matisse;
import com.arun.a85mm.matisse.MimeType;
import com.arun.a85mm.matisse.engine.impl.GlideEngine;
import com.arun.a85mm.matisse.ui.MatisseActivity;
import com.arun.a85mm.presenter.AddMessagePresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView3;
import com.arun.a85mm.widget.GridViewForScrollView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends BaseActivity implements ImagePickerListener, CommonView3 {
    private EditText reply_receiver;
    private EditText reply_description;
    private GridViewForScrollView gridView;
    private TextView image_right;
    public static final String KEY_SEND_UID = "send_uid";
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;
    private static final int REQUEST_CODE_CHOOSE = 1;
    private List<Uri> mSelected;
    //存放需要上传服务器的imageUrl
    private List<MsgImgRequest> uploadImages = new ArrayList<>();
    private AddMessagePresenter addMessagePresenter;

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
        uploadImageAdapter = new UploadImageAdapter(this, images);
        uploadImageAdapter.setImagePickerListener(this);
        gridView.setAdapter(uploadImageAdapter);

        setTitle("发私信");
        setRight();
        setBack();
        setCommonShow();
    }

    private void setRight() {
        image_right = (TextView) findViewById(R.id.image_right);
        if (image_right.getLayoutParams() != null
                && image_right.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            image_right.getLayoutParams().height = DensityUtil.dp2px(this, 22);
            image_right.getLayoutParams().width = DensityUtil.dp2px(this, 46);
            ((RelativeLayout.LayoutParams) image_right.getLayoutParams())
                    .setMargins(DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10));
        }
        image_right.setVisibility(View.VISIBLE);
        image_right.setText("发送");
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        image_right.setBackgroundResource(R.drawable.shape_btn_reply);
        image_right.setTextColor(getResources().getColor(R.color.white));
        image_right.setGravity(Gravity.CENTER);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            String uid = getIntent().getExtras().getString(KEY_SEND_UID);
            if (TextUtils.isEmpty(uid)) {
                reply_receiver.setEnabled(true);
            } else {
                reply_receiver.setEnabled(false);
                reply_receiver.setText(uid);
            }
            //InputUtils.setInputEditEnd(reply_receiver);
        }
        images.add(new UploadImageBean(false, null));
        uploadImageAdapter.notifyDataSetChanged();
        if (addMessagePresenter == null) {
            addMessagePresenter = new AddMessagePresenter(this);
            addMessagePresenter.attachView(this);
        }
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

            if (mSelected != null && mSelected.size() > 0) {
                if (images != null && images.size() > 0) {
                    images.remove(images.size() - 1);
                }
                for (int i = 0; i < mSelected.size(); i++) {
                    if (images.size() < 9) {

                        String key = String.valueOf(System.currentTimeMillis());
                        UploadImageBean bean = new UploadImageBean(true, mSelected.get(i), key);
                        images.add(bean);

                        OssUploadImageHelper.uploadImage(this,
                                FileUtils.getRealFilePathByUri(this, mSelected.get(i)),
                                key,
                                new UploadImageListener() {
                                    @Override
                                    public void uploadSuccess(String key, String imageUrl) {
                                        MsgImgRequest item = new MsgImgRequest(imageUrl);
                                        uploadImages.add(item);

                                        for (int i = 0; i < images.size(); i++) {
                                            if (!TextUtils.isEmpty(key) && key.equals(images.get(i).key)) {
                                                images.get(i).imageUrl = imageUrl;
                                            }
                                        }
                                    }
                                });
                    }
                }
                if (images.size() < 9) {
                    images.add(new UploadImageBean(false, null));
                }
                uploadImageAdapter.notifyDataSetChanged();
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
                    if (uploadImages.get(i) != null) {
                        if (bean.imageUrl.equals(uploadImages.get(i).imageUrl)) {
                            uploadImages.remove(i);
                            break;
                        }
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

    public void sendMessage() {
        if (addMessagePresenter != null) {
            if (TextUtils.isEmpty(reply_receiver.getText())) {
                showTop("请填写收件人");
                return;
            } else if (TextUtils.isEmpty(reply_description.getText()) && (uploadImages == null || uploadImages.size() == 0)) {
                showTop("请填写消息内容或图片");
                return;
            }
            addMessagePresenter.addMessage(userId, reply_receiver.getText().toString(), reply_description.getText().toString(), uploadImages);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addMessagePresenter != null) {
            addMessagePresenter.detachView();
        }
    }

    @Override
    public void refresh(int type, Object data) {
        //showTop("发送成功");
        Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
        onBackPressed();
        EventBus.getDefault().post(new UpdateSendMsg());
    }

}

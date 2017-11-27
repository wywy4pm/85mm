package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.MMApplication;
import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.AmountBean;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.MatisseHelper;
import com.arun.a85mm.helper.OssUploadImageHelper;
import com.arun.a85mm.listener.ImagePickerListener;
import com.arun.a85mm.listener.UploadImageListener;
import com.arun.a85mm.matisse.ui.MatisseActivity;
import com.arun.a85mm.presenter.AddCommunityPresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.InputUtils;
import com.arun.a85mm.utils.PermissionUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.UploadImageUtils;
import com.arun.a85mm.widget.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class AddAmountActivity extends BaseActivity implements ImagePickerListener {

    public TextView image_right;
    public EditText amount_description;
    public EditText edit_amount;
    public GridViewForScrollView gridView;
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;
    private List<Uri> mSelected;
    private static final String INTENT_KEY_DATA = "data";
    public static final String BACK_MODE_ADD_AMOUNT = "add_amount";
    private AmountBean amountBean;

    public static void jumpToAddAmountForResult(Activity context, int requestCode, AmountBean amountBean) {
        Intent intent = new Intent(context, AddAmountActivity.class);
        intent.putExtra(INTENT_KEY_DATA, amountBean);
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_add_amount);
        initView();
        initData();
    }

    private void initView() {
        image_right = (TextView) findViewById(R.id.image_right);
        edit_amount = (EditText) findViewById(R.id.edit_amount);
        amount_description = (EditText) findViewById(R.id.amount_description);
        gridView = (GridViewForScrollView) findViewById(R.id.gridView);
        uploadImageAdapter = new UploadImageAdapter(this, images);
        uploadImageAdapter.setImagePickerListener(this);
        gridView.setAdapter(uploadImageAdapter);

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
        image_right.setText("完成");
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        image_right.setBackgroundResource(R.drawable.shape_btn_reply);
        image_right.setTextColor(getResources().getColor(R.color.white));
        image_right.setGravity(Gravity.CENTER);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAmount();
            }
        });
    }

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(INTENT_KEY_DATA)) {
            amountBean = getIntent().getExtras().getParcelable(INTENT_KEY_DATA);
        }
        if (amountBean != null) {
            amount_description.setText(amountBean.paidText);
            InputUtils.setInputEditEnd(amount_description);
            edit_amount.setText(String.valueOf(amountBean.coin));
            if (amountBean.paidImageList != null && amountBean.paidImageList.size() > 0) {
                images.addAll(amountBean.paidImageList);
            }
        } else {
            images.add(new UploadImageBean(0, null));
        }
        uploadImageAdapter.notifyDataSetChanged();
    }

    private void addAmount() {
        if (TextUtils.isEmpty(amount_description.getText())) {
            showTop("请输入收费内容信息");
            return;
        } else if (TextUtils.isEmpty(edit_amount.getText())) {
            showTop("请输入金币数");
            return;
        }
        String amount = edit_amount.getText().toString();
        String description = amount_description.getText().toString();

        Intent resultIntent = new Intent();
        AmountBean bean = new AmountBean(Integer.parseInt(amount), description, images);
        resultIntent.putExtra(Constant.INTENT_ADD_AMOUNT, bean);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
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

                        String realFilePath = FileUtils.getRealFilePathByUri(this, mSelected.get(i));
                        String fileType = FileUtils.getFileTypeByPath(realFilePath);
                        String objectKey = MMApplication.OSS_UPLOAD_IMAGE_FOLDER + userId + "_" + System.currentTimeMillis() + "." + fileType;

                        UploadImageBean bean = new UploadImageBean(1, mSelected.get(i));
                        bean.imageUrl = MMApplication.IMAGE_URL_BASE + objectKey;
                        images.add(bean);

                        OssUploadImageHelper.uploadImage(realFilePath, objectKey,
                                new UploadImageListener() {
                                    @Override
                                    public void uploadPrepare(String imageUrl) {
                                    }

                                    @Override
                                    public void uploadSuccess(String imageUrl) {
                                        Log.d("TAG", "imageUrl = " + imageUrl);
                                    }
                                });
                    }
                }
                if (images.size() < 9) {
                    images.add(new UploadImageBean(0, null));
                }
                uploadImageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void openImagePicker() {
        if (FileUtils.hasSdcard() && PermissionUtils.hasPermission(this, PermissionUtils.WRITE_EXTERNAL_STORAGE)) {
            MatisseHelper.startPicturePicker(this, BACK_MODE_ADD_AMOUNT);
        } else {
            showTop("请开启sd卡存储权限");
        }
    }

    @Override
    public void removeSelect(int position) {
        if (images != null && images.size() > 0 && position <= images.size() - 1) {
            images.remove(position);
            if (UploadImageUtils.getSelectCount(images) == 8) {
                images.add(new UploadImageBean(0, null));
            }
            uploadImageAdapter.notifyDataSetChanged();
        }
    }
}

package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.a85mm.MMApplication;
import com.arun.a85mm.R;
import com.arun.a85mm.adapter.UploadImageAdapter;
import com.arun.a85mm.bean.AmountBean;
import com.arun.a85mm.bean.UploadImageBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.helper.MatisseHelper;
import com.arun.a85mm.helper.OssUploadImageHelper;
import com.arun.a85mm.helper.ShowTopToastHelper;
import com.arun.a85mm.listener.ImagePickerListener;
import com.arun.a85mm.listener.UploadImageListener;
import com.arun.a85mm.matisse.ui.MatisseActivity;
import com.arun.a85mm.presenter.AddCommunityPresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FileUtils;
import com.arun.a85mm.utils.PermissionUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.UploadImageUtils;
import com.arun.a85mm.view.CommonView3;
import com.arun.a85mm.widget.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class AddCommunityActivity extends BaseActivity implements ImagePickerListener, CommonView3 {

    private TextView image_right;
    private EditText community_description;
    private EditText community_title;
    private TextView text_add_amount;
    private GridViewForScrollView gridView;
    private List<UploadImageBean> images = new ArrayList<>();
    private UploadImageAdapter uploadImageAdapter;
    private List<Uri> mSelected;
    private AddCommunityPresenter presenter;
    public static final String BACK_MODE_ADD_COMMUNITY = "add_community";
    private AmountBean amountBean;

    public static void jumpToAddCommunity(Context context) {
        Intent intent = new Intent(context, AddCommunityActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToAddCommunityForResult(Context context) {
        Intent intent = new Intent(context, AddCommunityActivity.class);
        ((Activity) context).startActivityForResult(intent, Constant.REQUEST_CODE_ASSOCIATE_MAIN);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_add_community);
        initView();
        initData();
    }

    private void initView() {
        image_right = (TextView) findViewById(R.id.image_right);
        text_add_amount = (TextView) findViewById(R.id.text_add_amount);
        community_title = (EditText) findViewById(R.id.community_title);
        community_description = (EditText) findViewById(R.id.community_description);
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
        image_right.setText("发布");
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        image_right.setBackgroundResource(R.drawable.shape_btn_reply);
        image_right.setTextColor(getResources().getColor(R.color.white));
        image_right.setGravity(Gravity.CENTER);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommunity();
            }
        });
    }

    private void initData() {
        images.add(new UploadImageBean(0, null));
        uploadImageAdapter.notifyDataSetChanged();

        presenter = new AddCommunityPresenter(this);
        presenter.attachView(this);
    }

    private void addCommunity() {
        if (TextUtils.isEmpty(community_title.getText())) {
            showTop("请填写标题");
            return;
        } else if (TextUtils.isEmpty(community_description.getText())) {
            showTop("请填写描述");
            return;
        }
        String title = community_title.getText().toString();
        String description = community_description.getText().toString();
        if (presenter != null) {
            presenter.addCommunity(title, description, UploadImageUtils.getUploadImages(images), amountBean);
        }
    }

    @Override
    public void openImagePicker() {
        if (FileUtils.hasSdcard() && PermissionUtils.hasPermission(this, PermissionUtils.WRITE_EXTERNAL_STORAGE)) {
            MatisseHelper.startPicturePicker(this, BACK_MODE_ADD_COMMUNITY);
        } else {
            showTop("请开启sd卡存储权限");
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
    public void removeSelect(int position) {
        if (images != null && images.size() > 0 && position <= images.size() - 1) {
            images.remove(position);
            if (UploadImageUtils.getSelectCount(images) == 8) {
                images.add(new UploadImageBean(0, null));
            }
            uploadImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refresh(int type, Object data) {
        //Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {
        ShowTopToastHelper.showTopToastView(this, "发布失败，请重试", R.color.red);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    public void jumpToAddAmount(View view) {
        AddAmountActivity.jumpToAddAmountForResult(this, Constant.REQUEST_CODE_ADD_AMOUNT, amountBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_ADD_AMOUNT) {
                if (data != null && data.getExtras() != null) {
                    amountBean = data.getExtras().getParcelable(Constant.INTENT_ADD_AMOUNT);
                    if (amountBean != null) {
                        text_add_amount.setText("编辑收费内容");
                    } else {
                        text_add_amount.setText("添加收费内容");
                    }
                } else {
                    amountBean = null;
                    text_add_amount.setText("添加收费内容");
                }
            }
        }
    }
}

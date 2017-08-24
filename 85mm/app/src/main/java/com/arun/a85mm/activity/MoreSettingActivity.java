package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.ContactDialog;
import com.arun.a85mm.event.UpdateProductEvent;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.presenter.MorePresenter;
import com.arun.a85mm.utils.CacheUtils;
import com.arun.a85mm.utils.DataCleanManager;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DrawableUtils;
import com.arun.a85mm.utils.GlideCircleTransform;
import com.arun.a85mm.utils.OtherAppStartUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.SystemServiceUtils;
import com.arun.a85mm.view.CommonView3;
import com.arun.a85mm.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener, CommonView3 {

    private TextView cache_size;
    private RelativeLayout layout_share, layout_clear, layout_user_info, layout_my_tags;
    private ImageView user_head;
    private TextView user_name;
    //private ListView configListView;
    //private ConfigAdapter configAdapter;
    private List<String> texts = new ArrayList<>();
    private ImageView more_detail;
    private SwitchCompat switchView;
    private MorePresenter morePresenter;
    private int hideReadEnable = 0;
    private ContactDialog contactDialog;
    private UserInfoBean userInfoBean;

    public static void jumpToMoreSettingActivity(Context context) {
        Intent intent = new Intent(context, MoreSettingActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initView();
        initData();
    }

    private void initView() {
        layout_share = (RelativeLayout) findViewById(R.id.layout_share);
        layout_clear = (RelativeLayout) findViewById(R.id.layout_clear);
        cache_size = (TextView) findViewById(R.id.cache_size);
        more_detail = (ImageView) findViewById(R.id.more_detail);
        switchView = (SwitchCompat) findViewById(R.id.switchView);
        layout_user_info = (RelativeLayout) findViewById(R.id.layout_user_info);
        layout_my_tags = (RelativeLayout) findViewById(R.id.layout_my_tags);
        user_head = (ImageView) findViewById(R.id.user_head);
        user_name = (TextView) findViewById(R.id.user_name);
        /*switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morePresenter != null) {
                    morePresenter.setHideReadStatus(userId, switchView.isChecked() ? 1 : 0);
                }
            }
        });*/
        switchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //阻止手指离开时onTouch方法的继续执行
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (hideReadEnable == 1) {//有隐藏已读权限
                        if (eventStatisticsHelper != null) {
                            eventStatisticsHelper.recordUserAction(MoreSettingActivity.this, EventConstant.HIDE_READ);
                        }
                        switchView.setChecked(!switchView.isChecked());
                        if (morePresenter != null) {
                            morePresenter.setHideReadStatus(switchView.isChecked() ? 1 : 0);
                        }
                    } else {
                        showHideReadDialog();
                    }
                    return true;
                }
                return true;
            }
        });
        /*configListView = (ListView) findViewById(R.id.configListView);
        configAdapter = new ConfigAdapter(this, texts);
        configListView.setAdapter(configAdapter);*/

        layout_share.setOnClickListener(this);
        layout_clear.setOnClickListener(this);
        layout_user_info.setOnClickListener(this);
        layout_my_tags.setOnClickListener(this);
        setHeadTitle(SharedPreferencesUtils.getUid(this));
        setBack();
        setCommonShow();
    }

    private void setHeadTitle(String uid) {
        setTitle("欢迎你，我们的第" + uid + "号用户");
    }

    private void showHideReadDialog() {
        if (contactDialog == null) {
            contactDialog = new ContactDialog(this, R.style.CustomDialog);
            contactDialog.setCanceledOnTouchOutside(true);
        }
        contactDialog.show();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        hideReadEnable = SharedPreferencesUtils.getConfigInt(MoreSettingActivity.this, SharedPreferencesUtils.KEY_HIDE_READ_ENABLED);
        if (hideReadEnable == 1
                && SharedPreferencesUtils.getConfigInt(this, SharedPreferencesUtils.KEY_HIDE_READ_OPENED) == 1) {
            switchView.setChecked(true);
        } else {
            switchView.setChecked(false);
        }
        cache_size.setText(DataCleanManager.getImageCacheSize(this));
        /*String moreImageUrl = SharedPreferencesUtils.getMoreImage(this);
        Glide.with(this).load(moreImageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(more_detail);*/
        Bitmap bitmap = CacheUtils.getBitmap(this, CacheUtils.KEY_BITMAP_CONFIG);
        if (bitmap != null) {
            more_detail.setImageBitmap(bitmap);
        }
        morePresenter = new MorePresenter(this);
        morePresenter.attachView(this);

        setUser();
    }

    private void showShare() {
        onEvent(EventConstant.BTN_SHARE);
        ShareWindow.show(this, getString(R.string.share_title), getString(R.string.share_description), getString(R.string.share_url), "", eventStatisticsHelper);
    }

    private void clearCache() {
        onEvent(EventConstant.BTN_CLEAR_CACHE);
        DataCleanManager.clearImageAllCache(this);
        cache_size.setText(DataCleanManager.getImageCacheSize(this));
        showTop("清除缓存成功");
    }

    public void openWeChat(View view) {
        onEvent(EventConstant.BTN_OPEN_WECHAT);
        OtherAppStartUtils.jumpToWeChat(this);
    }

    public void copyWeChat(View view) {
        SystemServiceUtils.copyText(this, getString(R.string.wechat_num));
        showTop("复制成功");
    }

    private void setUser() {
        userInfoBean = UserManager.getInstance().getUserInfoBean();
        if (UserManager.getInstance() != null) {
            if (!UserManager.getInstance().isLogin()) {
                setNotLogin();
            } else {
                setLogin(userInfoBean);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeadTitle(SharedPreferencesUtils.getUid(this));
        setUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_share:
                showShare();
                break;
            case R.id.layout_clear:
                clearCache();
                break;
            case R.id.layout_user_info:
                if (UserManager.getInstance() != null) {
                    if (!UserManager.getInstance().isLogin()) {
                        LoginActivity.jumpToLoginForResult(this);
                    } else {
                        UserInfoActivity.jumpToUserInfo(this);
                    }
                }
                break;
            case R.id.layout_my_tags:
                TagsActivity.jumpToMyTags(this);
                break;
        }
    }

    private void setNotLogin() {
        Glide.with(this)
                .load(R.mipmap.default_avatar)
                .placeholder(R.mipmap.default_avatar)
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(this))
                .into(user_head);
        user_name.setText("点击登录注册");
        user_name.setBackgroundResource(R.drawable.shape_btn_circle_stroke);
        user_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        user_name.setPadding(DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 8), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 8));
    }

    private void setLogin(UserInfoBean bean) {
        if (bean != null) {
            GradientDrawable drawable = DrawableUtils.getHeadBgDrawable(user_head);
            Glide.with(this)
                    .load(bean.headUrl)
                    .placeholder(drawable)
                    .error(drawable)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            user_head.setImageResource(R.mipmap.default_avatar);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .bitmapTransform(new GlideCircleTransform(this))
                    .into(user_head);
            user_name.setText(bean.name);
            user_name.setBackgroundColor(getResources().getColor(R.color.white));
            user_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            user_name.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refresh(int type, Object data) {
        if (type == MorePresenter.TYPE_HIDE_READ) {
            int isOpen = switchView.isChecked() ? 1 : 0;
            SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_HIDE_READ_OPENED, isOpen);
            EventBus.getDefault().post(new UpdateProductEvent());
        }
    }

    @Override
    public void onError(int errorType, @StringRes int errorMsg) {
        super.onError(errorType, errorMsg);
        switchView.setChecked(!switchView.isChecked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (morePresenter != null) {
            morePresenter.detachView();
        }
    }
}

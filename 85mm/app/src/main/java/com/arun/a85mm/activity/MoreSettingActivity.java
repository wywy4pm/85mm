package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.AllUserBodyBean;
import com.arun.a85mm.bean.MenuListBean;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.ContactDialog;
import com.arun.a85mm.event.UpdateProductEvent;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.presenter.MorePresenter;
import com.arun.a85mm.utils.AppUtils;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreSettingActivity extends BaseActivity implements View.OnClickListener, CommonView3 {

    private TextView cache_size, current_server;
    private RelativeLayout layout_share, layout_clear, layout_my_tags, layout_change_server, layout_user_info;
    private LinearLayout custom_menu;
    private View line_custom_menu;
    private ImageView user_head;
    private TextView user_name;
    private ImageView more_detail;
    private SwitchCompat switchView;
    private SwitchCompat switchView_wImg;
    private MorePresenter morePresenter;
    private int hideReadEnable = 0;
    private ContactDialog contactDialog;
    private UserInfoBean userInfoBean;
    private int currentServer = 1;

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
        current_server = (TextView) findViewById(R.id.current_server);
        more_detail = (ImageView) findViewById(R.id.more_detail);
        switchView = (SwitchCompat) findViewById(R.id.switchView);
        switchView_wImg = (SwitchCompat) findViewById(R.id.switchView_wImg);
        layout_user_info = (RelativeLayout) findViewById(R.id.layout_user_info);
        layout_my_tags = (RelativeLayout) findViewById(R.id.layout_my_tags);
        layout_change_server = (RelativeLayout) findViewById(R.id.layout_change_server);
        custom_menu = (LinearLayout) findViewById(R.id.custom_menu);
        line_custom_menu = findViewById(R.id.line_custom_menu);
        user_head = (ImageView) findViewById(R.id.user_head);
        user_name = (TextView) findViewById(R.id.user_name);
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

        switchView_wImg.setChecked(ConfigHelper.isShowWImage == Constant.VALUE_SHOW_WIMAGE);
        switchView_wImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConfigHelper.isShowWImage = Constant.VALUE_SHOW_WIMAGE;
                    SharedPreferencesUtils.setConfigInt(MoreSettingActivity.this, SharedPreferencesUtils.KEY_WIDTH_IMAGE, Constant.VALUE_SHOW_WIMAGE);
                } else {
                    ConfigHelper.isShowWImage = Constant.VALUE_SHOW_COMMON;
                    SharedPreferencesUtils.setConfigInt(MoreSettingActivity.this, SharedPreferencesUtils.KEY_WIDTH_IMAGE, Constant.VALUE_SHOW_COMMON);
                }
                //showTop("操作成功");
            }
        });

        layout_share.setOnClickListener(this);
        layout_clear.setOnClickListener(this);
        layout_my_tags.setOnClickListener(this);
        layout_user_info.setOnClickListener(this);
        setHeadTitle(SharedPreferencesUtils.getUid(this));
        setBack();
        setCommonShow();
        if (AppUtils.isApkDebug(this)) {
            layout_change_server.setVisibility(View.VISIBLE);
        } else {
            layout_change_server.setVisibility(View.GONE);
        }
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
        Bitmap bitmap = CacheUtils.getBitmap(this, CacheUtils.KEY_BITMAP_CONFIG);
        if (bitmap != null) {
            more_detail.setImageBitmap(bitmap);
        }
        morePresenter = new MorePresenter(this);
        morePresenter.attachView(this);
        morePresenter.getUserInfo();

        setUser();
        currentServer = SharedPreferencesUtils.getConfigInt(this, SharedPreferencesUtils.KEY_SERVER);
        if (currentServer == Constant.SERVER_TYPE_TEST) {
            current_server.setText("测试服务器");
        } else {
            current_server.setText("正式服务器");
        }
    }

    private void setCustomMenu(List<MenuListBean> customMenuList) {
        if (customMenuList != null && customMenuList.size() > 0) {
            custom_menu.removeAllViews();
            custom_menu.setVisibility(View.VISIBLE);
            line_custom_menu.setVisibility(View.VISIBLE);
            for (int i = 0; i < customMenuList.size(); i++) {
                final MenuListBean bean = customMenuList.get(i);
                if (bean != null) {
                    View itemMenu = LayoutInflater.from(this).inflate(R.layout.layout_custom_menu_item, custom_menu, false);
                    TextView text_custom_menu = (TextView) itemMenu.findViewById(R.id.text_custom_menu);
                    View line_divide = itemMenu.findViewById(R.id.line_menu_bottom);
                    text_custom_menu.setText(bean.showName);
                    itemMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bean.dataType == 7) {//个人主页已发布作品列表
                                Map<String, String> map = new HashMap<>();
                                map.put(ProductionFragment.INTENT_KEY_TYPE, String.valueOf(bean.dataType));
                                FragmentCommonActivity.jumpToFragmentCommonActivity(MoreSettingActivity.this,
                                        FragmentCommonActivity.FRAGMENT_LATEST_WORKS, bean.showName, map);
                            } else {
                                if (!TextUtils.isEmpty(bean.url)) {
                                    UrlJumpHelper.urlJumpTo(MoreSettingActivity.this, bean.url, "我的金币");
                                }
                            }
                        }
                    });
                    if (i == customMenuList.size() - 1) {
                        line_divide.setVisibility(View.GONE);
                    } else {
                        line_divide.setVisibility(View.VISIBLE);
                    }
                    custom_menu.addView(itemMenu);
                }
            }
        } else {
            custom_menu.setVisibility(View.GONE);
            line_custom_menu.setVisibility(View.GONE);
        }
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
            case R.id.layout_change_server:
                if (currentServer == Constant.SERVER_TYPE_TEST) {
                    SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_SERVER, Constant.SERVER_TYPE_PROD);
                } else {
                    SharedPreferencesUtils.setConfigInt(this, SharedPreferencesUtils.KEY_SERVER, Constant.SERVER_TYPE_TEST);
                }
                showTop("切换成功，请清除进程后重启app");
                //AppUtils.restartApp(this);
                break;
           /* case R.id.layout_my_coins:
                WebViewActivity.jumpToWebViewActivity(this, "", WebViewActivity.TYPE_MY_COIN, "我的金币");
                break;*/
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
        if (type == MorePresenter.TYPE_USER_INFO) {
            if (data instanceof AllUserBodyBean) {
                AllUserBodyBean bean = (AllUserBodyBean) data;
                if (bean.userInfo != null) {
                    setCustomMenu(bean.userInfo.customMenuList);
                }
            }
        } else if (type == MorePresenter.TYPE_HIDE_READ) {
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

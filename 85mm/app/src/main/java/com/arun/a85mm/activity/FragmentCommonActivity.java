package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.fragment.LeftWorksFragment;
import com.arun.a85mm.fragment.OneWorkFragment;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.utils.StatusBarUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.Serializable;
import java.util.Map;

public class FragmentCommonActivity extends BaseActivity {

    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String EXTRAS = "extras";
    public static final String BACK_MODE = "back";
    public static final String FRAGMENT_LEFT_WORKS = "fragment_left_works";
    public static final String FRAGMENT_ONE_WORK = "fragment_one_work";
    public static final int BACK_MODE_MAIN = 1;
    public static final int BACK_MODE_COM = 0;
    //public static final String FRAGMENT_SEND_MESSAGE = "fragment_send_message";
    public String title;
    private String type = "";
    private Fragment fragment;
    private int backMode = 1;

    public static void jumpToFragmentCommonActivity(Context context, String type, String title, Map<String, String> extras) {
        Intent intent = new Intent(context, FragmentCommonActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(TITLE, title);
        if (extras != null) {
            intent.putExtra(EXTRAS, (Serializable) extras);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToFragmentCommonActivity(Context context, String type, String title, Map<String, String> extras, int backMode) {
        Intent intent = new Intent(context, FragmentCommonActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(TITLE, title);
        intent.putExtra(BACK_MODE, backMode);
        if (extras != null) {
            intent.putExtra(EXTRAS, (Serializable) extras);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToFragmentCommonActivity(Context context, String type, Map<String, String> extras) {
        Intent intent = new Intent(context, FragmentCommonActivity.class);
        intent.putExtra(TYPE, type);
        if (extras != null) {
            if (extras.containsKey(TITLE)) {
                String title = extras.get(TITLE);
                intent.putExtra(TITLE, title);
            }
            //extras.remove(TITLE);
            intent.putExtra(EXTRAS, (Serializable) extras);
        }
        if (FRAGMENT_ONE_WORK.equals(type)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);
        new SystemBarTintManager(this).setStatusBarTintEnabled(true);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        Map<String, String> map = null;
        if (getIntent() != null) {
            type = getIntent().getExtras().getString(TYPE);
            title = getIntent().getExtras().getString(TITLE);
            backMode = getIntent().getExtras().getInt(BACK_MODE);
            map = (Map<String, String>) getIntent().getExtras().getSerializable(EXTRAS);
            initFragment(type, map);
        }
        initView();
        setSaveImage(true);
    }

    private void initView() {
        RelativeLayout layout_title_bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
        ImageView image_back = (ImageView) findViewById(R.id.image_back);
        TextView titleText = (TextView) findViewById(R.id.title);
        if (layout_title_bar != null) {
            if (!TextUtils.isEmpty(title)) {
                layout_title_bar.setVisibility(View.VISIBLE);
                if (FRAGMENT_ONE_WORK.equals(type)
                        && backMode == BACK_MODE_MAIN) {
                    setSwipeBackEnable(false);
                    setBackToMain(image_back);
                } else {
                    setBack(image_back);
                    setSwipeBackEnable(true);
                }

                if (titleText != null) {
                    titleText.setText(title);
                }
            } else {
                layout_title_bar.setVisibility(View.GONE);
            }
        }
    }

    public void setShowBottomRight(final String linkUrl, final String workId) {
        ImageView image_right = (ImageView) findViewById(R.id.image_right);
        if (image_right != null) {
            image_right.setVisibility(View.VISIBLE);
            image_right.setImageResource(R.mipmap.ic_home_more);
            image_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper.showBottomSourceLink(FragmentCommonActivity.this, linkUrl, workId, eventStatisticsHelper);
                }
            });
        }
    }

    public void setShowBottomRight(final String linkUrl, final String workId, final String type, final String authorUid) {
        ImageView image_right = (ImageView) findViewById(R.id.image_right);
        if (image_right != null) {
            image_right.setVisibility(View.VISIBLE);
            image_right.setImageResource(R.mipmap.ic_home_more);
            image_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper.showBottomSourceLink(FragmentCommonActivity.this, linkUrl, workId, eventStatisticsHelper, type, authorUid);
                }
            });
        }
    }

    private void initFragment(String type, Map<String, String> map) {
        Fragment fragment = createFragment(type);
        putData(map, fragment);
        setFragment(fragment);
    }

    private void putData(Map<String, String> map, Fragment fragment) {
        if (map != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRAS, (Serializable) map);
            if (fragment != null) {
                fragment.setArguments(bundle);
            }
        }
    }

    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragmentTransaction != null) {
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commit();
                }
            }
        }
    }

    private Fragment createFragment(String type) {
        fragment = null;
        if (FRAGMENT_LEFT_WORKS.equals(type)) {
            fragment = new LeftWorksFragment();
        } else if (FRAGMENT_ONE_WORK.equals(type)) {
            fragment = new OneWorkFragment();
        }
        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FRAGMENT_ONE_WORK.equals(type)
                    && backMode == BACK_MODE_MAIN) {
                MainActivity.jumpToMain(FragmentCommonActivity.this, MainActivity.INTENT_TYPE_PUSH_BACK);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setBackToMain(ImageView imageView) {
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.jumpToMain(FragmentCommonActivity.this, MainActivity.INTENT_TYPE_PUSH_BACK);
                    finish();
                }
            });
        }
    }
}

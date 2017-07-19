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
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.fragment.TagWorkFragment;
import com.arun.a85mm.utils.StatusBarUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.Serializable;
import java.util.Map;

public class FragmentCommonActivity extends BaseActivity {

    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String EXTRAS = "extras";
    public static final String FRAGMENT_LATEST_WORKS = "fragment_latest_works";
    public static final String FRAGMENT_LEFT_WORKS = "fragment_left_works";
    public static final String FRAGMENT_TAG_WORKS = "fragment_tag_works";
    public String title;
    private String type = "";
    private Fragment fragment;

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

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        Map<String, String> map = null;
        if (getIntent() != null) {
            type = getIntent().getExtras().getString(TYPE);
            title = getIntent().getExtras().getString(TITLE);
            map = (Map<String, String>) getIntent().getExtras().getSerializable(EXTRAS);
            initFragment(type, map);
        }
        initView();
        setSaveImage(false);
    }

    private void initView() {
        RelativeLayout layout_title_bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
        ImageView image_back = (ImageView) findViewById(R.id.image_back);
        TextView titleText = (TextView) findViewById(R.id.title);
        if (layout_title_bar != null) {
            if (!TextUtils.isEmpty(title)) {
                layout_title_bar.setVisibility(View.VISIBLE);
                setBack(image_back);
                if (titleText != null) {
                    titleText.setText(title);
                }
            } else {
                layout_title_bar.setVisibility(View.GONE);
            }

            layout_title_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FRAGMENT_LATEST_WORKS.equals(type)) {
                        if (fragment != null && fragment instanceof ProductionFragment) {
                            ((ProductionFragment) fragment).refreshData();
                        }
                    }
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
        } else if (FRAGMENT_LATEST_WORKS.equals(type)) {
            fragment = ProductionFragment.newInstance();
        } else if (FRAGMENT_TAG_WORKS.equals(type)) {
            fragment = TagWorkFragment.newInstance();
        }
        return fragment;
    }

}

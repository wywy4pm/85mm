package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.fragment.LeftWorksFragment;
import com.arun.a85mm.utils.StatusBarUtils;

import java.io.Serializable;
import java.util.Map;

public class FragmentCommonActivity extends BaseActivity {

    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String EXTRAS = "extras";
    public static final String FRAGMENT_LEFT_WORKS = "fragment_left_works";
    public RelativeLayout layout_title_bar;
    public ImageView image_back;
    public TextView titleText;
    public String title;

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
        if (getIntent() != null) {
            String type = getIntent().getExtras().getString(TYPE);
            title = getIntent().getExtras().getString(TITLE);
            Map<String, String> map = (Map<String, String>) getIntent().getExtras().getSerializable(EXTRAS);
            initFragment(type, map);
        }
        initView();
    }

    private void initView() {
        layout_title_bar = (RelativeLayout) findViewById(R.id.layout_title_bar);
        image_back = (ImageView) findViewById(R.id.image_back);
        titleText = (TextView) findViewById(R.id.title);
        if (!TextUtils.isEmpty(title)) {
            layout_title_bar.setVisibility(View.VISIBLE);
            setBack(image_back);
            titleText.setText(title);
        } else {
            layout_title_bar.setVisibility(View.GONE);
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
        Fragment fragment = null;
        if (FRAGMENT_LEFT_WORKS.equals(type)) {
            fragment = new LeftWorksFragment();
        }
        return fragment;
    }

}

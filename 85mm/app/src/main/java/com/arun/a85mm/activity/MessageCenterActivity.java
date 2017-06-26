package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.CommonFragmentPagerAdapter;
import com.arun.a85mm.fragment.MessageFragment;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.TextViewUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BaseActivity {

    public SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    public ImageView image_right;
    private String[] titles = new String[]{"收件箱", "已发送"};
    private List<Fragment> list = new ArrayList<>();

    public static void jumpToMessageCenter(Context context) {
        Intent intent = new Intent(context, MessageCenterActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initFragment();
        initView();
        setListener();
        initData();
    }

    private void initFragment() {
        MessageFragment receiveMessage = MessageFragment.getInstance(0);
        MessageFragment sendMessage = MessageFragment.getInstance(1);
        list.add(receiveMessage);
        list.add(sendMessage);
    }

    private void initView() {
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        image_right = (ImageView) findViewById(R.id.image_right);
        viewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));

        tabLayout.setViewPager(viewPager, titles);
        TextViewUtils.setTextBold(tabLayout.getTitleView(0), true);
        viewPager.setCurrentItem(0);

        setBack();
        setSaveImage(false);
    }

    private void setListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /*int type = -1;
                if (position == 0) {
                    type = EventConstant.TAB_NEWEST;
                } else if (position == 1) {
                    type = EventConstant.TAB_HOTEST;
                }
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(MessageCenterActivity.this, type, EventStatisticsHelper.createOneActionList(type));
                }*/
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == position) {
                        TextViewUtils.setTextBold(tabLayout.getTitleView(i), true);
                    } else {
                        TextViewUtils.setTextBold(tabLayout.getTitleView(i), false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.jumpToSendMessage(MessageCenterActivity.this, "");
                /*FragmentCommonActivity.jumpToFragmentCommonActivity(MessageCenterActivity.this,
                        FragmentCommonActivity.FRAGMENT_SEND_MESSAGE, "发私信", null);*/
            }
        });
    }

    private void initData() {

    }

}

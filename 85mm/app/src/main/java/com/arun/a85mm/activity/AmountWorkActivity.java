package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.AmountImageAdapter;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.utils.SystemServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class AmountWorkActivity extends BaseActivity implements OnImageClick {

    //private TextView image_right;
    private TextView amount_description, pay_done_tips;
    private RecyclerView imageRecyclerView;
    private AwardBodyBean awardBodyBean;
    private AmountImageAdapter adapter;
    private List<WorkListItemBean> imageList = new ArrayList<>();
    private int type;
    private String titleName;
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_PAY = 1;

    public static void jumpToAmountWork(Context context, int type, String titleName, AwardBodyBean awardBodyBean) {
        Intent intent = new Intent(context, AmountWorkActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, type);
        intent.putExtra(Constant.INTENT_TITLE, titleName);
        intent.putExtra(Constant.INTENT_DATA, awardBodyBean);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_work);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initView();
        initData();
    }

    private void initView() {
        pay_done_tips = (TextView) findViewById(R.id.pay_done_tips);
        amount_description = (TextView) findViewById(R.id.amount_description);
        imageRecyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new AmountImageAdapter(this, imageList);
        adapter.setOnImageClick(this);
        imageRecyclerView.setLayoutManager(manager);
        imageRecyclerView.setAdapter(adapter);
        setCommonShow();
        setSaveImage(false);
    }

    private void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constant.INTENT_TYPE)) {
                type = getIntent().getExtras().getInt(Constant.INTENT_TYPE);
            }
            if (getIntent().getExtras().containsKey(Constant.INTENT_TITLE)) {
                titleName = getIntent().getExtras().getString(Constant.INTENT_TITLE);
                setTitle(titleName);
            }
            if (getIntent().getExtras().containsKey(Constant.INTENT_DATA)) {
                awardBodyBean = (AwardBodyBean) getIntent().getExtras().getSerializable(Constant.INTENT_DATA);
            }
        }

        if (type == TYPE_PAY && awardBodyBean != null
                && awardBodyBean.orderInfo != null) {
            pay_done_tips.setVisibility(View.VISIBLE);
            int payCoins = awardBodyBean.orderInfo.paidCoin;
            int leftCoins = awardBodyBean.orderInfo.buyerLeftCoin;
            pay_done_tips.setText(getResources().getString(R.string.award_pay_done, payCoins, leftCoins));
        } else {
            pay_done_tips.setVisibility(View.GONE);
        }

        if (awardBodyBean != null) {
            if (awardBodyBean.productInfo != null) {
                if (!TextUtils.isEmpty(awardBodyBean.productInfo.paidText)) {
                    amount_description.setText(awardBodyBean.productInfo.paidText);
                }
                imageList.addAll(awardBodyBean.productInfo.paidImageList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /*private void setRight() {
        image_right = (TextView) findViewById(R.id.image_right);
        if (image_right.getLayoutParams() != null
                && image_right.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            image_right.getLayoutParams().height = DensityUtil.dp2px(this, 22);
            image_right.getLayoutParams().width = DensityUtil.dp2px(this, 46);
            ((RelativeLayout.LayoutParams) image_right.getLayoutParams())
                    .setMargins(DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10), DensityUtil.dp2px(this, 10));
        }
        image_right.setVisibility(View.VISIBLE);
        image_right.setText("编辑");
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        image_right.setBackgroundResource(R.drawable.shape_btn_reply);
        image_right.setTextColor(getResources().getColor(R.color.white));
        image_right.setGravity(Gravity.CENTER);
        image_right.setOnClickListener(this);
    }*/

    public void copyText(View view) {
        if (!TextUtils.isEmpty(amount_description.getText())) {
            SystemServiceUtils.copyText(this, amount_description.getText().toString());
            showTop("复制成功");
        }
    }

    @Override
    public void onCoverClick(String workId, String coverUrl, String authorName) {
        saveImageShowTop(workId, coverUrl, authorName);
    }

    @Override
    public void onCountClick(int groupPosition) {
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl, String authorUid) {
    }
}

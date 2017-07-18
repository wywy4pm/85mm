package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.MyTagsAdapter;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.ItemTouchHelperCallback;
import com.arun.a85mm.listener.OnItemTouchCallbackListener;
import com.arun.a85mm.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagsActivity extends BaseActivity implements View.OnClickListener {

    public TextView title;
    public TextView image_right;
    public RecyclerView recyclerView;
    private TextView btn_cancel;
    private TextView btn_confirm;
    private RelativeLayout layout_edit_top;
    private List<UserTagBean> userTags = new ArrayList<>();
    private MyTagsAdapter myTagsAdapter;

    public static void jumpToMyTags(Context context) {
        Intent intent = new Intent(context, TagsActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        initView();
        initData();
    }


    private void initView() {
        title = (TextView) findViewById(R.id.title);
        image_right = (TextView) findViewById(R.id.image_right);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        layout_edit_top = (RelativeLayout) findViewById(R.id.layout_edit_top);
        layout_edit_top.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myTagsAdapter = new MyTagsAdapter(this, userTags);
        recyclerView.setAdapter(myTagsAdapter);
        //创建ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(new OnItemTouchCallbackListener() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (userTags == null) {
                    return false;
                }
                //处理拖动排序
                //使用Collection对数组进行重排序，目的是把我们拖动的Item换到下一个目标Item的位置
                Collections.swap(userTags, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //通知Adapter它的Item发生了移动
                myTagsAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }
        }));
        //并绑定RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setTitle("我的标签");
        setBack();
        setRight();
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
        image_right.setText("编辑");
        image_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        image_right.setBackgroundResource(R.drawable.shape_btn_reply);
        image_right.setTextColor(getResources().getColor(R.color.white));
        image_right.setGravity(Gravity.CENTER);
        image_right.setOnClickListener(this);
    }

    private void initData() {

        for (int i = 0; i < 40; i++) {
            UserTagBean bean = new UserTagBean();
            bean.type = MyTagsAdapter.DATA_TYPE_ITEM;
            bean.id = String.valueOf(System.currentTimeMillis());
            bean.name = "#哈哈哈哈" + i;
            userTags.add(bean);
        }
        //userTags.addAll(ConfigHelper.userTags);
        addBottom();
        myTagsAdapter.notifyDataSetChanged();
    }

    private void addBottom() {
        UserTagBean bottom = new UserTagBean();
        bottom.type = MyTagsAdapter.DATA_TYPE_BOTTOM;
        userTags.add(bottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_right:
                if (myTagsAdapter.isEdit()) {
                    myTagsAdapter.setIsEdit(false);
                } else {
                    myTagsAdapter.setIsEdit(true);
                }
                myTagsAdapter.notifyDataSetChanged();
                break;
        }
    }
}

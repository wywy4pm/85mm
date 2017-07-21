package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.MyTagsAdapter;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.event.UpdateTagEvent;
import com.arun.a85mm.helper.ConfigHelper;
import com.arun.a85mm.helper.ItemTouchHelperCallback;
import com.arun.a85mm.listener.OnItemTouchCallbackListener;
import com.arun.a85mm.presenter.UserTagPresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagsActivity extends BaseActivity implements View.OnClickListener, CommonView3 {

    public TextView title;
    public TextView image_right;
    public RecyclerView recyclerView;
    private TextView btn_cancel;
    private TextView btn_confirm;
    private RelativeLayout layout_edit_top;
    private List<UserTagBean> userTags = new ArrayList<>();
    private MyTagsAdapter myTagsAdapter;
    private UserTagPresenter presenter;

    public static void jumpToMyTags(Context context) {
        Intent intent = new Intent(context, TagsActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        EventBus.getDefault().register(this);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(myTagsAdapter, new OnItemTouchCallbackListener() {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder instanceof MyTagsAdapter.TagItemHolder) {
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
                return false;
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
        presenter = new UserTagPresenter(this);
        presenter.attachView(this);

        List<UserTagBean> list = ConfigHelper.userTags;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                UserTagBean bean = list.get(i);
                if (bean != null) {
                    UserTagBean newBean = new UserTagBean();
                    newBean.name = bean.name;
                    newBean.id = bean.id;
                    newBean.isShow = bean.isShow;
                    newBean.type = MyTagsAdapter.DATA_TYPE_ITEM;
                    userTags.add(newBean);
                }
            }
        }
        addBottom();
        myTagsAdapter.notifyDataSetChanged();
    }

    private void addBottom() {
        UserTagBean bottom = new UserTagBean();
        bottom.type = MyTagsAdapter.DATA_TYPE_BOTTOM;
        userTags.add(bottom);
    }

    private void refreshData() {
        if (presenter != null) {
            if (userTags.size() > 0) {
                presenter.updateUserTag(getUserTags());
            }
        }
    }

    @Override
    public void refresh(int type, Object data) {
        //myTagsAdapter.notifyDataSetChanged();
        ConfigHelper.userTags = getUserTags();
        showTop("操作成功");
    }

    private List<UserTagBean> getUserTags() {
        List<UserTagBean> uploadTags = new ArrayList<>();
        for (int i = 0; i < userTags.size(); i++) {
            if (userTags.get(i) != null
                    && MyTagsAdapter.DATA_TYPE_ITEM.equals(userTags.get(i).type)) {
                uploadTags.add(userTags.get(i));
            }
        }
        return uploadTags;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_right:
                if (userTags.size() > 1) {
                    if (myTagsAdapter.isEdit()) {
                        setCommonMode();
                    } else {
                        setEditMode();
                    }
                }
                break;
            case R.id.btn_cancel:
                setCommonMode();
                break;
            case R.id.btn_confirm:
                refreshData();
                setCommonMode();
                break;
        }
    }

    private void setEditMode() {
        myTagsAdapter.setIsEdit(true);
        showEditTop();
        myTagsAdapter.notifyDataSetChanged();
    }

    private void setCommonMode() {
        myTagsAdapter.setIsEdit(false);
        hideEditTop();
        myTagsAdapter.notifyDataSetChanged();
    }

    private void showEditTop() {
        layout_edit_top.setVisibility(View.VISIBLE);
        if (image_back != null) {
            image_back.setVisibility(View.GONE);
        }
        image_right.setVisibility(View.GONE);
    }

    private void hideEditTop() {
        layout_edit_top.setVisibility(View.GONE);
        if (image_back != null) {
            image_back.setVisibility(View.VISIBLE);
        }
        image_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTag(UpdateTagEvent event) {
        if (event != null && !TextUtils.isEmpty(event.tagName)) {
            boolean isHaveSame = false;
            for (int i = 0; i < userTags.size(); i++) {
                if (userTags.get(i) != null) {
                    if (!TextUtils.isEmpty(userTags.get(i).name)
                            && userTags.get(i).name.equals(event.tagName)) {
                        isHaveSame = true;
                    }
                }
            }
            if (isHaveSame) {
                showTop("已经存在");
            } else {
                if (event.isAdd) {
                    UserTagBean bean = new UserTagBean();
                    bean.type = MyTagsAdapter.DATA_TYPE_ITEM;
                    bean.id = SharedPreferencesUtils.getUid(this) + "_" + String.valueOf(System.currentTimeMillis());
                    bean.name = event.tagName;
                    bean.isShow = 1;
                    userTags.add(0, bean);
                    refreshData();
                } else {
                    if (event.position < userTags.size() - 1) {
                        UserTagBean bean = userTags.get(event.position);
                        if (bean != null) {
                            bean.name = event.tagName;
                        }
                    }
                }
                myTagsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteTagData(String name) {
        if (!TextUtils.isEmpty(name)) {
            for (int i = 0; i < userTags.size(); i++) {
                if (userTags.get(i) != null) {
                    if (!TextUtils.isEmpty(userTags.get(i).name)
                            && userTags.get(i).name.equals(name)) {
                        userTags.remove(i);
                        myTagsAdapter.notifyDataSetChanged();
                        showTop("删除成功");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myTagsAdapter != null && myTagsAdapter.isEdit()) {
                setCommonMode();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

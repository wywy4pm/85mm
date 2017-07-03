package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.OneWorkAdapter;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.presenter.OneWorkPresenter;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyLinearLayoutManager;
import com.arun.a85mm.utils.KeyBoardUtils;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView3;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OneWorkActivity extends BaseActivity implements CommonView3, OnImageClick, View.OnClickListener {

    public static final String FRAGMENT_ONE_WORK = "fragment_one_work";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String EXTRAS = "extras";
    public static final String BACK_MODE = "back";
    private int backMode = 1;
    public static final int BACK_MODE_MAIN = 1;
    public static final int BACK_MODE_COM = 0;
    private RelativeLayout layout_top;
    private ImageView image_back;
    private ImageView image_more;
    private String title;
    //private String type = "";

    private OneWorkPresenter oneWorkPresenter;
    private String workId;
    private String sourceUrl;
    public ImageView author_image;
    public TextView author_name;
    public TextView author_create_time;
    public RelativeLayout work_list_item_author;
    public RecyclerView recyclerView;
    //private LinearLayout bottom_view;
    //private TextView over_size;
    //private TextView recommend_new;
    private RelativeLayout layout_add_comment;
    private EditText edit_add_comment;
    private TextView btn_add_comment;
    private OneWorkAdapter oneWorkAdapter;
    public static final String KEY_TYPE = "audit";
    public static final String TYPE_AUDIT = "1";
    public static final String TYPE_COMMUNITY = "2";
    private List<WorkListItemBean> workListItems = new ArrayList<>();
    private String type;
    private String authorUid = "";

    public static void jumpToOneWorkActivity(Context context, String type, String title, Map<String, String> extras, int backMode) {
        Intent intent = new Intent(context, OneWorkActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(TITLE, title);
        intent.putExtra(BACK_MODE, backMode);
        if (extras != null) {
            intent.putExtra(EXTRAS, (Serializable) extras);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static void jumpToOneWorkActivity(Context context, String type, Map<String, String> extras) {
        Intent intent = new Intent(context, OneWorkActivity.class);
        intent.putExtra(TYPE, type);
        if (extras != null) {
            if (extras.containsKey(TITLE)) {
                String title = extras.get(TITLE);
                intent.putExtra(TITLE, title);
            }
            intent.putExtra(EXTRAS, (Serializable) extras);
        }
        if (FRAGMENT_ONE_WORK.equals(type)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_one_work);
        new SystemBarTintManager(this).setStatusBarTintEnabled(true);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        initView();
        initData();
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        if (getIntent() != null) {
            type = getIntent().getExtras().getString(TYPE);
            title = getIntent().getExtras().getString(TITLE);
            backMode = getIntent().getExtras().getInt(BACK_MODE);
            Map<String, String> map = (Map<String, String>) getIntent().getExtras().getSerializable(EXTRAS);
            if (map != null) {
                workId = map.get(UrlJumpHelper.WORK_ID);
                type = map.get(KEY_TYPE);
            }
        }

        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        image_back = (ImageView) findViewById(R.id.image_back);
        image_more = (ImageView) findViewById(R.id.image_more);
        author_image = (ImageView) findViewById(R.id.author_image);
        author_name = (TextView) findViewById(R.id.author_name);
        author_create_time = (TextView) findViewById(R.id.author_create_time);
        work_list_item_author = (RelativeLayout) findViewById(R.id.work_list_item_author);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //bottom_view = (LinearLayout) findViewById(R.id.bottom_view);
        //over_size = (TextView) findViewById(R.id.over_size);
        //recommend_new = (TextView) findViewById(R.id.recommend_new);
        layout_add_comment = (RelativeLayout) findViewById(R.id.layout_add_comment);
        edit_add_comment = (EditText) findViewById(R.id.edit_add_comment);
        btn_add_comment = (TextView) findViewById(R.id.btn_add_comment);
        //over_size.setOnClickListener(this);
        //recommend_new.setOnClickListener(this);
        btn_add_comment.setOnClickListener(this);
        edit_add_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (UserManager.getInstance() != null) {
                        if (!UserManager.getInstance().isLogin()) {
                            LoginActivity.jumpToLoginForResult(OneWorkActivity.this);
                        }
                    }
                }
            }
        });

        if (layout_top.getLayoutParams() != null && layout_top.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layout_top.getLayoutParams()).setMargins(0, DensityUtil.getStatusHeight(this), 0, 0);
        }

        oneWorkAdapter = new OneWorkAdapter(this, workListItems);
        oneWorkAdapter.setOnImageClick(this);
        recyclerView.setAdapter(oneWorkAdapter);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        if (FRAGMENT_ONE_WORK.equals(type)
                && backMode == BACK_MODE_MAIN) {
            setSwipeBackEnable(false);
            setBackToMain(image_back);
        } else {
            setBack(image_back);
            setSwipeBackEnable(true);
        }

        setSaveImage(true);
    }

    private void initData() {

        if (TYPE_AUDIT.equals(type)) {
        } else if (TYPE_COMMUNITY.equals(type)) {
            layout_add_comment.setVisibility(View.VISIBLE);
        } else {
            layout_add_comment.setVisibility(View.GONE);
        }
        oneWorkPresenter = new OneWorkPresenter(this);
        oneWorkPresenter.attachView(this);
        refreshData();
    }

    private void refreshData() {
        if (oneWorkPresenter != null) {
            oneWorkPresenter.getOneWorkDetail(workId);
        }
    }

    @Override
    public void refresh(int dataType, Object data) {
        if (dataType == OneWorkPresenter.TYPE_DETAIL && data instanceof WorkListBean) {
            WorkListBean bean = (WorkListBean) data;
            sourceUrl = bean.sourceUrl;

            authorUid = bean.uid;
            if (TYPE_COMMUNITY.equals(type)) {
                setShowBottomRight(sourceUrl, workId, TYPE_COMMUNITY, authorUid);
            } else {
                setShowBottomRight(sourceUrl, workId);
            }

            workListItems.clear();
            oneWorkAdapter.setWorkListBean(bean);

            addCoverImage(bean);
            addImage(bean.workDetail);

            addHead(bean);

            if (TYPE_COMMUNITY.equals(type)) {
                addDescription(bean);
                addComments(bean);
            }
            oneWorkAdapter.notifyDataSetChanged();
        } else if (dataType == OneWorkPresenter.TYPE_ADD_COMMENT) {
            showTop("评论成功");
            refreshData();
        }
    }

    public void setShowBottomRight(final String linkUrl, final String workId) {
        image_more.setVisibility(View.VISIBLE);
        image_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showBottomSourceLink(OneWorkActivity.this, linkUrl, workId, eventStatisticsHelper);
            }
        });
    }

    public void setShowBottomRight(final String linkUrl, final String workId, final String type, final String authorUid) {
        image_more.setVisibility(View.VISIBLE);
        image_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showBottomSourceLink(OneWorkActivity.this, linkUrl, workId, eventStatisticsHelper, type, authorUid);
            }
        });
    }

    private void addHead(WorkListBean bean) {
        WorkListItemBean itemHeadBean = new WorkListItemBean();
        itemHeadBean.type = OneWorkAdapter.DATA_TYPE_HEAD;
        itemHeadBean.authorHeadImg = bean.authorHeadImg;
        itemHeadBean.authorName = bean.authorName;
        itemHeadBean.createTime = bean.createTime;
        workListItems.add(itemHeadBean);
    }

    private void addCoverImage(WorkListBean bean) {
        WorkListItemBean itemBean = new WorkListItemBean();
        itemBean.type = OneWorkAdapter.DATA_TYPE_IMAGE;
        itemBean.imageUrl = bean.coverUrl;
        itemBean.width = bean.coverWidth;
        itemBean.height = bean.coverHeight;
        itemBean.backgroundColor = RandomColorHelper.getRandomColor();
        workListItems.add(itemBean);
    }

    private void addImage(List<WorkListItemBean> list) {
        for (int i = 0; i < list.size(); i++) {
            WorkListItemBean bean = list.get(i);
            bean.type = OneWorkAdapter.DATA_TYPE_IMAGE;
            bean.backgroundColor = RandomColorHelper.getRandomColor();
        }
        workListItems.addAll(list);
    }

    private void addDescription(WorkListBean bean) {
        WorkListItemBean itemDesBean = new WorkListItemBean();
        itemDesBean.type = OneWorkAdapter.DATA_TYPE_DESCRIPTION;
        itemDesBean.workTitle = bean.workTitle;
        itemDesBean.description = bean.description;
        workListItems.add(itemDesBean);
    }

    private void addComments(WorkListBean bean) {
        WorkListItemBean itemComBean = new WorkListItemBean();
        itemComBean.type = OneWorkAdapter.DATA_TYPE_COMMENTS;
        itemComBean.comments = bean.comments;
        workListItems.add(itemComBean);
    }

    @Override
    public void onCountClick(int groupPosition) {

    }

    @Override
    public void onCoverClick(String workId, String coverUrl, int width, int height) {
        saveImageShowTop(workId, coverUrl, width, height);
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl) {
        if (TYPE_COMMUNITY.equals(type)) {
            DialogHelper.showBottomSourceLink(this, sourceUrl, workId, eventStatisticsHelper, type, authorUid);
        } else {
            DialogHelper.showBottomSourceLink(this, sourceUrl, workId, eventStatisticsHelper);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.recommend_new:
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(this, EventConstant.WORK_AUDIT_RECOMMEND, workId, "");
                }
                break;
            case R.id.over_size:
                if (eventStatisticsHelper != null) {
                    eventStatisticsHelper.recordUserAction(this, EventConstant.WORK_SCALE_OVER, workId, "");
                }
                break;*/
            case R.id.btn_add_comment:
                if (TextUtils.isEmpty(edit_add_comment.getText())) {
                    showTop("请输入评论内容");
                    return;
                }
                if (oneWorkPresenter != null) {
                    oneWorkPresenter.addComment(workId, edit_add_comment.getText().toString());
                }
                //发送成功后关闭软键盘
                KeyBoardUtils.hideKeyBoard(this);
                edit_add_comment.getText().clear();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (oneWorkPresenter != null) {
            oneWorkPresenter.detachView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FRAGMENT_ONE_WORK.equals(type)
                    && backMode == BACK_MODE_MAIN) {
                MainActivity.jumpToMain(OneWorkActivity.this, MainActivity.INTENT_TYPE_PUSH_BACK);
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
                    MainActivity.jumpToMain(OneWorkActivity.this, MainActivity.INTENT_TYPE_PUSH_BACK);
                    finish();
                }
            });
        }
    }
}
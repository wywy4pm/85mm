package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.OneWorkAdapter;
import com.arun.a85mm.bean.AmountBean;
import com.arun.a85mm.bean.AmountInfoBean;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.bean.UserTagBean;
import com.arun.a85mm.bean.WorkListBean;
import com.arun.a85mm.bean.WorkListItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.RewardDialog;
import com.arun.a85mm.event.DeleteCommentEvent;
import com.arun.a85mm.helper.AppHelper;
import com.arun.a85mm.helper.DialogHelper;
import com.arun.a85mm.helper.ObjectAnimatorManager;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.helper.ShareWindow;
import com.arun.a85mm.helper.UrlJumpHelper;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.listener.OnImageClick;
import com.arun.a85mm.listener.OnTagWorkListener;
import com.arun.a85mm.presenter.OneWorkPresenter;
import com.arun.a85mm.utils.AppUtils;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.FullyLinearLayoutManager;
import com.arun.a85mm.utils.KeyBoardUtils;
import com.arun.a85mm.utils.PatternUtils;
import com.arun.a85mm.utils.ShareParaUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.arun.a85mm.view.CommonView3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OneWorkActivity extends BaseActivity implements CommonView3, OnImageClick, View.OnClickListener, OnTagWorkListener {

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
    private ImageView image_share;
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
    public static final String KEY_TYPE = "show_bottom_type";
    //public static final String KEY_AMOUNT = "amount";
    private List<WorkListItemBean> workListItems = new ArrayList<>();
    private String type;
    private String showBottomType;
    private String authorUid = "";

    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    private String newUid;
    //private AmountInfoBean amountInfoBean;

    public static void jumpToOneWorkActivity(Context context, String type, String title, Map<String, Serializable> extras, int backMode) {
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
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        if (getIntent() != null) {
            type = getIntent().getExtras().getString(TYPE);
            title = getIntent().getExtras().getString(TITLE);
            backMode = getIntent().getExtras().getInt(BACK_MODE);
            Map<String, Serializable> map = (Map<String, Serializable>) getIntent().getExtras().getSerializable(EXTRAS);
            if (map != null) {
                workId = (String) map.get(UrlJumpHelper.WORK_ID);
                showBottomType = (String) map.get(KEY_TYPE);
                //amountInfoBean = (AmountInfoBean) map.get(KEY_AMOUNT);
            }
        }

        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        image_back = (ImageView) findViewById(R.id.image_back);
        image_share = (ImageView) findViewById(R.id.image_share);
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

        if (layout_top.getLayoutParams() != null && layout_top.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layout_top.getLayoutParams()).setMargins(0, DensityUtil.getStatusHeight(this), 0, 0);
        }

        oneWorkAdapter = new OneWorkAdapter(this, workListItems);
        oneWorkAdapter.setOnImageClick(this);
        oneWorkAdapter.setOnTagWorkListener(this);
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

        View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 应用可以显示的区域。此处包括应用占用的区域，
                // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                // 屏幕高度,这个高度不含虚拟按键的高度
                int screenHeight = DensityUtil.getScreenHeight(OneWorkActivity.this);
                int heightDiff = screenHeight - (r.bottom - r.top);
                int statusBarHeight = DensityUtil.getStatusHeight(OneWorkActivity.this);
                if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
                    keyboardHeight = heightDiff - statusBarHeight;
                }

                if (isShowKeyboard) {
                    // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
                    // 说明这时软键盘已经收起
                    if (heightDiff <= statusBarHeight) {
                        isShowKeyboard = false;
                        //onHideKeyboard();
                        ObjectAnimatorManager.translationY(layout_add_comment, -keyboardHeight, 0, 100, null);
                    }
                } else {
                    // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
                    // 说明这时软键盘已经弹出
                    if (heightDiff > statusBarHeight) {
                        isShowKeyboard = true;
                        ObjectAnimatorManager.translationY(layout_add_comment, 0, -keyboardHeight, 100, null);
                        //onShowKeyboard();
                    }
                }
            }
        });
    }

    private void initData() {
        /*if (Constant.TYPE_COMMUNITY.equals(showBottomType)) {
            layout_add_comment.setVisibility(View.VISIBLE);
        } else {
            layout_add_comment.setVisibility(View.GONE);
        }*/
        oneWorkPresenter = new OneWorkPresenter(this);
        oneWorkPresenter.attachView(this);
        refreshData();
    }

    private void setAddCommentView(AmountInfoBean amountInfoBean) {
        if (Constant.TYPE_COMMUNITY.equals(showBottomType) && amountInfoBean == null) {
            layout_add_comment.setVisibility(View.VISIBLE);
        } else {
            layout_add_comment.setVisibility(View.GONE);
        }
    }

    private void refreshData() {
        if (oneWorkPresenter != null) {
            oneWorkPresenter.getOneWorkDetail(workId);
        }
    }

    @Override
    public void refresh(int dataType, Object data) {
        if (dataType == OneWorkPresenter.TYPE_DETAIL
                && data instanceof WorkListBean) {
            WorkListBean bean = (WorkListBean) data;
            sourceUrl = bean.sourceUrl;
            authorUid = bean.uid;

            setShare(bean.title, ShareParaUtils.getWorkDetailShareDescription(bean.authorName),
                    ShareParaUtils.getWorkDetailShareUrl(workId), bean.coverUrl);
            setShowBottomRight(sourceUrl, workId, showBottomType, authorUid);

            workListItems.clear();
            oneWorkAdapter.setWorkListBean(bean);

            addCoverImage(bean);
            addImage(bean.imageList);

            //addHead(bean);
            if (Constant.TYPE_COMMUNITY.equals(showBottomType)) {
                addDescription(bean);
                addHead(bean);
                addTagView(bean);
                if (bean.productInfo == null) {
                    addComments(bean);
                }
            } else {
                addHead(bean);
                addTagView(bean);
            }
            if (bean.productInfo != null) {
                addReward(bean);
            }
            setAddCommentView(bean.productInfo);

            if (bean.imageList == null || bean.imageList.size() == 0) {
                oneWorkAdapter.setNoImage(true);
            }
            oneWorkAdapter.notifyDataSetChanged();
        } else if (dataType == OneWorkPresenter.TYPE_ADD_COMMENT) {
            if (TextUtils.isEmpty(newUid)) {
                showTop("评论成功");
                refreshData();
            } else {
                SharedPreferencesUtils.saveUid(this, newUid);
                AppHelper.getInstance().getAppConfig().setUid(newUid);
                AppUtils.restartApp(this);
            }
        } else if (dataType == OneWorkPresenter.TYPE_TAG_WORK) {
            if (data instanceof UserTagBean) {
                showTop("打标成功");
            }
        } else if (dataType == OneWorkPresenter.TYPE_USER_AWARD) {
            if (data instanceof AwardBodyBean) {
                AwardBodyBean bean = (AwardBodyBean) data;
                jumpToAmountWork(bean, AmountWorkActivity.TYPE_PAY);
            }
        }
    }

    public void noEnoughCoins(AwardBodyBean bean) {
        int coins = 0;
        if (bean.productInfo != null) {
            coins = bean.leftCoin;
        }
        if (coins == 0) {
            showDialog(this, RewardDialog.TYPE_NO_COINS, coins);
        } else {
            showDialog(this, RewardDialog.TYPE_NO_ENOUGH_COINS, coins);
        }
    }

    public void jumpToAmountWork(AwardBodyBean awardBodyBean, int type) {
        String titleName = workId + "号收费内容";
        AmountWorkActivity.jumpToAmountWork(this, type, titleName, awardBodyBean);
    }

    private void showDialog(Context context, int type, int leftCoin) {
        RewardDialog rewardDialog = new RewardDialog(context, R.style.CustomDialog, type, leftCoin);
        rewardDialog.show();
    }

    @Override
    public void onUserAward(String workId) {
        if (oneWorkPresenter != null) {
            oneWorkPresenter.userAward(workId);
        }
    }

    private void setShare(final String shareTitle, final String shareDescription, final String shareUrl, final String shareImageUrl) {
        image_share.setVisibility(View.VISIBLE);
        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWindow.show(OneWorkActivity.this, shareTitle, shareDescription, shareUrl, shareImageUrl, eventStatisticsHelper);
            }
        });
    }

    private void setShowBottomRight(final String linkUrl, final String workId, final String type, final String authorUid) {
        image_more.setVisibility(View.VISIBLE);
        image_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogHelper.showBottomSourceLink(OneWorkActivity.this, linkUrl, workId, eventStatisticsHelper, type, authorUid);
                if (Constant.TYPE_COMMUNITY.equals(showBottomType) || Constant.TYPE_AUDIT.equals(showBottomType)) {
                    DialogHelper.showBottom(OneWorkActivity.this, type, linkUrl, workId, authorUid, eventStatisticsHelper);
                } else {
                    DialogHelper.showBottom(OneWorkActivity.this, type, linkUrl, workId, "", eventStatisticsHelper);
                }
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
        itemBean.url = bean.coverUrl;
        itemBean.width = bean.coverWidth;
        itemBean.height = bean.coverHeight;
        itemBean.backgroundColor = RandomColorHelper.getRandomColor();
        workListItems.add(itemBean);
    }

    private void addImage(List<WorkListItemBean> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                WorkListItemBean bean = list.get(i);
                bean.type = OneWorkAdapter.DATA_TYPE_IMAGE;
                bean.backgroundColor = RandomColorHelper.getRandomColor();
            }
            workListItems.addAll(list);
        }
    }

    private void addDescription(WorkListBean bean) {
        WorkListItemBean itemDesBean = new WorkListItemBean();
        itemDesBean.type = OneWorkAdapter.DATA_TYPE_DESCRIPTION;
        itemDesBean.workTitle = bean.title;
        itemDesBean.description = bean.description;
        workListItems.add(itemDesBean);
    }

    private void addTagView(WorkListBean bean) {
        WorkListItemBean itemComBean = new WorkListItemBean();
        itemComBean.type = OneWorkAdapter.DATA_TYPE_ADD_TAG;
        itemComBean.workTags = bean.workTags;
        itemComBean.id = workId;
        workListItems.add(itemComBean);
    }

    private void addComments(WorkListBean bean) {
        WorkListItemBean itemComBean = new WorkListItemBean();
        itemComBean.type = OneWorkAdapter.DATA_TYPE_COMMENTS;
        itemComBean.comments = bean.commentList;
        workListItems.add(itemComBean);
    }

    private void addReward(WorkListBean bean) {
        WorkListItemBean itemComBean = new WorkListItemBean();
        itemComBean.type = OneWorkAdapter.DATA_TYPE_REWARD;
        itemComBean.id = bean.id;
        itemComBean.productInfo = bean.productInfo;
        workListItems.add(itemComBean);
    }

    @Override
    public void onCountClick(int groupPosition) {

    }

    @Override
    public void onCoverClick(String workId, String coverUrl, String authorName) {
        saveImageShowTop(workId, coverUrl, authorName);
    }

    @Override
    public void onMoreLinkClick(String workId, String sourceUrl, String authorUid) {
        DialogHelper.showBottom(this, showBottomType, sourceUrl, workId, this.authorUid, eventStatisticsHelper);
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
                if (UserManager.getInstance() != null) {
                    if (!UserManager.getInstance().isLogin()) {
                        LoginActivity.jumpToLoginForResult(OneWorkActivity.this);
                        return;
                    }
                }
                if (TextUtils.isEmpty(edit_add_comment.getText())) {
                    showTop("请输入评论内容");
                    return;
                }
                if (!TextUtils.isEmpty(SharedPreferencesUtils.getUid(this))) {
                    if (PatternUtils.judgeChangeUser(edit_add_comment.getText().toString())) {
                        newUid = edit_add_comment.getText().toString().replace("c ", "");
                        /*if (oneWorkPresenter != null) {
                            oneWorkPresenter.userLogout();
                        }*/
                    }
                }
                if (oneWorkPresenter != null) {
                    oneWorkPresenter.addComment(workId, edit_add_comment.getText().toString());
                }
                //发送成功后关闭软键盘
                KeyBoardUtils.hideKeyBoard(this, edit_add_comment);
                edit_add_comment.getText().clear();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    public void onClickMyTag(UserTagBean tagBean, String workId) {
        if (oneWorkPresenter != null) {
            oneWorkPresenter.tagWork(tagBean, workId);
        }
    }

    public void resetUserTag(UserTagBean tagBean) {
        tagBean.tagType = tagBean.tagType == 1 ? 0 : 1;
        oneWorkAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteComment(DeleteCommentEvent event) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, EventConstant.ASSOCIATION_COMMENT_DELETE, event.commentId);
        }
    }

    public void refreshComments(String commentId) {
        //refreshData();
        for (int i = 0; i < workListItems.size(); i++) {
            if (workListItems.get(i) != null && workListItems.get(i).comments != null) {
                for (int j = 0; j < workListItems.get(i).comments.size(); j++) {
                    String id = String.valueOf(workListItems.get(i).comments.get(j).id);
                    if (!TextUtils.isEmpty(commentId) && commentId.equals(id)) {
                        workListItems.get(i).comments.remove(j);
                        oneWorkAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }
}

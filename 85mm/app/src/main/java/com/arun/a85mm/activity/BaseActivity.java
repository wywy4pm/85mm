package com.arun.a85mm.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.ActionBean;
import com.arun.a85mm.bean.ShowTopBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.handler.ShowTopHandler;
import com.arun.a85mm.helper.EventStatisticsHelper;
import com.arun.a85mm.helper.ObjectAnimatorHelper;
import com.arun.a85mm.helper.SaveImageHelper;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.DeviceUtils;
import com.arun.a85mm.utils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by WY on 2017/4/15.
 */
public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    private boolean isShowingTop;
    private SaveImageHelper saveImageHelper;
    private ShowTopHandler showTopHandler;
    private ObjectAnimatorHelper objectAnimatorHelper;
    private EventStatisticsHelper eventStatisticsHelper;
    private TextView topCommonView;
    private TextView toastView;
    private ImageView image_back;
    public String userId;
    public String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        initCommon();
    }

    private void initCommon() {
        deviceId = DeviceUtils.getMobileIMEI(this);
        userId = SharedPreferencesUtils.getUid(this);
    }

    public void setSaveImage() {
        initToastView();
        saveImageHelper = new SaveImageHelper();
        showTopHandler = new ShowTopHandler(this);
        objectAnimatorHelper = new ObjectAnimatorHelper();
        eventStatisticsHelper = new EventStatisticsHelper(this);
    }

    public void setCommonShow() {
        initToastView();
        showTopHandler = new ShowTopHandler(this);
        objectAnimatorHelper = new ObjectAnimatorHelper();
    }

    public void showTop(String showData) {
        if (showTopHandler != null) {
            Message message = new Message();
            message.what = Constant.WHAT_SHOW_TOP;
            message.obj = new ShowTopBean(isShowingTop, showData);
            if (showTopHandler != null) {
                showTopHandler.sendMessage(message);
            }
        }
    }

    private void initToastView() {
        toastView = (TextView) findViewById(R.id.toastView);
        topCommonView = (TextView) findViewById(R.id.topCommonView);
        if (toastView.getLayoutParams() != null && topCommonView.getLayoutParams() != null) {
            toastView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
            topCommonView.getLayoutParams().height = DensityUtil.getStatusHeight(this);
        }
    }

    public void saveImageShowTop(String workId, String coverUrl, int width, int height) {
        if (saveImageHelper != null && showTopHandler != null) {
            if (!TextUtils.isEmpty(workId)) {
                onActionEvent(EventConstant.WORK_IMAGE_DOWNLOAD, EventStatisticsHelper.createOneActionList(EventConstant.WORK_IMAGE_DOWNLOAD, workId, coverUrl));
            }
            saveImageHelper.saveImageShowTop(this, coverUrl, width, height, showTopHandler, isShowingTop);
        }
    }

    public void showTopToastView(ShowTopBean showTopBean) {
        if (showTopBean != null && objectAnimatorHelper != null) {
            objectAnimatorHelper.managerShowTopView(this, toastView, showTopBean);
        }
    }

    public void setShowingTop(boolean isShowingTop) {
        this.isShowingTop = isShowingTop;
    }

    public void onActionEvent(int type, List<ActionBean> actionList) {
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.recordUserAction(this, type, actionList);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    public void setBack(View backView) {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setBack() {
        image_back = (ImageView) findViewById(R.id.image_back);
        if (image_back != null) {
            setBack(image_back);
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventStatisticsHelper != null) {
            eventStatisticsHelper.detachView();
        }
    }
}

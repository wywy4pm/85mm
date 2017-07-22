package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.helper.UserManager;
import com.arun.a85mm.presenter.UserPresenter;
import com.arun.a85mm.utils.StatusBarUtils;
import com.arun.a85mm.view.CommonView3;

public class UpdateUserBriefActivity extends BaseActivity implements CommonView3 {

    public TextView image_right;
    public EditText edit_brief;
    public TextView edit_num;
    private UserInfoBean user;
    private UserPresenter presenter;
    private String brief;

    public static void jumpToUpdateUserBrief(Context context) {
        Intent intent = new Intent(context, UpdateUserBriefActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_user_brief);
        initView();
        initData();
    }

    private void initView() {
        image_right = (TextView) findViewById(R.id.image_right);
        edit_brief = (EditText) findViewById(R.id.edit_brief);
        edit_num = (TextView) findViewById(R.id.edit_num);

        edit_brief.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = s.length();
                edit_num.setText(String.valueOf(num));
            }
        });

        setTitle("简介");
        setBack();
        setRight();
    }

    private void setRight() {
        TextView right = (TextView) findViewById(R.id.image_right);
        if (right != null) {
            right.setVisibility(View.VISIBLE);
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            right.setTextColor(getResources().getColor(R.color.charcoalgrey));
            right.setText("完成");
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(edit_brief.getText())) {
                        showTop("不能为空");
                    } else {
                        if (presenter != null) {
                            brief = edit_brief.getText().toString();
                            presenter.updateUserInfo("", "", brief, "");
                        }
                    }
                }
            });
        }
    }

    private void initData() {
        presenter = new UserPresenter(this);
        presenter.attachView(this);
        user = UserManager.getInstance().getUserInfoBean();
        if (user != null) {
            edit_brief.setText(user.description);
        }
    }


    @Override
    public void refresh(int type, Object data) {
        if (type == UserPresenter.TYPE_UPDATE_USER_DESCRIPTION) {
            UserManager.getInstance().setUserBrief(brief);
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}

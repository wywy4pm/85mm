package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.UserInfoBean;
import com.arun.a85mm.helper.UserManager;

public class UpdateUserNameActivity extends BaseActivity {
    public TextView image_right;
    public EditText edit_user_name;
    public RelativeLayout layout_update_name;
    public ImageView edit_clean;
    private UserInfoBean user;

    public static void jumpToUpdateUserName(Context context) {
        Intent intent = new Intent(context, UpdateUserNameActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_name);
        initView();
        initData();
    }

    private void initView() {
        this.image_right = (TextView) findViewById(R.id.image_right);
        this.edit_user_name = (EditText) findViewById(R.id.edit_user_name);
        this.layout_update_name = (RelativeLayout) findViewById(R.id.layout_update_name);
        this.edit_clean = (ImageView) findViewById(R.id.edit_clean);

        setTitle("用户名");
        setBack();
        setRight();

        edit_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    edit_clean.setVisibility(View.VISIBLE);
                } else {
                    edit_clean.setVisibility(View.GONE);
                }
            }
        });
        edit_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edit_user_name.getText())) {
                    edit_user_name.getText().clear();
                }
            }
        });
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

                }
            });
        }
    }

    private void initData() {
        user = UserManager.getInstance().getUserInfoBean();
        if (user != null) {
            edit_user_name.setText(user.name);
        }
    }

}

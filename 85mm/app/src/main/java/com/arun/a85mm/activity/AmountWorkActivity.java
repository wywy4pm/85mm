package com.arun.a85mm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.bean.AwardBodyBean;
import com.arun.a85mm.utils.SystemServiceUtils;

public class AmountWorkActivity extends Activity {

    public TextView image_right;
    private TextView amount_description;

    public static void jumpToAmountWork(Context context, AwardBodyBean awardBodyBean) {
        Intent intent = new Intent(context, AmountWorkActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_work);
        initView();
    }

    private void initView() {
        amount_description = (TextView) findViewById(R.id.amount_description);

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
        //SystemServiceUtils.copyText(this, );
    }
}

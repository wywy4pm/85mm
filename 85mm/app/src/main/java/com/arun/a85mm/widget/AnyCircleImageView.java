package com.arun.a85mm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.arun.a85mm.utils.DensityUtil;

/**
 * Created by WY on 2017/11/5.
 */
public class AnyCircleImageView extends ImageView {

    //圆角弧度
    private float[] rids;

    public AnyCircleImageView(Context context) {
        super(context);
        init(context);
    }

    public AnyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnyCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        float dp = DensityUtil.dp2px(context, 6);
        rids = new float[]{dp, dp, 0, 0, 0, 0, dp, dp};
    }

    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        //绘制圆角imageview
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}

package com.arun.a85mm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.arun.a85mm.R;

/**
 * Created by wy on 2017/6/16.
 */

public class AutoLineLinearLayout extends ViewGroup {

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip
    private int ROW_SPACE;

    public AutoLineLinearLayout(Context context) {
        this(context, null);
    }

    public AutoLineLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoLineLinearLayout);
        LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.AutoLineLinearLayout_leftAndRightSpace, 10);
        ROW_SPACE = ta.getDimensionPixelSize(R.styleable.AutoLineLinearLayout_rowSpace, 10);
        ta.recycle(); //回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //为所有的标签childView计算宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //获取高的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //建议的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //布局的宽度采用建议宽度（match_parent或者size），如果设置wrap_content也是match_parent的效果
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height = 0;
        int childAllHeight = 0;
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果高度模式为EXACTLY（match_parent或者size），则使用建议高度
            height = heightSize;
        } else {
            //其他情况下（AT_MOST、UNSPECIFIED）需要计算计算高度
            int childCount = getChildCount();
            if (childCount <= 0) {
                height = 0;   //没有标签时，高度为0
            } else {
                int row = 1;  // 标签行数
                int widthSpace = width;// 当前行右侧剩余的宽度
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    //获取标签宽度
                    int childW = view.getMeasuredWidth();
                    //Log.v(TAG , "标签宽度:"+childW +" 行数："+row+"  剩余宽度："+widthSpace);
                    if (i == 0) {
                        childAllHeight += getChildAt(i).getMeasuredHeight();
                    }
                    if (widthSpace >= childW) {
                        //如果剩余的宽度大于此标签的宽度，那就将此标签放到本行
                        widthSpace -= childW;
                    } else {
                        row++;    //增加一行
                        //如果剩余的宽度不能摆放此标签，那就将此标签放入一行
                        widthSpace = width - childW;
                        childAllHeight += getChildAt(i).getMeasuredHeight();
                    }
                    //减去标签左右间距
                    widthSpace -= LEFT_RIGHT_SPACE;
                }
               /* //由于每个标签的高度是相同的，所以直接获取第一个标签的高度即可
                int childH = getChildAt(0).getMeasuredHeight();
                //最终布局的高度=标签高度*行数+行距*(行数-1)
                height = (childH * row) + ROW_SPACE * (row - 1);*/
                height = childAllHeight + ROW_SPACE * (row - 1);
            }
        }

        //设置测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 1;
        int right = 0;   // 标签相对于布局的右侧位置
        int bottom = 0;       // 标签相对于布局的底部位置
        int preLineBottom = 0;//上一行的bottom
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();
            //右侧位置=本行已经占有的位置+当前标签的宽度
            right += childW;

            // 如果右侧位置已经超出布局右边缘，跳到下一行
            if (right > (r - LEFT_RIGHT_SPACE)) {
                row++;
                right = childW;
                preLineBottom = bottom;
                bottom = preLineBottom + (childH + ROW_SPACE);
            } else {
                //底部位置=上一行的bottom +（标签高度+行距）
                if (row == 1) {
                    bottom = preLineBottom + childH;
                } else {
                    bottom = preLineBottom + (childH + ROW_SPACE);
                }
            }
            childView.layout(right - childW, bottom - childH, right, bottom);
            right += LEFT_RIGHT_SPACE;
        }
    }
}

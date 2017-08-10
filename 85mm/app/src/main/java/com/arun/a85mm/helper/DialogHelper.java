package com.arun.a85mm.helper;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.OneWorkActivity;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.dialog.UploadImageDialog;
import com.arun.a85mm.event.DeleteCommentEvent;
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wy on 2017/5/5.
 */

public class DialogHelper {

    public static void showBottom(final Context context, String type, final String linkUrl, final String workId, String authorUid, final EventStatisticsHelper helper) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        String uid = SharedPreferencesUtils.getUid(context);

        for (int i = 0; i < 12; i++) {
            if (Constant.TYPE_AUDIT.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 7) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 8 || i == 10 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_WORK.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 5 || i == 7) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 8 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_COMMUNITY.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 5 || i == 6 || i == 9) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                } else if (!TextUtils.isEmpty(authorUid) && authorUid.equals(uid)) {
                    if (i == 9) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 1 || i == 2 || i == 3 || i == 4 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_PUSH.equals(type)) {
                if (i == 3 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            }
        }

        dialog.setContentView(root);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

    private static void setView(Context context, int position, LinearLayout root, Dialog dialog, String workId, String linkUrl, EventStatisticsHelper helper) {
        View layout_item = LayoutInflater.from(context).inflate(R.layout.layout_bottom_item, root, false);
        TextView text_big = (TextView) layout_item.findViewById(R.id.text_big);
        TextView text_small = (TextView) layout_item.findViewById(R.id.text_small);
        setOneText(context, position, layout_item, text_big, text_small, workId);
        setOneClick(context, position, layout_item, dialog, workId, linkUrl, helper);
        root.addView(layout_item);
    }

    private static void setOneText(Context context, int position, View layout_item, TextView bigText, TextView smallText, String workId) {
        String big = "";
        if (position == 10) {
            big = context.getString(R.string.recommend_new);
            bigText.setTextColor(context.getResources().getColor(R.color.darkgreen));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 6) {
            big = context.getString(R.string.recommend_jingxuan);
            bigText.setTextColor(context.getResources().getColor(R.color.darkgreen));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 7) {
            big = context.getString(R.string.move_association);
            bigText.setTextColor(context.getResources().getColor(R.color.darkgreen));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 5) {
            big = context.getString(R.string.move_audit);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 0) {
            big = context.getString(R.string.read_origin);
            smallText.setText(String.valueOf("ID：" + workId));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            big = context.getString(R.string.report);
            smallText.setText(context.getResources().getString(R.string.report_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            big = context.getString(R.string.scale_over);
            smallText.setText(context.getResources().getString(R.string.scale_over_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 8) {
            big = context.getString(R.string.bad_comment);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setText(context.getResources().getString(R.string.bad_comment_detail));
            smallText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 4) {
            big = context.getString(R.string.repeat);
            smallText.setText(context.getResources().getString(R.string.repeat_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 3) {
            big = context.getString(R.string.show_seq);
            smallText.setText(context.getResources().getString(R.string.show_seq_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 9) {
            big = context.getString(R.string.delete);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setText(String.valueOf("ID：" + workId));
            smallText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 11) {
            big = context.getString(R.string.cancel);
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
            layout_item.setBackgroundColor(context.getResources().getColor(R.color.area_divide));
        }
        bigText.setText(big);
    }

    private static void setOneClick(final Context context, final int position, View itemView, final Dialog dialog, final String workId, final String linkUrl, final EventStatisticsHelper helper) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 1 || position == 2 || position == 3 || position == 4
                        || position == 5 || position == 6 || position == 7 || position == 8
                        || position == 9 || position == 10) {
                    int type = 0;
                    if (position == 10) {
                        type = EventConstant.WORK_AUDIT_RECOMMEND;
                    } else if (position == 6) {
                        type = EventConstant.WORK_ASSOCIATION_RECOMMEND;
                    } else if (position == 7) {
                        type = EventConstant.WORK_MOVE_TO_ASSOCIATION;
                    } else if (position == 5) {
                        type = EventConstant.WORK_MOVE_TO_AUDIT;
                    } else if (position == 1) {
                        type = EventConstant.WORK_REPORT;
                    } else if (position == 2) {
                        type = EventConstant.WORK_SCALE_OVER;
                    } else if (position == 8) {
                        type = EventConstant.WORK_BAD_COMMNET;
                    } else if (position == 4) {
                        type = EventConstant.WORK_REPEAT;
                    } else if (position == 3) {
                        type = EventConstant.WORK_SHOW_SEQ;
                    } else {
                        type = EventConstant.WORK_ASSOCIATION_DELETE;
                    }
                    if (helper != null) {
                        helper.recordUserAction(context, type, workId, "");
                    }
                    dialog.cancel();
                } else if (position == 0) {
                    WebViewActivity.jumpToWebViewActivity(context, linkUrl);
                    int type = EventConstant.WORK_OPEN_ORIGIN;
                    if (helper != null) {
                        helper.recordUserAction(context, type, workId, "");
                    }
                    dialog.cancel();
                } else if (position == 11) {
                    dialog.cancel();
                }
            }
        });
    }

    public static void showUploadImageBottom(Context context, int requestCode) {
        UploadImageDialog dialog = new UploadImageDialog(context, R.style.ActionSheetDialogStyle, requestCode);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

    public static void showDeleteCommentBottom(final Context context, final String commentId) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View deleteView = LayoutInflater.from(context).inflate(R.layout.dialog_upload_image, null);
        TextView delete = (TextView) deleteView.findViewById(R.id.btn_album_upload);
        delete.setText("删除");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteCommentEvent(commentId));
                dialog.dismiss();
            }
        });

        dialog.setContentView(deleteView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = 0; // 新位置Y坐标
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

}

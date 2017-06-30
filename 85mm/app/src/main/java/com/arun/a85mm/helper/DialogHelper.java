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
import android.widget.TextView;

import com.arun.a85mm.R;
import com.arun.a85mm.activity.WebViewActivity;
import com.arun.a85mm.common.EventConstant;
import com.arun.a85mm.fragment.OneWorkFragment;
import com.arun.a85mm.utils.SharedPreferencesUtils;

/**
 * Created by wy on 2017/5/5.
 */

public class DialogHelper {

    public static void showBottomSourceLink(final Context context, final String linkUrl, final String workId, final EventStatisticsHelper helper) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        TextView workIdView = (TextView) root.findViewById(R.id.text_works_id);
        workIdView.setText(String.valueOf("ID：" + workId));
        root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jumpToWebViewActivity(context, linkUrl);
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPORT, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPORT, workId, ""));
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SCALE_OVER, EventStatisticsHelper.createOneActionList(EventConstant.WORK_SCALE_OVER, workId, ""));
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_bad_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_BAD_COMMNET, EventStatisticsHelper.createOneActionList(EventConstant.WORK_BAD_COMMNET, workId, ""));
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPEAT, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPEAT, workId, ""));
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_image_seq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SHOW_SEQ, EventStatisticsHelper.createOneActionList(EventConstant.WORK_SHOW_SEQ, workId, ""));
                }
                dialog.cancel();
            }
        });

        /*root.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPEAT, workId, ""));
                }
                dialog.cancel();
            }
        });*/
        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
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

    public static void showBottomSourceLink(final Context context, final String linkUrl, final String workId, final EventStatisticsHelper helper, String type, String authorUid) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        String uid = SharedPreferencesUtils.getUid(context);
        if (OneWorkFragment.TYPE_COMMUNITY.equals(type)) {
            TextView read_origin = (TextView) root.findViewById(R.id.read_origin);

            TextView report_detail = (TextView) root.findViewById(R.id.report_detail);
            report_detail.setText("作品是广告，作品低俗" + " ID:" + workId);

            LinearLayout btn_recommend = (LinearLayout) root.findViewById(R.id.btn_recommend);
            View line_btn_recommend = root.findViewById(R.id.line_btn_recommend);
            if ("4".equals(uid)) {
                line_btn_recommend.setVisibility(View.VISIBLE);
                btn_recommend.setVisibility(View.VISIBLE);
                btn_recommend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (helper != null) {
                            helper.recordUserAction(context, EventConstant.WORK_ASSOCIATION_RECOMMEND,
                                    EventStatisticsHelper.createOneActionList(EventConstant.WORK_ASSOCIATION_RECOMMEND, workId, ""));
                        }
                        dialog.cancel();
                    }
                });
            } else {
                line_btn_recommend.setVisibility(View.GONE);
                btn_recommend.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(authorUid) && authorUid.equals(uid)) {
                root.findViewById(R.id.btn_link).setVisibility(View.VISIBLE);
                read_origin.setTextColor(context.getResources().getColor(R.color.red));
                read_origin.setText("删除");
                TextView workIdView = (TextView) root.findViewById(R.id.text_works_id);
                workIdView.setText(String.valueOf("ID：" + workId));
                root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (helper != null) {
                            helper.recordUserAction(context, EventConstant.WORK_ASSOCIATION_DELETE,
                                    EventStatisticsHelper.createOneActionList(EventConstant.WORK_ASSOCIATION_DELETE, workId, ""));
                        }
                        dialog.cancel();
                    }
                });
            } else {
                root.findViewById(R.id.btn_link).setVisibility(View.GONE);
            }
        } else {
            root.findViewById(R.id.btn_link).setVisibility(View.VISIBLE);
            root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.jumpToWebViewActivity(context, linkUrl);
                    dialog.cancel();
                }
            });
        }

        root.findViewById(R.id.btn_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPORT, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPORT, workId, ""));
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SCALE_OVER, EventStatisticsHelper.createOneActionList(EventConstant.WORK_SCALE_OVER, workId, ""));
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_bad_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_BAD_COMMNET, EventStatisticsHelper.createOneActionList(EventConstant.WORK_BAD_COMMNET, workId, ""));
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPEAT, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPEAT, workId, ""));
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_image_seq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SHOW_SEQ, EventStatisticsHelper.createOneActionList(EventConstant.WORK_SHOW_SEQ, workId, ""));
                }
                dialog.cancel();
            }
        });

        /*root.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant, EventStatisticsHelper.createOneActionList(EventConstant.WORK_REPEAT, workId, ""));
                }
                dialog.cancel();
            }
        });*/
        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
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
}

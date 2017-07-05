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
import com.arun.a85mm.fragment.ProductionFragment;
import com.arun.a85mm.utils.DensityUtil;
import com.arun.a85mm.utils.SharedPreferencesUtils;

/**
 * Created by wy on 2017/5/5.
 */

public class DialogHelper {

    /*public static void showBottomSourceLink(final Context context, final String linkUrl, final String workId, final EventStatisticsHelper helper) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        TextView workIdView = (TextView) root.findViewById(R.id.text_works_id);
        workIdView.setText(String.valueOf("ID：" + workId));
        if (!TextUtils.isEmpty(linkUrl)) {
            root.findViewById(R.id.btn_link).setVisibility(View.VISIBLE);
            root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.jumpToWebViewActivity(context, linkUrl);
                    dialog.cancel();
                }
            });
        } else {
            root.findViewById(R.id.btn_link).setVisibility(View.GONE);
        }

        root.findViewById(R.id.btn_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPORT, workId, "");
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SCALE_OVER, workId, "");
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_bad_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_BAD_COMMNET, workId, "");
                }
                dialog.cancel();
            }
        });
        root.findViewById(R.id.btn_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPEAT, workId, "");
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_image_seq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SHOW_SEQ, workId, "");
                }
                dialog.cancel();
            }
        });

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
    }*/

    /*public static void showBottomSourceLink(final Context context, final String linkUrl, final String workId, final EventStatisticsHelper helper, String type, String authorUid) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        String uid = SharedPreferencesUtils.getUid(context);
        if (Constant.TYPE_COMMUNITY.equals(type)) {
            TextView read_origin = (TextView) root.findViewById(R.id.read_origin);

            TextView report_detail = (TextView) root.findViewById(R.id.report_detail);
            report_detail.setText("作品是广告，作品低俗" + " ID:" + workId);

            LinearLayout btn_recommend = (LinearLayout) root.findViewById(R.id.btn_recommend);
            View line_btn_recommend = root.findViewById(R.id.line_btn_recommend);
            LinearLayout btn_move_audit = (LinearLayout) root.findViewById(R.id.btn_move_audit);
            View line_move_audit = root.findViewById(R.id.line_move_audit);

            if ("4".equals(uid)) {
                line_btn_recommend.setVisibility(View.VISIBLE);
                btn_recommend.setVisibility(View.VISIBLE);
                line_move_audit.setVisibility(View.VISIBLE);
                btn_move_audit.setVisibility(View.VISIBLE);

                btn_recommend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (helper != null) {
                            helper.recordUserAction(context, EventConstant.WORK_ASSOCIATION_RECOMMEND, workId, "");
                        }
                        dialog.cancel();
                    }
                });
                btn_move_audit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (helper != null) {
                            helper.recordUserAction(context, EventConstant.WORK_MOVE_TO_AUDIT, workId, "");
                        }
                        dialog.cancel();
                    }
                });

            } else {
                line_btn_recommend.setVisibility(View.GONE);
                btn_recommend.setVisibility(View.GONE);
                line_move_audit.setVisibility(View.GONE);
                btn_move_audit.setVisibility(View.GONE);
            }

            if ((!TextUtils.isEmpty(authorUid) && authorUid.equals(uid)) || "4".equals(uid)) {
                root.findViewById(R.id.btn_link).setVisibility(View.VISIBLE);
                read_origin.setTextColor(context.getResources().getColor(R.color.red));
                read_origin.setText("删除");
                TextView workIdView = (TextView) root.findViewById(R.id.text_works_id);
                workIdView.setText(String.valueOf("ID：" + workId));
                root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (helper != null) {
                            helper.recordUserAction(context, EventConstant.WORK_ASSOCIATION_DELETE, workId, "");
                        }
                        dialog.cancel();
                    }
                });
            } else {
                root.findViewById(R.id.btn_link).setVisibility(View.GONE);
            }
        } else if (Constant.TYPE_AUDIT.equals(type)) {
            LinearLayout btn_recommend_work = (LinearLayout) root.findViewById(R.id.btn_recommend_work);
            View line_btn_recommend_work = root.findViewById(R.id.line_btn_recommend_work);
            btn_recommend_work.setVisibility(View.VISIBLE);
            line_btn_recommend_work.setVisibility(View.VISIBLE);
            btn_recommend_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper != null) {
                        helper.recordUserAction(context, EventConstant.WORK_AUDIT_RECOMMEND, workId, "");
                    }
                    dialog.cancel();
                }
            });

            if (!TextUtils.isEmpty(linkUrl)) {
                root.findViewById(R.id.btn_link).setVisibility(View.VISIBLE);
                TextView workIdView = (TextView) root.findViewById(R.id.text_works_id);
                workIdView.setText(String.valueOf("ID：" + workId));
                root.findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.jumpToWebViewActivity(context, linkUrl);
                        dialog.cancel();
                    }
                });
            } else {
                root.findViewById(R.id.btn_link).setVisibility(View.GONE);
            }
        }

        root.findViewById(R.id.btn_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPORT, workId, "");
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SCALE_OVER, workId, "");
                }
                dialog.cancel();
            }
        });

        if (Constant.TYPE_COMMUNITY.equals(type)) {
            root.findViewById(R.id.btn_bad_comment).setVisibility(View.GONE);
            root.findViewById(R.id.line_bad_comment).setVisibility(View.GONE);
        } else {
            root.findViewById(R.id.line_bad_comment).setVisibility(View.VISIBLE);
            root.findViewById(R.id.btn_bad_comment).setVisibility(View.VISIBLE);
            root.findViewById(R.id.btn_bad_comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper != null) {
                        helper.recordUserAction(context, EventConstant.WORK_BAD_COMMNET, workId, "");
                    }
                    dialog.cancel();
                }
            });
        }

        root.findViewById(R.id.btn_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_REPEAT, workId, "");
                }
                dialog.cancel();
            }
        });

        root.findViewById(R.id.btn_image_seq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper != null) {
                    helper.recordUserAction(context, EventConstant.WORK_SHOW_SEQ, workId, "");
                }
                dialog.cancel();
            }
        });

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
    }*/


    public static void showBottom(final Context context, String type, final String linkUrl, final String workId, String authorUid, final EventStatisticsHelper helper) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        String uid = SharedPreferencesUtils.getUid(context);

        for (int i = 0; i < 12; i++) {
            if (Constant.TYPE_AUDIT.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 2) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 0 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_WORK.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 2 || i == 3) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_COMMUNITY.equals(type)) {
                if ("4".equals(uid)) {
                    if (i == 1 || i == 3 || i == 10) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                } else if (!TextUtils.isEmpty(authorUid) && authorUid.equals(uid)) {
                    if (i == 10) {
                        setView(context, i, root, dialog, workId, linkUrl, helper);
                    }
                }
                if (i == 5 || i == 6 || i == 8 || i == 9 || i == 11) {
                    setView(context, i, root, dialog, workId, linkUrl, helper);
                }
            } else if (Constant.TYPE_PUSH.equals(type)) {
                if (i == 9 || i == 11) {
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
        setOneText(context, position, text_big, text_small, workId);
        setOneClick(context, position, layout_item, dialog, workId, linkUrl, helper);
        root.addView(layout_item);
    }

    private static void setOneText(Context context, int position, TextView bigText, TextView smallText, String workId) {
        String big = "";
        if (position == 0) {
            big = context.getString(R.string.recommend_new);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 1) {
            big = context.getString(R.string.recommend_jingxuan);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 2) {
            big = context.getString(R.string.move_association);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 3) {
            big = context.getString(R.string.move_audit);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        } else if (position == 4) {
            big = context.getString(R.string.read_origin);
            smallText.setText(String.valueOf("ID：" + workId));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 5) {
            big = context.getString(R.string.report);
            smallText.setText(context.getResources().getString(R.string.report_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 6) {
            big = context.getString(R.string.scale_over);
            smallText.setText(context.getResources().getString(R.string.scale_over_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 7) {
            big = context.getString(R.string.bad_comment);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setText(context.getResources().getString(R.string.bad_comment_detail));
            smallText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 8) {
            big = context.getString(R.string.repeat);
            smallText.setText(context.getResources().getString(R.string.repeat_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 9) {
            big = context.getString(R.string.show_seq);
            smallText.setText(context.getResources().getString(R.string.show_seq_detail));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 10) {
            big = context.getString(R.string.delete);
            bigText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setText(String.valueOf("ID：" + workId));
            smallText.setTextColor(context.getResources().getColor(R.color.red));
            smallText.setVisibility(View.VISIBLE);
        } else if (position == 11) {
            big = context.getString(R.string.cancel);
            bigText.setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 12));
        }
        bigText.setText(big);
    }

    private static void setOneClick(final Context context, final int position, View itemView, final Dialog dialog, final String workId, final String linkUrl, final EventStatisticsHelper helper) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0 || position == 1 || position == 2 || position == 3
                        || position == 5 || position == 6 || position == 7 || position == 8
                        || position == 9 || position == 10) {
                    int type = 0;
                    if (position == 0) {
                        type = EventConstant.WORK_AUDIT_RECOMMEND;
                    } else if (position == 1) {
                        type = EventConstant.WORK_ASSOCIATION_RECOMMEND;
                    } else if (position == 2) {
                        type = EventConstant.WORK_MOVE_TO_ASSOCIATION;
                    } else if (position == 3) {
                        type = EventConstant.WORK_MOVE_TO_AUDIT;
                    } else if (position == 5) {
                        type = EventConstant.WORK_REPORT;
                    } else if (position == 6) {
                        type = EventConstant.WORK_SCALE_OVER;
                    } else if (position == 7) {
                        type = EventConstant.WORK_BAD_COMMNET;
                    } else if (position == 8) {
                        type = EventConstant.WORK_REPEAT;
                    } else if (position == 9) {
                        type = EventConstant.WORK_SHOW_SEQ;
                    } else {
                        type = EventConstant.WORK_ASSOCIATION_DELETE;
                    }
                    if (helper != null) {
                        helper.recordUserAction(context, type, workId, "");
                    }
                    dialog.cancel();
                } else if (position == 4) {
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

}

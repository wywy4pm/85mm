package com.arun.a85mm.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.arun.a85mm.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by wy on 2017/5/15.
 */

public class ShareHelper {

    private static SHARE_MEDIA getPlatform(View view) {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;

        switch (view.getId()) {
            case R.id.layout_wechat:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.layout_pengyouquan:
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.layout_sina:
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.layout_qq:
                platform = SHARE_MEDIA.QQ;
                break;
        }
        return platform;
    }

    public static void share(Activity activity, View view, String title, String description, String url, String shareImage) {
        SHARE_MEDIA platform = getPlatform(view);
        UMWeb web = new UMWeb(url);
        if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            web.setTitle(description);
        } else {
            web.setTitle(title);
        }
        web.setThumb(new UMImage(activity, R.mipmap.ic_launcher));
        web.setDescription(description);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                }).share();
    }

}

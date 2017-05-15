package com.arun.a85mm.helper;

import android.app.Activity;
import android.content.Context;

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
    public static void openShare(Activity activity, String title, String description, String url, String shareImage) {
        share(activity, title, description, url, shareImage);
    }

    private static void share(Activity activity, String title, String description, String url, String shareImage) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(activity, R.mipmap.ic_launcher));
        web.setDescription(description);
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
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
                }).open();
    }
}

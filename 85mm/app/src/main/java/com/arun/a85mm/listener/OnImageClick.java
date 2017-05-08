package com.arun.a85mm.listener;

/**
 * Created by wy on 2017/5/8.
 */

public interface OnImageClick {
    void onCountClick(int groupPosition);

    void onCoverClick(String workId,String coverUrl, int width, int height);

    void onMoreLinkClick(String sourceUrl);
}

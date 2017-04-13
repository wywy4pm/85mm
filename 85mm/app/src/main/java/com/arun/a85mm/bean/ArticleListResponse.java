package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/4/13.
 */
public class ArticleListResponse {

    /**
     * articleList : [{"brief":"sssssss","headImage":"https://photo.tuchong.com/1054687/f/25325309.jpg","id":8,"title":"ssss"},{"brief":"浣犳槸鍚﹁sRGB锛孭roPhoto RGB锛�16浣嶆瘡閫氶亾绛夌瓑璇嶈鎼炲緱鏅曞ご杞悜锛屼笉鐭ラ亾鍦ㄥ悗鏈熷鐞嗗拰杈撳嚭鍥剧墖鏃惰濡備綍璁剧疆锛� 鏈枃灏嗚缁嗚皥璋堣壊鍩熷拰鑹叉繁鐨勭浉鍏崇煡璇嗭紝浠ュ強鍚庢湡娴佺▼涓殑鑹插僵璁剧疆銆�","headImage":"https://photo.tuchong.com/395013/l/20015654.jpg","id":7,"title":"鑹插煙鍜岃壊娣辨槸浠\u20ac涔堥"},{"brief":"绠\u20ac娲�","headImage":"http://pub.alltuu.com/work/PG52249/content131349.jpg@2426w_1360h.jpg","id":6,"title":"瀹変節涔濆悰鐨勬棩甯�"},{"brief":"浣嗘晥鏋滆兘鎸佺画澶氫箙锛屽氨涓嶅ソ璇翠簡銆�","headImage":"http://img.qdaily.com/uploads/20170410175931l2jdmQ4MfIZhYJUx.jpg-WebpWebW640","id":5,"title":"鑷冲皯缇庡浗鐨勭數瑙嗘柊闂讳笟锛屾槸闈犵潃鐗规湕鏅尟鍏翠簡锝滃ソ濂囧績灏忔暟鎹�"},{"brief":"鎴戜滑姣忓ぉ閮戒负浣犳帹鑽愬嚑涓渶杩戝彂鐜扮殑濂借璁°\u20ac�","headImage":"http://img.qdaily.com/article/article_show/20170406024256VKmGpobNHvOY8X7P.jpg?imageMogr2/auto-orient/thumbnail/!755x450r/gravity/Center/crop/755x450/quality/85/format/jpg/ignore-error/1","id":2,"title":"璁╁鐢熷厷鏉ヨ璁￠厭搴楀鍏凤紝缁撴灉灏辨槸杩欐牱"},{"brief":"浣嗕綔涓轰腑鍥借浼楃殑鍚挋缇庡墽锛屻\u20ac婅秺鐙便\u20ac嬪湪鍥藉唴鏄釜鐗规畩鐨勫瓨鍦ㄣ\u20ac�","headImage":"http://img.qdaily.com/article/article_show/20170406152124NDbL6FlYdmVByh2z.jpg?imageMogr2/auto-orient/thumbnail/!755x450r/gravity/Center/crop/755x450/quality/85/format/jpg/ignore-error/1","id":1,"title":"銆婅秺鐙便\u20ac嬬浜斿鍥炲綊棣栨挱锛岀編鍥戒汉瀵瑰畠鍏磋叮涓\u20ac鑸�"}]
     * code : 200
     */

    public int code;
    public List<ArticleListBean> articleList;

    public static class ArticleListBean {
        /**
         * brief : sssssss
         * headImage : https://photo.tuchong.com/1054687/f/25325309.jpg
         * id : 8
         * title : ssss
         */

        public String brief;
        public String headImage;
        public int id;
        public String title;
    }
}

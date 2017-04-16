package com.arun.a85mm.bean;

import java.util.List;

/**
 * Created by WY on 2017/4/14.
 */
public class ArticleDetailResponse {


    public ArticleBean article;
    public int code;

    public static class ArticleBean {
        /**
         * author :
         * authorHeadImg :
         * contentComponents : [{"alignment":"left","componentType":"title","text":"4鎷涙暀浣犳崟鎹夊涓殑鑷劧鍏� 鎷嶅嚭鍏呮弧姘旀皼鐨勪綔鍝�","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鎽勫奖甯圓myShire鏃╁厛鍐欎簡涓\u20ac绡囨枃绔狅紝鍚戞垜浠垎浜簡4涓皬鎶\u20ac宸э紝鏁欎綘濡備綍鍒╃敤瀹朵腑鐨勮嚜鐒跺厜鍜屼竴浜涘皬閬撳叿锛屽湪瀹朵腑鐨勭獥鍓嶅皬绌洪棿灏辫兘鎷嶅嚭鍏呮弧姘旀皼鐨勪綔鍝併\u20ac�","textColor":"#000000"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/310/cepb0x2DOlM.jpg"},{"componentType":"paragraph","text":"1銆佽瀵熷厜绾跨殑鍙樺寲","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鎽勫奖鏄敤鍏夌殑鑹烘湳锛屽浠讳綍涓\u20ac寮犵収鐗囨潵璇达紝鍏夊氨鏄竴鍒囥\u20ac傛垜浠鍋氱殑灏辨槸瀵绘壘閭ｄ簺鏈夌壒娈婃\u20acц川鐨勫厜锛屽畠浠湁鍔╀簬鎻愰珮浣犵殑鐓х墖璐ㄩ噺锛岃浣犵殑浣滃搧鐪嬭捣鏉ユ洿鍏锋垙鍓ф晥鏋溿\u20ac�","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鍏夌嚎绌块\u20ac忕獥鎴凤紝鍐嶆姇灏勫埌鍦版澘鎴栧澹佷笂浼氬垱閫犲嚭鏌斿拰鐨勫厜褰辨晥鏋溿\u20ac傜壒鍒娉ㄦ剰涓\u20ac澶╁唴姣忎釜涓嶅悓鐨勫鑺傦紝涓嶅悓鐨勬椂闂存鍏夌嚎鍦ㄤ綘鐨勬埧闂村唴鐨勫彉鍖栵紝浜嗚В鍚勪釜鏃堕棿鍏夌嚎鐨勭壒鐐广\u20ac�","textColor":"#000000"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/311/ceBjhK28mI4Y.jpg"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鍦ㄩ粍鏄忔垨鑰呮竻鏅ㄦ椂鍒嗘洿瀹规槗鎹曟崏鍒拌繖鏍锋煍鍜岀殑鍏夌嚎銆�","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac榛勬槒鐨勬椂鍊欏お闃充綅缃緝浣庯紝杩欐椂鎴块棿浼氬彉寰椾竴鐗囬噾榛勶紝涔熸鏄媿鐓х殑濂芥椂鏈恒\u20ac�","textColor":"#000000"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/312/cew5OMjoRFv0c.jpg"},{"componentType":"paragraph","text":"2銆佸埗閫犵儫闆剧殑鏈﹁儳鏁堟灉","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鍒涢\u20ac犲嚭鐑熼浘鎰熺殑閬撳叿鏈夊緢澶氾紝鍦ㄥ厜绾跨殑鐓ц\u20ac\u20ac涓嬶紝鐑熼浘鍙互甯姪浣犳洿鏈夋晥鐨勬崟鎹夎繖浜涚編涓界殑鍏夌嚎銆備綘鍙互浣跨敤涓\u20ac涓皬鍨嬬殑鐑熼浘鏈恒\u20ac�","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac甯傞潰涓婂彲浠ュ埗閫犵儫闆剧殑浜у搧鏈夊緢澶氾紝璁板緱浣犳槸鍦ㄥ閲屾媿鐓э紝鎵\u20ac浠ュ崈涓囪娉ㄦ剰鐑熼浘鐨勫畨鍏ㄦ\u20acу拰鍋ュ悍鎬э紝浣犺偗瀹氫笉甯屾湜灏忔湅鍙嬪惛鍒版湁姣掔殑鐑熼浘銆傚綋鐒朵綘涔熷彲浠ヤ娇鐢ㄥ共鍐帮紝涓嶈繃閭ｆ牱鏁堟灉浼氫笉鍚屻\u20ac�","textColor":"#000000"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/313/ceMeVOH6prRS.jpg"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac涓嶈鐜╁効鐏紒锛�","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac鍦ㄦ埧闂撮噷鍒堕\u20ac犵儫闆撅紝鐒跺悗绛夊緟鍏夌嚎绌胯繃杩欎簺鐑熼浘锛岃繖鏃跺\u20ac欏揩鍙瀛愪滑杩涙潵鎷嶇収鍚с\u20ac�","textColor":"#000000"},{"componentType":"paragraph","text":"3銆佸阀濡欑殑鍒╃敤闃村奖","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac浣犺繕闇\u20ac瑕佽\u20ac冭檻鍒伴槾褰便\u20ac傚綋鍏夌嚎鍜岄槾褰卞鍋氭槑鏆楀姣旀椂锛屼細鏇村姞绐佸嚭鍏夌嚎鐨勬晥鏋溿\u20ac備綘杩樺彲浠ュ埄鐢ㄩ槾褰辨媿鍑轰竴浜涙洿鐗瑰埆鐨勬晥鏋滐紝渚嬪鍓奖銆�","textColor":"#000000"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/314/ceSrlTSLaMqLk.jpg"},{"componentType":"fullImage","imageUrl":"http://img2.fengniao.com/product/157_700x2000/315/cenAg3o17zYI.jpg"},{"componentType":"paragraph","text":"4銆佸埄鐢ㄥ厜婧愬埗閫犱笉鍚岀殑鍥炬鏁堟灉","textColor":"#000000"},{"componentType":"paragraph","text":"銆\u20ac銆\u20ac浣犲彲浠ュ皾璇曞湪鍏夋簮鍓嶅埗閫犱笉鍚岀殑鍥炬鏁堟灉銆傛牴鎹厜婧愮┛閫忕殑鏂瑰悜鎽嗘斁閬撳叿锛屽埄鐢ㄩ亾鍏风殑褰㈢姸閫嗗厜杩涜鎷嶆憚锛屽垱閫犱笉涓\u20ac鏍风殑鍓奖鏁堟灉銆�","textColor":"#000000"}]
         * createTime :
         * id : 11
         * title :
         */

        public String author;
        public String authorHeadImg;
        public String createTime;
        public int id;
        public String title;
        public List<ArticleDetailBean> contentComponents;

        /*public static class ArticleDetailBean {
            *//**
             * alignment : left
             * componentType : title
             * text :
             * textColor : #000000
             * imageUrl : http://img2.fengniao.com/product/157_700x2000/310/cepb0x2DOlM.jpg
             *//*

            public String alignment;
            public String componentType;
            public String text;
            public String textColor;
            public String imageUrl;
        }*/
    }
}

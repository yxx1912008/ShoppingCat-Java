package cn.luckydeer.common.constants.webmagic;

/**
 * 
 * 抓取请求的xpath语句
 * 
 * @author yuanxx
 * @version $Id: XpathConstant.java, v 0.1 2018年6月22日 上午10:21:36 yuanxx Exp $
 */
public interface XpathConstant {

    /** 首页海报抓取 根位置  */
    String INDEX_POSTER_TARGET_XPATH     = "//div[@class='unslider swiper-container fl']/div/div";
    /**  海报图片链接地址   */
    String INDEX_POSTER_IMG_XPATH        = "//div/a/img/@src";
    /** 海报链接地址  */
    String INDEX_POSTER_TARGET_URL_XPATH = "//div/a/@href";

}

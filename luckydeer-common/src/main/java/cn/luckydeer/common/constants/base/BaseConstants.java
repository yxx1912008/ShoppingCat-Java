package cn.luckydeer.common.constants.base;

/**
 * 基础参数
 * 
 * @author yuanxx
 * @version $Id: BaseConstants.java, v 0.1 2018年8月26日 下午3:09:07 yuanxx Exp $
 */
public interface BaseConstants {

    /**
     * 主站地址 用于 采集 商品信息 ，不建议采用自己的商品网站
     * 会给自己的服务器造成压力
     * 所以 次要信息从其它人网站抓取
     * 关键商品信息 还是用自己的 ，因为需要商品码 
     */
    String MAIN_BASE_URL      = "http://www.haowuzi.net/index.php?";

    //关键站点根地址
    String IMPORT_BASE_URL    = "http://star0393.com/index.php?";

    //淘宝商品主图信息接口
    String TAOBAO_FOOD_IMG    = "http://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?";

    //默认请求超时时间
    int    DEFAULT_TIME_OUT   = 3000;

    //大淘客网站内部ID
    String DATAOKE_USER_ID    = "675425";

    //默认缓存更新时间
    int    DEFAULT_CACHE_TIME = 120;

    //大淘客Api平台host
    String DTK_MAIN_URL       = "http://api.dataoke.com/index.php?";

    //小程序 数据请求 基础地、地址
    String WX_BASE_API_URL    = "https://api.luckydeer.cn/";

}

package cn.luckydeer.webmagic.constants;

/**
 * Http请求常量
 * 
 * @author yuanxx
 * @version $Id: HttpConstant.java, v 0.1 2018年6月22日 上午9:19:04 yuanxx Exp $
 */
public interface WebmagicConstant {

    //购物猫网站网址
    String  CAT_HOST                   = "http://star0393.com/index.php";

    //默认开启线程
    Integer DEFAULT_THREAD             = 5;

    //默认爬虫抓取等待时间
    Integer DEFAULT_WAIT_TIME          = 180000;

    //默认监听休眠时间
    Integer DEFAULT_MONITOR_SLEEP_TIME = 50;

    //缓存键值
    String  POST_CACHE_KEY             = "expiryTime";

    //首页海报缓存时间
    Integer INDEX_POSTER_CACHED_TIME   = 120;

}

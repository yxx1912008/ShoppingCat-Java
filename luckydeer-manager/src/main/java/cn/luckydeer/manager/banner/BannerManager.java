package cn.luckydeer.manager.banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.manager.api.WebCrawlApi;

/**
 * 首页海报管理
 * 
 * @author yuanxx
 * @version $Id: BannerManager.java, v 0.1 2018年8月24日 下午4:19:11 yuanxx Exp $
 */
public class BannerManager {

    /**
     * 
     * 注解：获取首页海报
     * @return
     * @author yuanxx @date 2018年8月24日
     */
    public ResponseObj getBanner() {
        return new ResponseObj(WebCrawlApi.getBanner());
    }

    public static void main(String[] args) throws Exception {

        Document doc = Jsoup.connect("http://star0393.com/index.php?r=l&kw=小笼包").get();

        System.out.println(doc.toString());

    }
}

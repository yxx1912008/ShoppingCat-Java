package cn.luckydeer.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import cn.luckydeer.webmagic.constants.XpathConstant;

/**
 * 首页海报
 * 
 * @author yuanxx
 * @version $Id: IndexPoster.java, v 0.1 2018年6月22日 上午9:59:27 yuanxx Exp $
 */
public class IndexPoster implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        String num = page.getHtml().xpath(XpathConstant.INDEX_POSTER_TARGET_URL_XPATH).toString();
        System.out.println(num);
    }

    public static void main(String[] args) {
        Spider.create(new IndexPoster()).addUrl("http://star0393.com/index.php")
        //开启5个线程抓取
            .thread(5)
            //启动爬虫
            .run();

    }

}

package cn.luckydeer.dispatch.task.movie;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

/**
 * 小鹿影音定时任务
 * 
 * @author yuanxx
 * @version $Id: MovieTask.java, v 0.1 2018年9月30日 上午9:25:24 yuanxx Exp $
 */
public class MovieTask {

    private static final Log logger = LogFactory.getLog("LUCKYDEER-TASK-LOG");

    /**
     * 
     * 注解：定时执行任务
     * @author yuanxx @date 2018年9月30日
     */
    public void run() {

        logger.info("==============开始抓取电影数据==============");

        try {
            Jsoup.connect("http://wq.luckydeer.cn/api.php/timming/index.html?name=qiangqiang")
                .timeout(5000).get();
        } catch (IOException e) {
            logger.error("抓取电影数据连接超时", e);
        }

    }

}

package cn.luckydeer.dispatch.task.cat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.luckydeer.manager.api.WebCrawlApi;

/**
 * 购物猫定时任务 
 * 定时 请求 购物猫 
 * 更新 购物猫缓存
 * 
 * @author yuanxx
 * @version $Id: CatRequestController.java, v 0.1 2018年9月25日 下午1:40:05 yuanxx Exp $
 */
public class CatRequestTask {

    private static final Log logger = LogFactory.getLog("LUCKYDEER-TASK-LOG");

    /**
     * 
     * 注解：更新购物猫缓存
     * 加快系统冷启动反应速度
     * @author yuanxx @date 2018年9月25日
     */
    public void run() {
        logger.info("开始启动更新购物猫缓存的定时任务");
        WebCrawlApi.getBanner();//更新海报
        WebCrawlApi.getTicketLive("1");
        WebCrawlApi.getCurrentQiang();
        logger.info("更新缓存任务结束");
    }

}

package cn.luckydeer.dispatch.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 测试调度任务
 * 
 * @author yuanxx
 * @version $Id: DemoTask.java, v 0.1 2018年9月25日 上午10:11:02 yuanxx Exp $
 */
public class DemoTask {

    private static final Log logger = LogFactory.getLog("LUCKYDEER-TASK-LOG");

    /**
     * 
     * 注解：运行调度任务
     * @author yuanxx @date 2018年9月25日
     */
    public void run() {
        logger.info("开始定时任务");
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        logger.info("执行总时长为：" + queryTime);
        logger.info("结束定时调度任务");

    }

}

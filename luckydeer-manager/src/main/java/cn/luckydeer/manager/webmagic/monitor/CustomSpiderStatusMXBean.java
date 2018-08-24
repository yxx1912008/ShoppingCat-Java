package cn.luckydeer.manager.webmagic.monitor;

import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

/**
 * 监听爬虫状态
 * 
 * @author yuanxx
 * @version $Id: CustomSpiderStatusMXBean.java, v 0.1 2018年6月25日 上午9:37:03 yuanxx Exp $
 */
public interface CustomSpiderStatusMXBean extends SpiderStatusMXBean {

    /**  获取调度名称  */
    public String getSchedulerName();

}

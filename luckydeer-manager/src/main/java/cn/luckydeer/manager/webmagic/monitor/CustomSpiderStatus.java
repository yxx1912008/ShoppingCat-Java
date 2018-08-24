package cn.luckydeer.manager.webmagic.monitor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor.MonitorSpiderListener;
import us.codecraft.webmagic.monitor.SpiderStatus;

public class CustomSpiderStatus extends SpiderStatus implements CustomSpiderStatusMXBean {

    public CustomSpiderStatus(Spider spider, MonitorSpiderListener monitorSpiderListener) {
        super(spider, monitorSpiderListener);
    }

    /**
     * @see cn.luckydeer.manager.webmagic.monitor.CustomSpiderStatusMXBean#getSchedulerName()
     */
    @Override
    public String getSchedulerName() {
        return spider.getScheduler().getClass().getName();
    }

}

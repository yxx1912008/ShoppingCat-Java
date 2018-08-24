package cn.luckydeer.manager.webmagic.monitor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

public class CustomSpiderMonitor extends SpiderMonitor {

    @Override
    public SpiderStatusMXBean getSpiderStatusMBean(Spider spider,
                                                   MonitorSpiderListener monitorSpiderListener) {
        return new CustomSpiderStatus(spider, monitorSpiderListener);
    }

}

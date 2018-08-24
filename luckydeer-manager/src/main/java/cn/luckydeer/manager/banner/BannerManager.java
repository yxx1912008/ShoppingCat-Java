package cn.luckydeer.manager.banner;

import cn.luckydeer.manager.api.BannerApi;

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
    public String getBanner() {
        return BannerApi.getBanner();
    }

}

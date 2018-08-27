package cn.luckydeer.manager.cat;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.manager.api.WebCrawlApi;
import cn.luckydeer.model.banner.BannerModel;

/**
 * 购物猫接口管理
 * 
 * @author yuanxx
 * @version $Id: CatManager.java, v 0.1 2018年8月27日 上午9:25:25 yuanxx Exp $
 */
public class CatManager {

    Logger logger = LoggerFactory.getLogger("LUCKYDEER-MANAGER-LOG");

    /**
     * 
     * 注解：获取首页海报
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public ResponseObj getBanner() {
        List<BannerModel> list = WebCrawlApi.getBanner();
        return new ResponseObj(list);
    }

    /**
     * 
     * 注解：获取咚咚抢商品列表
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public String getDDongQ() {
        String result = WebCrawlApi.getDDongQ();
        if (StringUtils.isBlank(result)) {
            logger.error("获取抢购商品列表失败");
            return "获取信息失败";
        }
        return result;
    }

    /**
     * 
     * 注解：获取 9.9包邮商品列表
     * @param page
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public String getNine(String page) {

        String result = WebCrawlApi.getNine(page);
        System.out.println(result);
        if (StringUtils.isBlank(result)) {
            return "获取商品列表失败";
        }
        return result;
    }

    /**
     * 
     * 注解：获取 实时疯抢榜
     * @param catId
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public String getRealTime(Integer catId) {
        return WebCrawlApi.getRealTime(catId);
    }

}

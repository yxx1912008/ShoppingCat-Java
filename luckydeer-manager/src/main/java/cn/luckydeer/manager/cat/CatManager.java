package cn.luckydeer.manager.cat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.common.utils.http.HttpClientSend;
import cn.luckydeer.dao.cat.daoInterface.IWxAppStatusDao;
import cn.luckydeer.dao.cat.dataobject.WxAppStatusDo;
import cn.luckydeer.manager.api.WebCrawlApi;
import cn.luckydeer.model.banner.BannerModel;

import com.alibaba.fastjson.JSON;

/**
 * 购物猫接口管理
 * 
 * @author yuanxx
 * @version $Id: CatManager.java, v 0.1 2018年8月27日 上午9:25:25 yuanxx Exp $
 */
public class CatManager {

    Logger                  logger = LoggerFactory.getLogger("LUCKYDEER-MANAGER-LOG");

    private IWxAppStatusDao wxAppStatusDao;

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

    /**
     * 
     * 注解：搜索商品信息
     * @param keyWords
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public String searchGood(String keyWords) {
        return WebCrawlApi.searchGood(keyWords);
    }

    /**
     * 
     * 注解：获取商品详情
     * @param goodId
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public String getGoodDetail(String goodId) {
        return WebCrawlApi.getGoodDetailNew(goodId);
    }

    /**
     * 
     * 注解：根据商品ID 获取商品 购买码
     * @param goodId
     * @return
     * @author yuanxx @date 2018年9月5日
     */
    public String getGoodCodeText(String goodId) {
        return WebCrawlApi.getGoodCodeText(goodId);
    }

    /**
     * 
     * 注解：获取领券直播商品列表
     * @param page
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    public String getTicketLive(String page) {
        return WebCrawlApi.getTicketLive(page);
    }

    /**
     * 
     * 注解：获取正在抢商品列表
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    public String getCurrentQiang() {
        return WebCrawlApi.getCurrentQiang();
    }

    /**
     * 
     * 注解：通过商品真实ID获取商品的主图信息
     * @param realGoodId
     * @return
     * @author yuanxx @date 2018年9月13日
     */
    public String getGoodDescImg(String realGoodId) {
        return WebCrawlApi.getGoodDescImg(realGoodId);
    }

    /**
     * 
     * 注解：通过商品真实ID获取商品信息
     * @param realGoodId
     * @return
     * @author yuanxx @date 2018年9月17日
     */
    public String getGoodDetailByRealId(String realGoodId) {
        return WebCrawlApi.getGoodDetailByRealId(realGoodId);
    }

    /**
     * 
     * 注解：查询微信小程序状态
     * @param versionId
     * @return
     * @author yuanxx @date 2018年9月18日
     */
    public WxAppStatusDo getWxAppStatus(String versionId) {
        return wxAppStatusDao.selectByPrimaryKey(versionId);
    }

    public String getNewsBanner() {

        String url = "https://spider.dcloud.net.cn/api/banner/36kr";
        try {
            Response res = Jsoup.connect(url).ignoreContentType(true).execute();
            return res.body();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public String getNewsDetail(String postId) throws IOException {
        String url = "https://spider.dcloud.net.cn/api/news/36kr/" + postId;
        Response res = Jsoup.connect(url).ignoreContentType(true).execute();
        return res.body();
    }

    public String getNews() {
        String url = "https://spider.dcloud.net.cn/api/news";
        try {
            Response res = Jsoup.connect(url).ignoreContentType(true).execute();
            return res.body();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public String queryAgent(String areaName) {
        String urlString = "http://member.icaomei.com/acaomei/qufx/search.do?areaName="+areaName;
        Map<String, String> headerParameter = new HashMap<String, String>();
        headerParameter.put("content-type", "application/x-www-form-urlencoded");
        String jsonParam = JSON.toJSONString(new HashMap<>().put("areaName", areaName));
        String res = HttpClientSend.jsonPost(urlString, headerParameter, jsonParam, "UTF-8");
        return res;
    }

    public void setWxAppStatusDao(IWxAppStatusDao wxAppStatusDao) {
        this.wxAppStatusDao = wxAppStatusDao;
    }

}

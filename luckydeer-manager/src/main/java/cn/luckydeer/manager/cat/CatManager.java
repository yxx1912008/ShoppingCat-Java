package cn.luckydeer.manager.cat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import cn.luckydeer.common.constants.base.BaseConstants;
import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.common.utils.cache.CacheData;
import cn.luckydeer.common.utils.date.DateUtilSelf;
import cn.luckydeer.common.utils.email.AliyunEmail;
import cn.luckydeer.common.utils.email.EmailOrder;
import cn.luckydeer.common.utils.http.HttpClientSend;
import cn.luckydeer.common.utils.thread.ExecutorServiceUtils;
import cn.luckydeer.common.utils.wechat.model.WeixinPicTextItem;
import cn.luckydeer.dao.cat.daoInterface.IWxAppStatusDao;
import cn.luckydeer.dao.cat.dataobject.WxAppStatusDo;
import cn.luckydeer.manager.api.WebCrawlApi;
import cn.luckydeer.model.banner.BannerModel;
import cn.luckydeer.model.cat.SearchGoodInfo;
import cn.luckydeer.model.enums.WebCrawEnums;

import com.alibaba.fastjson.JSON;

/**
 * 购物猫接口管理
 * 
 * @author yuanxx
 * @version $Id: CatManager.java, v 0.1 2018年8月27日 上午9:25:25 yuanxx Exp $
 */
public class CatManager {

    private static final Logger           logger = LoggerFactory.getLogger("LUCKYDEER-MANAGER-LOG");

    //缓存 
    private static Map<String, CacheData> cache  = new ConcurrentHashMap<String, CacheData>();

    private IWxAppStatusDao               wxAppStatusDao;

    private WebCrawlApi                   webCrawlApi;

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
        return webCrawlApi.getGoodDetailNew(goodId);
    }

    /**
     * 
     * 注解：根据商品ID 获取商品 购买码
     * @param goodId
     * @return
     * @author yuanxx @date 2018年9月5日
     */
    public String getGoodCodeText(final HttpServletRequest request, final String goodId) {

        ExecutorServiceUtils.getExcutorPools().execute(new Runnable() {
            @Override
            public void run() {
                String ip = request.getHeader("x-forwarded-for");
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                EmailOrder emailOrder = new EmailOrder();
                emailOrder.setContent("有用户主动领取商品优惠券,时间:"
                                      + DateUtilSelf.simpleFormat(new Date())
                                      + "<br>用户的Ip地址为："
                                      + (StringUtils.equals(ip, "0:0:0:0:0:0:0:1") ? "127.0.0.1"
                                          : ip) + "<br>商品链接地址为:" + BaseConstants.IMPORT_BASE_URL
                                      + "r=p/d&id=" + goodId);
                emailOrder.setTitle("购物猫用户主动领取优惠券通知");
                emailOrder.setReceives(BaseConstants.EMAIL_RECEIVES);
                AliyunEmail.send(emailOrder);
            }
        });
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
    public List<String> getGoodDescImg(String realGoodId) {
        return webCrawlApi.getGoodDescImgs(realGoodId);
    }

    /**
     * 
     * 注解：通过商品真实ID获取商品信息
     * @param realGoodId
     * @return
     * @author yuanxx @date 2018年9月17日
     */
    public String getGoodDetailByRealId(String realGoodId) {
        return webCrawlApi.getGoodDetailByRealId(realGoodId);
    }

    /**
     * 
     * 注解：搜索五个商品信息
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    public List<WeixinPicTextItem> getSearchGoods(String keyWords) {

        String result = searchGood(keyWords);
        if (StringUtils.isNotBlank(result)) {
            //将搜索结果转换为数组
            List<SearchGoodInfo> goodList = JSON.parseArray(result, SearchGoodInfo.class);
            if (CollectionUtils.isEmpty(goodList)) {
                return null;
            }
            int fromIndex = 0;
            //如果搜索结果查过五条 ，就查询前五条 否则提取前几个
            int toIndex = goodList.size() > 5 ? 4 : goodList.size();
            List<SearchGoodInfo> topList = goodList.subList(fromIndex, toIndex);
            List<WeixinPicTextItem> resultList = new ArrayList<>();
            WeixinPicTextItem picTextItem = null;
            for (SearchGoodInfo searchGoodInfo : topList) {
                picTextItem = new WeixinPicTextItem();
                picTextItem.setDescription(searchGoodInfo.getQuan_jine() + "元优惠券，点击领取");
                picTextItem.setPicUrl(searchGoodInfo.getPic());
                picTextItem.setTitle(searchGoodInfo.getD_title());
                picTextItem.setUrl(BaseConstants.IMPORT_BASE_URL + "r=p/d&id="
                                   + searchGoodInfo.getGoodsid() + "&type=3");
                resultList.add(picTextItem);
            }
            return resultList;
        }
        return null;
    }

    /**
     * 
     * 注解：查询微信小程序状态
     * 使用缓存功能 缓存一天
     * @param versionId
     * @return
     * @author yuanxx @date 2018年9月18日
     */
    public WxAppStatusDo getWxAppStatus(String versionId) {
        String key = WebCrawEnums.WX_STATUS.getCode() + versionId;
        if (getCacheTimeOut(key)) {
            CacheData data = cache.get(key);
            return (WxAppStatusDo) data.getData();
        }
        WxAppStatusDo info = wxAppStatusDao.selectByPrimaryKey(versionId);

        if (null != info) {
            info.setBaseUrl(BaseConstants.WX_BASE_API_URL);
            updateCache(info, key);
        }

        return info;
    }

    /**
     * 
     * 注解：购物猫伪装功能 查询代理商区域费用
     * @param areaName
     * @return
     * @author yuanxx @date 2018年9月21日
     */
    public String queryAgent(String areaName) {
        String urlString = "http://member.icaomei.com/acaomei/qufx/search.do?areaName=" + areaName;
        Map<String, String> headerParameter = new HashMap<String, String>();
        headerParameter.put("content-type", "application/x-www-form-urlencoded");
        String jsonParam = JSON.toJSONString(new HashMap<>().put("areaName", areaName));
        String res = HttpClientSend.jsonPost(urlString, headerParameter, jsonParam, "UTF-8");
        return res;
    }

    /**
     * 
     * 注解：判断缓存是否过期
     * 当前仅存放 一天的小程序状态缓存 
     * 省去 经常去数据库捞取
     * @param key
     * @return
     * @author yuanxx @date 2018年9月25日
     */
    private static boolean getCacheTimeOut(String key) {
        CacheData cahe = cache.get(key);
        if (null == cahe) {
            return false;
        }
        Date now = new Date();
        Date expiryTime = cahe.getExpiryTime();
        int count = DateUtilSelf.calculateDecreaseMinute(expiryTime, now);
        if (count > 1440) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 注解：更新缓存
     * @param data
     * @param key
     * @author yuanxx @date 2018年9月25日
     */
    private static void updateCache(Object data, String key) {
        Date date = new Date();
        Date expiryTime = DateUtilSelf.increaseHour(date, 24);
        CacheData cacheData = new CacheData();
        cacheData.setKey(key);
        cacheData.setExpiryTime(expiryTime);
        cacheData.setData(data);
        cache.put(key, cacheData);
    }

    /**
     * 
     * 注解：清空缓存
     * @author yuanxx @date 2018年9月26日
     */
    public void clearCache() {
        cache.clear();
        WebCrawlApi.clearCache();
    }

    public void setWxAppStatusDao(IWxAppStatusDao wxAppStatusDao) {
        this.wxAppStatusDao = wxAppStatusDao;
    }

    public void setWebCrawlApi(WebCrawlApi webCrawlApi) {
        this.webCrawlApi = webCrawlApi;
    }

}

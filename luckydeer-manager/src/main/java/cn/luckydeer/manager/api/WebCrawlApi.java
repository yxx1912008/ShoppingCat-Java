package cn.luckydeer.manager.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.constants.base.BaseConstants;
import cn.luckydeer.common.utils.DateUtilSelf;
import cn.luckydeer.common.utils.cache.CacheData;
import cn.luckydeer.model.banner.BannerModel;
import cn.luckydeer.model.enums.WebCrawEnums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 获取海报
 * @author yuanxx
 * @version $Id: BannerApi.java, v 0.1 2018年8月24日 下午4:58:43 yuanxx Exp $
 */
public class WebCrawlApi {

    private static Logger                 logger       = LoggerFactory.getLogger(WebCrawlApi.class);

    /** 网页抓取缓存 固定时间更新 避免多次抓取 节约资源  */
    private static Map<String, CacheData> webCrawCache = new ConcurrentHashMap<String, CacheData>();

    private static String                 nine_cac_id;                                              //9.9包邮 

    private static String                 live_cac_id;                                              //领券直播

    /**
     * 
     * 注解：获取首页海报
     * @author yuanxx @date 2018年8月24日
     */
    @SuppressWarnings("unchecked")
    public static List<BannerModel> getBanner() {

        String key = WebCrawEnums.BANNER.getCode();

        boolean flag = getCacheTimeOut(key);

        if (flag) {
            CacheData cache = webCrawCache.get(key);
            return (List<BannerModel>) cache.getData();
        }

        //请求海报信息
        Document doc;
        try {
            doc = Jsoup.connect(BaseConstants.MAIN_BASE_URL)
                .timeout(BaseConstants.DEFAULT_TIME_OUT).get();
            Elements elements = doc.getElementsByClass("swiper-slide");
            Iterator<Element> it = elements.iterator();
            BannerModel model = null;
            List<BannerModel> list = new ArrayList<>();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                model = new BannerModel();
                String goodId = element.select("a").attr("data-gid");
                String imgUrl = element.select("img").attr("src");
                model.setBannerImg(imgUrl.trim());
                model.setGoodId(goodId);
                list.add(model);
            }
            updateCache(list, key);
            return list;
        } catch (Exception e) {
            logger.error("首页海报获取失败", e);
            return null;
        }
    }

    /**
     * 
     * 注解：判断缓存是否过期
     * @param key
     * @return
     * @author yuanxx @date 2018年8月26日
     */
    private static boolean getCacheTimeOut(String key) {
        CacheData cahe = webCrawCache.get(key);
        if (null == cahe) {
            return false;
        }
        Date now = new Date();
        Date expiryTime = cahe.getExpiryTime();
        int count = DateUtilSelf.calculateDecreaseMinute(expiryTime, now);
        if (count > BaseConstants.DEFAULT_CACHE_TIME) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 注解：更新缓存
     * @param data
     * @param key
     * @author yuanxx @date 2018年8月26日
     */
    private static void updateCache(Object data, String key) {
        Date date = new Date();
        Date expiryTime = DateUtilSelf.increaseHour(date, 2);
        CacheData cacheData = new CacheData();
        cacheData.setKey(key);
        cacheData.setExpiryTime(expiryTime);
        cacheData.setData(data);
        webCrawCache.put(key, cacheData);
    }

    /**
     * 
     * 注解：获取咚咚抢商品列表
     * @return
     * @author yuanxx @date 2018年8月26日
     */
    public static String getDDongQ() {
        String key = WebCrawEnums.QIANGGOU.getCode();
        boolean flag = getCacheTimeOut(key);
        if (flag) {
            CacheData cache = webCrawCache.get(key);
            return (String) cache.getData();
        }
        Document doc;
        try {
            doc = Jsoup.connect(BaseConstants.MAIN_BASE_URL + "r=ddq/wap").get();
            String rexString = "data = (.*?);";
            Pattern pattern = Pattern.compile(rexString);
            Matcher m = pattern.matcher(doc.toString());
            if (m.find()) {
                updateCache(m.group(1).trim(), key);
                return m.group(1).trim();
            }
            return null;
        } catch (IOException e) {
            logger.error("获取咚咚枪信息失败", e);
            return null;
        }
    }

    /**
     * 
     * 注解：获取9.9包邮商品列表
     * @param page
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public static String getNine(String page) {

        //拼接请求参数
        StringBuilder builder = new StringBuilder(BaseConstants.MAIN_BASE_URL);
        builder.append("r=nine/listajax&n_id=58&page=").append(page).append("&cac_id=");
        if (StringUtils.isNotBlank(nine_cac_id)) {
            builder.append(nine_cac_id);
        }
        String url = builder.toString();
        try {
            Document doc = Jsoup.connect(url).get();
            String result = doc.text();
            if (StringUtils.equals("1", page)) {
                String str = JSON.parseObject(result).getJSONObject("data").getString("cac_id");
                nine_cac_id = str;
            }
            return result;
        } catch (IOException e) {
            logger.error("读取9.9包邮商品列表失败", e);
            return "读取9.9包邮商品列表失败";
        }
    }

    /**
     * 
     * 注解：获取实时疯抢榜
     * @param catId
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public static String getRealTime(Integer catId) {

        if (null == catId) {
            catId = 0;
        }
        String key = WebCrawEnums.REAL_TIME.getCode() + catId;
        boolean flag = getCacheTimeOut(key);

        if (flag) {
            CacheData cache = webCrawCache.get(key);
            return (String) cache.getData();
        }
        String url = BaseConstants.MAIN_BASE_URL + "r=realtime/wapajax&cid=" + catId;
        try {
            Document doc = Jsoup.connect(url).timeout(BaseConstants.DEFAULT_TIME_OUT).get();
            String result = doc.text();
            updateCache(result, key);
            return result;
        } catch (IOException e) {
            logger.error("获取实时疯抢榜失败", e);
            return "获取实时疯抢榜失败";
        }
    }

    /**
     * 
     * 注解：搜索 全网优惠券商品
     * @param type
     * @param keyWords
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public static String searchGood(String keyWords) {

        StringBuilder builder = new StringBuilder(BaseConstants.MAIN_BASE_URL);
        builder.append("r=index%2Fsearch&s_type=1&kw=");
        builder.append(keyWords);
        try {
            String url = builder.toString();
            Document doc = Jsoup.connect(url).timeout(BaseConstants.DEFAULT_TIME_OUT).get();
            //正则匹配规则
            String regex = "dtk_data=(.*?);";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(doc.html());
            if (m.find()) {
                return m.group(1).trim();
            }
            logger.error("搜索结果转换失败");
            return null;
        } catch (IOException e) {
            logger.error("搜索商品失败", e);
            return null;
        }
    }

    /**
     * 
     * 注解：获取商品详情
     * @param goodId
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    public static String getGoodDetail(String goodId) {

        StringBuilder builder = new StringBuilder(BaseConstants.DTK_MAIN_URL);
        builder.append("r=port/index&appkey=f0z3ez7qoh&v=2&id=");
        builder.append(goodId);
        String url = builder.toString();
        Document doc;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true)
                .timeout(BaseConstants.DEFAULT_TIME_OUT).post();
            return doc.text();
        } catch (IOException e) {
            logger.error("获取商品详细信息失败", e);
            return null;
        }
    }

    /**
     * 
     * 注解：获取商品 复制码
     * @param goodId
     * @return
     * @author yuanxx @date 2018年9月5日
     */
    public static String getGoodCodeText(String goodId) {

        if (StringUtils.isBlank(goodId)) {
            return null;
        }
        StringBuilder builder = new StringBuilder(BaseConstants.IMPORT_BASE_URL);
        builder.append("r=p/d&id=").append(goodId);
        String url = builder.toString();
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(BaseConstants.DEFAULT_TIME_OUT).get();
            Element element = doc.getElementById("codeText");
            return element.text();
        } catch (IOException e) {
            logger.error("获取商品购买码失败", e);
            return null;
        }
    }

    /**
     * 
     * 注解：获取领券直播商品列表
     * @param page
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    public static String getTicketLive(String page) {

        //拼接请求参数
        StringBuilder builder = new StringBuilder(BaseConstants.MAIN_BASE_URL);
        builder.append("r=index/ajaxnew&page=").append(page).append("&cac_id=");
        if (StringUtils.isNotBlank(live_cac_id)) {
            builder.append(live_cac_id);
        }
        String url = builder.toString();
        try {
            Document doc = Jsoup.connect(url).get();
            String result = doc.text();
            if (StringUtils.equals("1", page)) {
                String str = JSON.parseObject(result).getJSONObject("data").getString("cac_id");
                live_cac_id = str;
            }
            return result;
        } catch (IOException e) {
            logger.error("读取领券直播商品列表失败", e);
            return "读取领券直播商品列表失败";
        }
    }

    /**
     * 
     * 注解：获取正在抢商品列表
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    public static String getCurrentQiang() {

        String key = WebCrawEnums.NEW_QIANG.getCode();
        boolean flag = getCacheTimeOut(key);
        if (flag) {
            CacheData cache = webCrawCache.get(key);
            return (String) cache.getData();
        }
        Document doc;
        try {
            doc = Jsoup.connect(BaseConstants.MAIN_BASE_URL + "r=index/wap").get();
            String rexString = "indexContentNav = (.*?)];";
            Pattern pattern = Pattern.compile(rexString);
            Matcher m = pattern.matcher(doc.toString());
            if (m.find()) {
                String resultString = m.group(1).trim() + "]";

                if (StringUtils.isNotBlank(resultString)) {

                    JSONArray list = JSONArray.parseArray(resultString);
                    JSONArray resultList = new JSONArray();
                    Iterator<Object> it = list.iterator();

                    while (it.hasNext()) {
                        JSONObject object = (JSONObject) it.next();
                        if (StringUtils.equals("indexWillBring", object.getString("type"))) {
                            JSONArray goods = object.getJSONObject("data").getJSONObject("config")
                                .getJSONArray("list");

                            it = goods.iterator();
                            while (it.hasNext()) {
                                JSONObject object2 = (JSONObject) it.next();
                                BigDecimal xiaoliang = object2.getBigDecimal("xiaoliang");
                                BigDecimal yuanjia = object2.getBigDecimal("yuanjia");
                                BigDecimal quanJine = object2.getBigDecimal("quan_jine");

                                if (xiaoliang.compareTo(new BigDecimal("10000")) > 0) {
                                    object2.put("xiaoliang", xiaoliang.divide(new BigDecimal(
                                        "10000"), 2, BigDecimal.ROUND_HALF_UP)+"万件");
                                }else{
                                    object2.put("xiaoliang", xiaoliang+"件");
                                }

                                object2.put("nowPrice",
                                    yuanjia.subtract(quanJine)
                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                                resultList.add(object2);
                            }
                            updateCache(resultList.toString(), key);
                            return resultList.toString();
                        }
                    }
                }

                return null;
            }
            return null;
        } catch (Exception e) {
            logger.error("获取正在抢购商品信息失败", e);
            return null;
        }

    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        System.out.println(WebCrawlApi.getCurrentQiang());
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();

        System.out.println(WebCrawlApi.getCurrentQiang());
        end = System.currentTimeMillis();
        System.out.println(end - start);

    }
}

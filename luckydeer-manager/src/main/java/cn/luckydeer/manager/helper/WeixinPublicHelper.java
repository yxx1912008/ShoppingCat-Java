package cn.luckydeer.manager.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.CollectionUtils;

import cn.luckydeer.common.constants.base.BaseConstants;
import cn.luckydeer.common.constants.weixin.WeixinPublicConfig;
import cn.luckydeer.common.utils.date.DateUtilSelf;
import cn.luckydeer.common.utils.email.AliyunEmail;
import cn.luckydeer.common.utils.email.EmailOrder;
import cn.luckydeer.common.utils.wechat.WeixinOffAccountUtil;
import cn.luckydeer.common.utils.wechat.model.WeixinPicTextItem;
import cn.luckydeer.manager.cat.CatManager;
import cn.luckydeer.model.cat.SearchGoodInfo;
import cn.luckydeer.model.enums.WeixinMsgType;

import com.alibaba.fastjson.JSON;

/**
 * 微信工具类
 * 
 * @author yuanxx
 * @version $Id: WeixinPublicHelper.java, v 0.1 2018年9月27日 下午6:40:10 yuanxx Exp $
 */
public class WeixinPublicHelper {

    private static final Log logger = LogFactory.getLog(WeixinPublicHelper.class);

    private CatManager       catManager;

    /**
     * 
     * 注解：处理微信推送事件和普通事件
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月26日
     */
    @SuppressWarnings("unchecked")
    public String handleWeixin(HttpServletRequest request, HttpServletResponse response) {

        InputStream is;
        String xmlstr = null;
        String msg = "";
        try {
            is = request.getInputStream();
            //将微信发送的流转换为String -utf8类型
            xmlstr = IOUtils.toString(is, "utf-8");
            // System.out.println(xmlstr);
            if (StringUtils.isNotBlank(xmlstr)) {
                Document document;
                document = DocumentHelper.parseText(xmlstr);//将xml文本转换为对象
                //发送方账号
                List<Element> fName = document.selectNodes("/xml/FromUserName");
                String openId = fName.get(0).getText();
                List<Element> tName = document.selectNodes("/xml/ToUserName");
                String toName = tName.get(0).getText();
                List<Element> msgType = document.selectNodes("/xml/MsgType");
                String realType = msgType.get(0).getText();

                //获取消息类型
                WeixinMsgType type = WeixinMsgType.getEnumByCode(realType);
                if (null == type) {
                    return null;
                }
                switch (type) {
                    case EVENT:
                        List<Element> eventList = document.selectNodes("/xml/Event");
                        String event = eventList.get(0).getText();
                        String result = hanleWeixinEvent(event, openId, toName);
                        return result;
                    case TEXT:
                        List<Element> contentList = document.selectNodes("/xml/Content");
                        String content = contentList.get(0).getText();
                        //处理用户的微信回复消息
                        String resultString = hanleWeixinText(openId, toName, content);
                        return resultString;
                    default:
                        break;
                }
            }
            return msg;
        } catch (IOException e) {
            logger.error("收取微信消息失败", e);
        } catch (DocumentException e) {
            logger.error("将文本转换为xml对象失败，文本内容为：" + xmlstr);
        }
        return null;
    }

    /**
     * 
     * 注解：处理 用户向微信发送的 的文本消息
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    public String hanleWeixinText(String fName, String toName, String content) {

        //功能列表 1.搜索电影  2.搜索优惠券 3.添加电影
        if (StringUtils.contains(content, "电影")) {
            content = StringUtils.replace(content, "电影", "").trim();
            List<WeixinPicTextItem> articles = getMovieInfo(content, fName, toName);
            if (CollectionUtils.isEmpty(articles)) {
                return null;
            }
            return WeixinOffAccountUtil.sendTextAndPic(fName, toName, articles);
        }

        if (StringUtils.contains(content, "优惠券")) {
            content = StringUtils.replace(content, "优惠券", "").trim();
            List<WeixinPicTextItem> list = getSearchGoods(content);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            //最下面的 点击查看更多
            WeixinPicTextItem picTextItem = new WeixinPicTextItem();
            picTextItem.setTitle("点击查看更多优惠商品");
            picTextItem.setPicUrl(BaseConstants.BASE_LOGO_URL);
            picTextItem.setDescription("查看更多优惠商品");
            picTextItem.setUrl(BaseConstants.IMPORT_BASE_URL + "r=index%2Fsearch&s_type=1&kw="
                               + content);
            list.add(picTextItem);

            String resultString = WeixinOffAccountUtil.sendTextAndPic(fName, toName, list);
            System.out.println(resultString);
            return resultString;
        }
        //添加电影
        if (StringUtils.contains(content, "添加")) {
            content = StringUtils.replace(content, "添加", "").trim();
            EmailOrder emailOrder = new EmailOrder();
            emailOrder.setTitle("购物猫公众号添加电影通知");
            emailOrder.setContent("有用户需要您添加电影:" + content + DateUtilSelf.simpleDate(new Date()));
            emailOrder.setReceives(BaseConstants.EMAIL_RECEIVES);
            //发送邮件
            AliyunEmail.send(emailOrder);
            return WeixinOffAccountUtil
                .messageText(fName, toName, "/:heart感谢您的大力支持\r\n/:rose《" + content
                                            + "》已经申请添加\r\n/:gift稍后会发送到您个人微信");
        }
        //默认回复内容
        return WeixinOffAccountUtil.messageText(fName, toName,
            WeixinPublicConfig.WEIXIN_PUBLIC_RETURN);
    }

    /**
     * 
     * 注解：处理微信事件
     * @param openId
     * @param toName
     * @param content
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    public String hanleWeixinEvent(String event, String fName, String toName) {
        EmailOrder emailOrder = null;
        //如果有人取消关注 发送邮件
        if (StringUtils.equals("unsubscribe", event)) {
            emailOrder = new EmailOrder();
            emailOrder.setTitle("购物猫公众号用户取消订阅通知");
            emailOrder.setContent("有用户取消订阅:" + DateUtilSelf.simpleDate(new Date()));
        }
        if (StringUtils.equals("subscribe", event)) {
            emailOrder = new EmailOrder();
            emailOrder.setTitle("购物猫公众号用户订阅通知");
            emailOrder.setContent("有新用户订阅:" + DateUtilSelf.simpleDate(new Date()));
            WeixinOffAccountUtil
                .messageText(fName, toName, WeixinPublicConfig.WEIXIN_PUBLIC_RETURN);
        }
        if (null != emailOrder) {
            emailOrder.setReceives(BaseConstants.EMAIL_RECEIVES);
            AliyunEmail.send(emailOrder);
        }
        return "success";
    }

    /**
     * 
     * 注解：搜索五个商品信息
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    private List<WeixinPicTextItem> getSearchGoods(String keyWords) {

        String result = catManager.searchGood(keyWords);
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
     * 注解：搜索电影信息
     * @param keyWord
     * @param fName
     * @param toName
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    public List<WeixinPicTextItem> getMovieInfo(String keyWord, String fName, String toName) {

        List<WeixinPicTextItem> list = new ArrayList<>();
        WeixinPicTextItem picTextItem = new WeixinPicTextItem();
        picTextItem.setTitle("影视《" + keyWord + "》已经找到，点击查看");
        picTextItem.setPicUrl(BaseConstants.BASE_LOGO_URL);
        picTextItem.setDescription("查看更多电影信息");

        //拼接电影搜索网址
        String urlString;
        try {
            urlString = BaseConstants.MOVIE_URL + "/search/" + URLEncoder.encode(keyWord, "UTF-8")
                        + ".html";
            picTextItem.setUrl(urlString);
            list.add(picTextItem);
            return list;
        } catch (UnsupportedEncodingException e) {
            logger.error("搜索电影失败", e);
            return null;
        }

    }

    public void setCatManager(CatManager catManager) {
        this.catManager = catManager;
    }
}

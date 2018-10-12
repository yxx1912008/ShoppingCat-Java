package cn.luckydeer.manager.helper;

import java.io.IOException;
import java.io.InputStream;
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
import cn.luckydeer.manager.movie.MovieManager;
import cn.luckydeer.model.enums.WeixinMsgType;

/**
 * 微信工具类
 * 
 * @author yuanxx
 * @version $Id: WeixinPublicHelper.java, v 0.1 2018年9月27日 下午6:40:10 yuanxx Exp $
 */
public class WeixinPublicHelper {

    private static final Log logger = LogFactory.getLog("LUCKYDEER-WEIXIN-LOG");

    private CatManager       catManager;

    private MovieManager     movieManager;

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
            logger.error("微信公众号收到信息:" + xmlstr);
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
            List<WeixinPicTextItem> articles = movieManager.getMovieInfo(content, fName, toName);
            if (CollectionUtils.isEmpty(articles)) {
                return null;
            }
            return WeixinOffAccountUtil.sendTextAndPic(fName, toName, articles);
        }

        if (StringUtils.contains(content, "优惠券")) {
            content = StringUtils.replace(content, "优惠券", "").trim();
            List<WeixinPicTextItem> list = catManager.getSearchGoods(content);
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
            return resultString;
        }
        //添加电影
        if (StringUtils.contains(content, "添加")) {
            content = StringUtils.replace(content, "添加", "").trim();
            EmailOrder emailOrder = new EmailOrder();
            emailOrder.setTitle("购物猫公众号添加电影通知");
            emailOrder.setContent("有用户需要您添加电影:" + content + DateUtilSelf.simpleDate(new Date()));
            sendMail(emailOrder);//发送异步邮件
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
            sendMail(emailOrder);//异步发送邮件
        }
        if (StringUtils.equals("subscribe", event)) {
            emailOrder = new EmailOrder();
            emailOrder.setTitle("购物猫公众号用户订阅通知");
            emailOrder.setContent("有新用户订阅:" + DateUtilSelf.simpleDate(new Date()));
            //设置关注回复消息
            sendMail(emailOrder);//异步发送邮件
            return WeixinOffAccountUtil.messageText(fName, toName,
                WeixinPublicConfig.WEIXIN_PUBLIC_RETURN);
        }
        return "success";
    }

    /**
     * 
     * 注解：异步发送邮件
     * @param emailOrder
     * @author yuanxx @date 2018年10月8日
     */
    public void sendMail(EmailOrder emailOrder) {
        emailOrder.setReceives(BaseConstants.EMAIL_RECEIVES);
        AliyunEmail.send(emailOrder);
    }

    public void setCatManager(CatManager catManager) {
        this.catManager = catManager;
    }

    public void setMovieManager(MovieManager movieManager) {
        this.movieManager = movieManager;
    }
}

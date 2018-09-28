package cn.luckydeer.weixin.controller.wxpublic;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.luckydeer.common.constants.weixin.WeixinPublicConfig;
import cn.luckydeer.common.utils.encrypt.EncryptUtil;
import cn.luckydeer.common.utils.wechat.WeixinOffAccountUtil;
import cn.luckydeer.manager.helper.WeixinPublicHelper;

/**
 * 微信公众号处理
 * 
 * @author yuanxx
 * @version $Id: WxPublicController.java, v 0.1 2018年9月27日 下午4:23:08 yuanxx Exp $
 */
@Controller
@RequestMapping(value = "/wxPublic")
public class WxPublicController {

    private static final Log   logger = LogFactory.getLog(WxPublicController.class);

    @Autowired
    private WeixinPublicHelper weixinPublicHelper;

    @RequestMapping(value = "/wxAuthen.do", produces = "application/json;charset=utf-8")
    public void wxAuthen(HttpServletResponse response, HttpServletRequest request) {

        System.out.println("开始");

        if (request.getMethod().toUpperCase().equals("GET")) {// GET模式，微信服务器验证

            String token = WeixinPublicConfig.TOKEN;
            String signature = WeixinOffAccountUtil.loadString(request, "signature");
            String timestamp = WeixinOffAccountUtil.loadString(request, "timestamp");
            String nonce = WeixinOffAccountUtil.loadString(request, "nonce");
            String echostr = WeixinOffAccountUtil.loadString(request, "echostr");

            if (StringUtils.isNotBlank(echostr) && StringUtils.isNotBlank(signature)) {
                //将获取到的参数放入数组
                String[] ArrTmp = { token, timestamp, nonce };
                //按微信提供的方法，对数据内容进行排序
                Arrays.sort(ArrTmp);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < ArrTmp.length; i++) {
                    sb.append(ArrTmp[i]);
                }
                // 对排序后的字符串进行SHA-1加密
                if (StringUtils.equalsIgnoreCase(signature, EncryptUtil.sha1(sb.toString()))) {
                    // success
                    WeixinOffAccountUtil.outWriteText(response, echostr);
                } else {
                    WeixinOffAccountUtil.outWriteText(response, "failed");
                }
            } else {
                WeixinOffAccountUtil.outWriteText(response, "failed");
            }
        } else {
            // POST模式，微信用户消息处理
            //TODO yxx 微信用户消息处理
            try {
                System.out.println("处理用户消息");
                String message = weixinPublicHelper.handleWeixin(request, response);
                if (StringUtils.isNotEmpty(message)) {
                    WeixinOffAccountUtil.outWriteText(response, message);
                }
            } catch (Exception e) {
                logger.error("微信用户消息处理异常", e);
            }
        }

    }

}

package cn.luckydeer.manager.helper;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 微信工具类
 * 
 * @author yuanxx
 * @version $Id: WeixinPublicHelper.java, v 0.1 2018年9月27日 下午6:40:10 yuanxx Exp $
 */
public class WeixinPublicHelper {

    private static final Log logger = LogFactory.getLog(WeixinPublicHelper.class);

    /**
     * 
     * 注解：处理微信推送事件和普通事件
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月26日
     */
    public String handleWeixin(HttpServletRequest request, HttpServletResponse response) {

        InputStream is;
        try {
            is = request.getInputStream();
            String xmlstr = IOUtils.toString(is, "utf-8");
            System.out.println(xmlstr);
        } catch (IOException e) {
            logger.error("", e);
        }

        return null;
    }

}

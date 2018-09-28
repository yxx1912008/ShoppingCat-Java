package cn.luckydeer.common.utils.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 消息相关工具类
 * 
 * @author yuanxx
 * @version $Id: WeixinOffAccountUtil.java, v 0.1 2018年9月27日 下午4:45:25 yuanxx Exp $
 */
public class WeixinOffAccountUtil {

    private static final Log   logger            = LogFactory.getLog(WeixinOffAccountUtil.class);

    public final static String characterEncoding = "UTF-8";

    /**
     * 
     * 注解：将请求转换为 xml
     * @param request
     * @return
     * @throws Exception
     * @author yuanxx @date 2018年9月27日
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<String, String>();

        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();

        for (Element e : elementList) {
            System.out.println(e.getName() + "," + e.getText());
            map.put(e.getName(), e.getText());
        }
        inputStream.close();
        inputStream = null;
        return map;
    }

    public static void outWriteText(HttpServletResponse response, String str) {
        writerString(response, "text/plain; charset=utf-8", str);
    }

    private static void writerString(HttpServletResponse response, String type, String str) {
        response.setContentType(type);
        response.setCharacterEncoding(characterEncoding);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(str);
        } catch (IOException e) {
            logger.error("输出失败：" + e);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 
     * 注解：从 request 中获取 String
     * @param request
     * @param key
     * @return
     * @author yuanxx @date 2018年9月27日
     */
    public static String loadString(HttpServletRequest request, String key) {
        try {
            request.setCharacterEncoding(characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = request.getParameter(key);
        if (str == null) {
            return "";
        } else {
            return str.replaceAll("[']", "");
        }
    }

}

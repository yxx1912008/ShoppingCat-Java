package cn.luckydeer.common.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 网络请求帮助类
 * 
 * @author yuanxx
 * @version $Id: HttpHelper.java, v 0.1 2018年6月19日 下午4:26:16 yuanxx Exp $
 */
public class HttpHelper {

    /**
     * 取客户端真实Ip
     * @param request
     * @return
     */
    public static String loadIpAddr(HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");

        if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("PRoxy-Client-IP");
            if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getRemoteAddr();
            }
            if (StringUtils.length(clientIp) > 60) {
                clientIp = StringUtils.substring(clientIp, 0, 60);
            }
        } else if (clientIp.length() > 15) {
            String[] ips = clientIp.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    clientIp = strIp;
                    break;
                }
            }
        }
        return clientIp;
    }

}

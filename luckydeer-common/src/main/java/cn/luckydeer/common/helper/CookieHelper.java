package cn.luckydeer.common.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.constants.HeaderContants;
import cn.luckydeer.common.constants.ViewConstants;
import cn.luckydeer.common.enums.HttpChannelType;
import cn.luckydeer.common.model.ClientModel;
import cn.luckydeer.common.utils.DomainUtils;
import cn.luckydeer.common.utils.HttpParamSignUtil;

/**
 * 
 * Cookie 缓存帮助类
 * @author yuanxx
 * @version $Id: CookieHelper.java, v 0.1 2018年6月19日 下午3:38:27 yuanxx Exp $
 */
public class CookieHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("LUCKYDEER-SESSION-LOG");

    /**
     * 取请求头部信息
     * @param request
     * @return
     */
    public ClientModel getClientHeader(HttpServletRequest request) {

        //获取根路径
        String contextPath = request.getContextPath() + request.getServletPath();
        ClientModel clientModel = new ClientModel();
        String ticket = getTicket(request);
        clientModel.setTicket(ticket);
        clientModel.setSessionId(request.getSession().getId());
        /** 获取登陆 版本号 */
        clientModel.setSysVersion(request.getHeader(HeaderContants.SYS_VERSION));
        clientModel.setSysVersionName(request.getHeader(HeaderContants.SYS_VERSION_NAME));
        clientModel.setDebugNo(request.getHeader(HeaderContants.DEBUG_NO));

        //登陆渠道 各个渠道信息请参考   HttpChannelType
        String loginChannel = request.getHeader(HeaderContants.LOGIN_CHANNEL);

        //设置登陆渠道
        if (StringUtils.isBlank(loginChannel)) {
            HttpChannelType httpChannelType = HttpParamSignUtil.getByUrl(contextPath);
            if (null != httpChannelType) {
                clientModel.setLoginChannel(httpChannelType.getCode());
            } else {
                clientModel.setLoginChannel(HttpChannelType.H5.getCode());
            }
        } else {
            clientModel.setLoginChannel(loginChannel);
        }

        // 设备唯一识别码 序列号等
        clientModel.setDeviceId(request.getHeader(HeaderContants.DEVICE_NO));

        /** 设备信息： [android/ios];[设备系统版本];[设备名称]*/
        clientModel.setTerminalDevice(request.getHeader(HeaderContants.TERMINAL_DEVICE));
        clientModel.setTimeStamp(request.getHeader(HeaderContants.TIME_STAMP));
        //获取签名
        clientModel.setSign(request.getHeader(HeaderContants.SIGN));
        clientModel.setLoginIp(HttpHelper.loadIpAddr(request));
        clientModel.setContextPath(request.getContextPath() + request.getServletPath());
        clientModel.setMethod(request.getMethod());
        //TODO 单点登录暂时未去实现，优先完成微信端

        if (DomainUtils.isDebug()) {
            LOGGER.info("HEADER：" + clientModel.toString());
        }
        return clientModel;
    }

    /**
     * 获取cookie、request中ticket值
     * @param request
     * @return
     */
    public String getTicket(HttpServletRequest request) {
        String ticket = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equalsIgnoreCase(ViewConstants.LOGIN_TICKET_KEY, cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (StringUtils.isBlank(ticket)) {
            ticket = request.getParameter(ViewConstants.LOGIN_TICKET_KEY);
        }
        return ticket;
    }

    /**
     * 
     * 注解：更新Cookie时间
     * 防止xss漏洞
     * @param response
     * @param ticket
     * @param expiry
     * @author yuanxx @date 2018年6月19日
     */
    public void setCookie(HttpServletResponse response, String ticket, int expiry) {
        // https 使用ture
        boolean isSecure = false;
        // 信任保存
        Cookie cookie = new Cookie(ViewConstants.LOGIN_TICKET_KEY, ticket);
        // 要使此处有效 则访问地址必须与此处的域名一致
        cookie.setDomain(DomainUtils.getDomain());
        // 设置父path
        cookie.setPath("/");
        // 设置有效期存放至客户端硬盘否则为浏览器内存
        cookie.setMaxAge(expiry); //存活期为秒 
        // 只接收https
        cookie.setSecure(isSecure);
        // 防xss漏洞
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        /*        // 防xss漏洞低版本servlet方式
                StringBuffer strBufferCookie = new StringBuffer();
                strBufferCookie.append(ViewContants.LOGIN_TICKET_KEY + "=" + ticket + ";");
                strBufferCookie.append("Max-Age=" + expiry + ";");
                strBufferCookie.append("domain=" + DomainUtils.getDomain() + ";");
                strBufferCookie.append("path=" + "/" + ";");

                if (isSecure) {
                    strBufferCookie.append("secure;HTTPOnly;");
                } else {
                    strBufferCookie.append("HTTPOnly;");
                }
                response.setHeader("Set-Cookie", strBufferCookie.toString());*/
    }

    /**
     * 登录后 过滤器 校验头部参数 兼容pc端未传Header
     * @param clientModel
     * @return
     */
    public boolean validFilterHeader(ClientModel clientModel) {
        if (null == clientModel) {
            return false;
        }

        HttpChannelType httpChannelType = HttpChannelType.getEnumByCode(clientModel
            .getLoginChannel());

        if (null == httpChannelType) {
            return false;
        }

        // 微信端 必传头部参数
        if (httpChannelType.isWechat()) {
            //如果是测试模式 忽略头部校验
            if (StringUtils.equals("TEST", clientModel.getDebugNo()) && DomainUtils.isDebug()) {
                return true;
            }
            if (StringUtils.isBlank(clientModel.getSysVersion())
                || StringUtils.isBlank(clientModel.getTerminalDevice())
                || StringUtils.isBlank(clientModel.getDeviceId())
                || StringUtils.isBlank(clientModel.getSign())
                || StringUtils.isBlank(clientModel.getTimeStamp())) {
                return false;
            }
        }
        return true;
    }

}

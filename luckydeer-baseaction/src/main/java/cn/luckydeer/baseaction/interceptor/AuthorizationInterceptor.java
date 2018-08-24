package cn.luckydeer.baseaction.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.luckydeer.baseaction.annotation.IgnoreAuth;
import cn.luckydeer.baseaction.exception.TokenException;
import cn.luckydeer.baseaction.utils.OperationContextHolder;
import cn.luckydeer.common.constants.ViewConstants;
import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.helper.CookieHelper;
import cn.luckydeer.common.model.ClientModel;
import cn.luckydeer.common.utils.DateUtilSelf;
import cn.luckydeer.manager.token.TokenManager;
import cn.luckydeer.model.user.UserSessionModel;

/**
 * Token校验
 * 针对制定的请求进行校验
 * 如果请求想要不通过校验
 * 可以通过自己写的注解
 * 来进行忽略校验的请求
 * 
 * @author yuanxx
 * @version $Id: AuthorizationInterceptor.java, v 0.1 2018年6月15日 下午1:51:28 yuanxx Exp $
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private CookieHelper cookieHelper;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        IgnoreAuth auth;
        if (handler instanceof HandlerMethod) {
            /** 如果方法中包含注解 直接忽略Token验证 */
            auth = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }
        if (null != auth) {
            return true;
        }

        //TODO 暂时只去验证ticket（token）
        ClientModel clientModel = cookieHelper.getClientHeader(request);

        /** 1.获取Token */
        String token = request.getHeader(ViewConstants.LOGIN_TICKET_KEY);

        /** 2.如果Token获取失败  */
        if (StringUtils.isBlank(token)) {
            throw new TokenException("您尚未登陆,请登录", ViewShowEnums.ERROR_FAILED.getStatus());
        }

        /** 3.查询token信息 */
        UserSessionModel userSession = tokenManager.getModelByToken(token);

        if (null == userSession) {
            throw new TokenException("登录信息查询失败,请重新登录", ViewShowEnums.ERROR_FAILED.getStatus());
        }

        if (DateUtilSelf.calculateDecreaseDate(userSession.getExpireTime(), new Date()) > 0) {
            throw new TokenException("登录信息失效,请重新登录", ViewShowEnums.ERROR_FAILED.getStatus());
        }

        OperationContextHolder.setIsLoggerUser(userSession);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        /** 清楚当前现线程的 缓存会话 */
        OperationContextHolder.clearUser();
        super.afterCompletion(request, response, handler, ex);
    }

}

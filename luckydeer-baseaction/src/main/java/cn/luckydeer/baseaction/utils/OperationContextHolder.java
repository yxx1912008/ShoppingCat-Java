package cn.luckydeer.baseaction.utils;

import cn.luckydeer.model.user.UserSessionModel;

/**
 * 
 * 会话上下文环境
 * @author yuanxx
 * @version $Id: OperationContextHolder.java, v 0.1 2018年6月15日 下午4:26:36 yuanxx Exp $
 */
public class OperationContextHolder {

    /**
     * 
     * 注解：设置当前登陆用户
     * @param sessionUser
     * @author yuanxx @date 2018年6月15日
     */
    public static void setIsLoggerUser(UserSessionModel sessionUser) {
        ContextHolder.setConcurrentObject(sessionUser);
    }

    /**
     * 获取当前会话
     * 
     * @return
     */
    public static UserSessionModel getSessionUser() {
        return (UserSessionModel) ContextHolder.getConcurrentObject();
    }

    /**
     * 获取上下文登录用户Id
     * 
     * @return
     */
    public static String getUserId() {
        UserSessionModel sessionUser = getSessionUser();
        return null != sessionUser ? sessionUser.getUserId() : null;
    }

    /**
     * 清除当前会话
     */
    public static void clearUser() {
        ContextHolder.clearContext();
    }
}

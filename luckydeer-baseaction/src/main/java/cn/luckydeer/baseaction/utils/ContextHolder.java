package cn.luckydeer.baseaction.utils;

/**
 * 缓存会话
 * 
 * @author yuanxx
 * @version $Id: ContextHolder.java, v 0.1 2018年6月15日 下午4:40:33 yuanxx Exp $
 */
public class ContextHolder {

    /** 会话缓存 */
    private static final ThreadLocal<Object> sessionHolder = new ThreadLocal<Object>();

    /**
     * 缓存当前会话
     * 注解：
     * @param session
     * @author yuanxx @date 2018年6月15日
     */
    public static void setConcurrentObject(Object session) {
        sessionHolder.set(session);
    }

    /**
     * 
     * 注解：获取当前会话
     * @return
     * @author yuanxx @date 2018年6月15日
     */
    public static Object getConcurrentObject() {
        return sessionHolder.get();
    }

    /**
     * 
     * 注解：清楚当前会话
     * @author yuanxx @date 2018年6月15日
     */
    public static void clearContext() {
        sessionHolder.remove();
    }

}

package cn.luckydeer.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Http请求的通道类型
 * 
 * @author yuanxx
 * @version $Id: HttpChannelType.java, v 0.1 2018年6月15日 下午2:14:51 yuanxx Exp $
 */
public enum HttpChannelType {

    // 所有用户可登录
    USER_APP("USER_APP", "用户app"), H5("H5", "微信H5"), WEIXIN("WEIXIN", "微信登陆"), PC("PC", "电脑浏览器"), ;

    private HttpChannelType(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    /**
     * 
     * 注解：是否是微信登陆
     * @return
     * @author yuanxx @date 2018年6月15日
     */
    public boolean isWechat() {

        switch (this) {
            case WEIXIN:
                return true;
            default:
                return false;
        }

    }

    /**
     * 获得枚举
     * 
     * @param code
     * @return
     */
    public static HttpChannelType getEnumByCode(String code) {

        if (StringUtils.isNotBlank(code)) {
            for (HttpChannelType activitie : HttpChannelType.values()) {
                if (StringUtils.equals(activitie.getCode(), code)) {
                    return activitie;
                }
            }
        }
        return null;
    }

    /**
     * 根据code获取详情
     * @param code
     * @return
     */
    public static String getDetailByCode(String code) {

        if (null != code) {
            for (HttpChannelType activitie : HttpChannelType.values()) {
                if (StringUtils.equals(activitie.getCode(), code)) {
                    return activitie.getDetail();
                }
            }
        }
        return null;
    }

    private String code;

    private String detail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

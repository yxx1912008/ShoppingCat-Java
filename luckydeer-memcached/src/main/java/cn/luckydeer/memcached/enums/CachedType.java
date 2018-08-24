package cn.luckydeer.memcached.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 缓存类型
 * 暂时只分为两种
 * 1.活跃用户登陆会话
 * 2.业务数据缓存
 * @author yuanxx
 * @version $Id: CachedType.java, v 0.1 2018年6月19日 下午3:04:07 yuanxx Exp $
 */
public enum CachedType {

    // 用于活跃用户登录会话 (非用户会话不要使用该类型)
    USER_SESSION("USER_SESSION", "登录用户缓存"),
    // 用于业务缓存
    STATISTICS("STATISTICS", "业务数据缓存"), ;

    private CachedType(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    /**
     * 获得枚举
     * 
     * @param code
     * @return
     */
    public static CachedType getEnumByCode(String code) {

        if (StringUtils.isNotBlank(code)) {
            for (CachedType activitie : CachedType.values()) {
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
            for (CachedType activitie : CachedType.values()) {
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

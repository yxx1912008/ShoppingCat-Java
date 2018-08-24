package cn.luckydeer.common.enums.user;

/**
 * 用户状态
 * 
 * @author yuanxx
 * @version $Id: UserStatus.java, v 0.1 2018年6月20日 上午11:49:20 yuanxx Exp $
 */
public enum UserStatus {

    NORMAL(1, "正常"), CLOSED(0, "已停止使用"), HIDDEN(2, "已被注销");

    private int    code;
    private String detail;

    UserStatus(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    /**
     * 
     * 注解：用户状态是否正常
     * @return
     * @author yuanxx @date 2018年6月20日
     */
    public boolean isNormal() {
        switch (this) {
            case NORMAL:
                return true;
            default:
                return false;
        }
    }

    /**
     * 
     * 注解：根据code获取指定枚举
     * @param code
     * @return
     * @author yuanxx @date 2018年6月20日
     */
    public UserStatus getEnumByCode(Integer code) {
        if (null != code) {
            for (UserStatus type : UserStatus.values()) {
                if (code.intValue() == type.getCode()) {
                    return type;
                }
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

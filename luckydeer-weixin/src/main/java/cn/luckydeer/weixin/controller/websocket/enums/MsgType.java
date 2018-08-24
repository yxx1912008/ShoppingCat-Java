package cn.luckydeer.weixin.controller.websocket.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 消息类型
 * 
 * @author yuanxx
 * @version $Id: MsgType.java, v 0.1 2018年7月31日 下午6:18:37 yuanxx Exp $
 */
public enum MsgType {

    USER_LIST("USER_LIST", "用户列表"), USER_INFO("USER_INFO", "用户信息"), ON_LINE_NOTICE(
                                                                                   "ON_LINE_NOTICE",
                                                                                   "上线状态通知"), ERROR_MSG(
                                                                                                        "ERROR_MSG",
                                                                                                        "错误信息"), USER_MESSANGE(
                                                                                                                               "USER_MESSANGE",
                                                                                                                               "用户消息"), SUCESS_MSG(
                                                                                                                                                   "SUCESS_MSG",
                                                                                                                                                   "正常通知"), GROUP_MSG(
                                                                                                                                                                      "GROUP_MSG",
                                                                                                                                                                      "用户群发信息"), FOR_ONE_MSG(
                                                                                                                                                                                             "FOR_ONE_MSG",
                                                                                                                                                                                             "一对一聊天信息");

    private String code;
    private String detail;

    MsgType(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public boolean isGroupMsg() {
        switch (this) {
            case GROUP_MSG:
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
    public static MsgType getEnumByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (MsgType type : MsgType.values()) {
                if (StringUtils.equals(code, type.getCode())) {
                    return type;
                }
            }
        }
        return null;
    }

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

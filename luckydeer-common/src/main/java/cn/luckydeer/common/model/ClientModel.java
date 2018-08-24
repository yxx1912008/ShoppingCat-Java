package cn.luckydeer.common.model;

import java.io.Serializable;

/**
 * 
 * 客户端Head参数
 * @author yuanxx
 * @version $Id: ClientModel.java, v 0.1 2018年6月19日 下午3:57:41 yuanxx Exp $
 */
public class ClientModel implements Serializable {

    /** serialVersionUID  */
    private static final long serialVersionUID = 2097530131563364234L;

    private String            ticket;

    private String            sessionId;

    /** 请求来源ip */
    private String            loginIp;

    /** 请求接口地址 */
    private String            contextPath;

    /** 请求方法 */
    private String            method;

    /** 登录渠道 @see HttpChannelType*/
    private String            loginChannel;

    /** app版本号 */
    private String            sysVersion;

    /** app版本名  */
    private String            sysVersionName;

    /** 设备唯一识别码 */
    private String            deviceId;

    /** 手机系统升级会变更设备版本号，设备信息：;[设备系统版本];[设备名称]*/
    private String            terminalDevice;

    /** 签名 */
    private String            sign;

    /** 时间戳 */
    private String            timeStamp;

    /**测试环境 header部分传递参数 debugNo=TEST 避免校验签名 */
    private String            debugNo;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLoginChannel() {
        return loginChannel;
    }

    public void setLoginChannel(String loginChannel) {
        this.loginChannel = loginChannel;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getSysVersionName() {
        return sysVersionName;
    }

    public void setSysVersionName(String sysVersionName) {
        this.sysVersionName = sysVersionName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTerminalDevice() {
        return terminalDevice;
    }

    public void setTerminalDevice(String terminalDevice) {
        this.terminalDevice = terminalDevice;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDebugNo() {
        return debugNo;
    }

    public void setDebugNo(String debugNo) {
        this.debugNo = debugNo;
    }

}

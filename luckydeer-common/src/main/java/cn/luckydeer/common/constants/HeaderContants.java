package cn.luckydeer.common.constants;

/**
 * 
 * 请求头相关常量
 * @author yuanxx
 * @version $Id: HeaderContants.java, v 0.1 2018年6月19日 下午4:08:28 yuanxx Exp $
 */
public interface HeaderContants {

    /** app版本号 */
    String  SYS_VERSION        = "sysVersion";

    /** app上可见的版本名 */
    String  SYS_VERSION_NAME   = "sysVersionName";

    /** 测试标志(TEST) （测试环境 header部分传递参数 debugNo=TEST 避免校验签名） */
    String  DEBUG_NO           = "debugNo";

    /** 请求渠道 */
    String  LOGIN_CHANNEL      = "loginChannel";

    /** 设备唯一识别码 序列号等 */
    String  DEVICE_NO          = "deviceNo";

    /** 设备信息： [android/ios];[设备系统版本];[设备名称]*/
    String  TERMINAL_DEVICE    = "terminalDevice";

    /** 时间戳 */
    String  TIME_STAMP         = "timeStamp";

    /** 客户端签名 md5(ticket + timeStamp + 秘钥)  */
    String  SIGN               = "sign";

    /** token 默认失效时间  当前设置为1个小时 */
    Integer TOKEN_FAILURE_TIME = 60 * 60 * 1;

}

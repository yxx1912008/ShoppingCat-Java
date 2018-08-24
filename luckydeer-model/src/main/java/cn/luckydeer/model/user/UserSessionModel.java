package cn.luckydeer.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 用户Session 的模型
 * @author yuanxx
 * @version $Id: SessionClientModel.java, v 0.1 2018年6月15日 下午4:28:47 yuanxx Exp $
 */
public class UserSessionModel implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8066583204484291506L;

    private String            token;

    /** 登录渠道 @see HttpChannelType*/
    private String            loginChannel;

    /**　过期时间　*/
    private Date              expireTime;

    /** 更新时间 */
    private Date              updateTime;

    private String            userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginChannel() {
        return loginChannel;
    }

    public void setLoginChannel(String loginChannel) {
        this.loginChannel = loginChannel;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

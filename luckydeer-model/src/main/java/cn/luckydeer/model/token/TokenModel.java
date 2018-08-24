package cn.luckydeer.model.token;

import java.io.Serializable;
import java.util.Date;

import cn.luckydeer.model.user.UserSessionModel;

/**
 * 
 * Token的详细信息
 * @author yuanxx
 * @version $Id: TokenModel.java, v 0.1 2018年6月19日 下午5:07:58 yuanxx Exp $
 */
public class TokenModel implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -7991522556641457631L;
    //用户ID
    private UserSessionModel  sessionInfo;
    //token
    private String            token;
    //过期时间
    private Date              expireTime;
    //更新时间
    private Date              updateTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public UserSessionModel getSessionInfo() {
        return sessionInfo;
    }

    public void setSessionInfo(UserSessionModel sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

}

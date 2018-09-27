package cn.luckydeer.manager.token;

import java.util.Date;

import cn.luckydeer.common.constants.HeaderContants;
import cn.luckydeer.common.utils.date.DateUtilSelf;
import cn.luckydeer.memcached.api.DistributedCached;
import cn.luckydeer.memcached.enums.CachedType;
import cn.luckydeer.model.user.UserSessionModel;

/**
 * Token管理
 * 
 * @author yuanxx
 * @version $Id: TokenManager.java, v 0.1 2018年6月19日 下午5:05:07 yuanxx Exp $
 */
public class TokenManager {

    private DistributedCached distributedCached;

    /**
     * 
     * 注解：设置Token信息
     * @param userId
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    public boolean setToken(String token, UserSessionModel sessionModel) {

        UserSessionModel userSession = (UserSessionModel) distributedCached.get(
            CachedType.USER_SESSION, token);
        Date date = new Date();
        if (null == userSession) {
            sessionModel.setToken(token);
            sessionModel.setLoginChannel("WX");
            sessionModel.setUpdateTime(date);
            sessionModel.setExpireTime(DateUtilSelf.increaseHour(date, 1));
            return distributedCached.put(CachedType.USER_SESSION, token,
                HeaderContants.TOKEN_FAILURE_TIME, sessionModel);
        } else {
            userSession.setUpdateTime(new Date());
            userSession.setExpireTime(DateUtilSelf.increaseHour(date, 1));
            return distributedCached.update(CachedType.USER_SESSION, token,
                HeaderContants.TOKEN_FAILURE_TIME, userSession);
        }
    }

    /**
     * 
     * 注解：获取TokenModel
     * @param token
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    public UserSessionModel getModelByToken(String token) {
        UserSessionModel userSessionModel = (UserSessionModel) distributedCached.get(
            CachedType.USER_SESSION, token);
        return userSessionModel;
    }

    public void setDistributedCached(DistributedCached distributedCached) {
        this.distributedCached = distributedCached;
    }

}

package cn.luckydeer.websocket.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.CollectionUtils;

import cn.luckydeer.websocket.entity.UserInfo;
import cn.luckydeer.websocket.enums.ViewShowEnums;
import cn.luckydeer.websocket.model.ResponseObj;
import cn.luckydeer.websocket.utils.UserHeadUtils;

/**
 * 用户信息管理
 * 
 * @author yuanxx
 * @version $Id: UserInfoManager.java, v 0.1 2018年7月26日 下午5:04:26 yuanxx Exp $
 */
public class UserInfoManager {

    /** 缓存用户数据 */
    private static Map<String, ConcurrentHashMap<String, UserInfo>> userCache = new ConcurrentHashMap<>();

    /**
     * 
     * 注解：注册用户
     * @param userName
     * @param tableNo
     * @return
     * @author yuanxx @date 2018年7月27日
     */
    public ResponseObj register(String userName, String tableNo) {

        //根据桌号查询信息
        ConcurrentHashMap<String, UserInfo> userInfos = userCache.get(tableNo);

        if (!CollectionUtils.isEmpty(userInfos)) {
            UserInfo userInfo = userInfos.get(userName);
            if (null != userInfo) {
                return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "该用户已经注册");
            }
            UserInfo info = new UserInfo();
            info.setIconUrl(UserHeadUtils.getRandomUserIcon());
            info.setTableNo(tableNo);
            info.setUserName(userName);
            userCache.get(tableNo).put(userName, info);
            return new ResponseObj(ViewShowEnums.INFO_SUCCESS.getStatus(), "恭喜您，注册成功");
        }
        UserInfo info = new UserInfo();
        info.setIconUrl(UserHeadUtils.getRandomUserIcon());
        info.setTableNo(tableNo);
        info.setUserName(userName);
        ConcurrentHashMap<String, UserInfo> tableInfo = new ConcurrentHashMap<String, UserInfo>();
        tableInfo.put(userName, info);
        userCache.put(tableNo, tableInfo);
        return new ResponseObj(ViewShowEnums.INFO_SUCCESS.getStatus(), "恭喜您，注册成功");
    }

}

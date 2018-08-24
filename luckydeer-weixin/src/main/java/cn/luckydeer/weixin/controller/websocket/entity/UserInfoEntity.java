package cn.luckydeer.weixin.controller.websocket.entity;

import java.io.Serializable;

import javax.websocket.Session;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfoEntity implements Serializable {

    /**  */
    private static final long serialVersionUID = 8456571912058478042L;
    //用户会话
    @JSONField(serialize = false)
    private Session           session;
    //用户ID 
    private String            userName;
    //桌位号
    private String            tableNo;
    //用户的头像地址
    private String            iconUrl;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}

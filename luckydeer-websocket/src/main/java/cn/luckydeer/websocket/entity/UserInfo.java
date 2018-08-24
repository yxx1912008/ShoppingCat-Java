package cn.luckydeer.websocket.entity;

/**
 * 用户信息
 * 
 * @author yuanxx
 * @version $Id: UserInfo.java, v 0.1 2018年7月26日 上午11:12:48 yuanxx Exp $
 */
public class UserInfo {

    //用户名
    private String userName;
    //头像地址
    private String iconUrl;
    //用户桌号
    private String tableNo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

}

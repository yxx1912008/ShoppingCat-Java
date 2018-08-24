package cn.luckydeer.dao.user.dataobject;

import java.util.Date;

/**
 * 系统用户POJO
 * 
 * @author yuanxx
 * @version $Id: SysUserDo.java, v 0.1 2018年6月20日 上午11:25:31 yuanxx Exp $
 */
public class SysUserDo {

    /** 用户ID  */
    private String  userId;
    /** 昵称  */
    private String  nickName;
    /**  手机号码 */
    private String  phone;
    /** 头像地址  */
    private String  headPicUrl;
    /**  微信openid */
    private String  openId;
    /** 微信联合id  */
    private String  unionId;
    /** 注册时间  */
    private Date    regTime;
    /**  更新时间 */
    private Date    updateTime;
    /**  用户状态 */
    private Integer userStatus;
    /**  注册渠道 */
    private String  regChanel;
    /**  分组ID */
    private Integer groupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getRegChanel() {
        return regChanel;
    }

    public void setRegChanel(String regChanel) {
        this.regChanel = regChanel;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
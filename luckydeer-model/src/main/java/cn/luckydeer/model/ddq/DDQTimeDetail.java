package cn.luckydeer.model.ddq;

/**
 * 
 * 时间列表
 * @author yuanxx
 * @version $Id: DDQTimeList.java, v 0.1 2018年8月26日 下午6:08:41 yuanxx Exp $
 */
public class DDQTimeDetail {

    //开始键值
    private String activeKey;

    //开始时间
    private String beganTime;

    //状态值： 已开抢 正在疯抢 即将开始
    private String status;

    public String getActiveKey() {
        return activeKey;
    }

    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
    }

    public String getBeganTime() {
        return beganTime;
    }

    public void setBeganTime(String beganTime) {
        this.beganTime = beganTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

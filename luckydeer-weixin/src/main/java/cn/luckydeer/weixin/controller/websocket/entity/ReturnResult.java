package cn.luckydeer.weixin.controller.websocket.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 聊天用户信息
 * @author yuanxx
 * @version $Id: ChatUserInfo.java, v 0.1 2018年7月31日 下午6:10:06 yuanxx Exp $
 */
public class ReturnResult implements Serializable {

    /**  */
    private static final long serialVersionUID = -272001372894734603L;

    private Integer           status;

    private Object            data;

    private String            showMessage;

    private String            dataType;

    public ReturnResult(Integer status, String showMessage, String dataType, Object data) {
        this.status = status;
        this.data = data;
        this.showMessage = showMessage;
        this.dataType = dataType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}

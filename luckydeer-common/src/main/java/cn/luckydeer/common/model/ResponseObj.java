package cn.luckydeer.common.model;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import cn.luckydeer.common.constants.ViewConstants;
import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.utils.DomainUtils;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 前端返回 操作结果
 * @author yuanxx
 * @version $Id: ResponseObj.java, v 0.1 2018年6月15日 上午11:56:24 yuanxx Exp $
 */
public class ResponseObj implements Serializable {

    private static final long serialVersionUID = -7049443021893341045L;

    private int               status;
    /**
     * 前台显示的提示信息
     */
    private String            showMessage;
    /**
     * 返回app端的的json数据字符串
     */
    private Object            data;

    // 异常情况
    public ResponseObj(int status, String showMessage) {
        this.status = status;
        this.showMessage = showMessage;
    }

    // 正确情况
    public ResponseObj() {
        this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
        this.showMessage = ViewShowEnums.INFO_SUCCESS.getDetail();
    }

    // 正确情况
    public ResponseObj(Object data) {
        this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
        this.showMessage = ViewShowEnums.INFO_SUCCESS.getDetail();
        this.data = data;
    }

    // 正确情况
    public ResponseObj(int status, String showMessage, Object data) {
        this.status = status;
        this.showMessage = showMessage;
        this.data = data;
    }

    /** <p class="detail">
    * 功能：web端返回的json字符串
    * </p>
    * @author panwuhai
    * @date 2016年4月15日 
    * @param request
    * @param response
    * @return    
    */
    public String toJson(HttpServletRequest request, HttpServletResponse response) {

        // 设置跨域
        DomainUtils.setAccessContrlAllowOrigin(request, response);
        //如果客户端传入了callBack变量说明该请求是jsonp跨域请求，则将数据包装成jsonp所需格式返回
        String callBackFunName = request.getParameter(ViewConstants.JSONP_CALLBACK_FUN_NAME);

        String resultJson = null;

        if (null == this.data || (data instanceof String && StringUtils.isBlank(data.toString()))
            || (data instanceof Collection && CollectionUtils.isEmpty((Collection<?>) data))) {
            resultJson = getBlankDataJson();
            if (StringUtils.isNotBlank(callBackFunName)) {
                return callBackFunName + "(" + resultJson + ")";
            }
            return resultJson;
        }

        if (StringUtils.isNotBlank(callBackFunName)) {
            return callBackFunName + "(" + JSON.toJSONString(this) + ")";
        }
        return JSON.toJSONString(this);
    }

    /** <p class="detail">
    * 功能：web端data拼写的json
    * </p>
    * @author panwuhai
    * @date 2016年4月18日 
    * @param request
    * @param response
    * @return    
    */
    @Deprecated
    public String toDataString(HttpServletRequest request, HttpServletResponse response) {
        DomainUtils.setAccessContrlAllowOrigin(request, response);
        if (null == this.data) {
            this.data = "";
        }
        if (data instanceof String) {
            if (StringUtils.isBlank(data.toString())) {
                this.data = "";
            }
        }
        String result = dataToString();
        //如果客户端传入了callBack变量说明该请求是jsonp跨域请求，则将数据包装成jsonp所需格式返回
        String callBackFunName = request.getParameter(ViewConstants.JSONP_CALLBACK_FUN_NAME);
        if (StringUtils.isNotBlank(callBackFunName)) {
            return callBackFunName + "(" + result + ")";
        }

        return result;
    }

    /** <p class="detail">
    * 功能：data由自己拼写的json串
    * </p>
    * @author panwuhai
    * @date 2016年4月18日 
    * @param request
    * @param response
    * @return    
    */
    private String dataToString() {
        StringBuilder builder = new StringBuilder("{\"status\":");
        builder.append(this.getStatus());
        builder.append(",\"showMessage\":\"");
        builder.append(this.getShowMessage());
        builder.append("\",\"data\":");
        builder.append(this.data);
        builder.append("}");
        return builder.toString();
    }

    /** <p class="detail">
     * 功能：data数据为空时配合app的json结果
     * </p>
     * @author panwuhai
     * @date 2016年4月12日 
     * @return    
     */
    private String getBlankDataJson() {
        StringBuilder builder = new StringBuilder("{\"status\":");
        builder.append(this.getStatus());
        builder.append(",\"showMessage\":\"");
        builder.append(this.getShowMessage());
        builder.append("\",\"data\":{}}");
        return builder.toString();
    }

    /** <p class="detail">
    * 功能：0:失败; 1:成功
    * </p>
    * @author panwuhai
    * @date 2016年4月17日 
    * @return    
    */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    /**
     * @return the showMessage
     */
    public String getShowMessage() {
        return showMessage;
    }

}

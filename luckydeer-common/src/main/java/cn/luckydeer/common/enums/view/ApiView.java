package cn.luckydeer.common.enums.view;

/**
 * 
 * 非业务类型枚举
 * @author yuanxx
 * @version $Id: XXX.java, v 0.1 2018年6月15日 上午11:57:17 yuanxx Exp $
 */
public enum ApiView {

    ERROR_FAILED(1, "系统异常"), INFO_SUCCESS(0, "操作成功");

    private int    status;

    private String detail;

    /**
     * 返回结果是否错误
     * @return
     */
    public boolean isError() {
        switch (this) {
            case INFO_SUCCESS:
                return false;
            default:
                return true;
        }
    }

    public boolean isSuccess() {
        switch (this) {
            case INFO_SUCCESS:
                return true;
            default:
                return false;
        }
    }

    ApiView(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    /**
     * 获得枚举
     * 
     * @param code
     * @return
     */
    public static ApiView getEnumByStatus(Integer status) {
        if (null != status) {
            for (ApiView activitie : ApiView.values()) {
                if (status.intValue() == activitie.getStatus()) {
                    return activitie;
                }
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

}

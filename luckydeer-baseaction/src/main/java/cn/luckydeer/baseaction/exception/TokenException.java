package cn.luckydeer.baseaction.exception;

/**
 * 
 * 自建异常
 * token验证异常
 * @author yuanxx
 * @version $Id: TokenException.java, v 0.1 2018年6月15日 下午2:28:24 yuanxx Exp $
 */

public class TokenException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 4643842674003196638L;

    private String            msg;
    private int               code             = 500;

    public TokenException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public TokenException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}

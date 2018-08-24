package cn.luckydeer.baseaction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.model.ResponseObj;

/**
 * 
 * 全局异常处理
 * @author yuanxx
 * @version $Id: GlobalExceptionHandler.java, v 0.1 2018年6月15日 下午2:49:23 yuanxx Exp $
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(Exception ex) {
        logger.error("未处理的系统异常:", ex);
        return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
            ViewShowEnums.ERROR_FAILED.getDetail()).toString();
    }

    /**
     * 拦截捕捉自定义异常TokenException.class
     * 此异常为token验证异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = TokenException.class)
    public String handlerTokenException(TokenException exception) {
        return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), exception.getMsg())
            .toString();
    }

}

package cn.luckydeer.baseaction.basecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.baseaction.exception.TokenException;
import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.helper.HttpHelper;
import cn.luckydeer.common.model.ResponseObj;

/**
 * 基类Controller
 * 建议所有的Controller继承此基类
 * @author yuanxx
 * @version $Id: BaseController.java, v 0.1 2018年6月15日 下午2:40:28 yuanxx Exp $
 */
@ControllerAdvice
public class BaseController {

    protected Logger              logger = Logger.getLogger("LUCKYDEER-CONTROLLER-LOG");

    /**
     * 得到request对象
     */
    @Autowired
    protected HttpServletRequest  request;
    /**
     * 得到response对象
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * 参数绑定异常
     */
    @ExceptionHandler({ BindException.class, MissingServletRequestParameterException.class,
            UnauthorizedException.class, TypeMismatchException.class })
    @ResponseBody
    public String bindException(Exception e) {
        if (e instanceof BindException) {
            logger.error("参数绑定异常", e);
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
                ViewShowEnums.ERROR_FAILED.getDetail()).toJson(request, response);
        } else if (e instanceof UnauthorizedException) {
            logger.error("无访问权限", e);
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
                ViewShowEnums.ERROR_FAILED.getDetail()).toJson(request, response);
        }
        return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
            ViewShowEnums.ERROR_FAILED.getDetail()).toJson(request, response);
    }

    /**
     * 参数绑定异常
     * 如果是Token验证异常，则绑定
     */
    @ExceptionHandler({ Exception.class, TokenException.class })
    @ResponseBody
    public String defaultException(Exception e) {
        if (e instanceof TokenException) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
                ((TokenException) e).getMsg()).toJson(request, response);
        }
        return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(),
            ViewShowEnums.ERROR_FAILED.getDetail()).toJson(request, response);
    }

    /**
     * 获取请求方IP
     *
     * @return 客户端Ip
     */
    public String getClientIp() {
        return HttpHelper.loadIpAddr(request);
    }

}

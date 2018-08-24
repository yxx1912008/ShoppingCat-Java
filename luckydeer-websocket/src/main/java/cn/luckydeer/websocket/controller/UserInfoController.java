package cn.luckydeer.websocket.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.websocket.enums.ViewShowEnums;
import cn.luckydeer.websocket.model.ResponseObj;

/**
 * 用户信息控制器
 * 
 * @author yuanxx
 * @version $Id: UserInfoController.java, v 0.1 2018年7月26日 上午10:49:46 yuanxx Exp $
 */
@Controller
@RequestMapping(value = "/websocket/user/")
public class UserInfoController {

    /*  @Autowired
      private UserInfoManager userInfoManager;*/

    /**
     * 
     * 注解：注册新用户
     * @param userName
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年7月26日
     */
    @RequestMapping(value = "/register.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String register(String userName, String tableNo, HttpServletRequest request,
                           HttpServletResponse response) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(tableNo)) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "用户名、桌号不能为空").toJson(
                request, response);
        }
        /*  ResponseObj obj = userInfoManager.register(userName, tableNo);*/
        return "接口成功";
    }
}

package cn.luckydeer.weixin.controller.banner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.manager.banner.BannerManager;

/**
 * 首页海报控制类
 * 
 * @author yuanxx
 * @version $Id: ShopBanner.java, v 0.1 2018年8月24日 下午4:16:03 yuanxx Exp $
 */
@Controller
@RequestMapping(value = "/api/banner", method = RequestMethod.POST)
public class ShopBanner {

    @Autowired
    private BannerManager bannerManager;

    /**
     * 
     * 注解：获取首页海报
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年8月24日
     */
    @RequestMapping(value = "/getIndexBanner.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getIndexBanner(HttpServletRequest request, HttpServletResponse response) {

        return bannerManager.getBanner();
    }

}

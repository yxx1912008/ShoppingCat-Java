package cn.luckydeer.weixin.controller.cat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.manager.cat.CatManager;

/**
 * 首页海报控制类
 * 
 * @author yuanxx
 * @version $Id: ShopBanner.java, v 0.1 2018年8月24日 下午4:16:03 yuanxx Exp $
 */
@Controller
@RequestMapping(value = "/cat/api", method = RequestMethod.POST)
public class CatController {

    @Autowired
    private CatManager catManager;

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
        return catManager.getBanner().toJson(request, response);
    }

    /**
     * 
     * 注解：获取咚咚抢 抢购信息
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    @RequestMapping(value = "/getDDongQ.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getDDongQ(HttpServletRequest request, HttpServletResponse response) {
        return catManager.getDDongQ();
    }

    /**
     * 
     * 注解：获取9.9包邮商品列表
     * @param page
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    @RequestMapping(value = "/getNine.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getNine(String page, HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isBlank(page)) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "请求页码不能为空").toJson(
                request, response);
        }
        return catManager.getNine(page);
    }

    /**
     * 
     * 注解：获取实时疯抢榜
     * @param catId 
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    @RequestMapping(value = "/getRealTime.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getRealTime(Integer catId, HttpServletRequest request,
                              HttpServletResponse response) {
        return catManager.getRealTime(catId);
    }

    /**
     * 
     * 注解：搜索商品
     * @param keyWords
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年8月27日
     */
    @RequestMapping(value = "/searchGood.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String searchGood(String keyWords, HttpServletRequest request,
                             HttpServletResponse response) {

        return catManager.searchGood(keyWords);
    }

    @RequestMapping(value = "/getGoodDetail.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getGoodDetail(String goodId, HttpServletRequest request,
                                HttpServletResponse response) {
        return catManager.getGoodDetail(goodId);
    }

    /**
     * 
     * 注解：获取商品复制码
     * @param goodId
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月5日
     */
    @RequestMapping(value = "/getGoodCodeText.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getGoodCodeText(String goodId, HttpServletRequest request,
                                  HttpServletResponse response) {

        if (StringUtils.isBlank(goodId)) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "商品ID不能为空").toJson(
                request, response);
        }
        String codeText = catManager.getGoodCodeText(goodId);
        return new ResponseObj(codeText).toJson(request, response);
    }
}

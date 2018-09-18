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
            page = "1";
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

    /**
     * 
     * 注解：获取领券直播商品
     * @param page
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    @RequestMapping(value = "/getTicketLive.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getTicketLive(String page, HttpServletRequest request,
                                HttpServletResponse response) {

        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        return catManager.getTicketLive(page);
    }

    /**
     * 
     * 注解：获取正在抢商品列表
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月6日
     */
    @RequestMapping(value = "/getCurrentQiang.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getCurrentQiang(HttpServletRequest request, HttpServletResponse response) {

        return catManager.getCurrentQiang();
    }

    /**
     * 
     * 注解：通过商品真实ID获取商品主图信息
     * @param realGoodId
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月13日
     */
    @RequestMapping(value = "/getGoodDescImg.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getGoodDescImg(String realGoodId, HttpServletRequest request,
                                 HttpServletResponse response) {

        if (StringUtils.isBlank(realGoodId)) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "商品真实ID不能为空").toJson(
                request, response);
        }
        return catManager.getGoodDescImg(realGoodId);
    }

    /**
     * 
     * 注解：根据商品真实（即淘宝内部ID）获取商品信息
     * @param realGoodId
     * @param request
     * @param response
     * @return
     * @author yuanxx @date 2018年9月17日
     */
    @RequestMapping(value = "/getGoodDetailByRealId.do", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getGoodDetailByRealId(String realGoodId, HttpServletRequest request,
                                        HttpServletResponse response) {

        if (StringUtils.isBlank(realGoodId)) {
            return new ResponseObj(ViewShowEnums.ERROR_FAILED.getStatus(), "商品真实Id不能为空").toJson(
                request, response);
        }
        return catManager.getGoodDetailByRealId(realGoodId);
    }

}
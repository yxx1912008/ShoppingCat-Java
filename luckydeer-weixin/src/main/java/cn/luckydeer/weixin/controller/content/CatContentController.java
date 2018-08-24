package cn.luckydeer.weixin.controller.content;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.luckydeer.baseaction.annotation.IgnoreAuth;
import cn.luckydeer.baseaction.basecontroller.BaseController;
import cn.luckydeer.common.model.ResponseObj;
import cn.luckydeer.manager.indexposter.IndexPosterManager;
import cn.luckydeer.webmagic.model.IndexPosterModel;

@Controller
@RequestMapping(value = "/wechat/cat/", method = RequestMethod.POST)
public class CatContentController extends BaseController {

    @Autowired
    private IndexPosterManager indexPosterManager;

    @IgnoreAuth
    @RequestMapping(value = "getIndexPoster.wx", produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public String getIndexPoster(HttpServletRequest request, HttpServletResponse response) {
        List<IndexPosterModel> list = indexPosterManager.getIndexPoster();
        return new ResponseObj(list).toJson(request, response);
    }

}

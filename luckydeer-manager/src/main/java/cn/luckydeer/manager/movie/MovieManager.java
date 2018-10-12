package cn.luckydeer.manager.movie;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.constants.base.BaseConstants;
import cn.luckydeer.common.utils.wechat.model.WeixinPicTextItem;
import cn.luckydeer.movie.dao.movie.daoInterface.IMacVodDao;
import cn.luckydeer.movie.dao.movie.dataobject.MacVodDo;

//TODO 电影管理
public class MovieManager {

    private IMacVodDao          macVodDao;

    private static final Logger logger = LoggerFactory.getLogger("LUCKYDEER-MANAGER-LOG");

    /**
     * 
     * 注解：查询五条电影信息
     * @return
     * @author yuanxx @date 2018年10月12日
     */
    public List<MacVodDo> selectTopFiveMovie() {
        return macVodDao.selectTopFiveMovie();
    }

    /**
     * 
     * 注解：搜索电影信息
     * @param keyWord
     * @param fName
     * @param toName
     * @return
     * @author yuanxx @date 2018年9月28日
     */
    public List<WeixinPicTextItem> getMovieInfo(String keyWord, String fName, String toName) {

        List<WeixinPicTextItem> list = new ArrayList<>();
        WeixinPicTextItem picTextItem = new WeixinPicTextItem();
        picTextItem.setTitle("影视《" + keyWord + "》已经找到，点击查看");
        picTextItem.setPicUrl(BaseConstants.BASE_LOGO_URL);
        picTextItem.setDescription("查看更多电影信息");

        //拼接电影搜索网址
        String urlString;
        try {
            urlString = BaseConstants.MOVIE_URL + "/index.php/vod/search.html?wd="
                        + URLEncoder.encode(keyWord, "UTF-8");
            picTextItem.setUrl(urlString);
            list.add(picTextItem);
            return list;
        } catch (UnsupportedEncodingException e) {
            logger.error("搜索电影失败", e);
            return null;
        }
    }

    public void setMacVodDao(IMacVodDao macVodDao) {
        this.macVodDao = macVodDao;
    }

}

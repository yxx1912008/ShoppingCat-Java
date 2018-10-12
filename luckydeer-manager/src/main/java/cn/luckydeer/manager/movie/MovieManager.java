package cn.luckydeer.manager.movie;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
    private List<MacVodDo> selectTopFiveMovie(String keyWords) {
        return macVodDao.selectTopFiveMovie(keyWords);
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

        List<MacVodDo> movieList = selectTopFiveMovie(keyWord);

        if (!CollectionUtils.isEmpty(movieList)) {
            WeixinPicTextItem movieItem = null;
            String url = BaseConstants.MOVIE_URL + "/index.php/vod/detail/id/";
            for (MacVodDo macVodDo : movieList) {
                movieItem = new WeixinPicTextItem();
                movieItem.setTitle(macVodDo.getVodName());
                movieItem.setDescription(macVodDo.getVodContent());
                String pic = macVodDo.getVodPic();
                //如果地址里包含 前缀 替换前缀
                if (StringUtils.contains(macVodDo.getVodPic(), "pic.php?pic=")) {
                    pic = StringUtils.replace(pic, "pic.php?pic=", "http://");
                }
                movieItem.setPicUrl(pic);
                movieItem.setUrl(url + macVodDo.getVodId() + ".html");
                list.add(movieItem);
            }
        }

        WeixinPicTextItem picTextItem = new WeixinPicTextItem();
        picTextItem.setTitle("影视《" + keyWord + "》已经找到，点击查看更多信息");
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

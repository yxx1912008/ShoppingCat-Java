package cn.luckydeer.manager.indexposter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.utils.DateUtilSelf;
import cn.luckydeer.dao.webmagic.daoInterface.ICatIndexPosterDao;
import cn.luckydeer.dao.webmagic.dataobject.CatIndexPosterDo;
import cn.luckydeer.manager.webmagic.pipeline.CatPipeLine;
import cn.luckydeer.manager.webmagic.utils.WebMagicUtils;
import cn.luckydeer.webmagic.constants.WebmagicConstant;
import cn.luckydeer.webmagic.task.IndexPosterTask;

/**
 * 
 * 购物猫首页海报管理
 * @author yuanxx
 * @version $Id: IndexPosterManager.java, v 0.1 2018年6月22日 下午5:07:50 yuanxx Exp $
 */
public class IndexPosterManager {

    Logger                             logger      = LoggerFactory
                                                       .getLogger("LUCKYDEER-MANAGER-LOG");

    /** 缓存 用来缓存首页海报  */
    private static Map<String, Object> posterCache = new HashMap<String, Object>();

    private ICatIndexPosterDao         catIndexPosterDao;

    private CatPipeLine                catPipeLine;

    /**
     * 
     * 注解：获取首页海报
     * @return
     * @author yuanxx @date 2018年6月22日
     */
    public List<IndexPosterTask> getIndexPoster() {
        Date date = new Date();
        /**  是否超过 最后更新时间 默认缓存更新时间为每两个小时  */
        if (null == posterCache.get("expiryTime")
            || (DateUtilSelf.calculateDecreaseMinute((Date) posterCache.get("expiryTime"), date) > 120)) {
            logger.info("读取海报缓存过期时间失败");
            Date expiryTime = DateUtilSelf.increaseHour(date, 2);
            posterCache.put("expiryTime", expiryTime);
            boolean flag = WebMagicUtils.crawlContent(WebmagicConstant.CAT_HOST,
                IndexPosterTask.class, catPipeLine);
            if (!flag) {
                logger.error("首页海报抓取失败");
                return null;
            }
            List<CatIndexPosterDo> list = catIndexPosterDao.getIndexPoster();
            List<IndexPosterTask> modelList = new ArrayList<>();
            for (CatIndexPosterDo catIndexPosterDo : list) {
                if (null != indexPosterConvert(catIndexPosterDo)) {
                    modelList.add(indexPosterConvert(catIndexPosterDo));
                }
            }
            posterCache.put("value", modelList);
            return modelList;
        }

        if (null != posterCache.get("value")) {
            @SuppressWarnings("unchecked")
            List<IndexPosterTask> list = (List<IndexPosterTask>) posterCache.get("value");
            return list;
        }

        return null;
    }

    /**
     * 
     * 注解：插入一条海报数据
     * @param record
     * @return
     * @author yuanxx @date 2018年6月25日
     */
    public int insert(CatIndexPosterDo record) {
        return catIndexPosterDao.insert(record);
    }

    public IndexPosterTask indexPosterConvert(CatIndexPosterDo indexPosterDo) {
        IndexPosterTask indexPosterModel = null;
        if (null != indexPosterDo) {
            indexPosterModel = new IndexPosterTask();
            indexPosterModel.setImgUrl(indexPosterDo.getImgUrl());
            indexPosterModel.setTargetUrl(indexPosterDo.getTargetUrl());
        }
        return indexPosterModel;
    }

    public void setCatIndexPosterDao(ICatIndexPosterDao catIndexPosterDao) {
        this.catIndexPosterDao = catIndexPosterDao;
    }

    public void setCatPipeLine(CatPipeLine catPipeLine) {
        this.catPipeLine = catPipeLine;
    }
}

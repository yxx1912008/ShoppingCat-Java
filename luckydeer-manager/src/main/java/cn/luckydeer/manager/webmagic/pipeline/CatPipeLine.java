package cn.luckydeer.manager.webmagic.pipeline;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import cn.luckydeer.dao.webmagic.dataobject.CatIndexPosterDo;
import cn.luckydeer.manager.indexposter.IndexPosterManager;
import cn.luckydeer.webmagic.task.IndexPosterTask;

public class CatPipeLine implements PageModelPipeline<Object> {

    private Logger             logger = LoggerFactory.getLogger("LUCKYDEER-WEBMAGIC-LOG");

    private IndexPosterManager indexPosterManager;

    @Override
    public void process(Object obj, Task task) {
        /**  1.判断抓取的对象是否是 指定类型 */
        if (obj instanceof IndexPosterTask) {
            IndexPosterTask model = (IndexPosterTask) obj;
            CatIndexPosterDo record = new CatIndexPosterDo();
            record.setGmtCreate(new Date());
            record.setImgUrl(model.getImgUrl());
            record.setTargetUrl(model.getTargetUrl());
            indexPosterManager.insert(record);
            logger.info("新增海报:" + ToStringBuilder.reflectionToString(obj));
        }
    }

    public IndexPosterManager getIndexPosterManager() {
        return indexPosterManager;
    }

    public void setIndexPosterManager(IndexPosterManager indexPosterManager) {
        this.indexPosterManager = indexPosterManager;
    }

}

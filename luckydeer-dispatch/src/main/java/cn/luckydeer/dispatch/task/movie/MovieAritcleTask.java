package cn.luckydeer.dispatch.task.movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.luckydeer.manager.movie.MovieManager;

/**
 * 电影每周自动更新文章
 * 
 * @author yuanxx
 * @version $Id: MovieAritcleTask.java, v 0.1 2018年10月22日 下午4:58:04 yuanxx Exp $
 */
public class MovieAritcleTask {

    private static final Log logger = LogFactory.getLog("LUCKYDEER-TASK-LOG");

    @Autowired
    private MovieManager     movieManager;

    public void run() {
        logger.info("开始自动生成 订阅号文章");
        movieManager.getAirtcleInfo();
    }

}

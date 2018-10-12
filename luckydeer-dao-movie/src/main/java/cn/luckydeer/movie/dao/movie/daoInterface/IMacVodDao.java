package cn.luckydeer.movie.dao.movie.daoInterface;

import java.util.List;

import cn.luckydeer.movie.dao.movie.dataobject.MacVodDo;

/**
 * 电影信息操作
 * 
 * @author yuanxx
 * @version $Id: IMacVodDao.java, v 0.1 2018年10月12日 下午2:40:38 yuanxx Exp $
 */
public interface IMacVodDao {

    MacVodDo selectByPrimaryKey(Integer vodId);

    /**
     * 
     * 注解：获取最近更新前五的电影信息
     * @return
     * @author yuanxx @date 2018年10月12日
     */
    List<MacVodDo> selectTopFiveMovie(String keyWord);

}
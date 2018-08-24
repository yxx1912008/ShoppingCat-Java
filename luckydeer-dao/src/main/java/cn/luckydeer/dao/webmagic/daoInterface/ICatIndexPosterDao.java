package cn.luckydeer.dao.webmagic.daoInterface;

import java.util.List;

import cn.luckydeer.dao.webmagic.dataobject.CatIndexPosterDo;

/**
 * 购物猫海报
 * 
 * @author yuanxx
 * @version $Id: ICatIndexPosterDao.java, v 0.1 2018年6月22日 下午3:17:59 yuanxx Exp $
 */
public interface ICatIndexPosterDao {

    int deleteByPrimaryKey(Integer id);

    int insert(CatIndexPosterDo record);

    /**
     * 
     * 注解：获取海报内容
     * @return
     * @author yuanxx @date 2018年6月22日
     */
    List<CatIndexPosterDo> getIndexPoster();

}

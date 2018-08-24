package cn.luckydeer.dao.webmagic.daoImpl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.luckydeer.dao.webmagic.daoInterface.ICatIndexPosterDao;
import cn.luckydeer.dao.webmagic.dataobject.CatIndexPosterDo;

/**
 * 
 * 
 * @author yuanxx
 * @version $Id: CatIndexPosterDao.java, v 0.1 2018年6月22日 下午4:13:48 yuanxx Exp $
 */
public class CatIndexPosterDao extends SqlSessionDaoSupport implements ICatIndexPosterDao {

    private String namespace;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(CatIndexPosterDo record) {
        return getSqlSession().insert(namespace + "insert", record);
    }

    @Override
    public List<CatIndexPosterDo> getIndexPoster() {
        return getSqlSession().selectList(namespace + "getIndexPoster");
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}

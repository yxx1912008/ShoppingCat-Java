package cn.luckydeer.movie.dao.movie.daoImpl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.luckydeer.movie.dao.movie.daoInterface.IMacVodDao;
import cn.luckydeer.movie.dao.movie.dataobject.MacVodDo;

public class MacVodDao extends SqlSessionDaoSupport implements IMacVodDao {

    private String namespace = this.getClass().getName() + ".";

    /**
     * @see cn.luckydeer.movie.dao.movie.daoInterface.IMacVodDao#selectByPrimaryKey(java.lang.Integer)
     */
    @Override
    public MacVodDo selectByPrimaryKey(Integer vodId) {
        return getSqlSession().selectOne(namespace + "selectByPrimaryKey", vodId);
    }

    /**
     * @see cn.luckydeer.movie.dao.movie.daoInterface.IMacVodDao#selectTopFiveMovie()
     */
    @Override
    public List<MacVodDo> selectTopFiveMovie() {
        return getSqlSession().selectList(namespace + "selectTopFiveMovie");
    }

}

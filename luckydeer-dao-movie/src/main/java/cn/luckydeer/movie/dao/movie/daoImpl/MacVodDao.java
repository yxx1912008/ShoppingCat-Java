package cn.luckydeer.movie.dao.movie.daoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
    public List<MacVodDo> selectTopFiveMovie(String keyWords) {

        Map<String, String> param = new HashMap<String, String>();
        if (StringUtils.isNotBlank(keyWords)) {
            param.put("keyWords", keyWords);
        }
        return getSqlSession().selectList(namespace + "selectTopFiveMovie", param);
    }

}

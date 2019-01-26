package cn.luckydeer.dao.cat.daoImpl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.luckydeer.dao.cat.daoInterface.ISysOptionsDao;
import cn.luckydeer.dao.cat.dataobject.SysOptionsDo;

public class SysOptionsDao extends SqlSessionDaoSupport implements ISysOptionsDao {

    private String namespace = this.getClass().getName() + ".";

    /**
     * @see cn.luckydeer.dao.cat.daoInterface.ISysOptionsDao#selectByPrimaryKey(java.lang.Integer)
     */
    @Override
    public SysOptionsDo selectByPrimaryKey(Integer optionId) {
        return getSqlSession().selectOne(namespace + "selectByPrimaryKey", optionId);
    }

}

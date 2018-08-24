package cn.luckydeer.dao.user.daoImpl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.luckydeer.dao.user.daoInterface.ISysUserDao;
import cn.luckydeer.dao.user.dataobject.SysUserDo;

public class SysUserDao extends SqlSessionDaoSupport implements ISysUserDao {

    private String namespace;

    @Override
    public int deleteByPrimaryKey(String userId) {
        return 0;
    }

    /**
     * @see cn.luckydeer.dao.user.daoInterface.ISysUserDao#insert(cn.luckydeer.dao.user.dataobject.SysUserDo)
     */
    @Override
    public int insert(SysUserDo record) {
        return getSqlSession().insert(namespace + "insert", record);
    }

    @Override
    public int insertSelective(SysUserDo record) {
        return 0;
    }

    @Override
    public SysUserDo selectByPrimaryKey(String userId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(SysUserDo record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(SysUserDo record) {
        return 0;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}

package cn.luckydeer.dao.cat.daoImpl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.luckydeer.dao.cat.daoInterface.IWxAppStatusDao;
import cn.luckydeer.dao.cat.dataobject.WxAppStatusDo;

public class WxAppStatusDao extends SqlSessionDaoSupport implements IWxAppStatusDao{

    private String namespace=this.getClass().getName()+".";
    
    @Override
    public int deleteByPrimaryKey(String versionId) {
        return 0;
    }

    @Override
    public int insert(WxAppStatusDo record) {
        return 0;
    }

    @Override
    public int insertSelective(WxAppStatusDo record) {
        return 0;
    }

    @Override
    public WxAppStatusDo selectByPrimaryKey(String versionId) {
        return getSqlSession().selectOne(namespace+"selectByPrimaryKey",versionId);
    }

    @Override
    public int updateByPrimaryKeySelective(WxAppStatusDo record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(WxAppStatusDo record) {
        return 0;
    }

}

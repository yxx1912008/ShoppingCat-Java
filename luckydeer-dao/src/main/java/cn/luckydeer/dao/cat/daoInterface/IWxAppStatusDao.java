package cn.luckydeer.dao.cat.daoInterface;

import cn.luckydeer.dao.cat.dataobject.WxAppStatusDo;

public interface IWxAppStatusDao {
    
    int deleteByPrimaryKey(String versionId);

    int insert(WxAppStatusDo record);

    int insertSelective(WxAppStatusDo record);

    WxAppStatusDo selectByPrimaryKey(String versionId);

    int updateByPrimaryKeySelective(WxAppStatusDo record);

    int updateByPrimaryKey(WxAppStatusDo record);
}
package cn.luckydeer.dao.cat.daoInterface;

import cn.luckydeer.dao.cat.dataobject.SysOptionsDo;

public interface ISysOptionsDao {

    SysOptionsDo selectByPrimaryKey(Integer optionId);
}
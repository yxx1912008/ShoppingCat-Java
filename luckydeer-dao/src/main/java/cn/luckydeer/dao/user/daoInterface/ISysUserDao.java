package cn.luckydeer.dao.user.daoInterface;

import cn.luckydeer.dao.user.dataobject.SysUserDo;

/**
 * 
 * 系统用户操作
 * @author yuanxx
 * @version $Id: ISysUserDao.java, v 0.1 2018年6月20日 上午11:25:06 yuanxx Exp $
 */
public interface ISysUserDao {

    int deleteByPrimaryKey(String userId);

    int insert(SysUserDo record);

    int insertSelective(SysUserDo record);

    SysUserDo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(SysUserDo record);

    int updateByPrimaryKey(SysUserDo record);

}
package cn.luckydeer.test.cat;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.luckydeer.dao.cat.daoInterface.IWxAppStatusDao;
import cn.luckydeer.dao.cat.dataobject.WxAppStatusDo;
import cn.luckydeer.test.base.BaseTest;

public class WxAppStatusDaoTest extends BaseTest{

    
    @Autowired
    private IWxAppStatusDao wxAppStatusDao;
    
    @Test
    public void selectByPrimaryKey() throws Exception {
        String versionId="1.0.0";
      WxAppStatusDo info = wxAppStatusDao.selectByPrimaryKey(versionId);
      System.out.println(ToStringBuilder.reflectionToString(info));
    }
    
}

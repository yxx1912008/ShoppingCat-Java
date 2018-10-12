package cn.luckydeer.test.movie.base;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.luckydeer.movie.dao.movie.daoInterface.IMacVodDao;
import cn.luckydeer.movie.dao.movie.dataobject.MacVodDo;

public class MovieDaoTest extends BaseTest {

    @Autowired
    private IMacVodDao macVodDao;

    /**
     * 
     * 注解：测试根据主键查询电影信息
     * @throws Exception
     * @author yuanxx @date 2018年10月12日
     */
    @Test
    public void selectByPrimaryKey() throws Exception {
        Integer vodId = 281;
        MacVodDo info = macVodDao.selectByPrimaryKey(vodId);
        System.out.println(ToStringBuilder.reflectionToString(info));
    }

}

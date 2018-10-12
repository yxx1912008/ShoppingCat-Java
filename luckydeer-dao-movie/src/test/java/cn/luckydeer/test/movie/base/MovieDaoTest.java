package cn.luckydeer.test.movie.base;

import java.util.List;

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

    /**
     * 
     * 注解：查询最新添加入库的五个电影信息
     * @throws Exception
     * @author yuanxx @date 2018年10月12日
     */
    @Test
    public void selectTopFiveMovie() throws Exception {
        String keyWords = "女人";
        List<MacVodDo> list = macVodDao.selectTopFiveMovie(keyWords);
        System.out.println(list.size());
        for (MacVodDo macVodDo : list) {
            System.out.println(ToStringBuilder.reflectionToString(macVodDo));
        }
    }

}

package cn.luckydeer.test.webmagic;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.luckydeer.dao.webmagic.daoInterface.ICatIndexPosterDao;
import cn.luckydeer.dao.webmagic.dataobject.CatIndexPosterDo;
import cn.luckydeer.test.base.BaseTest;

/**
 * 首页海报操作测试
 * 
 * @author yuanxx
 * @version $Id: IndexPosterTest.java, v 0.1 2018年6月22日 下午4:40:20 yuanxx Exp $
 */
public class IndexPosterTest extends BaseTest {

    @Autowired
    private ICatIndexPosterDao catIndexPosterDao;

    /**
     * 
     * 测试：插入一条海报数据
     * @throws Exception
     * @author yuanxx @date 2018年6月25日
     */
    @Test
    public void insert() throws Exception {
        CatIndexPosterDo record = new CatIndexPosterDo();
        String imgUrl = "imgurl";
        record.setImgUrl(imgUrl);
        String targetUrl = "imgurl";
        record.setTargetUrl(targetUrl);
        catIndexPosterDao.insert(record);

    }

    /**
     * 
     * 注解：获取数据库存储的海报图片信息
     * @throws Exception
     * @author yuanxx @date 2018年6月22日
     */
    @Test
    public void getIndexPoster() throws Exception {
        List<CatIndexPosterDo> list = catIndexPosterDao.getIndexPoster();
        for (CatIndexPosterDo catIndexPosterDo : list) {
            System.out.println(catIndexPosterDo.getId());
        }

    }

}

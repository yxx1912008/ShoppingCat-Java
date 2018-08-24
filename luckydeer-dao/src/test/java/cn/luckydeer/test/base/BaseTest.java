package cn.luckydeer.test.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:testSpringConfig/testparent.xml",
                       "classpath:springConfig/spring-*.xml" })
public class BaseTest {

    /**
     * 
     * 注解：junit启动测试
     * @author yuanxx @date 2018年6月6日
     */
    @Test
    public void baseTest() {

        System.out.println("Junit启动成功");
    }

}

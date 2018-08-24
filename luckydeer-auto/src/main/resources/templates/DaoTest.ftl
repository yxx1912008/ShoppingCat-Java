package ${classPath};




/**
 * 测试类:${className}
 * 
 * @author yuanxx
 * @version $Id: ${className}Test.java, v 0.1 ${.now}  Exp $
 */
public class ${className}Test extends BaseTest {

    @Autowired
    private I${className} ${className?uncap_first};


  	@Test
    public void testName() throws Exception {
        
        
    }

   
    private long startTime;

    @Test
    @Before
    public void start() throws Exception {
        System.out.println("----------测试开始----------");
        startTime = System.currentTimeMillis();
    }

    @Test
    @After
    public void end() throws Exception {
        System.out.println("--------执行结束，耗时:" + (System.currentTimeMillis() - startTime) + "ms---");
    }
   
   
}

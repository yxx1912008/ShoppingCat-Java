package cn.luckydeer.common.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 由系统内存 线程量决定的公共线程池
 * 不需要被关闭,自动回收
 * 
 * @author yuanxx
 * @version $Id: ExecutorServiceUtils.java, v 0.1 2018年6月26日 下午2:16:55 yuanxx Exp $
 */
public class ExecutorServiceUtils {

    //     prod 4核cpu  
    //     如果是CPU（计算）密集型应用，则线程池大小设置为N+1
    //     如果是IO（网络）密集型应用，则线程池大小设置为2N+1
    private final static int       DEFAULT_THREAD_NUM = 9;

    /**
     * 公共线程池(不能被关闭，自动回收)
     */
    private static ExecutorService executorService;

    /**
     * 私有化默认构造器
     */
    private ExecutorServiceUtils() {

    }

    /**
     * 获取系统限制线程池
     * @return
     */
    public static ExecutorService getExcutorPools() {

        if (null == executorService) {
            synchronized (ExecutorServiceUtils.class) {
                if (null == executorService) {
                    executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);
                }
            }
        }
        return executorService;
    }

    public static void main(String[] args) {

        /**  系统限制线程池使用示例 */
        ExecutorServiceUtils.getExcutorPools().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是线程1");
            }
        });
        ExecutorServiceUtils.getExcutorPools().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是线程2");
            }
        });

    }
}

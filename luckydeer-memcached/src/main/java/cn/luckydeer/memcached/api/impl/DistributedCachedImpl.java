package cn.luckydeer.memcached.api.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import cn.luckydeer.memcached.api.parent.AbstractTairCached;
import cn.luckydeer.memcached.enums.CachedType;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 分布式缓存的实现类
 * 32位数系统支持的最大缓存为2G
 * key.length<250
 * value<1M
 * @author yuanxx
 * @version $Id: DistributedCachedImpl.java, v 0.1 2018年6月19日 下午3:16:34 yuanxx Exp $
 */
public class DistributedCachedImpl extends AbstractTairCached {

    private static final Logger          LOGGER    = LoggerFactory
                                                       .getLogger("LUCKYDEER-MEMCACHED-LOG");

    /** 互斥常量 （业务数据与锁数据分离，不覆盖）*/
    private static final String          mutex     = "_MUTEX";

    /** 缓存器链 用于分开存放业务数据与用户数据  */
    private Map<String, MemcachedClient> cachedMap = new ConcurrentHashMap<String, MemcachedClient>();

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#get(cn.luckydeer.memcached.enums.CachedType, java.lang.String)
     */
    @Override
    public Object get(CachedType cachedType, String key) {
        /** 验证key */
        if (validateKey(key)) {
            try {
                return cachedMap.get(cachedType.getCode()).get(key);
            } catch (Exception e) {
                LOGGER.error("读取分布式缓存异常:key=" + key, e);
            }
        }
        return null;
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#remove(cn.luckydeer.memcached.enums.CachedType, java.lang.String)
     */
    @Override
    public boolean remove(CachedType cachedType, String key) {
        if (validateKey(key)) {
            try {
                cachedMap.get(cachedType.getCode()).delete(key);
                return true;
            } catch (Exception e) {
                LOGGER.error("删除分布式缓存异常,key=" + key, e);
                return false;
            }
        }
        return false;
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#put(cn.luckydeer.memcached.enums.CachedType, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean put(CachedType cachedType, String key, Object value) {
        return put(cachedType, key, 24 * 60 * 60, value);
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#put(cn.luckydeer.memcached.enums.CachedType, java.lang.String, int, java.lang.Object)
     */
    @Override
    public boolean put(CachedType cachedType, String key, int exp, Object value) {

        if (validateKey(key) && validateExp(exp) && validateValue(value)) {
            try {
                cachedMap.get(cachedType.getCode()).set(key, exp, value);
                return true;
            } catch (Exception e) {
                LOGGER.error("缓存内部系统异常,保存失败key=" + key, e);
                return false;
            }
        }
        return false;
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#nonblock(cn.luckydeer.memcached.enums.CachedType, java.lang.String)
     */
    @Override
    public boolean nonblock(CachedType cachedType, String key) {
        final String lockKey = key + mutex;
        if (validateKey(lockKey)) {
            try {
                // 避免宕机死锁  300秒锁失效时间
                OperationFuture<Boolean> future = cachedMap.get(cachedType.getCode()).add(lockKey,
                    300, Boolean.TRUE);
                return future.get().booleanValue();
            } catch (InterruptedException e) {
                LOGGER.error("缓存内部系统异常,锁失败key=" + key, e);
            } catch (ExecutionException e) {
                LOGGER.error("缓存内部系统异常,锁失败key=" + key, e);
            }
        }
        return false;
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#block(cn.luckydeer.memcached.enums.CachedType, java.lang.String)
     */
    @Override
    public boolean block(CachedType cachedType, String key) {
        long excuteTime = 50;
        int synchronizedNum = 0;
        final String lockKey = key + mutex;
        if (validateKey(lockKey)) {
            try {
                // 避免宕机死锁  300秒锁失效时间
                while (true) {
                    OperationFuture<Boolean> future = cachedMap.get(cachedType.getCode()).add(
                        lockKey, 300, Boolean.TRUE);
                    if (future.get().booleanValue()) {
                        return true;
                    }
                    ++synchronizedNum;
                    // 测试用100个并发线程，计算出来的最优时差为睡眠时间==具体方法的执行时间
                    // 方法执行时间50ms,阻塞sleep(60) 结果 6127ms处理100个并发线程 17:09:32,341 - 17:09:26,214
                    // 方法执行时间50ms,无睡眠阻塞 结果 5197ms处理100个并发线程 10:25:13,809 - 10:25:08,612(虽然快，对缓存网络频繁调用的压力过大不建议使用)
                    // 方法执行时间50ms,阻塞sleep(50) 结果 5310ms处理100个并发线程 16:59:07,801 - 16:59:02,491
                    // 方法执行时间50ms,阻塞sleep(50) 结果 5378ms处理100个并发线程 17:11:58,943 - 17:11:53,565
                    // 方法执行时间50ms,阻塞sleep(30) 结果 6230ms处理100个并发线程 17:04:11,607 - 17:04:05,377
                    // 方法执行时间50ms,阻塞sleep(35) 结果 6261ms处理100个并发线程 17:06:05,939 - 17:05:59,678   
                    if (synchronizedNum == 1 || (synchronizedNum > 0 && synchronizedNum % 5 == 0)) {
                        LOGGER.warn(key + ": 方法同步次数synchronizedNum=" + synchronizedNum);
                    }
                    // 限制最大400ms睡眠
                    if (synchronizedNum >= 30 && excuteTime < 400) {
                        excuteTime = excuteTime * 2;
                    }
                    Thread.sleep(excuteTime);
                }
            } catch (Exception e) {
                LOGGER.error("缓存内部系统异常,锁失败key=" + key, e);
            }
        }
        throw new RuntimeException("调用缓存错误key=" + key);
    }

    /**
     * 
     * @see cn.luckydeer.memcached.api.DistributedCached#unlock(cn.luckydeer.memcached.enums.CachedType, java.lang.String)
     */
    @Override
    public boolean unlock(CachedType cachedType, String key) {
        final String lockKey = key + mutex;
        if (validateKey(lockKey)) {
            try {
                OperationFuture<Boolean> future = cachedMap.get(cachedType.getCode()).delete(
                    lockKey);
                return future.get().booleanValue();
            } catch (InterruptedException e) {
                LOGGER.error("缓存内部系统异常,解锁失败key=" + key, e);
            } catch (ExecutionException e) {
                LOGGER.error("缓存内部系统异常,解锁失败key=" + key, e);
            }
        }
        return false;
    }

    @Override
    public boolean cleanAll() {
        if (!CollectionUtils.isEmpty(cachedMap)) {
            for (Map.Entry<String, MemcachedClient> entry : cachedMap.entrySet()) {
                OperationFuture<Boolean> future = entry.getValue().flush();
                LOGGER.info(future.getStatus() + ": " + JSON.toJSONString(future));
            }
        }
        return true;
    }

    @Override
    public boolean update(CachedType cachedType, String key, int exp, Object value) {

        if (validateExp(exp) && validateKey(key) && validateValue(value)) {
            try {
                OperationFuture<Boolean> future = cachedMap.get(cachedType.getCode()).replace(key,
                    exp, value);
                return future.get().booleanValue();
            } catch (Exception e) {
                LOGGER.error("缓存更新失败", e);
            }
        }
        return false;
    }

    public void setCachedMap(Map<String, MemcachedClient> cachedMap) {
        this.cachedMap = cachedMap;
    }

}

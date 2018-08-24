package cn.luckydeer.memcached.api.parent;

import net.spy.memcached.KeyUtil;
import net.spy.memcached.MemcachedClientIF;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.memcached.api.DistributedCached;
import cn.luckydeer.memcached.constants.CacheConstants;

/**
 * 
 * 抽象类 Memecached工厂校验器
 * @author yuanxx
 * @version $Id: AbstractTairCached.java, v 0.1 2018年6月19日 下午3:09:25 yuanxx Exp $
 */
public abstract class AbstractTairCached implements DistributedCached {

    private static final Logger LOGGER = LoggerFactory.getLogger("LUCKYDEER-MEMCACHED-LOG");

    /**
     * 
     * 校验：验证value是否为空
     * Memcached中 value不能为空
     * @param value
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    protected boolean validateValue(Object value) {
        if (null == value) {
            LOGGER.error("value 不能为空");
            return false;
        }
        return true;
    }

    /**
     * 
     * 校验：校验有效期
     * @param exp
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    protected boolean validateExp(int exp) {
        if (exp <= 0) {
            LOGGER.error("超时时间不合法,exp= " + exp);
            return false;
        }
        // 不得超过30天缓存
        if (exp > CacheConstants.MAX_CACHE_TIME) {
            LOGGER.error("超时时间不得超过30天,exp= " + exp);
            return false;
        }
        return true;
    }

    /**
     * 
     * 校验：验证Key值格式
     * @param key
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    protected boolean validateKey(String key) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("Memcahced中，key值不能为空");
            return false;
        }
        byte[] keyBytes = KeyUtil.getKeyBytes(key);
        if (keyBytes.length >= MemcachedClientIF.MAX_KEY_LENGTH) {
            LOGGER.error("Key 过长 (最大长度 = " + MemcachedClientIF.MAX_KEY_LENGTH + "key= " + key);
            return false;
        }
        if (keyBytes.length == 0) {
            LOGGER.error("Memchached中，key必须包含至少一个字符");
            return false;
        }
        // Validate the key
        for (byte b : keyBytes) {
            if (b == ' ' || b == '\n' || b == '\r') {
                LOGGER.error("key不能包含空格、换行符:  ``" + key);
                return false;
            }
        }
        return true;
    }

}

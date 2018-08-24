package cn.luckydeer.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * Properties 配置文件处理工具类
 * 
 * @author yuanxx
 * @version $Id: CustomizedProperty.java, v 0.1 2018年6月19日 下午1:47:31 yuanxx Exp $
 */
public class CustomizedProperty extends PropertyPlaceholderConfigurer {

    private Logger                     logger = LoggerFactory.getLogger("LUCKYDEER-SYSTEM-LOG");

    /** 用来缓存系统properties文件的键值 */
    private static Map<String, String> ctxPropertiesMap;

    /**
     * 
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.Properties)
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        /** 初始化缓存 */
        ctxPropertiesMap = new HashMap<String, String>();

        /** 判断Properties配置文件是否存在或者是否为空 */
        if (null == props || props.isEmpty()) {
            logger.error("Properties配置文件不存在");
            return;
        }

        /** 循环读入properties配置 */

        for (Object key : props.keySet()) {
            String keyString = StringUtils.trim(key.toString());
            /** 跳过注释文件 */
            if (StringUtils.startsWith(keyString, "##")) {
                continue;
            }
            String value = props.getProperty(keyString);
            /** 在日志执行之前进行判断 增加系统执行效率 */
            if (logger.isInfoEnabled()) {
                logger.info("properties载入内存：key=" + keyString + ";value=" + value);
            }
            ctxPropertiesMap.put(StringUtils.trim(keyString), StringUtils.trim(value));
        }

    }

    /**
     * 
     * 注解：获取全局properties变量的值
     * @param name
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    public static String getContextProperty(String key) {
        return ctxPropertiesMap.get(key);
    }

}

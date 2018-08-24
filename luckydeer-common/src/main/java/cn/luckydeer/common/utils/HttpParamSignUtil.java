package cn.luckydeer.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.luckydeer.common.enums.HttpChannelType;

/**
 * 请求参数签名工具
 * 
 * @author yuanxx
 * @version $Id: HttpParamSignUtil.java, v 0.1 2018年6月19日 下午4:15:47 yuanxx Exp $
 */
public class HttpParamSignUtil {

    /**
     * 
     * 注解：根据请求获取当前登陆渠道
     * @param contextPath
     * @return
     * @author yuanxx @date 2018年6月19日
     */
    public static HttpChannelType getByUrl(String contextPath) {
        if (StringUtils.isBlank(contextPath)) {
            return null;
        }

        String tails[] = StringUtils.split(contextPath, ".");

        if (null == tails || tails.length < 2) {
            return null;
        }
        String tail = tails[(tails.length - 1)];

        if (StringUtils.equalsIgnoreCase("web", tail)) {
            return HttpChannelType.PC;
        }

        if (StringUtils.equalsIgnoreCase("wx", tail)) {
            return HttpChannelType.WEIXIN;
        }

        return null;
    }

    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String toBeSignStirng(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }

        return prestr.toString();
    }

    /**
     * httpClient 常用的参数签名方式
     * key=value&key=value
     * @param keyValues
     * @return  获取待签名的字符串
     */
    public static String toBeSignStirng(List<String> keyValues) {
        StringBuilder buider = new StringBuilder();
        Collections.sort(keyValues);
        int size = keyValues.size();
        for (int i = 0; i < size; i++) {
            // 最后一次不需要拼接符
            if (i == size - 1) {
                buider.append(keyValues.get(i));
            } else {
                buider.append(keyValues.get(i)).append("&");
            }

        }
        return buider.toString();
    }

}

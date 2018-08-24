package cn.luckydeer.manager.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.luckydeer.common.enums.view.ApiView;
import cn.luckydeer.common.utils.http.HttpClientSend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 获取海报
 * @author yuanxx
 * @version $Id: BannerApi.java, v 0.1 2018年8月24日 下午4:58:43 yuanxx Exp $
 */
public class BannerApi {

    private static Logger logger = LoggerFactory.getLogger(BannerApi.class);

    private static String url    = "http://mapi.dataoke.com/";

    private static String appId  = "1";

    private static String length = "237";

    private static String data   = "V2sBLoKEzCt0vdb7WbqDTxTl2ZX2fdjYQ68HrM9gBV/EcmtCTbELT3ia3FY5Is022GCWa/T8tg0bF7A=";

    /**
     * 
     * 注解：获取首页海报
     * @author yuanxx @date 2018年8月24日
     */
    public static String getBanner() {
        Map<String, String> textParm = new HashMap<>();
        textParm.put("appid", appId);
        textParm.put("length", length);
        textParm.put("data", data);
        String res = HttpClientSend.mapParamPost(url, null, textParm, null);
        if (StringUtils.isBlank(res)) {
            logger.error("获取首页海报错误");
            return null;
        }
        JSONObject result = JSON.parseObject(res);
        if (result.getIntValue("status") == ApiView.INFO_SUCCESS.getStatus()) {
            return res;
        }
        return null;
    }

}

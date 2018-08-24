package cn.luckydeer.common.utils.wechat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.luckydeer.common.utils.HttpParamSignUtil;
import cn.luckydeer.common.utils.md5.Md5Utils;

/**
 * 微信支付工具类
 * 
 * @author yuanxx
 * @version $Id: WeixinPayUtil.java, v 0.1 2018年6月21日 下午4:49:06 yuanxx Exp $
 */
public class WeixinPayUtil {

    /** 日志. */
    private static final Log    logger  = LogFactory.getLog("PAYMESSAGE-LOG");

    private static final String CHARSET = "UTF-8";

    /**
     * 签名
     * @param stringSignTemp
     * @return
     */
    public static String sign(String stringSignTemp) {

        return Md5Utils.encrypMD5_32(stringSignTemp).toUpperCase();
    }

    /**
     * 获取待签名串
     * @param keyValues
     * @param weixin_private_key
     * @return
     */
    public static String getToBesignd(List<String> keyValues, String weixin_private_key) {
        // 获取待签名字符串
        String stringA = HttpParamSignUtil.toBeSignStirng(keyValues);

        return stringA + "&key=" + weixin_private_key;
    }

    /**
     * 请求微信
     * @param url
     * @param xmlParam
     * @return
     */
    public static String sendPost(String url, String xmlParam) {

        CloseableHttpClient httpclient = HttpClients.custom().build();

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(xmlParam, "UTF-8"));

        try {

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity(), CHARSET);

        } catch (Exception e) {
            logger.error(url + ":微信请求异常", e);
        }
        return null;
    }

    /**
     * 
     * 注解：读取通知InputStream内容
     * @param stream
     * @param length
     * @return
     * @author yuanxx @date 2018年6月21日
     */
    public static final String inputRead(InputStream stream, int length) {
        try {
            if (length > 0) {
                // 读取回调通知参数，微信通知参数在request.getInputStream()内，使用request.getParameter()无法读取
                DataInputStream input = new DataInputStream(stream);
                byte[] dataOrigin = new byte[length];
                input.readFully(dataOrigin);
                input.close();
                return new String(dataOrigin);
            }
        } catch (IOException ex) {
            logger.error("微信回调读取流异常", ex);
        }
        return null;
    }

}

package cn.luckydeer.common.utils.http;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.luckydeer.common.utils.date.DateUtilSelf;
import cn.luckydeer.common.utils.wechat.WeixinPayUtil;

/**
 * HttpClientSend
 * @author panwuhai
 * @version $Id: HttpClientSend.java, v 0.1 2016年10月20日 上午10:09:01 panwuhai Exp $
 */
public class HttpClientSend {

    /** 日志. */
    private static final Log           logger         = LogFactory.getLog(HttpClientSend.class);

    private static final String        CHARSET        = "UTF-8";

    /** 10秒链接探测服务器 */
    private static final int           connectTimeOut = 10000;

    /** 30秒数据传输 */
    private static final int           socketTimeOut  = 50000;

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = null;
        //采用绕过验证的方式处理https请求  
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (KeyManagementException e) {
            logger.error("绕过https验证异常", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("绕过https验证异常", e);
        }

        if (null != sslcontext) {
            // 设置协议http和https对应的处理socket链接工厂的对象  
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        } else {
            cm = new PoolingHttpClientConnectionManager();
        }
        //设置线程数最大100
        cm.setMaxTotal(10000);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(1000);

        // 请求重试处理
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount,
                                        HttpContext context) {
                if (executionCount >= 2) {// 如果已经重试了2次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(myRetryHandler)
            .build();
    }

    /** 
     * 绕过验证 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException,
                                                    KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    /**
     * 字符串流参数post请求
     * @param url
     * @param jsonParam
     * @param charSet
     * @return
     */
    public static String jsonPost(String url, Map<String, String> headerParameter,
                                  String jsonParam, String charSet) {

        charSet = StringUtils.isNotBlank(charSet) ? charSet : CHARSET;

        try {
            // 5秒链接探测服务器，10秒数据传输
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            if (!CollectionUtils.isEmpty(headerParameter)) {
                Iterator<Entry<String, String>> it = headerParameter.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> parmEntry = (Entry<String, String>) it.next();
                    httpPost.addHeader(parmEntry.getKey(), parmEntry.getValue());
                }
            }

            // 字符串流post请求
            StringEntity postEntity = new StringEntity(jsonParam, charSet);
            postEntity.setContentEncoding(charSet);
            postEntity.setContentType("application/json");

            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (Exception e) {
            logger.error(url + "：请求异常", e);
            return null;
        }
    }

    /**
     * 上传文件
     * key=value&key=value参数post请求
     * @param url
     * @param textParm
     * @param charSet
     * @return
     */
    public static String uploadMutilPartFile(String url, MultipartFile file,
                                             Map<String, String> textParm, String charSet) {

        charSet = StringUtils.isNotBlank(charSet) ? charSet : CHARSET;
        try {
            String fileName = file.getOriginalFilename();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA,
                fileName);// 文件流

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            if (!CollectionUtils.isEmpty(textParm)) {
                Iterator<Entry<String, String>> it = textParm.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> parmEntry = (Entry<String, String>) it.next();
                    builder.addTextBody(parmEntry.getKey(), parmEntry.getValue());
                }
            }

            httpPost.setEntity(builder.build());

            HttpResponse httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (Exception e) {
            logger.error(url + "：请求异常", e);
            return null;
        }
    }

    /**
     * 上传文件
     * key=value&key=value参数post请求
     * @param url
     * @param textParm
     * @param charSet
     * @return
     */
    public static String uploadFile(String url, File file, Map<String, String> textParm,
                                    String charSet) {

        charSet = StringUtils.isNotBlank(charSet) ? charSet : CHARSET;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data",
                charSet), file.getName());
            multipartEntityBuilder.addPart("file", fileBody);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            if (!CollectionUtils.isEmpty(textParm)) {
                Set<String> keyNames = textParm.keySet();
                String keyName;
                StringBody keyValue;
                for (Iterator<String> iterator = keyNames.iterator(); iterator.hasNext(); multipartEntityBuilder
                    .addPart(keyName, keyValue)) {
                    keyName = (String) iterator.next();
                    keyValue = new StringBody((String) textParm.get(keyName), ContentType.create(
                        "text/plain", charSet));
                }
            }

            httpPost.setEntity(multipartEntityBuilder.build());

            HttpResponse httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (Exception e) {
            logger.error(url + "：请求异常", e);
            return null;
        }
    }

    /**
     * key=value&key=value参数post请求
     * @param url
     * @param textParm
     * @param charSet
     * @return
     */
    public static String mapParamPost(String url, Map<String, String> headerParameter,
                                      Map<String, String> textParm, String charSet) {

        charSet = StringUtils.isNotBlank(charSet) ? charSet : CHARSET;

        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            if (!CollectionUtils.isEmpty(headerParameter)) {
                Iterator<Entry<String, String>> it = headerParameter.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> parmEntry = (Entry<String, String>) it.next();
                    httpPost.addHeader(parmEntry.getKey(), parmEntry.getValue());
                }
            }

            List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
            if (!CollectionUtils.isEmpty(textParm)) {
                Iterator<Entry<String, String>> it = textParm.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> parmEntry = (Entry<String, String>) it.next();
                    param.add(new BasicNameValuePair(parmEntry.getKey(), parmEntry.getValue()));
                }
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(param, charSet);

            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (Exception e) {
            logger.error(url + "：请求异常", e);
            return null;
        }
    }

    /**
     * 字符串流参数post请求
     * @param url
     * @param jsonParam
     * @param charSet
     * @return
     */
    public static String jsonCookiePost(String url, Map<String, String> headerParameter,
                                        String jsonParam, String charSet, String ticket) {

        charSet = StringUtils.isNotBlank(charSet) ? charSet : CHARSET;

        try {
            // 5秒链接探测服务器，10秒数据传输
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                .setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            if (!CollectionUtils.isEmpty(headerParameter)) {
                Iterator<Entry<String, String>> it = headerParameter.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> parmEntry = (Entry<String, String>) it.next();
                    httpPost.addHeader(parmEntry.getKey(), parmEntry.getValue());
                }
            }

            // 字符串流post请求
            StringEntity postEntity = new StringEntity(jsonParam, charSet);
            postEntity.setContentEncoding(charSet);
            postEntity.setContentType("application/json");

            httpPost.setEntity(postEntity);
            CloseableHttpClient httpCookieClient = getCookieCilent(ticket);
            HttpResponse httpResponse = httpCookieClient.execute(httpPost);

            return EntityUtils.toString(httpResponse.getEntity(), charSet);
        } catch (Exception e) {
            logger.error(url + "：请求异常", e);
            return null;
        }

    }

    /**
     * 获取数据流
     * @param request
     * @return
     */
    public static String getRequestJson(HttpServletRequest request) {

        String respJson = null;
        try {
            respJson = WeixinPayUtil
                .inputRead(request.getInputStream(), request.getContentLength());
        } catch (IOException e) {
            logger.error("读取json数据异常", e);
            return null;
        }
        return respJson;
    }

    /**
     * 获取字典参数
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {

        Map<String, String> params = new HashMap<String, String>();

        Enumeration<String> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }

        return params;
    }

    private static CloseableHttpClient getCookieCilent(String ticket) {
        CookieStore cookieStores = new BasicCookieStore();

        BasicClientCookie clientCookie = new BasicClientCookie("ticket", ticket);

        // 设置成与请求的域名或Ip一致即可
        // clientCookie.setDomain("192.168.18.226");
        clientCookie.setPath("/");
        // 设置保持几分钟有效时间
        clientCookie.setExpiryDate(DateUtilSelf.increaseMinute(10));
        cookieStores.addCookie(clientCookie);
        return HttpClients.custom().setDefaultCookieStore(cookieStores).build();
    }
}

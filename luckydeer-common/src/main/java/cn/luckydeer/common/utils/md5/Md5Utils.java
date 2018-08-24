/**
 * winchance Inc.
 * Copyright (c) 2010-2014 All Rights Reserved.
 */
package cn.luckydeer.common.utils.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 不可逆加密（摘要）
 * 
 * @author yuanxx
 * @version $Id: Md5Utils.java, v 0.1 2018年6月21日 下午4:48:37 yuanxx Exp $
 */
public class Md5Utils {

    private static final Log logger = LogFactory.getLog(Md5Utils.class);

    /**
     * 32位MD5摘要不可逆算法
     * @return 
     */
    public static String encrypMD5_32(String password) {
        return Md5(password, 32);
    }

    /**
     * 16位MD5摘要不可逆算法
     * @return 
     */
    public static String encrypMD5_16(String password) {
        return Md5(password, 16);
    }

    /**
     * MD5 计算 加密 
     * @param plainText
     * @return
     */
    private static String Md5(String plainText, int bit) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            // 32位加密
            if (32 == bit) {
                return buf.toString();
            }
            // 16位加密
            if (16 == bit) {
                return buf.toString().substring(8, 24);
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("md5加密异常", e);
        }
        return null;
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}

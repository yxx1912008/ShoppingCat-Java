package cn.luckydeer.common.utils;

import java.awt.Color;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cn.luckydeer.common.utils.date.DateUtilSelf;

/**
 * 生成Code工具类
 * 
 * @author yuanxx
 * @version $Id: AutoCodeUtils.java, v 0.1 2018年6月21日 下午4:17:23 yuanxx Exp $
 */
public class AutoCodeUtils {

    /** 
     * 订单号的生成方式
     * 前缀业务n位+13位毫秒时间戳+2位随机数
     * @param 
     * @return
     */
    public static String createTradeNo(String prefix) {
        return prefix + System.currentTimeMillis() + getRandNum(2);
    }

    /**
     * 25位主键
     * 生成25位唯一 15位毫秒+10位 uid.hashcode
     * 绝对的唯一(带有时间的 瞬时发生的唯一数据)
     * 
     * @return yyMMddHHmmssS + 10位补全hashCode
     */
    public static String getUniqueKey() {
        String uid = UUID.randomUUID().toString();
        long hashCodeV = uid.hashCode();

        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }

        String hashCodeStr = String.valueOf(hashCodeV);

        // 时间长度   yyMMddHHmmssS  15位
        String longMilltime = DateUtilSelf.yyLongMillFormat(new Date());

        StringBuilder primaryBuilder = new StringBuilder(longMilltime);

        primaryBuilder.append(hashCodeStr);

        // 需要补全的随机数
        int fillNum = 25 - longMilltime.length() - hashCodeStr.length();

        // hashCodeStr 长度小于等于10 总长度小于25用随机数补全
        if (fillNum > 0) {
            primaryBuilder.append(getRandNum(fillNum));
            return primaryBuilder.toString();
        }

        if (fillNum < 0) {
            return StringUtils.substring(primaryBuilder.toString(), 0, 25);
        }
        return primaryBuilder.toString();
    }

    /**
     * 
     * 注解：生成邀请码  6位=头字母大写+5位数字
     * @return
     * @author yuanxx @date 2018年6月21日
     */
    public static String getInvitationCode() {
        StringBuilder builder = new StringBuilder();
        // 1位随机大写字母
        builder.append(getRandUpperStr(1));
        // 5位随机数字
        builder.append(getRandNum(5));

        return builder.toString();
    }

    /**
     * 
     * 注解：取charCount位随机数字
     * @param length 随机数字位数
     * @return
     * @author yuanxx @date 2018年6月21日
     */
    public static String getRandNum(int length) {

        StringBuilder charValue = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            // 取大于等于0小于10的整数
            charValue.append(r.nextInt(10));
        }
        return charValue.toString();
    }

    /*
     * 获取随机数颜色
     */
    public static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /*
     * 返回某颜色的反色
     */
    public static Color getReverseColor(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    /**
     * 随机大写字母
     * @param charCount
     * @return
     */
    public static String getRandUpperStr(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 大写字母 65 : 小写字母 97
            builder.append((char) (65 + random.nextInt(26)));
        }
        return builder.toString();
    }

    /**
     * 生成随机数字和字母组合
     * @param length[生成随机数的长度]
     * @return
     */
    public static String getRandCharAndNumr(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equals(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (choice + random.nextInt(26)));
            } else { // 数字
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    /**
     * 生成随机数字和大写字母组合
     * @param length[生成随机数的长度]
     * @return
     */
    public static String getRandUpperCharAndNumr(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equals(charOrNum)) {
                // 取得大写字母
                val.append((char) (65 + random.nextInt(26)));
            } else { // 数字
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    /**
     * 生成15位时间+随机数的流水号
     * @param length  总长度
     * @return
     */
    public static String getTimeSerialNumber(int length) {

        // 时间长度   yyMMddHHmmssS 12 + 3位 
        String longMilltime = DateUtilSelf.yyLongMillFormat(new Date());
        StringBuilder primaryBuilder = new StringBuilder(longMilltime);
        // 需要补全的随机数
        int fillNum = length - longMilltime.length();

        if (fillNum > 0) {
            primaryBuilder.append(getRandNum(fillNum));
            return primaryBuilder.toString();
        }

        if (fillNum < 0) {
            return StringUtils.substring(primaryBuilder.toString(), 0, length);
        }
        return primaryBuilder.toString();
    }

}

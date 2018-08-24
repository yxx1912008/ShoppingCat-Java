package cn.luckydeer.weixin.controller.websocket.utils;

import java.util.Random;

/**
 * 用户头像工具类
 * 
 * @author yuanxx
 * @version $Id: UserHeadUtils.java, v 0.1 2018年7月26日 上午11:30:05 yuanxx Exp $
 */
public class UserHeadUtils {

    private static String[] userIcons = new String[] { "./static/headIcon/angry-birds.png",
            "./static/headIcon/captain-america.png", "./static/headIcon/hand-holding-business.png",
            "./static/headIcon/minion.png", "./static/headIcon/ninja.png" };

    /**
     * 
     * 注解：获取随机头像
     * @return
     * @author yuanxx @date 2018年7月26日
     */
    public static String getRandomUserIcon() {
        Random r = new Random();
        Integer id = r.nextInt(userIcons.length);
        return userIcons[id];
    }

    public static void main(String[] args) {
        Random r = new Random();
        Integer id = r.nextInt(userIcons.length);
        System.out.println(id);
    }
}

package cn.luckydeer.websocket.socket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.util.CollectionUtils;

import cn.luckydeer.websocket.utils.UserHeadUtils;

import com.alibaba.fastjson.JSON;

/**
 * 用户信息获取
 * 
 * @author yuanxx
 * @version $Id: UserInfoMessange.java, v 0.1 2018年7月24日 上午10:46:52 yuanxx Exp $
 */
@ServerEndpoint(value = "/websocket/{userId}/{tableNo}")
public class UserInfoMessange {

    private static int                                                                    onlieCount        = 0;

    //用户数据缓存
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, UserInfoMessange>> userInfoMessanges = new ConcurrentHashMap<>();
    //用户会话
    private Session                                                                       session;
    //用户ID 
    private String                                                                        userId;
    //桌位号
    private String                                                                        tableNo;
    //用户的头像地址
    @SuppressWarnings("unused")
    private String                                                                        iconUrl;

    /**
     * 
     * 注解：连接成功
     * @param session
     * @author yuanxx @date 2018年7月24日
     * @throws IOException 
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId,
                       @PathParam(value = "tableNo") String tableNo, Session session)
                                                                                     throws IOException {
        this.session = session;
        System.out.println("websocket连接成功");
        //设置桌号 id 随机用户头像
        this.userId = userId;
        this.tableNo = tableNo;
        this.iconUrl = UserHeadUtils.getRandomUserIcon();

        //根据桌号获取信息
        ConcurrentHashMap<String, UserInfoMessange> userInfos = userInfoMessanges.get(tableNo);

        if (CollectionUtils.isEmpty(userInfos)) {
            userInfos = new ConcurrentHashMap<String, UserInfoMessange>();
            userInfoMessanges.put(tableNo, userInfos);
        }
        userInfoMessanges.get(tableNo).put(userId, this);
        addOnlineCount();

        //通知组员上线了
        if (!CollectionUtils.isEmpty(userInfoMessanges.get(tableNo))) {
            for (Map.Entry<String, UserInfoMessange> entry : userInfoMessanges.get(tableNo)
                .entrySet()) {
                if (entry.getValue().userId != userId) {
                    entry.getValue().session.getAsyncRemote().sendText(
                        this.userId + "上线了,桌号是" + this.tableNo);
                } else {
                    //上线后给自己的通知
                    entry.getValue().session.getAsyncRemote().sendText(JSON.toJSONString(this));
                }

            }
        }

        System.out.println(userId + "加入，在线人数为" + onlieCount + "人");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String toUserId = JSON.parseObject(message).getString("toUserId");
        String toTableNo = JSON.parseObject(message).getString("toTableNo");
        boolean flag = userInfoMessanges.get(toTableNo).containsKey(toUserId);

        if (!flag) {
            this.session.getAsyncRemote().sendText("您选择的用户不在线！");
            return;
        }

        System.out.println(this.userId + "发送信息给" + toUserId);
        UserInfoMessange obj = (UserInfoMessange) userInfoMessanges.get(toTableNo).get(toUserId);
        obj.session.getAsyncRemote().sendText(
            this.userId + "说:" + JSON.parseObject(message).getString("message"));
    }

    /**
     * 
     * 注解：发生错误时调用
     * @param session
     * @param error
     * @author yuanxx @date 2018年7月24日
     * @throws IOException 
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        System.out.println("发生错误");
        error.printStackTrace();
        session.getBasicRemote().sendText("未知错误");
    }

    /**
     * 
     * 注解：
     * @author yuanxx @date 2018年7月24日
     * @throws IOException 
     */
    @OnClose
    public void onClose() throws IOException {
        String userId = this.userId;
        String tableNo = this.tableNo;
        System.out.println(userId + "下线");
        //获取桌位信息
        if (!CollectionUtils.isEmpty(userInfoMessanges.get(tableNo))) {

            for (Map.Entry<String, UserInfoMessange> entry : userInfoMessanges.get(tableNo)
                .entrySet()) {
                if (userId != entry.getKey()) {
                    entry.getValue().session.getAsyncRemote().sendText(userId + "下线了");
                }
            }
        }
        userInfoMessanges.get(tableNo).remove(userId);
        subOnlineCount(); //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    private synchronized void subOnlineCount() {
        onlieCount -= 1;
    }

    /**
     * 
     * 注解：发送信息
     * @param message
     * @throws IOException
     * @author yuanxx @date 2018年7月24日
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 
     * 注解：在线人数加一
     * @author yuanxx @date 2018年7月24日
     */
    private synchronized void addOnlineCount() {
        UserInfoMessange.onlieCount++;
    }

    /**
     * 
     * 注解：获取当前在线人数
     * @return
     * @author yuanxx @date 2018年7月24日
     */
    private synchronized int getOnlineCount() {
        return onlieCount;
    }

}

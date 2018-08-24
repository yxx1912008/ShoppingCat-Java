package cn.luckydeer.weixin.controller.websocket.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import cn.luckydeer.common.enums.view.ViewShowEnums;
import cn.luckydeer.common.utils.DateUtilSelf;
import cn.luckydeer.weixin.controller.websocket.entity.ReturnResult;
import cn.luckydeer.weixin.controller.websocket.entity.UserInfoEntity;
import cn.luckydeer.weixin.controller.websocket.enums.MsgType;
import cn.luckydeer.weixin.controller.websocket.utils.UserHeadUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户信息获取
 * 
 * @author yuanxx
 * @version $Id: UserInfoMessange.java, v 0.1 2018年7月24日 上午10:46:52 yuanxx Exp $
 */
@ServerEndpoint(value = "/websocket/{userName}/{tableNo}")
public class UserInfoMessange {

    private static int                                                                  onlieCount        = 0;

    //用户数据缓存
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, UserInfoEntity>> userInfoMessanges = new ConcurrentHashMap<>();
    //用户会话
    private Session                                                                     session;
    //用户ID 
    private String                                                                      userName;
    //桌位号
    private String                                                                      tableNo;
    //用户的头像地址
    private String                                                                      iconUrl;

    /**
     * 
     * 注解：连接成功
     * @param session
     * @author yuanxx @date 2018年7月24日
     * @throws IOException 
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userName") String userName,
                       @PathParam(value = "tableNo") String tableNo, Session session)
                                                                                     throws IOException {
        this.session = session;
        System.out.println("websocket连接成功");
        //设置桌号 id 随机用户头像
        this.userName = userName;
        this.tableNo = tableNo;
        this.iconUrl = UserHeadUtils.getRandomUserIcon();
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUserName(userName);
        entity.setTableNo(tableNo);
        entity.setSession(session);
        entity.setIconUrl(this.iconUrl);
        //根据桌号获取信息
        ConcurrentHashMap<String, UserInfoEntity> userInfos = userInfoMessanges.get(tableNo);
        if (CollectionUtils.isEmpty(userInfos)) {
            userInfos = new ConcurrentHashMap<String, UserInfoEntity>();
            userInfoMessanges.put(tableNo, userInfos);
        }
        userInfoMessanges.get(tableNo).put(userName, entity);
        addOnlineCount();
        //通知组员上线了
        if (!CollectionUtils.isEmpty(userInfoMessanges.get(tableNo))) {

            /** 获取所有成员信息 UserInfoEntity里面的 Session字段会影响序列化 要注意  */
            ConcurrentHashMap<String, UserInfoEntity> map = userInfoMessanges.get(tableNo);
            /** 为了方便前端读取，此处将map转换为list  */
            List<UserInfoEntity> list = new ArrayList<>(map.values());
            String userList = JSON.toJSONString(list);
            System.out.println(userList);
            for (Map.Entry<String, UserInfoEntity> entry : userInfoMessanges.get(tableNo)
                .entrySet()) {
                Session entrySession = entry.getValue().getSession();
                /**  发送成员列表信息 */
                sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "获取列表成功",
                    MsgType.USER_LIST.getCode(), userList).toString(), entrySession);

                if (entry.getValue().getUserName() != userName) {
                    sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "新用户上线",
                        MsgType.ON_LINE_NOTICE.getCode(), userName + " 上线了").toString(),
                        entrySession);
                } else {
                    //上线后给自己的通知
                    Map<String, Object> param = new HashMap<>();
                    param.put("userName", this.userName);
                    param.put("tableNo", this.tableNo);
                    param.put("iconUrl", this.iconUrl);
                    sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "登录成功",
                        MsgType.SUCESS_MSG.getCode(), param).toString(), entrySession);
                }
            }
        }
        System.out.println(this.userName + "加入，在线人数为" + getOnlineCount() + "人");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println(message);

        JSONObject userMessageObject = JSON.parseObject(message);
        if (null == userMessageObject) {
            System.out.println("Json数据转换失败");
            return;
        }

        /**  获取用户基本信息 */
        String sendUserName = userMessageObject.getString("userName");
        String sendUserTableNo = userMessageObject.getString("tableNo");
        /**  信息发送时间 */
        String sendTime = DateUtilSelf.simpleDate(new Date());
        /**  信息体内容 */
        String chatMessange = userMessageObject.getString("messange");
        /** 获取用户消息类型  */
        String typeCode = userMessageObject.getString("msgType");

        MsgType msgType = MsgType.getEnumByCode(typeCode);

        if (null == msgType) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        if (!msgType.isGroupMsg()) {
            String toUserId = JSON.parseObject(message).getString("toUserId");
            String toTableNo = JSON.parseObject(message).getString("toTableNo");
            String msg = JSON.parseObject(message).getString("message");
            boolean flag = userInfoMessanges.get(toTableNo).containsKey(toUserId);

            if (!flag) {
                sendMessage(new ReturnResult(ViewShowEnums.ERROR_FAILED.getStatus(), "您选择的用户不在线！",
                    MsgType.ERROR_MSG.getDetail(), null).toString(), session);
                return;
            }

            System.out.println(this.userName + "发送信息给" + toUserId);
            UserInfoEntity obj = (UserInfoEntity) userInfoMessanges.get(toTableNo).get(toUserId);
            param.put("sendUser", this.userName);
            param.put("userIcon", this.iconUrl);
            param.put("msg", msg);
            param.put("sendUserTableNo", sendUserTableNo);
            sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "操作成功",
                MsgType.USER_MESSANGE.getDetail(), param).toString(), obj.getSession());
        } else {
            /** 循环向每个组员发送信息  */
            for (Map.Entry<String, UserInfoEntity> entry : userInfoMessanges.get(tableNo)
                .entrySet()) {
                Session entrySession = entry.getValue().getSession();

                if (entrySession.isOpen()) {
                    /**  发送消息给组员  */
                    param.put("time", sendTime);
                    param.put("iconUrl", this.iconUrl);
                    param.put("userName", sendUserName);
                    param.put("chatMessange", chatMessange);
                    sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(),
                        "获取一条新的组群消息", MsgType.GROUP_MSG.getCode(), param).toString(),
                        entrySession);
                }
            }
        }
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
    }

    /**
     * 
     * 注解：
     * @author yuanxx @date 2018年7月24日
     * @throws IOException 
     */
    @OnClose
    public void onClose() throws IOException {
        String userName = this.userName;
        String tableNo = this.tableNo;
        System.out.println(this.userName + "下线");

        //获取桌位信息
        if (!CollectionUtils.isEmpty(userInfoMessanges.get(tableNo))) {

            if (null != userInfoMessanges.get(tableNo).get(userName)) {
                userInfoMessanges.get(tableNo).remove(userName);
            }
            /** 获取所有成员信息 UserInfoEntity里面的 Session字段会影响序列化 要注意  */
            ConcurrentHashMap<String, UserInfoEntity> map = userInfoMessanges.get(tableNo);
            /** 为了方便前端读取，此处将map转换为list  */
            List<UserInfoEntity> list = new ArrayList<>(map.values());
            String userList = JSON.toJSONString(list);

            for (Map.Entry<String, UserInfoEntity> entry : userInfoMessanges.get(tableNo)
                .entrySet()) {
                if (userName != entry.getKey()) {
                    /**  发送成员列表信息 */
                    sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "获取列表成功",
                        MsgType.USER_LIST.getCode(), userList).toString(), entry.getValue()
                        .getSession());
                    sendMessage(new ReturnResult(ViewShowEnums.INFO_SUCCESS.getStatus(), "用户下线",
                        MsgType.ON_LINE_NOTICE.getCode(), userName + " 下线了").toString(), entry
                        .getValue().getSession());
                }
            }
        }

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
    public void sendMessage(String message, Session session) throws IOException {
        if (null == session) {
            if (this.session.isOpen()) {
                this.session.getAsyncRemote().sendText(message);
            }
        }
        if (session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }

        /** 用于清空消息缓存  */
        session.getAsyncRemote().flushBatch();
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

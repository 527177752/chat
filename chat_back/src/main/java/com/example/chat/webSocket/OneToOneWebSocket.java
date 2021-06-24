package com.example.chat.webSocket;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.Record;
import com.example.chat.entity.Unread;
import com.example.chat.entity.User;
import com.example.chat.service.IRecordService;
import com.example.chat.service.IUnreadService;
import com.example.chat.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前后端交互的类实现消息的接收推送(自己发送给另一个人)
 *
 * @ServerEndpoint(value = "/test/oneToOne") 前端通过此URI 和后端交互，建立连接
 */
@Slf4j
@ServerEndpoint(value = "/test/oneToOne/{sendId}")
@Component
public class OneToOneWebSocket {

    private static IUserService userService;

    private static IUnreadService unreadService;

    private static IRecordService recordService;

    @Autowired
    public void setUserService(IUserService userService) {
        OneToOneWebSocket.userService = userService;
    }

    @Autowired
    public void setUnreadService(IUnreadService unreadService) {
        OneToOneWebSocket.unreadService = unreadService;
    }

    @Autowired
    public void setRecordService(IRecordService recordService) {
        OneToOneWebSocket.recordService = recordService;
    }

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /** 存放所有在线的客户端 */
    private static Map<Integer, Session> clients = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("sendId") Integer sendId, Session session) {
        onlineCount.incrementAndGet(); // 在线数加1
        clients.put(sendId, session);
        log.info("有新连接加入：{}，当前在线人数为：{}", sendId, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("sendId") Integer sendId,Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(sendId);
        log.info("有一连接关闭：{}，当前在线人数为：{}", sendId, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam("sendId") Integer sendId, String message, Session session) {
        log.info("服务端收到客户端[{}]的消息[{}]", sendId, message);
                Map<String, Object> map = (Map<String, Object>) JSON.parse(message);
                Integer recId = (Integer) map.get("recId");
                message = (String) map.get("message");

                //sendId等用户信息应该是根据用户登录信息直接获取（我就意思一下）
                User user = userService.getById(sendId);
                //记录到聊天消息表
                Record record = new Record();
                record.setRecId(recId);
                record.setSendId(sendId);
                record.setImage(user.getImage());
                record.setContent(message);
                LocalDateTime now = LocalDateTime.now();
                record.setCreateTime(now);
                recordService.save(record);

                Session toSession = clients.get(recId);
                if (toSession == null) {
                    //记录到消息未读表
                    Unread unread = new Unread();
                    unread.setContent(message);
                    unread.setRecId(recId);
                    unread.setImage(user.getImage());
                    unread.setSendId(sendId);
                    unread.setCreateTime(now);
                    unreadService.save(unread);
                } else {
                    this.sendMessage(JSON.toJSONString(record), toSession);
                }
                //推送给自己
                this.sendMessage(JSON.toJSONString(record), session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息[{}]", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            // 这边也要做相应的处理，比如消息存到unread表
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }

}

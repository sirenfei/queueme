package com.kiwiip.queue;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        sendMessage("连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("有一连接关闭！");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param queryType 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String queryType) {
        log.info("收到信息:" + queryType);

        //群发消息
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage("hello");
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Object message) {
        try {
            this.session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现服务器主动群发消息
     */
    public static void sendInfo(Object message) {
        for (WebSocketServer item : webSocketSet) {
            item.sendMessage(message);
        }
    }
}


package com.lyra.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/websocket/{token}")
public class MyWebSocketHandle {
    private static Integer onlineCount = 0;

    private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandle.class);

    private static final Map<String, Session> map = new HashMap<>();

    private String token;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        onlineCount++;
        this.token = token;
        map.put(token, session);

        log.info("新用户打开连接:{}, 当前连接人数为:{}", session.getId(), onlineCount);
    }

    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {
        onlineCount--;
        map.remove(token);
        log.info("新用户关闭连接:{}, 当前连接人数为:{}", session.getId(), onlineCount);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端发出的消息:{}, 当前在线人数:{}", message, onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message) {
        for (String token : map.keySet()) {
            try {
                Session session = map.get(token);
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("消息发送失败:{}, 内容:{}", e.getMessage(), message);
                e.printStackTrace();
            }
            log.error("消息发送成功内容:{}",  message);
        }
    }
}

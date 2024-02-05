package com.test.websockets.spring.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ping pong测试
 * @author jiangbaojun
 * @date 2024/2/5 10:02
 * @version v1.0
 */
@Component
public class PingPongHandler extends TextWebSocketHandler {

    private static final long HEARTBEAT_INTERVAL = 10 * 1000; // 心跳间隔时间 秒

    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> heartbeatFuture;

    //AtomicInteger count = new AtomicInteger(0);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("New connection(ping): " + session.getId());
        //count.set(0);
        this.heartbeatFuture = this.heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                //如果浏览器关闭，此处无法ping通，会断开连接
                session.sendMessage(new PingMessage());
                //System.out.println(count.getAndAdd(1));
            } catch (Exception e) {
                // 处理发送 ping 消息时出现的异常
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理文本消息
        String msg = message.getPayload();
        System.out.println("received msg: "+msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("close connection(ping): " + session.getId());
        if(heartbeatFuture!=null){
            heartbeatFuture.cancel(true);
        }
    }

}

package com.test.websockets.spring.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.nio.ByteBuffer;

@Component
public class SpringSocketHandle extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //消息长度限制，可以全局配置
//        session.setBinaryMessageSizeLimit(10);
//        session.setTextMessageSizeLimit(10);
        System.out.println("New connection: " + session.getId());
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        System.out.println("received msg: "+msg);
        session.sendMessage(new TextMessage("sever response "+msg));
    }

    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        int payloadLength = message.getPayloadLength();
        ByteBuffer payload = message.getPayload();
        byte[] dst = new byte[payloadLength];
        payload.get(dst);
        System.out.println("received byte msg: "+new String(dst));
    }

    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        System.out.println("pong msg");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WS error");
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("WS close:"+closeStatus.toString());
    }

    // 支持分片消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
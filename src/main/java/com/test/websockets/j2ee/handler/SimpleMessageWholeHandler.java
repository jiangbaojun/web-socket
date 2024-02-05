package com.test.websockets.j2ee.handler;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 自定义消息接收
 * @apiNote 作用和@OnMessage注解一样
 * @author jiangbaojun
 * @date 2024/2/4 15:56
 * @version v1.0
 */
public class SimpleMessageWholeHandler implements  MessageHandler.Whole<String> {

    private Session session;

    public SimpleMessageWholeHandler(Session session) {
        this.session = session;
    }

    @Override
    public void onMessage(String message) {
        System.out.println("handler message: "+message);
        try {
            session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[0]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

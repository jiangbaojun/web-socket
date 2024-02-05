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
public class ByteMessageWholeHandler implements  MessageHandler.Whole<byte[]> {

    private Session session;

    public ByteMessageWholeHandler(Session session) {
        this.session = session;
    }

    @Override
    public void onMessage(byte[] message) {
        System.out.println("handler byte message: "+new String(message));
    }
}

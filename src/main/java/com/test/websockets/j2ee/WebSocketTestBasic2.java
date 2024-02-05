package com.test.websockets.j2ee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

@Slf4j
@Component
@ServerEndpoint("/websocket_b2")
public class WebSocketTestBasic2 {

  @OnMessage
  public void onMessage(byte[] message, Session session) throws IOException {
    System.out.println("接收到消息:"+new String(message,"utf-8"));
    //返回信息
    String resultStr="{name:\"张三\",age:18,addr:\"上海浦东\"}";
    //发送字符串信息的 byte数组
    ByteBuffer bf=ByteBuffer.wrap(resultStr.getBytes("utf-8"));
    session.getBasicRemote().sendBinary(bf);
  }
  
  @OnOpen
  public void onOpen(Session session) {
    System.out.println("Client connected2");
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("Connection closed2");
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    log.info("[websocket] 连接异常：id={}，throwable={}", session.getId(), throwable.getMessage());
    // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
    session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    throwable.printStackTrace();
  }
}
package com.test.websockets.j2ee;

import com.test.common.SpringContextHolder;
import com.test.service.TestService;
import com.test.websockets.j2ee.handler.ByteMessageWholeHandler;
import com.test.websockets.j2ee.handler.SimpleMessageWholeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Slf4j
@Component
@ServerEndpoint("/websocket2")
public class WebSocketTest2 {

  //不能直接注入
//  @Autowired
//  private TestService testService;
  
  @OnOpen
  public void onOpen(Session session) {
    Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
    //spring注入测试。报错
//    int increase = testService.increase(2);
    int increase = SpringContextHolder.getBean(TestService.class).increase(2);

    //作用和@OnMessage一致， 同一种消息类型不能重复声明
    session.addMessageHandler(String.class, new SimpleMessageWholeHandler(session));
    session.addMessageHandler(byte[].class, new ByteMessageWholeHandler(session));

    //设置消息最大长度。超过最大长度会断开连接
//    session.setMaxBinaryMessageBufferSize(10);
//    session.setMaxTextMessageBufferSize(10);

    //设置连接超时时间
    session.setMaxIdleTimeout(10*1000);

    System.out.println(increase);

    System.out.println("Client connected");
  }

  @OnMessage
  public void onPong(PongMessage pong, Session session) throws IOException {
      try {
        // 发送空的Ping消息
        System.out.println("pong");
        session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[0]));
      } catch (IOException e) {
        // 处理发送失败的情况
        e.printStackTrace();
        session.close();
      }
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    System.out.println("Connection closed:"+closeReason.getReasonPhrase());
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {

    log.info("[websocket] 连接异常：id={}，throwable={}", session.getId(), throwable.getMessage());
    // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
    session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    throwable.printStackTrace();
  }
}
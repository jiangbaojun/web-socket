package com.test.websockets.j2ee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@Component
@ServerEndpoint("/websocket_b1")
public class WebSocketTestBasic {

  @OnMessage
  public void onMessage(String message, Session session)
    throws IOException, InterruptedException {

    // Print the client message for testing purposes
    System.out.println("Received: " + message);

    // Send the first message to the client
    session.getBasicRemote().sendText("This is the first server message");

    // Send 3 messages to the client every 5 seconds
    int sentMessages = 0;
    while(sentMessages < 3){
      Thread.sleep(5000);
      session.getBasicRemote().
        sendText("This is an intermediate server message. Count: "
          + sentMessages);
      sentMessages++;
    }

    // Send a final message to the client
    session.getBasicRemote().sendText("This is the last server message");
  }
  
  @OnOpen
  public void onOpen(Session session) {
    System.out.println("Client connected");
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("Connection closed");
  }

  @OnError
  public void onError(Session session, Throwable throwable) throws IOException {
    log.info("[websocket] 连接异常：id={}，throwable={}", session.getId(), throwable.getMessage());
    // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
    session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    throwable.printStackTrace();
  }
}
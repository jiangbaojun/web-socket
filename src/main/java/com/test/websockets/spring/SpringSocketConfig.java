package com.test.websockets.spring;

import com.test.websockets.spring.handler.PingPongHandler;
import com.test.websockets.spring.handler.SpringSocketHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
public class SpringSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SpringSocketHandle springSocketHandle;
    @Autowired
    private PingPongHandler pingPongHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(springSocketHandle, "/spring-ws")
                .addInterceptors(new SimpleHandshakeInterceptor())
                .setAllowedOrigins("*");

        registry.addHandler(pingPongHandler, "/spring-ws1")
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024);
        container.setMaxBinaryMessageBufferSize(1024);
        container.setMaxSessionIdleTimeout(60 * 1000L);
        return container;
    }
}
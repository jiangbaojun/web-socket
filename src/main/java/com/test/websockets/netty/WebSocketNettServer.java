package com.test.websockets.netty;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNettServer implements SmartInitializingSingleton, DisposableBean {

    @Override
    public void afterSingletonsInstantiated() {
        new EmbedServer().start();
    }

    @Override
    public void destroy() throws Exception {
        new EmbedServer().stop();
    }
}
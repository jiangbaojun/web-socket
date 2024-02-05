package com.test.websockets.netty;


import com.test.websockets.netty.handle.HeartBeatServerHandler;
import com.test.websockets.netty.handle.WsByteInBoundHandle;
import com.test.websockets.netty.handle.WsTextInBoundHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class EmbedServer {
    private static final Logger logger = LoggerFactory.getLogger(EmbedServer.class);

    private Thread thread;

    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NioEventLoopGroup boss = new NioEventLoopGroup();
                NioEventLoopGroup work = new NioEventLoopGroup();

                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap
                            .group(boss, work)
                            .channel(NioServerSocketChannel.class)
                            //设置保持活动连接状态
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .localAddress(8081)
                            .handler(new LoggingHandler(LogLevel.DEBUG))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            // HTTP 请求解码和响应编码
                                            .addLast(new HttpServerCodec())
                                            // HTTP 压缩支持
                                            .addLast(new HttpContentCompressor())
                                            // HTTP 对象聚合完整对象
                                            .addLast(new HttpObjectAggregator(65536))
                                            // WebSocket支持
                                            .addLast(new WebSocketServerProtocolHandler("/netty-ws"))
                                            .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                                            .addLast(new HeartBeatServerHandler())
                                            .addLast(WsTextInBoundHandle.INSTANCE)
                                            .addLast(WsByteInBoundHandle.INSTANCE);
                                }
                            });

                    //绑定端口号，启动服务端
                    ChannelFuture channelFuture = bootstrap.bind().sync();
                    System.out.println("WebSocketNettServer started");

                    //对关闭通道进行监听
                    channelFuture.channel().closeFuture().sync();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    boss.shutdownGracefully().syncUninterruptibly();
                    work.shutdownGracefully().syncUninterruptibly();
                }
            }
        });
        thread.setDaemon(true);    // daemon, service jvm, user thread leave >>> daemon leave >>> jvm leave
        thread.start();
    }

    public void stop() throws Exception {
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        logger.info("ws destroy success.");
    }

}

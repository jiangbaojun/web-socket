package com.test.websockets.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.ByteBuffer;

@ChannelHandler.Sharable
public class WsByteInBoundHandle extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    public static WsByteInBoundHandle INSTANCE = new WsByteInBoundHandle();

    private WsByteInBoundHandle() {
        super();
        System.out.println("初始化 BinaryWebSocketFrame");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("WsBinaryInBoundHandle 收到了连接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame binaryWebSocketFrame) throws Exception {
        ByteBuf content = binaryWebSocketFrame.content();
        byte[] dst = new byte[content.readableBytes()];
        content.readBytes(dst);
        String message = new String(dst);
        String str = "BinaryWebSocketFrame 收到了一条消息, 内容为：" + message;

        System.out.println(str);

        String responseStr = "{\"status\":200, \"content\":\""+message+"\"}";

        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseStr));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        System.out.println("BinaryWebSocketFrame 消息收到完毕");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("BinaryWebSocketFrame 连接逻辑中发生了异常");
        cause.printStackTrace();
        ctx.close();
    }
}
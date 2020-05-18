package com.talkingdata.framework.gateway;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author tao.yang
 * @date 2019-11-05
 */
public class NettyDemo {

    public static void main(String[] args) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new SimpleChannelInboundHandler<ByteBuf>(){

            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                System.out.println("Receive Data");
            }
        });


    }

}

package com.talkingdata.framework.gateway;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.http.HttpTracing;
import brave.netty.http.NettyHttpTracing;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.zuul.netty.server.BaseZuulChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关初始化
 * @author tao.yang
 * @date 2019-11-19
 */
public class GatewayServerChannelInitializer extends BaseZuulChannelInitializer {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GatewayServerChannelInitializer.class);

    /**
     * 默认的构造函数
     * @param port
     * @param channelConfig
     * @param channelDependencies
     * @param channels
     */
    protected GatewayServerChannelInitializer(int port, ChannelConfig channelConfig, ChannelConfig channelDependencies, ChannelGroup channels) {
        super(port, channelConfig, channelDependencies, channels);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // Configure our pipeline of ChannelHandlerS.
        ChannelPipeline pipeline = ch.pipeline();

        storeChannel(ch);
        addTimeoutHandlers(pipeline);
        addPassportHandler(pipeline);
        addTcpRelatedHandlers(pipeline);
        addHttp1Handlers(pipeline);
        addTracingHandler(pipeline);
        addHttpRelatedHandlers(pipeline);
        addZuulHandlers(pipeline);
    }

    /**
     * 增加netty tracing pipeline handler
     * @param pipeline
     */
    private void  addTracingHandler(ChannelPipeline pipeline){
        logger.info("增加trace pipeline handler");
        Tracing tracing = Tracing.newBuilder().currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder().addScopeDecorator(MDCScopeDecorator.create()).build()).build();
        HttpTracing httpTracing = HttpTracing.create(tracing);
        NettyHttpTracing nettyHttpTracing = NettyHttpTracing.create(httpTracing);
        pipeline.addLast("tracing", nettyHttpTracing.serverHandler());
    }

}

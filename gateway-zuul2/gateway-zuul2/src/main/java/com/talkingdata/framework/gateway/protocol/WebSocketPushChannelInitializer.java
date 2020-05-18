package com.talkingdata.framework.gateway.protocol;

import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.zuul.netty.server.ZuulDependencyKeys;
import com.netflix.zuul.netty.server.push.PushAuthHandler;
import com.netflix.zuul.netty.server.push.PushChannelInitializer;
import com.netflix.zuul.netty.server.push.PushConnectionRegistry;
import com.netflix.zuul.netty.server.push.PushProtocol;
import com.netflix.zuul.netty.server.push.PushRegistrationHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支持通道推送通知
 * web socket协议通道初始化
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class WebSocketPushChannelInitializer extends PushChannelInitializer {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(WebSocketPushChannelInitializer.class);

    private final PushConnectionRegistry pushConnectionRegistry;

    private final PushAuthHandler pushAuthHandler;

    /**
     * 默认的构造函数
     *
     * @param port
     * @param channelConfig
     * @param channelDependencies
     * @param channelGroup
     */
    public WebSocketPushChannelInitializer(int port, ChannelConfig channelConfig, ChannelConfig channelDependencies, ChannelGroup channelGroup) {
        super(port, channelConfig, channelDependencies, channelGroup);
        pushConnectionRegistry = channelDependencies.get(ZuulDependencyKeys.pushConnectionRegistry);
        pushAuthHandler = new GatewayPushAuthHandler(PushProtocol.WEBSOCKET.getPath());
    }

    @Override
    protected void addPushHandlers(final ChannelPipeline pipeline) {
        pipeline.addLast(PushAuthHandler.NAME, pushAuthHandler);
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(PushProtocol.WEBSOCKET.getPath(), null, true));
        pipeline.addLast(new PushRegistrationHandler(pushConnectionRegistry, PushProtocol.WEBSOCKET));
        pipeline.addLast(new WebSocketPushClientProtocolHandler());
    }
}

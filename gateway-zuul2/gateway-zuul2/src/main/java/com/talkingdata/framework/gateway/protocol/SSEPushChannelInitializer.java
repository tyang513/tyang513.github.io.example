package com.talkingdata.framework.gateway.protocol;

import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.zuul.netty.server.push.PushChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;

/**
 * sse长连接协议初始化,支持通道推送通知
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class SSEPushChannelInitializer extends PushChannelInitializer {

    public SSEPushChannelInitializer(int port, ChannelConfig channelConfig, ChannelConfig channelDependencies, ChannelGroup channels) {
        super(port, channelConfig, channelDependencies, channels);
    }

    @Override
    protected void addPushHandlers(ChannelPipeline pipeline) {

    }
}

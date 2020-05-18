package com.talkingdata.framework.gateway.protocol;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.zuul.netty.server.push.PushConnectionRegistry;
import com.netflix.zuul.netty.server.push.PushMessageSender;
import com.netflix.zuul.netty.server.push.PushMessageSenderInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SamplePushMessageSenderInitializer
 *
 * @author tao.yang
 * @date 2019-10-25
 */
@Singleton
public class GatewayPushMessageSenderInitializer extends PushMessageSenderInitializer {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GatewayPushMessageSenderInitializer.class);

    /**
     *
     */
    private final PushMessageSender pushMessageSender;

    /**
     * 默认的构造函数
     *
     * @param pushConnectionRegistry
     */
    @Inject
    public GatewayPushMessageSenderInitializer(PushConnectionRegistry pushConnectionRegistry) {
        super(pushConnectionRegistry);
        pushMessageSender = new GatewayPushMessageSender(pushConnectionRegistry);
    }

    @Override
    protected PushMessageSender getPushMessageSender(PushConnectionRegistry pushConnectionRegistry) {
        return pushMessageSender;
    }
}

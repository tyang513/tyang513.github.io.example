package com.talkingdata.framework.gateway.protocol;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.netflix.zuul.netty.server.push.PushConnectionRegistry;
import com.netflix.zuul.netty.server.push.PushMessageSender;
import com.netflix.zuul.netty.server.push.PushUserAuth;
import com.talkingdata.framework.gateway.entity.GatewayAuthorized;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户授权信息获取
 * SamplePushMessageSender
 *
 * @author tao.yang
 * @date 2019-10-25
 */
@Singleton
@ChannelHandler.Sharable
public class GatewayPushMessageSender extends PushMessageSender {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(GatewayPushMessageSender.class);

    public GatewayPushMessageSender(PushConnectionRegistry pushConnectionRegistry) {
        super(pushConnectionRegistry);
    }

    @Override
    protected PushUserAuth getPushUserAuth(FullHttpRequest request) {
        // 获取客户认证信息 token???
        final String appKey = request.headers().get("appKey");
        final String appSecret = request.headers().get("appSecret");
        final String token = request.headers().get("token");
        if (Strings.isNullOrEmpty(token)) {
            logger.info("未从请求头中获取用户访问的授权信息,返回用户未授权");
            return new GatewayAuthorized(HttpResponseStatus.UNAUTHORIZED.code());
        }
        logger.info("从请求头中获取用户访问的授权信息 token {} ", token);
        return new GatewayAuthorized(token);
    }
}

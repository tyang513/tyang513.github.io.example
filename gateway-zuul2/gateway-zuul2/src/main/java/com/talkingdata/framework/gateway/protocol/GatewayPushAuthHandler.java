package com.talkingdata.framework.gateway.protocol;

import com.google.common.base.Strings;
import com.netflix.zuul.message.http.Cookies;
import com.netflix.zuul.netty.server.push.PushAuthHandler;
import com.netflix.zuul.netty.server.push.PushUserAuth;
import com.talkingdata.framework.gateway.entity.GatewayAuthorized;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2019-10-28
 */
public class GatewayPushAuthHandler extends PushAuthHandler {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GatewayPushAuthHandler.class);

    /**
     * 默认的构造函数
     *
     * @param pushConnectionPath
     * @param originDomain       源域名
     */
    public GatewayPushAuthHandler(String pushConnectionPath, String originDomain) {
        super(pushConnectionPath, originDomain);
    }

    /**
     * 默认的构造函数
     *
     * @param pushConnectionPath
     */
    public GatewayPushAuthHandler(String pushConnectionPath) {
        super(pushConnectionPath, "localhost");
    }

    /**
     * @param req
     * @param ctx
     * @return true if Auth credentials will be provided later, for example in first WebSocket frame sent
     */
    @Override
    protected boolean isDelayedAuth(FullHttpRequest req, ChannelHandlerContext ctx) {
        return false;
    }

    @Override
    protected PushUserAuth doAuth(FullHttpRequest req) {
        final Cookies cookies = parseCookies(req);
        for (final Cookie c : cookies.getAll()) {
            if (c.getName().equals("token")) {
                final String token = c.getValue();
                if (!Strings.isNullOrEmpty(token)) {
                    logger.info("从cookie中获取token = {}", token);
                    return new GatewayAuthorized(token);
                }
            }
        }
        return new GatewayAuthorized(HttpResponseStatus.UNAUTHORIZED.code());
    }
}

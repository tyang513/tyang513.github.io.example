package com.talkingdata.framework.gateway.protocol;

import com.netflix.zuul.netty.server.push.PushClientProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支持通道推送通知
 * web socket协议处理句柄
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class WebSocketPushClientProtocolHandler extends PushClientProtocolHandler {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(WebSocketPushClientProtocolHandler.class);

}

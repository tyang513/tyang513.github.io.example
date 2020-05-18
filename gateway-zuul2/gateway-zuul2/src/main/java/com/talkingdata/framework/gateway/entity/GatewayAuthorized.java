package com.talkingdata.framework.gateway.entity;

import com.netflix.zuul.netty.server.push.PushUserAuth;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * SamplePushUserAuth
 *
 * @author tao.yang
 * @date 2019-10-25
 */
public class GatewayAuthorized implements PushUserAuth {

    /**
     * 访问令牌
     */
    private String token;

    private int statusCode;

    /**
     * 默认的构造函数
     *
     * @param token
     * @param statusCode
     */
    public GatewayAuthorized(String token, int statusCode) {
        this.token = token;
        this.statusCode = statusCode;
    }

    public GatewayAuthorized(String token) {
        this(token, HttpResponseStatus.OK.code());
    }

    public GatewayAuthorized(int statusCode) {
        this("", statusCode);
    }


    @Override
    public boolean isSuccess() {
        return statusCode == 200;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String getClientIdentity() {
        return token;
    }
}

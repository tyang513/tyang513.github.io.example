package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 认证Pipeline
 * 认证失败，要求网关返回401认证失败的http status code
 * @author tao.yang
 * @date 2019-11-18
 */
public class AuthenticatePipelineChain extends AbstractPipelineChain {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatePipelineChain.class);

    /**
     * 处理
     *
     * @param request
     * @return
     */
    @Override
    public HttpRequestMessage execute(HttpRequestMessage request) {
        logger.info(" AuthenticatePipelineChain ");

        String token = getSafetyToken(request);
        if (token == null || "".equals(token)){
            SessionContext context = request.getContext();
            HttpResponseMessage response = new HttpResponseMessageImpl(request.getContext(), request, 401);
            response.setBodyAsText("认证失败,请求时必须带上token");
            context.setStaticResponse(response);
            return null;
        }
        return super.execute(request);
    }

    /**
     * 安全的获取token
     * @param request
     * @return token
     */
    private String getSafetyToken(HttpRequestMessage request){
        String token = request.getHeaders().getFirst("token");
        if (token == null || "".equals(token)){
            token = request.getQueryParams().getFirst("token");
            return token;
        }
        return null;
    }

}

package com.talkingdata.framework.gateway.filters.endpoint;

import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.filters.http.HttpSyncEndpoint;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关统一的错误处理
 * @author tao.yang
 * @date 2019-11-18
 */
public class ExceptionEndPoint extends HttpSyncEndpoint {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionEndPoint.class);

    @Override
    public HttpResponseMessage apply(HttpRequestMessage request) {

        logger.info(" == ExceptionEndPoint ==");
        HttpResponseMessage response = null;
        SessionContext context = request.getContext();
        Throwable t = context.getError();
        if (t instanceof Exception) {
            Exception e = (Exception) t;
            logger.info(e.getMessage());
            response = new HttpResponseMessageImpl(request.getContext(), request, 401);
            response.setBodyAsText("认证失败");
        }
        return response;
    }
}

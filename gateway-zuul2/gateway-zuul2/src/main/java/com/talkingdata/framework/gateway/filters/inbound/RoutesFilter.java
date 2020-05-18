package com.talkingdata.framework.gateway.filters.inbound;

import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.filters.http.HttpInboundSyncFilter;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.netty.filter.ZuulEndPointRunner;
import com.talkingdata.framework.gateway.filters.endpoint.ExceptionEndPoint;
import com.talkingdata.framework.gateway.filters.endpoint.LoginEndpoint;
import com.talkingdata.framework.gateway.filters.endpoint.StaticEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 路由过滤器
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class RoutesFilter extends HttpInboundSyncFilter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RoutesFilter.class);

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @param requestMessage
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    @Override
    public boolean shouldFilter(HttpRequestMessage requestMessage) {
        return true;
    }

    @Override
    public HttpRequestMessage apply(HttpRequestMessage requestMessage) {
        SessionContext context = requestMessage.getContext();
        context.setErrorEndpoint(ExceptionEndPoint.class.getCanonicalName());

        logger.info("HttpInboundSyncFilter.RoutesFilter执行顺序为{} 过滤器名称为 {}", filterOrder(), filterName());

        final String path = requestMessage.getPath();
        final String host = requestMessage.getOriginalHost();

        // Route healthchecks to the healthcheck endpoint.;
        if (path.equalsIgnoreCase("/static")) {
            // 健康状态检查点
            context.setEndpoint(StaticEndpoint.class.getCanonicalName());
        } else if (path.equalsIgnoreCase("/login")) {
            // 登录
            context.setEndpoint(LoginEndpoint.class.getCanonicalName());
        } else {
            // 接口调用
            context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME);
            context.setRouteVIP("api");
        }
        return requestMessage;
    }


}

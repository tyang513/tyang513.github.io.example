package com.talkingdata.framework.gateway.filters.inbound;

import com.netflix.zuul.filters.http.HttpInboundSyncFilter;
import com.netflix.zuul.message.http.HttpRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2019-10-28
 */
public class DebugFilter extends HttpInboundSyncFilter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DebugFilter.class);

    @Override
    public HttpRequestMessage apply(HttpRequestMessage requestMessage) {
        logger.info("HttpInboundSyncFilter.DebugFilter执行顺序为{} 过滤器名称为 {}", filterOrder(), filterName());
        requestMessage.getContext().setDebugRequest(true);
        requestMessage.getContext().setDebugRouting(true);
        return requestMessage;
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 20;
    }

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @param requestMessage
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    @Override
    public boolean shouldFilter(HttpRequestMessage requestMessage) {
        String value = requestMessage.getQueryParams().getFirst("debugRequest");
        boolean b = "true".equalsIgnoreCase(value);
        logger.info("HttpInboundSyncFilter.DebugFilter.shouldFilter = {} debugRequest = {}", b, value);
        return b;
    }
}

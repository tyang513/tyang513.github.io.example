package com.talkingdata.framework.gateway.filters.outbound;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.zuul.context.Debug;
import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.filters.http.HttpOutboundSyncFilter;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.stats.status.StatusCategory;
import com.netflix.zuul.stats.status.StatusCategoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的请求响应过滤器
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class DefaultResponseFilter extends HttpOutboundSyncFilter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseFilter.class);

    private static final DynamicBooleanProperty SEND_RESPONSE_HEADERS = new DynamicBooleanProperty("zuul.responseFilter.send.headers", true);

    @Override
    public HttpResponseMessage apply(HttpResponseMessage responseMessage) {

        logger.info("HttpOutboundSyncFilter.apply执行顺序为{} 过滤器名称为 {}", filterOrder(), filterName());

        SessionContext context = responseMessage.getContext();

        StatusCategory statusCategory = StatusCategoryUtils.getStatusCategory(responseMessage);

        if (SEND_RESPONSE_HEADERS.get()) {

        }

        if (context.debugRequest()) {
            Debug.getRequestDebug(context).forEach(s -> logger.info("REQ_DEBUG: " + s));
            Debug.getRoutingDebug(context).forEach(s -> logger.info("ZUUL_DEBUG: " + s));
        }
        return responseMessage;
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @param msg
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    @Override
    public boolean shouldFilter(HttpResponseMessage msg) {
        return true;
    }
}

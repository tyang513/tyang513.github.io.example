package com.talkingdata.framework.gateway.filters.inbound;

import com.netflix.zuul.context.Debug;
import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.filters.http.HttpInboundSyncFilter;
import com.netflix.zuul.message.Header;
import com.netflix.zuul.message.http.HttpRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * 调试请求过滤器
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class DebugRequestFilter extends HttpInboundSyncFilter {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(DebugRequestFilter.class);

    @Override
    public HttpRequestMessage apply(HttpRequestMessage requestMessage) {

        logger.info("HttpInboundSyncFilter.DebugRequestFilter执行顺序为{} 过滤器名称为 {}", filterOrder(), filterName());

        SessionContext sessionContext = requestMessage.getContext();

        Debug.addRequestDebug(sessionContext, "REQUEST:: " + requestMessage.getOriginalScheme() + " " + requestMessage.getOriginalHost() + ":" + requestMessage.getOriginalPort());

        Debug.addRequestDebug(sessionContext, "REQUEST:: > " + requestMessage.getMethod() + " " + requestMessage.reconstructURI() + " " + requestMessage.getProtocol());

        Iterator i = requestMessage.getHeaders().entries().iterator();
        while (i.hasNext()) {
            Header header = (Header) i.next();
            Debug.addRequestDebug(sessionContext, "REQUEST:: > " + header.getName() + ":" + header.getValue());
        }

        if (requestMessage.hasBody()) {
            Debug.addRequestDebug(sessionContext, "REQUEST:: > " + requestMessage.getBodyAsText());
        }

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
        return 21;
    }

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @param requestMessage
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    @Override
    public boolean shouldFilter(HttpRequestMessage requestMessage) {
        return requestMessage.getContext().debugRequest();
    }

    @Override
    public boolean needsBodyBuffered(HttpRequestMessage requestMessage) {
        return shouldFilter(requestMessage);
    }
}

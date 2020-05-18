package com.talkingdata.framework.gateway.filters.inbound;

import com.google.inject.Inject;
import com.netflix.zuul.filters.http.HttpInboundFilter;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.talkingdata.framework.gateway.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * @author tao.yang
 * @date 2019-10-28
 */
public class SampleServiceFilter extends HttpInboundFilter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SampleServiceFilter.class);

    private final SampleService sampleService;

    @Inject
    public SampleServiceFilter(SampleService sampleService) {
        this.sampleService = sampleService;
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
     * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
     *
     * @param requestMessage
     */
    @Override
    public Observable<HttpRequestMessage> applyAsync(HttpRequestMessage requestMessage) {
        return sampleService.makeSlowRequest().map(s -> {
            logger.info("sasdf", s);
            return requestMessage;
        });
    }

    /**
     * a "true" return from this method means that the apply() method should be invoked
     *
     * @param msg
     * @return true if the apply() method should be invoked. false will not invoke the apply() method
     */
    @Override
    public boolean shouldFilter(HttpRequestMessage msg) {
        return true;
    }
}

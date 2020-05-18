package com.talkingdata.framework.gateway.filters.inbound;

import com.google.inject.Inject;
import com.netflix.zuul.filters.http.HttpInboundSyncFilter;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.talkingdata.framework.gateway.filters.inbound.pipeline.PipelineManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 前置过滤器
 *
 * @author tao.yang
 * @date 2019-11-18
 */
public class PreFilter extends HttpInboundSyncFilter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PreFilter.class);

    /**
     * Pipeline 管理
     */
    private PipelineManagement pipelineManagement;

    @Inject
    public PreFilter(PipelineManagement pipelineManagement){
        this.pipelineManagement = pipelineManagement;
    }

    @Override
    public HttpRequestMessage apply(HttpRequestMessage requestMessage) {
        logger.info("执行 =============== PreFilter =============== ");
        return pipelineManagement.initialize().execute(requestMessage);
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 1;
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

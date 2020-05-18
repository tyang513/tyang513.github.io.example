package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.netflix.zuul.message.http.HttpRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 预处理,增加公共标识字段。例如:traceId
 *
 * @author tao.yang
 * @date 2019-11-18
 */
public class PrePipelineChain extends AbstractPipelineChain {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PrePipelineChain.class);

    /**
     * 处理
     *
     * @param requestMessage
     * @return
     */
    @Override
    public HttpRequestMessage execute(HttpRequestMessage requestMessage) {
        return super.execute(requestMessage);
    }
}

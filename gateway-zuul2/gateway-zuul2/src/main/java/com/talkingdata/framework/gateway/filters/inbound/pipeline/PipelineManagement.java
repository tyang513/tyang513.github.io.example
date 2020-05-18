package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pipeline 管理
 * @author tao.yang
 * @date 2019-11-18
 */
@Singleton
public class PipelineManagement {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PipelineManagement.class);

    public IPipeline initialize() {
        logger.info("创建IPipeline");
        IPipeline pipeline = new PrePipelineChain();
        IPipeline authenticatePipeline = new AuthenticatePipelineChain();
        IPipeline aclPipeline = new AclPipelineChain();
        IPipeline rateLimitPipeline = new RateLimitPipelineChain();

        pipeline.setNext(authenticatePipeline);
        authenticatePipeline.setNext(aclPipeline);
        aclPipeline.setNext(rateLimitPipeline);
        return pipeline;
    }

}

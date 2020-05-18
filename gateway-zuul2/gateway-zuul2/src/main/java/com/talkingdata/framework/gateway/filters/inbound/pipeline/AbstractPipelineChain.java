package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.netflix.zuul.message.http.HttpRequestMessage;

/**
 * @author tao.yang
 * @date 2019-11-18
 */
public abstract class AbstractPipelineChain implements IPipeline {

    /**
     * 下一个pipelines节点
     */
    private IPipeline nextPipeline;

    /**
     * 下一个节点
     *
     * @param pipeline
     */
    @Override
    public void setNext(IPipeline pipeline) {
        this.nextPipeline = pipeline;
    }

    @Override
    public HttpRequestMessage execute(HttpRequestMessage request) {
        return nextPipeline == null ? null : nextPipeline.execute(request);
    }
}

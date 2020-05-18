package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.netflix.zuul.message.http.HttpRequestMessage;

/**
 * Pipeline接口
 *
 * @author tao.yang
 * @date 2019-11-18
 */
public interface IPipeline {

    /**
     * 下一个节点
     * @param pipeline
     */
    void setNext(IPipeline pipeline);

    /**
     * 处理
     *
     * @param requestMessage
     * @return
     */
    HttpRequestMessage execute(HttpRequestMessage requestMessage);

}

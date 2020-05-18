package com.talkingdata.framework.gateway.filters.inbound.pipeline;

import com.netflix.zuul.message.http.HttpRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接口访问权限控制
 * 接口访问权限控制错误，网关需要返回403
 * @author tao.yang
 * @date 2019-11-18
 */
public class AclPipelineChain extends AbstractPipelineChain {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AclPipelineChain.class);

    /**
     *
     * @param request
     * @return
     */
    @Override
    public HttpRequestMessage execute(HttpRequestMessage request) {
        logger.info(" == AclPipelineChain 权限控制暂时忽略 以后根据需要进行添加 == ");
        return super.execute(request);
    }
}

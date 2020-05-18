package com.talkingdata.framework.gateway.filters.endpoint;

import com.netflix.zuul.filters.http.HttpSyncEndpoint;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import com.netflix.zuul.stats.status.StatusCategoryUtils;
import com.netflix.zuul.stats.status.ZuulStatusCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 健康检查
 *
 * @author tao.yang
 * @date 2019-10-28
 */
public class StaticEndpoint extends HttpSyncEndpoint {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StaticEndpoint.class);

    @Override
    public HttpResponseMessage apply(HttpRequestMessage request) {
        HttpResponseMessage response = new HttpResponseMessageImpl(request.getContext(), request, 200);
        response.setBodyAsText("StaticEndpoint HttpSyncEndpoint");
        logger.info("StaticEndpoint.apply");
        // need to set this manually since we are not going through the ProxyEndpoint
        StatusCategoryUtils.setStatusCategory(request.getContext(), ZuulStatusCategory.SUCCESS);
        return response;
    }
}

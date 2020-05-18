package com.talkingdata.framework.gateway.filters.endpoint;

import com.netflix.zuul.filters.http.HttpSyncEndpoint;
import com.netflix.zuul.message.http.HttpQueryParams;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import com.netflix.zuul.stats.status.StatusCategoryUtils;
import com.netflix.zuul.stats.status.ZuulStatusCategory;
import com.talkingdata.framework.gateway.entity.TokenEntity;
import com.talkingdata.framework.gateway.exception.CommonException;
import com.talkingdata.framework.gateway.util.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * 登录
 *
 * @author tao.yang
 * @date 2019-11-15
 */
public class LoginEndpoint extends HttpSyncEndpoint {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginEndpoint.class);

    @Override
    public HttpResponseMessage apply(HttpRequestMessage request) {
        HttpResponseMessage response = null;

        HttpQueryParams queryParams = request.getQueryParams();
        String appKey = queryParams.getFirst("appKey");
        String appSecret = queryParams.getFirst("appSecret");

        logger.info("收到 appKey = {} appSecret = {} 的登录请求", appKey, appSecret);

        try {
            TokenEntity tokenEntity = new TokenEntity(UUID.randomUUID().toString(), new Date());
            String body = ObjectMapperUtils.object2String(tokenEntity);
            response = new HttpResponseMessageImpl(request.getContext(), request, 200);
            response.setBodyAsText(body);
            StatusCategoryUtils.setStatusCategory(request.getContext(), ZuulStatusCategory.SUCCESS);
        } catch (CommonException e) {
            response = new HttpResponseMessageImpl(request.getContext(), request, 500);
            response.setBodyAsText(e.getLocalizedMessage());
        }
        return response;
    }

}

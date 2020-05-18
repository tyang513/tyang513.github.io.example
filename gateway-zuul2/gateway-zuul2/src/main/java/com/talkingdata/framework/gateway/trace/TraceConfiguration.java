package com.talkingdata.framework.gateway.trace;

import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.google.inject.Singleton;

/**
 * 链路跟踪配置
 * @author tao.yang
 * @date 2019-11-20
 */
@Singleton
public class TraceConfiguration {


    @Singleton
    CurrentTraceContext.Builder sleuthCurrentTraceContextBuilder() {
        return ThreadLocalCurrentTraceContext.newBuilder();
    }
}

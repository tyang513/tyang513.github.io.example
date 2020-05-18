package com.talkingdata.framework.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tao.yang
 * @date 2019-10-30
 */
@Singleton
public class SampleService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

    private final AtomicBoolean status;

    public SampleService() {
        // change to true for demo
        this.status = new AtomicBoolean(false);
    }

    public boolean isHealthy() {
        return status.get();
    }

    public Observable<String> makeSlowRequest() {
        logger.info("makeSlowRequest");
        return Observable.just("test").delay(500, TimeUnit.MILLISECONDS);
    }
}

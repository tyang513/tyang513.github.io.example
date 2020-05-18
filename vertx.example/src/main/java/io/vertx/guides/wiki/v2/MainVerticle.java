package io.vertx.guides.wiki.v2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-05-18
 */
public class MainVerticle extends AbstractVerticle {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    /**
     * Stop the verticle instance.
     * <p>
     * Vert.x calls this method when un-deploying the instance. You do not call it yourself.
     * <p>
     * A promise is passed into the method, and when un-deployment is complete the verticle should either call
     * {@link Promise#complete} or {@link Promise#fail} the future.
     *
     * @param stopPromise the future
     */
    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {

        Promise<String> dbVerticleDeployment = Promise.promise();
        vertx.deployVerticle(new DatabaseVerticle(), dbVerticleDeployment);

        dbVerticleDeployment.future().compose(s -> {
            Promise<String> httpVerticleDeployment = Promise.promise();
            vertx.deployVerticle("io.vertx.guides.wiki.v2.HttpServerVerticle", new DeploymentOptions().setInstances(2), httpVerticleDeployment);
            return httpVerticleDeployment.future();
        }).setHandler(event -> {
            if (event.succeeded()) {
                stopPromise.complete();
            } else {
                stopPromise.fail(event.cause());
            }
        });
    }

}

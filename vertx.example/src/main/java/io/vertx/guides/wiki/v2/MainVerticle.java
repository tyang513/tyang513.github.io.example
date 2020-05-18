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
     * Start the verticle instance.
     * <p>
     * Vert.x calls this method when deploying the instance. You do not call it yourself.
     * <p>
     * A promise is passed into the method, and when deployment is complete the verticle should either call
     * {@link Promise#complete} or {@link Promise#fail} the future.
     *
     * @param startPromise the future
     */
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        logger.info("start wiki");

        Promise<String> dbVerticleDeployment = Promise.promise();
        vertx.deployVerticle(new DatabaseVerticle(), dbVerticleDeployment);

        dbVerticleDeployment.future().compose(s -> {
            Promise<String> httpVerticleDeployment = Promise.promise();

            // new DeploymentOptions().setInstances(2) 如果改为2, 会同时部署2个 HttpServerVerticle,并且会启动2个http port
            vertx.deployVerticle("io.vertx.guides.wiki.v2.HttpServerVerticle", new DeploymentOptions().setInstances(1), httpVerticleDeployment);
            return httpVerticleDeployment.future();
        }).setHandler(event -> {
            if (event.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(event.cause());
            }
        });
    }

}

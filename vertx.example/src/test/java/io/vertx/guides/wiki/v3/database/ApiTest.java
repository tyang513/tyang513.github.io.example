package io.vertx.guides.wiki.v3.database;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.guides.wiki.v3.http.HttpServerVerticle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-05-20
 */
@RunWith(VertxUnitRunner.class)
public class ApiTest {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private Vertx vertx;

    private WebClient webClient;

    @Before
    public void prepare(TestContext context) {

        vertx = Vertx.vertx();

        JsonObject conf = new JsonObject();
        conf.put("url", "jdbc:mysql://172.23.6.249:3306/yt_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai");
        conf.put("driver_class", "com.mysql.cj.jdbc.Driver");
        conf.put("user", "rwuser");
        conf.put("password", "kw1ntu2p");
        conf.put("initial_pool_size", 3);
        conf.put("max_pool_size", 30);
        conf.put("max_idle_time", 1000);

        vertx.deployVerticle(new WikiDatabaseVerticle(),
            new DeploymentOptions().setConfig(conf), context.asyncAssertSuccess());

        vertx.deployVerticle(new HttpServerVerticle(), context.asyncAssertSuccess());

        webClient = WebClient.create(vertx, new WebClientOptions()
            .setDefaultHost("localhost")
            .setDefaultPort(8080));
    }

    @Test
    public void play_with_api(TestContext context) {
        Async async = context.async();

        JsonObject page = new JsonObject()
            .put("name", "Sample")
            .put("markdown", "# A page");

        Promise<HttpResponse<JsonObject>> postPagePromise = Promise.promise();
        webClient.post("/api/pages")
            .as(BodyCodec.jsonObject())
            .sendJsonObject(page, postPagePromise);

        Future<HttpResponse<JsonObject>> getPageFuture = postPagePromise.future().compose(resp -> {
            Promise<HttpResponse<JsonObject>> promise = Promise.promise();
            webClient.get("/api/pages")
                .as(BodyCodec.jsonObject())
                .send(promise);
            return promise.future();
        });

        Future<HttpResponse<JsonObject>> updatePageFuture = getPageFuture.compose(resp -> {
            JsonArray array = resp.body().getJsonArray("pages");
            context.assertEquals(1, array.size());
            context.assertEquals(0, array.getJsonObject(0).getInteger("id"));
            Promise<HttpResponse<JsonObject>> promise = Promise.promise();
            JsonObject data = new JsonObject()
                .put("id", 0)
                .put("markdown", "Oh Yeah!");
            webClient.put("/api/pages/2")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(data, promise);
            return promise.future();
        });

        Future<HttpResponse<JsonObject>> deletePageFuture = updatePageFuture.compose(resp -> {
            context.assertTrue(resp.body().getBoolean("success"));
            Promise<HttpResponse<JsonObject>> promise = Promise.promise();
            webClient.delete("/api/pages/2")
                .as(BodyCodec.jsonObject())
                .send(promise);
            return promise.future();
        });

        deletePageFuture.setHandler(ar -> {
            if (ar.succeeded()) {
                context.assertTrue(ar.result().body().getBoolean("success"));
                async.complete();
            } else {
                context.fail(ar.cause());
            }
        });

        async.awaitSuccess(500000);
    }

    @After
    public void finish(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
}

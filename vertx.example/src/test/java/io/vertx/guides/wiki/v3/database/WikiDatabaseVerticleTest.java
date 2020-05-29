package io.vertx.guides.wiki.v3.database;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


//@RunWith(VertxUnitRunner.class)
public class WikiDatabaseVerticleTest {

    private Vertx vertx;

    private WikiDatabaseService service;

    @Before
    public void prepare(TestContext context) throws InterruptedException {
        vertx = Vertx.vertx();

        JsonObject conf = new JsonObject();
        conf.put("url", "jdbc:mysql://172.23.6.249:3306/yt_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai");
        conf.put("driver_class", "com.mysql.cj.jdbc.Driver");
        conf.put("user", "rwuser");
        conf.put("password", "kw1ntu2p");
        conf.put("initial_pool_size", 3);
        conf.put("max_pool_size", 30);
        conf.put("max_idle_time", 1000);

        vertx.deployVerticle(new WikiDatabaseVerticle(), new DeploymentOptions().setConfig(conf),
            context.asyncAssertSuccess(id ->
                service = WikiDatabaseService.createProxy(vertx, WikiDatabaseVerticle.CONFIG_WIKIDB_QUEUE)));
    }

    @After
    public void finish(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void crud_operations(TestContext context) {
        Async async = context.async();

        service.createPage("Test", "Some content", context.asyncAssertSuccess(v1 -> {

            service.fetchPage("Test", context.asyncAssertSuccess(json1 -> {
                context.assertTrue(json1.getBoolean("found"));
                context.assertTrue(json1.containsKey("id"));
                context.assertEquals("Some content", json1.getString("rawContent"));

                service.savePage(json1.getInteger("id"), "Yo!", context.asyncAssertSuccess(v2 -> {

                    service.fetchAllPages(context.asyncAssertSuccess(array1 -> {
                        context.assertEquals(1, array1.size());

                        service.fetchPage("Test", context.asyncAssertSuccess(json2 -> {
                            context.assertEquals("Yo!", json2.getString("rawContent"));

                            service.deletePage(json1.getInteger("id"), v3 -> {

                                service.fetchAllPages(context.asyncAssertSuccess(array2 -> {
                                    context.assertTrue(array2.isEmpty());
                                    async.complete();
                                }));
                            });
                        }));
                    }));
                }));
            }));
        }));
        async.awaitSuccess(5000);
    }

}

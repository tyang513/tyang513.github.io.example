package io.vertx.guides.wiki.v3.database;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-05-19
 */
@RunWith(VertxUnitRunner.class)
public class HttpClientTest {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpClientTest.class);

    private Vertx vertx;

    @Before
    public void prepare(TestContext context) throws InterruptedException {

        logger.info("=============== prepare ===============");
        vertx = Vertx.vertx();
    }

    @Test
    public void start_http_server(TestContext context) {
        Async async = context.async();


        vertx.createHttpServer().requestHandler(request -> {
            logger.info(" =========== requestHandler  =============");
            request.response().putHeader("Content-Type", "text/plain").end("Ok");
        }).listen(8080, context.asyncAssertSuccess(server -> {

            WebClient client = WebClient.create(vertx);

            client.get(8080, "localhost", "/").send(request -> {

                logger.info(" ======== ======");

                if (request.succeeded()){
                    logger.info("============= 请求成功  =============");
                    HttpResponse<Buffer> response = request.result();
                    context.assertTrue(response.headers().contains("Content-Type"));
                    context.assertEquals("text/plain", response.getHeader("Content-Type"));
                    context.assertEquals("Ok", response.body().toString());
                    client.close();
                    async.complete();
                }
                else {
                    logger.error("请求异常");
                    Promise.promise().fail(request.cause());
                }

            });

        }));

    }
}

package io.vertx.guides.wiki.v2;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author tao.yang
 * @date 2020-05-15
 */
public class HttpServerVerticle extends AbstractVerticle {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private static final String EMPTY_PAGE_MARKDOWN =
        "# A new page\n" +
            "\n" +
            "Feel-free to write in Markdown!\n";

    /**
     * 端口号配置属性key
     */
    public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";

    /**
     * DB操作队列
     */
    public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";

    private String wikiDbQueue = "wikidb.queue";

    /**
     * 模板引擎
     */
    private FreeMarkerTemplateEngine templateEngine;

    /**
     * 启动 http 服务
     *
     * @return
     */
    @Override
    public void start(Promise<Void> promise) {
        logger.info("启动 HTTP 服务");

        wikiDbQueue = config().getString(CONFIG_WIKIDB_QUEUE, "wikidb.queue");

        logger.info("数据库操作队列为:{}", wikiDbQueue);

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/").handler(this::indexHandler);
        router.get("/wiki/:page").handler(this::pageRenderingHandler);

        router.post().handler(BodyHandler.create());
        router.post("/save").handler(this::pageUpdateHandler);
        router.post("/create").handler(this::pageCreateHandler);
        router.post("/delete").handler(this::pageDeletionHandler);

        templateEngine = FreeMarkerTemplateEngine.create(vertx);

        int port = config().getInteger(CONFIG_HTTP_SERVER_PORT, 8080);

        httpServer.requestHandler(router).listen(port, httpServerAsyncResult -> {

            if (httpServerAsyncResult.failed()) {
                logger.error("Could not start a HTTP server", httpServerAsyncResult.cause());
                promise.fail(httpServerAsyncResult.cause());
                return;
            }

            if (httpServerAsyncResult.succeeded()) {
                logger.info("HTTP server running on port {}", port);
                promise.complete();
            }
        });
    }


    /**
     * @param context
     */
    private void pageRenderingHandler(RoutingContext context) {

        String requestPage = context.request().getParam("page");
        JsonObject request = new JsonObject().put("page", requestPage);

        DeliveryOptions options = new DeliveryOptions().addHeader("action", "get-page");

        vertx.eventBus().request(wikiDbQueue, request, options, reply -> {
            if (reply.failed()){
                context.fail(reply.cause());
                return ;
            }

            JsonObject body = (JsonObject) reply.result().body();

            boolean found = body.getBoolean("found");
            String rawContent = body.getString("rawContent", EMPTY_PAGE_MARKDOWN);
            context.put("title", requestPage);
            context.put("id", body.getInteger("id", -1));
            context.put("newPage", found ? "no" : "yes");
            context.put("rawContent", rawContent);
            context.put("content", Processor.process(rawContent));
            context.put("timestamp", new Date().toString());
            templateEngine.render(context.data(), "templates/page.ftl", ar -> {
                if (ar.succeeded()) {
                    context.response().putHeader("Content-Type", "text/html");
                    context.response().end(ar.result());
                } else {
                    context.fail(ar.cause());
                }
            });
        });
    }

    /**
     * 主页
     *
     * @param context
     */
    private void indexHandler(RoutingContext context) {

        DeliveryOptions options = new DeliveryOptions().addHeader("action", "all-pages");

        vertx.eventBus().request(wikiDbQueue, new JsonObject(), options, reply -> {

            if (reply.succeeded()){
                JsonObject body = (JsonObject) reply.result().body();
                context.put("title", "Wiki home");
                context.put("pages", body.getJsonArray("pages").getList());

                templateEngine.render(context.data(), "templates/index.ftl", event -> {
                    if (event.succeeded()) {
                        context.response().putHeader("Content-Type", "text/html");
                        context.response().end(event.result());
                    } else {
                        context.fail(event.cause());
                    }
                });
                return ;
            }
            logger.error(" 事件发送或响应失败 {}", reply.cause());
            context.fail(reply.cause());
        });
    }

    /**
     * 创建
     *
     * @param context
     */
    private void pageCreateHandler(RoutingContext context) {
        String pageName = context.request().getParam("name");
        String location = "/wiki/" + pageName;
        if (pageName == null || pageName.isEmpty()) {
            location = "/";
        }
        context.response().setStatusCode(303);
        context.response().putHeader("Location", location);
        context.response().end();
    }

    /**
     * 更新
     *
     * @param context
     */
    private void pageUpdateHandler(RoutingContext context) {

        String title = context.request().getParam("title");
        JsonObject request = new JsonObject()
            .put("id", context.request().getParam("id"))
            .put("title", title)
            .put("markdown", context.request().getParam("markdown"));

        DeliveryOptions options = new DeliveryOptions();
        if ("yes".equals(context.request().getParam("newPage"))) {
            options.addHeader("action", "create-page");
        } else {
            options.addHeader("action", "save-page");
        }

        vertx.eventBus().request(wikiDbQueue, request, options, reply -> {
            if (reply.succeeded()) {
                context.response().setStatusCode(303);
                context.response().putHeader("Location", "/wiki/" + title);
                context.response().end();
            } else {
                context.fail(reply.cause());
            }
        });
    }


    /**
     * 删除
     *
     * @param context
     */
    private void pageDeletionHandler(RoutingContext context) {
        String id = context.request().getParam("id");
        JsonObject request = new JsonObject().put("id", id);
        DeliveryOptions options = new DeliveryOptions().addHeader("action", "delete-page");
        vertx.eventBus().request(wikiDbQueue, request, options, reply -> {
            if (reply.succeeded()) {
                context.response().setStatusCode(303);
                context.response().putHeader("Location", "/");
                context.response().end();
            } else {
                context.fail(reply.cause());
            }
        });
    }

}

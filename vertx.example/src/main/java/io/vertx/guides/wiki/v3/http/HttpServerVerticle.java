package io.vertx.guides.wiki.v3.http;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import io.vertx.guides.wiki.v3.database.WikiDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author tao.yang
 * @date 2020-05-18
 */
public class HttpServerVerticle extends AbstractVerticle {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    private static final String EMPTY_PAGE_MARKDOWN =
        "# A new page\n" +
            "\n" +
            "Feel-free to write in Markdown!\n";

    private String wikiDbQueue = "wikidb.queue";

    /**
     * 模板引擎
     */
    private FreeMarkerTemplateEngine templateEngine;

    /**
     * 数据库操作服务类
     */
    private WikiDatabaseService wikiDatabaseService;

    /**
     * 启动 http 服务
     *
     * @return
     */
    @Override
    public void start(Promise<Void> promise) {
        logger.info("启动 HTTP verticle ");

        wikiDatabaseService = WikiDatabaseService.createProxy(vertx, wikiDbQueue);

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/").handler(this::indexHandler);
        router.get("/wiki/:page").handler(this::pageRenderingHandler);

        router.post().handler(BodyHandler.create());
        router.post("/save").handler(this::pageUpdateHandler);
        router.post("/create").handler(this::pageCreateHandler);
        router.post("/delete").handler(this::pageDeletionHandler);

        templateEngine = FreeMarkerTemplateEngine.create(vertx);


        httpServer.requestHandler(router).listen(8080, httpServerAsyncResult -> {

            if (httpServerAsyncResult.failed()) {
                logger.error("Could not start a HTTP server", httpServerAsyncResult.cause());
                promise.fail(httpServerAsyncResult.cause());
                return;
            }

            if (httpServerAsyncResult.succeeded()) {
                logger.info("HTTP server running on port {}", 8080);
                promise.complete();
            }
        });

    }


    /**
     * @param context
     */
    private void pageRenderingHandler(RoutingContext context) {

        String requestPage = context.request().getParam("page");

        wikiDatabaseService.fetchPage(requestPage, reply -> {
            if (reply.failed()) {
                context.fail(reply.cause());
                return;
            }
            JsonObject body = reply.result();
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
        wikiDatabaseService.fetchAllPages(reply -> {
            if (reply.succeeded()) {
                context.put("title", "Wiki home");
                context.put("pages", reply.result().getList());
                templateEngine.render(context.data(), "templates/index.ftl", event -> {
                    if (event.succeeded()) {
                        context.response().putHeader("Content-Type", "text/html");
                        context.response().end(event.result());
                    } else {
                        context.fail(event.cause());
                    }
                });
                return;
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
        String markdown = context.request().getParam("markdown");

        Handler<AsyncResult<Void>> handler = reply -> {
            if (reply.succeeded()) {
                context.response().setStatusCode(303);
                context.response().putHeader("Location", "/wiki/" + title);
                context.response().end();
            } else {
                context.fail(reply.cause());
            }
        };

        if ("yes".equals(context.request().getParam("newPage"))) {
            wikiDatabaseService.createPage(title, markdown, handler);
        } else {
            int id = Integer.valueOf(context.request().getParam("id"));
            wikiDatabaseService.savePage(id, markdown, handler);
        }
    }


    /**
     * 删除
     *
     * @param context
     */
    private void pageDeletionHandler(RoutingContext context) {

        int id = Integer.valueOf(context.request().getParam("id"));
        wikiDatabaseService.deletePage(id, reply -> {
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

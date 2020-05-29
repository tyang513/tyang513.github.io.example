package io.vertx.guides.wiki.v3.http;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import io.vertx.guides.wiki.v3.database.WikiDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    WebClient webClient;

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

        webClient = WebClient.create(vertx, new WebClientOptions()
            .setSsl(true)
            .setUserAgent("vert-x3"));

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/").handler(this::indexHandler);
        router.get("/wiki/:page").handler(this::pageRenderingHandler);

        router.post().handler(BodyHandler.create());
        router.post("/save").handler(this::pageUpdateHandler);
        router.post("/create").handler(this::pageCreateHandler);
        router.post("/delete").handler(this::pageDeletionHandler);
        router.get("/backup").handler(this::backupHandler);

        Router apiRouter = Router.router(vertx);
        apiRouter.get("/pages").handler(this::apiRoot);
        apiRouter.get("/pages/:id").handler(this::apiGetPage);
        apiRouter.post().handler(BodyHandler.create());
        apiRouter.post("/pages").handler(this::apiCreatePage);
        apiRouter.put().handler(BodyHandler.create());
        apiRouter.put("/pages/:id").handler(this::apiUpdatePage);
        apiRouter.delete("/pages/:id").handler(this::apiDeletePage);
        router.mountSubRouter("/api", apiRouter);

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

    private void apiRoot(RoutingContext context) {
        wikiDatabaseService.fetchAllPagesData(reply -> {
            JsonObject response = new JsonObject();
            if (reply.succeeded()) {
                List<JsonObject> pages = reply.result()
                    .stream()
                    .map(obj -> new JsonObject()
                        .put("id", obj.getInteger("ID"))
                        .put("name", obj.getString("NAME")))
                    .collect(Collectors.toList());
                response
                    .put("success", true)
                    .put("pages", pages);
                context.response().setStatusCode(200);
                context.response().putHeader("Content-Type", "application/json");
                context.response().end(response.encode());
            } else {
                response
                    .put("success", false)
                    .put("error", reply.cause().getMessage());
                context.response().setStatusCode(500);
                context.response().putHeader("Content-Type", "application/json");
                context.response().end(response.encode());
            }
        });
    }

    private void apiGetPage(RoutingContext context) {
        logger.info("apiGetPage");

        int id = Integer.valueOf(context.request().getParam("id"));
        wikiDatabaseService.fetchPageById(id, reply -> {
            JsonObject response = new JsonObject();
            if (reply.succeeded()) {
                JsonObject dbObject = reply.result();
                if (dbObject.getBoolean("found")) {
                    JsonObject payload = new JsonObject()
                        .put("name", dbObject.getString("name"))
                        .put("id", dbObject.getInteger("id"))
                        .put("markdown", dbObject.getString("content"))
                        .put("html", Processor.process(dbObject.getString("content")));
                    response
                        .put("success", true)
                        .put("page", payload);
                    context.response().setStatusCode(200);
                } else {
                    context.response().setStatusCode(404);
                    response
                        .put("success", false)
                        .put("error", "There is no page with ID " + id);
                }
            } else {
                response
                    .put("success", false)
                    .put("error", reply.cause().getMessage());
                context.response().setStatusCode(500);
            }
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(response.encode());
        });

    }

    private void apiCreatePage(RoutingContext context) {
        logger.info("apiCreatePage");

        JsonObject page = context.getBodyAsJson();
        if (!validateJsonPageDocument(context, page, "name", "markdown")) {
            return;
        }
        wikiDatabaseService.createPage(page.getString("name"), page.getString("markdown"), reply -> {
            if (reply.succeeded()) {
                context.response().setStatusCode(201);
                context.response().putHeader("Content-Type", "application/json");
                context.response().end(new JsonObject().put("success", true).encode());
            } else {
                context.response().setStatusCode(500);
                context.response().putHeader("Content-Type", "application/json");
                context.response().end(new JsonObject()
                    .put("success", false)
                    .put("error", reply.cause().getMessage()).encode());
            }
        });

    }

    private boolean validateJsonPageDocument(RoutingContext context, JsonObject page, String... expectedKeys) {
        if (!Arrays.stream(expectedKeys).allMatch(page::containsKey)) {
            logger.error("Bad page creation JSON payload: " + page.encodePrettily() + " from " + context.request().remoteAddress());
            context.response().setStatusCode(400);
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(new JsonObject()
                .put("success", false)
                .put("error", "Bad request payload").encode());
            return false;
        }
        return true;
    }

    private void apiUpdatePage(RoutingContext context) {
        logger.info("apiUpdatePage");

        int id = Integer.valueOf(context.request().getParam("id"));
        JsonObject page = context.getBodyAsJson();
        if (!validateJsonPageDocument(context, page, "markdown")) {
            return;
        }
        wikiDatabaseService.savePage(id, page.getString("markdown"), reply -> {
            handleSimpleDbReply(context, reply);
        });
    }

    private void handleSimpleDbReply(RoutingContext context, AsyncResult<Void> reply) {
        if (reply.succeeded()) {
            context.response().setStatusCode(200);
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(new JsonObject().put("success", true).encode());
        } else {
            context.response().setStatusCode(500);
            context.response().putHeader("Content-Type", "application/json");
            context.response().end(new JsonObject()
                .put("success", false)
                .put("error", reply.cause().getMessage()).encode());
        }
    }

    private void apiDeletePage(RoutingContext context) {
        logger.info("apiDeletePage");
        int id = Integer.valueOf(context.request().getParam("id"));
        wikiDatabaseService.deletePage(id, reply -> {
            handleSimpleDbReply(context, reply);
        });
    }


    private void backupHandler(RoutingContext context) {

        wikiDatabaseService.fetchAllPagesData(reply -> {

            if (reply.succeeded()) {
                JsonArray filesObject = new JsonArray();
                JsonObject payload = new JsonObject()
                    .put("files", filesObject)
                    .put("language", "plaintext")
                    .put("title", "vertx-wiki-backup")
                    .put("public", true);

                reply.result().forEach(page -> {
                    JsonObject fileObject = new JsonObject();
                    fileObject.put("name", page.getString("NAME"));
                    fileObject.put("content", page.getString("CONTENT"));
                    filesObject.add(fileObject);
                });

                webClient.post(443, "snippets.glot.io", "/snippets")
                    .putHeader("Content-Type", "application/json").as(BodyCodec.jsonObject())
                    .sendJsonObject(payload, asyncResult -> {

                        if (asyncResult.succeeded()) {
                            HttpResponse<JsonObject> response = asyncResult.result();
                            if (response.statusCode() == 200) {
                                String url = "https://glot.io/snippets/" + response.body().getString("id");
                                context.put("backup_gist_url", url);
                                indexHandler(context);
                            } else {
                                StringBuilder message = new StringBuilder()
                                    .append("Could not backup the wiki: ")
                                    .append(response.statusMessage());
                                JsonObject body = response.body();
                                if (body != null) {
                                    message.append(System.getProperty("line.separator"))
                                        .append(body.encodePrettily());
                                }
                                logger.error(message.toString());
                                context.fail(502);
                            }

                        } else {
                            Throwable err = asyncResult.cause();
                            logger.error("HTTP Client error", err);
                            context.fail(err);
                        }
                    });
            } else {
                context.fail(reply.cause());
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

package io.vertx.guides.wiki;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主类
 *
 * @author tao.yang
 * @date 2020-05-14
 */
public class MainVerticle extends AbstractVerticle {

    private static final String EMPTY_PAGE_MARKDOWN =
        "# A new page\n" +
            "\n" +
            "Feel-free to write in Markdown!\n";

    private static final String SQL_GET_PAGE = "select id, content from pages where name = ?";
    private static final String SQL_CREATE_PAGE = "insert into pages values (?, ?)";
    private static final String SQL_SAVE_PAGE = "update pages set content = ? where id = ?";
    private static final String SQL_ALL_PAGES = "select name from pages";
    private static final String SQL_DELETE_PAGE = "delete from pages where id = ?";
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(MainVerticle.class);
    /**
     * JDBC 客户端
     */
    private JDBCClient jdbcClient;
    /**
     * 模板引擎
     */
    private FreeMarkerTemplateEngine templateEngine;

    /**
     * @param promise
     * @throws Exception
     */
    @Override
    public void start(Promise<Void> promise) {
        // promise 用于表名操作是否成功。
        logger.info(" ============================ start ============================");

        Future<Void> steps = prepareDatabase().compose(aVoid -> startHttpServer());
        steps.setHandler(promise);
    }

    /**
     * 准备数据库
     *
     * @return
     */
    private Future<Void> prepareDatabase() {

        logger.info("准备连接数据库");
        Promise<Void> promise = Promise.promise();

        JsonObject config = new JsonObject();
        config.put("url", "jdbc:mysql://172.23.6.249:3306/yt_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai");
        config.put("driver_class", "com.mysql.cj.jdbc.Driver");
        config.put("user", "rwuser");
        config.put("password", "kw1ntu2p");
        config.put("initial_pool_size", 3);
        config.put("max_pool_size", 30);
        config.put("max_idle_time", 1000);

        jdbcClient = JDBCClient.createShared(vertx, config);

        jdbcClient.getConnection(sqlConnectionAsyncResult -> {

            // 如果获取连接失败
            if (sqlConnectionAsyncResult.failed()) {
                logger.error("Could not open a database connection", sqlConnectionAsyncResult.cause());
                promise.fail(sqlConnectionAsyncResult.cause());
                return;
            }
            // 如果获取连接成功，则执行select 'x' 语句校验是否正常
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.execute("select 'x'", executeAsyncResult -> {
                connection.close();
                if (executeAsyncResult.failed()) {
                    logger.error("Database preparation error", executeAsyncResult.cause());
                    promise.fail(executeAsyncResult.cause());
                    return;
                }
                promise.complete();
            });

        });
        return promise.future();
    }

    /**
     * 启动 http 服务
     *
     * @return
     */
    private Future<Void> startHttpServer() {
        logger.info("启动 HTTP 服务");
        Promise<Void> promise = Promise.promise();

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
                logger.info("HTTP server running on port 8080");
                promise.complete();
            }
        });

        return promise.future();
    }

    /**
     * @param context
     */
    private void pageRenderingHandler(RoutingContext context) {

        String page = context.request().getParam("page");

        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.succeeded()) {
                SQLConnection connection = sqlConnectionAsyncResult.result();
                connection.queryWithParams(SQL_GET_PAGE, new JsonArray().add(page), queryResultAsyncResult -> {

                    connection.close();

                    if (queryResultAsyncResult.succeeded()) {
                        JsonArray row = queryResultAsyncResult.result().getResults().stream()
                            .findFirst().orElseGet(() -> new JsonArray().add(-1).add(EMPTY_PAGE_MARKDOWN));
                        Integer id = row.getInteger(0);
                        String rawContent = row.getString(1);

                        context.put("title", page);
                        context.put("id", id);
                        context.put("newPage", queryResultAsyncResult.result().getResults().size() == 0 ? "yes" : "no");
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
                    } else {
                        context.fail(queryResultAsyncResult.cause());
                    }
                });
            } else {
                context.fail(sqlConnectionAsyncResult.cause());
            }
        });
    }

    /**
     * 主页
     *
     * @param context
     */
    private void indexHandler(RoutingContext context) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            // 获取数据连接成功
            if (sqlConnectionAsyncResult.succeeded()) {

                SQLConnection connection = sqlConnectionAsyncResult.result();
                connection.query(SQL_ALL_PAGES, queryResultAsyncResult -> {
                    connection.close();

                    if (queryResultAsyncResult.succeeded()) {
                        ResultSet rs = (ResultSet) queryResultAsyncResult.result();
                        try {
                            List<String> dataList = new ArrayList<>();
                            while (rs.next()) {
                                String value = rs.getString("value");
                                dataList.add(value);
                            }

                            context.put("title", "Wiki home");
                            context.put("pages", dataList);
                            templateEngine.render(context.data(), "templates/index.ftl", event -> {
                                if (event.succeeded()) {
                                    context.response().putHeader("Content-Type", "text/html");
                                    context.response().end(event.result());
                                } else {
                                    context.fail(event.cause());
                                }
                            });
                        } catch (Exception e) {
                            logger.error("查询出错", e);
                            context.fail(e);
                        }
                        return;
                    }

                    // 如果查询出错
                    context.fail(queryResultAsyncResult.cause());
                });
                return;
            }

            // 如果未获取到数据库连接
            context.fail(sqlConnectionAsyncResult.cause());

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
        String id = context.request().getParam("id");
        String title = context.request().getParam("title");
        String markdown = context.request().getParam("markdown");
        boolean newPage = "yes".equals(context.request().getParam("newPage"));

        jdbcClient.getConnection(sqlConnectionAsyncResult -> {

            if (sqlConnectionAsyncResult.failed()) {
                logger.error("pageUpdateHandler 获取数据连接异常");
                context.fail(sqlConnectionAsyncResult.cause());
                return;
            }

            SQLConnection connection = sqlConnectionAsyncResult.result();

            String sql = newPage ? SQL_CREATE_PAGE : SQL_SAVE_PAGE;
            JsonArray params = new JsonArray();

            if (newPage) {
                params.add(title).add(markdown);
            } else {
                params.add(markdown).add(id);
            }

            connection.updateWithParams(sql, params, res -> {
                connection.close();
                if (res.succeeded()) {
                    context.response().setStatusCode(303);
                    context.response().putHeader("Location", "/wiki/" + title);
                    context.response().end();
                } else {
                    context.fail(res.cause());
                }
            });
        });
    }


    /**
     * 删除
     *
     * @param context
     */
    private void pageDeletionHandler(RoutingContext context) {
        String id = context.request().getParam("id");
        jdbcClient.getConnection(car -> {
            if (car.succeeded()) {
                SQLConnection connection = car.result();
                connection.updateWithParams(SQL_DELETE_PAGE, new JsonArray().add(id), res -> {
                    connection.close();
                    if (res.succeeded()) {
                        context.response().setStatusCode(303);
                        context.response().putHeader("Location", "/");
                        context.response().end();
                    } else {
                        context.fail(res.cause());
                    }
                });
            } else {
                context.fail(car.cause());
            }
        });
    }

}

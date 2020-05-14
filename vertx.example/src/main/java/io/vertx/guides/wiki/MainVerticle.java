package io.vertx.guides.wiki;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主类
 *
 * @author tao.yang
 * @date 2020-05-14
 */
public class MainVerticle extends AbstractVerticle {

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
    private FreeMarkerTemplateEngine freeMarkerTemplateEngine;

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
        config.put("url", "jdbc:mysql://172.23.6.249:3306/yt_test??useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai");
        config.put("driver_class", "com.mysql.jdbc.Driver");
        config.put("max_pool_size", "30");
        config.put("username", "rwuser");
        config.put("password", "kw1ntu2p");

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
                    return ;
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

        router.post().handler(BodyHandler.create());
        router.post("/save").handler(this::pageUpdateHandler);
        router.post("/create").handler(this::pageCreateHandler);
        router.post("/delete").handler(this::pageDeletionHandler);

        freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create(vertx);

        httpServer.requestHandler(router).listen(8080, httpServerAsyncResult -> {

            if (httpServerAsyncResult.failed()){
                logger.error("Could not start a HTTP server", httpServerAsyncResult.cause());
                promise.fail(httpServerAsyncResult.cause());
                return ;
            }

            if (httpServerAsyncResult.succeeded()){
                logger.info("HTTP server running on port 8080");
                promise.complete();
            }
        });

        return promise.future();
    }

    /**
     * 主页
     * @param context
     */
    private void indexHandler(RoutingContext context){
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {});
    }

    /**
     * 创建
     * @param context
     */
    private void pageCreateHandler(RoutingContext context) {

    }

    /**
     * 更新
     * @param context
     */
    private void pageUpdateHandler(RoutingContext context) {

    }

    /**
     * 删除
     * @param context
     */
    private void pageDeletionHandler(RoutingContext context) {

    }


}

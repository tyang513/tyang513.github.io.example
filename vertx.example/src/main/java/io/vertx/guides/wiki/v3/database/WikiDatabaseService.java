package io.vertx.guides.wiki.v3.database;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author tao.yang
 * @date 2020-05-18
 */
@ProxyGen
@VertxGen
public interface WikiDatabaseService {

    @GenIgnore
    static final Logger logger = LoggerFactory.getLogger(WikiDatabaseService.class);

    @GenIgnore
    static WikiDatabaseService create(JDBCClient dbClient, HashMap<SqlQuery, String> sqlQueries, Handler<AsyncResult<WikiDatabaseService>> readyHandler) {
        return new WikiDatabaseServiceImpl(dbClient, sqlQueries, readyHandler);
    }

    @GenIgnore
    static WikiDatabaseService createProxy(Vertx vertx, String address) {
        try {
            return (WikiDatabaseService) Class.forName("io.vertx.guides.wiki.v3.database.WikiDatabaseServiceVertxEBProxy")
                .getConstructor(Vertx.class, String.class).newInstance(vertx, address);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            logger.error("create proxy", e);
        }
        return null;
    }

    @Fluent
    WikiDatabaseService fetchAllPages(Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    WikiDatabaseService fetchPage(String name, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    WikiDatabaseService createPage(String title, String markdown, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    WikiDatabaseService savePage(int id, String markdown, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    WikiDatabaseService deletePage(int id, Handler<AsyncResult<Void>> resultHandler);
}

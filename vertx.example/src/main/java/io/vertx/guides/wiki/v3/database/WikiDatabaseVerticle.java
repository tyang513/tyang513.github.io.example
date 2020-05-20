package io.vertx.guides.wiki.v3.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author tao.yang
 * @date 2020-05-18
 */
public class WikiDatabaseVerticle extends AbstractVerticle {

    public static final String CONFIG_WIKIDB_QUEUE = "wikidb.queue";
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(WikiDatabaseVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {

        HashMap<SqlQuery, String> sqlQueries = loadSqlQueries();

        JsonObject config = new JsonObject();
        config.put("url", "jdbc:mysql://172.23.6.249:3306/yt_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=true&serverTimezone=Asia/Shanghai");
        config.put("driver_class", "com.mysql.cj.jdbc.Driver");
        config.put("user", "rwuser");
        config.put("password", "kw1ntu2p");
        config.put("initial_pool_size", 3);
        config.put("max_pool_size", 30);
        config.put("max_idle_time", 1000);

        JDBCClient dbClient = JDBCClient.createShared(vertx, config);

        WikiDatabaseService.create(dbClient, sqlQueries, ready -> {
            if (ready.succeeded()) {
                ServiceBinder binder = new ServiceBinder(vertx);
                binder
                    .setAddress(CONFIG_WIKIDB_QUEUE)
                    .register(WikiDatabaseService.class, ready.result());
                promise.complete();
            } else {
                promise.fail(ready.cause());
            }
        });
    }

    /*
     * Note: this uses blocking APIs, but data is small...
     */
    private HashMap<SqlQuery, String> loadSqlQueries() throws IOException {

        String SQL_GET_PAGE = "select id, content from pages where name = ?";
        String SQL_CREATE_PAGE = "insert into pages values (?, 'asdf')";
        String SQL_SAVE_PAGE = "update pages set content = ? where id = ?";
        String SQL_ALL_PAGES = "select name from pages";
        String SQL_DELETE_PAGE = "delete from pages where id = ?";
        String ALL_PAGES_DATA = "select * from pages";
        String CREATE_PAGES_TABLE = "select 'x'";

        HashMap<SqlQuery, String> sqlQueries = new HashMap<>();
        sqlQueries.put(SqlQuery.ALL_PAGES, SQL_ALL_PAGES);
        sqlQueries.put(SqlQuery.GET_PAGE, SQL_GET_PAGE);
        sqlQueries.put(SqlQuery.CREATE_PAGE, SQL_CREATE_PAGE);
        sqlQueries.put(SqlQuery.SAVE_PAGE, SQL_SAVE_PAGE);
        sqlQueries.put(SqlQuery.DELETE_PAGE, SQL_DELETE_PAGE);
        sqlQueries.put(SqlQuery.ALL_PAGES_DATA, ALL_PAGES_DATA);
        sqlQueries.put(SqlQuery.CREATE_PAGES_TABLE, CREATE_PAGES_TABLE);
        return sqlQueries;
    }
}


package io.vertx.guides.wiki;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-05-16
 */
public class DatabaseVerticle extends AbstractVerticle {

    private static final String SQL_GET_PAGE = "select id, content from pages where name = ?";
    private static final String SQL_CREATE_PAGE = "insert into pages values (?, ?)";
    private static final String SQL_SAVE_PAGE = "update pages set content = ? where id = ?";
    private static final String SQL_ALL_PAGES = "select name from pages";
    private static final String SQL_DELETE_PAGE = "delete from pages where id = ?";
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DatabaseVerticle.class);
    /**
     * JDBC 客户端
     */
    private JDBCClient jdbcClient;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

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
                startPromise.fail(sqlConnectionAsyncResult.cause());
                return;
            }
            // 如果获取连接成功，则执行select 'x' 语句校验是否正常
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.execute("select 'x'", executeAsyncResult -> {
                connection.close();
                if (executeAsyncResult.failed()) {
                    logger.error("Database preparation error", executeAsyncResult.cause());
                    startPromise.fail(executeAsyncResult.cause());
                    return;
                }
                startPromise.complete();
            });

        });
    }

    
}

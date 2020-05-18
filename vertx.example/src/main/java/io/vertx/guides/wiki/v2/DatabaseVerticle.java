package io.vertx.guides.wiki.v2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 消息处理
     *
     * @param message
     */
    public void onMessage(Message<JsonObject> message) {

        if (!message.headers().contains("action")) {
            logger.error("No action header specified for message with headers {} and body {}", message.headers(), message.body().encodePrettily());
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }

        String action = message.headers().get("action");

        switch (action) {
            case "all-pages":
                fetchAllPages(message);
                break;
            case "get-page":
                fetchPage(message);
                break;
            case "create-page":
                createPage(message);
                break;
            case "save-page":
                savePage(message);
                break;
            case "delete-page":
                deletePage(message);
                break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }

    }

    private void fetchAllPages(Message<JsonObject> message) {
        jdbcClient.query(SQL_ALL_PAGES, res -> {
            if (res.succeeded()) {
                List<String> pages = res.result()
                    .getResults()
                    .stream()
                    .map(json -> json.getString(0))
                    .sorted()
                    .collect(Collectors.toList());
                message.reply(new JsonObject().put("pages", new JsonArray(pages)));
            } else {
                reportQueryError(message, res.cause());
            }
        });
    }

    private void fetchPage(Message<JsonObject> message) {
        String requestedPage = message.body().getString("page");
        JsonArray params = new JsonArray().add(requestedPage);

        jdbcClient.queryWithParams(SQL_GET_PAGE, params, fetch -> {
            if (fetch.succeeded()) {
                JsonObject response = new JsonObject();
                ResultSet resultSet = fetch.result();
                if (resultSet.getNumRows() == 0) {
                    response.put("found", false);
                } else {
                    response.put("found", true);
                    JsonArray row = resultSet.getResults().get(0);
                    response.put("id", row.getInteger(0));
                    response.put("rawContent", row.getString(1));
                }
                message.reply(response);
            } else {
                reportQueryError(message, fetch.cause());
            }
        });
    }

    private void createPage(Message<JsonObject> message) {
        JsonObject request = message.body();
        JsonArray data = new JsonArray()
            .add(request.getString("title"))
            .add(request.getString("markdown"));

        jdbcClient.updateWithParams(SQL_CREATE_PAGE, data, res -> {
            if (res.succeeded()) {
                message.reply("ok");
            } else {
                reportQueryError(message, res.cause());
            }
        });
    }

    private void savePage(Message<JsonObject> message) {
        JsonObject request = message.body();
        JsonArray data = new JsonArray()
            .add(request.getString("markdown"))
            .add(request.getString("id"));

        jdbcClient.updateWithParams(SQL_SAVE_PAGE, data, res -> {
            if (res.succeeded()) {
                message.reply("ok");
            } else {
                reportQueryError(message, res.cause());
            }
        });
    }

    private void deletePage(Message<JsonObject> message) {
        JsonArray data = new JsonArray().add(message.body().getString("id"));

        jdbcClient.updateWithParams(SQL_DELETE_PAGE, data, res -> {
            if (res.succeeded()) {
                message.reply("ok");
            } else {
                reportQueryError(message, res.cause());
            }
        });
    }

    private void reportQueryError(Message<JsonObject> message, Throwable cause) {
        logger.error("Database query error", cause);
        message.fail(ErrorCodes.DB_ERROR.ordinal(), cause.getMessage());
    }

    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION,
        DB_ERROR
    }


}

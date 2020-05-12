package example;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/**
 * @author yangtao
 */
public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {

    }

    public MainVerticle() {

    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
        }).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

}

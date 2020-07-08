package async.servlet.asynchttpclient.example;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author tao.yang
 * @date 2020-05-28
 */
public class AsyncHttpClientExecutorsExample {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientExecutorsExample.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AsyncHttpClient client = asyncHttpClient();
        String loginUrl = "http://172.20.33.5:8081/tdmkaccount/authen/app/v3?apikey=d4263a26c6dd42e3909a68d93566978a&apitoken=349628f6e6c34a63b5055f533491eaed";
        ListenableFuture<Response> listenableFuture = client.prepareGet(loginUrl).execute();
        Runnable callback = () -> {
            try {
                Response response = listenableFuture.get();
                logger.info(response.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        };
        Executor executor = Executors.newFixedThreadPool(10);
        listenableFuture.addListener(callback, executor);
    }

}

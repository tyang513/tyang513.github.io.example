package async.servlet.asynchttpclient.example;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author tao.yang
 * @date 2020-05-28
 */
public class AsyncHttpClientExample {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientExample.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        AsyncHttpClient client = asyncHttpClient();
        AsyncHttpClientExample.asyncHttpClientDemo(client);

        client.close();
    }

    public static void asyncHttpClientDemo(AsyncHttpClient asyncHttpClient) throws ExecutionException, InterruptedException, IOException {
        String apiKey = "d4263a26c6dd42e3909a68d93566978a";
        String apiToken = "349628f6e6c34a63b5055f533491eaed";
        String token = null;
        String refreshToken = null;
        String loginUrl = "http://172.20.33.5:8081/tdmkaccount/authen/app/v3?apikey=" + apiKey + "&apitoken=" + apiToken;
        Response response = asyncHttpClient.prepareGet(loginUrl).execute().get();
        logger.info(response.getContentType());

        ListenableFuture<Response> listenableFuture = asyncHttpClient.prepareGet(loginUrl).execute();
        Runnable callback = () -> {

        };
        Executor executor = null;
        listenableFuture.addListener(callback, executor);

    }

}

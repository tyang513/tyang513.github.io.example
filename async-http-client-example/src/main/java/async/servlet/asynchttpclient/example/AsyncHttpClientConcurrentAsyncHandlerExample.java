package async.servlet.asynchttpclient.example;

import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * @author tao.yang
 * @date 2020-05-29
 */
public class AsyncHttpClientConcurrentAsyncHandlerExample {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpClientConcurrentAsyncHandlerExample.class);

    public static void main(String[] args) {
        AsyncHttpClient client = asyncHttpClient();
        String loginUrl = "http://172.20.33.5:8081/tdmkaccount/authen/app/v3?apikey=d4263a26c6dd42e3909a68d93566978a&apitoken=349628f6e6c34a63b5055f533491eaed";
        ListenableFuture<Response> listenableFuture = client.prepareGet(loginUrl).execute(new AsyncHandler<Response>() {
            @Override
            public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                return null;
            }

            @Override
            public State onHeadersReceived(HttpHeaders headers) throws Exception {
                return null;
            }

            @Override
            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                return null;
            }

            @Override
            public void onThrowable(Throwable t) {

            }

            @Override
            public Response onCompleted() throws Exception {
                return null;
            }
        });

    }

}

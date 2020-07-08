package async.servlet.apache.asynchttpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-07-06
 */
public class ApacheAsyncHttpClientDemo {

    private static final Logger logger = LoggerFactory.getLogger(ApacheAsyncHttpClientDemo.class);

    public static void main(String[] args) {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().build();
        httpclient.start();

        HttpGet get = new HttpGet("http://www.sina.com.cn");

        httpclient.execute(get, new SinaCallback());
        httpclient.execute(get, new SinaCallback());
        httpclient.execute(get, new SinaCallback());
        httpclient.execute(get, new SinaCallback());
    }

    private static final class SinaCallback implements FutureCallback<HttpResponse> {

        @Override
        public void completed(HttpResponse response) {
            logger.info("SinaCallback ");
            response.getEntity();
        }

        @Override
        public void failed(Exception e) {
            logger.error("SinaCallback.failed", e);
        }

        @Override
        public void cancelled() {
            logger.warn("访问http://www.sina.com.cn 取消");
        }
    }

}

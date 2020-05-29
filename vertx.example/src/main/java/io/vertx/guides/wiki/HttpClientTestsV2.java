package io.vertx.guides.wiki;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author tao.yang
 * @date 2020-05-22
 */
public class HttpClientTestsV2 {

    public static void main(String[] args) throws IOException, InterruptedException {

        Logger queryLog = LoggerFactory.getLogger(HttpClientTestsV2.class);
        String apiKey = "d4263a26c6dd42e3909a68d93566978a";
        String apiToken = "349628f6e6c34a63b5055f533491eaed";
        String token = null;
        String refreshToken = null;

        // 登录
        String url = "http://172.20.33.5:8081/tdmkaccount/authen/app/v3?apikey=" + apiKey + "&apitoken=" + apiToken;
        CloseableHttpResponse response = HttpClients.createDefault().execute(new HttpGet(url));
        String requestResult = EntityUtils.toString(response.getEntity());
        JSONObject json = JSONObject.parseObject(requestResult);
        token = json.getJSONObject("data").getString("token");
        refreshToken = json.getJSONObject("data").getString("refreshToken");

        Auth sdmkAuth = new Auth(apiKey, apiToken, token, refreshToken);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
            .setServiceUnavailableRetryStrategy(
                new SdmkServiceUnavailableRetryStrategy(3, 1000, sdmkAuth));

        // 执行业务代码
        String serviceurl = "http://it.dmk.tendcloud.com/data/tour/v1?areaId=110000&dataTime=20191001&frequency=0&kpiCode=travel.dest_province_day:cf:arrive_means_pt&prid=1111112";
        while (true) {
            HttpClient httpClient = httpClientBuilder.build();
            HttpUriRequest servicereqeust = new HttpGet(serviceurl);
            servicereqeust.setHeader("X-Access-Token", sdmkAuth.getToken());
            queryLog.error(" token = {} ", token);
            HttpResponse httpResponse = httpClient.execute(servicereqeust);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                queryLog.info(" while status:{}", httpResponse.getStatusLine().getStatusCode());
            }
        }
    }

    @Contract(threading = ThreadingBehavior.IMMUTABLE)
    private static class SdmkServiceUnavailableRetryStrategy extends DefaultServiceUnavailableRetryStrategy {
        Logger queryLog = LoggerFactory.getLogger("QueryLog");
        private Auth sdmkAuth;
        private int maxRetries;

        public SdmkServiceUnavailableRetryStrategy(final int retryCount, final int retryInterval, Auth sdmkAuth) {
            super(retryCount, retryInterval);
            this.sdmkAuth = sdmkAuth;
        }

        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {

            if (executionCount <= maxRetries && response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                return true;
            }

            try {
                if (response.getStatusLine().getStatusCode() == 401) {
                    String refreshUrl = "http://172.20.33.5:8081/authentication/api/refreshToken?apikey=" + sdmkAuth.getApiKey() + "&refreshToken=" + sdmkAuth.getRefreshToken();
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse refreshResponse = client.execute(new HttpPost(refreshUrl));
                    String requestResult = EntityUtils.toString(refreshResponse.getEntity());
                    JSONObject json = JSONObject.parseObject(requestResult);

                    String token = json.getJSONObject("data").getString("token");
                    String refreshToken = json.getJSONObject("data").getString("refreshToken");

                    sdmkAuth.setToken(token);
                    sdmkAuth.setRefreshToken(refreshToken);

                    queryLog.info("token = {}, refreshToken {}", token, refreshToken);

                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    request.setHeader("X-Access-Token", sdmkAuth.getToken());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.retryRequest(response, executionCount, context);
        }
    }

    static class Auth {
        String apiKey;
        String apiToken;
        String token;
        String refreshToken;

        public Auth(String apiKey,
                    String apiToken, String token, String refreshToken) {
            this.apiKey = apiKey;
            this.apiToken = apiToken;
            this.token = token;
            this.refreshToken = refreshToken;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

}

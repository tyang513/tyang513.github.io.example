package com.talkingdata.framework.gateway.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 网关线程池计算
 *
 * @author tao.yang
 * @date 2019-12-12
 */
public class GatewayPoolSizeCalculator extends PoolSizeCalculator {

    public static void main(String[] args) {
        GatewayPoolSizeCalculator calculator = new GatewayPoolSizeCalculator();
        calculator.calculateBoundaries(new BigDecimal(1.0), new BigDecimal(100000));
    }

    @Override
    protected long getCurrentThreadCPUTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    @Override
    protected Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    String getURL = "http://baidu.com";
                    URL getUrl = new URL(getURL);

                    connection = (HttpURLConnection) getUrl.openConnection();
                    connection.connect();
                    reader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // empty loop
                    }
                } catch (IOException e) {

                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {

                        }
                    }
                    connection.disconnect();
                }
            }
        };
    }

    @Override
    protected BlockingQueue<Runnable> createWorkQueue() {
        return new LinkedBlockingQueue<>();
    }
}

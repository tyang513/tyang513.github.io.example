package com.talkingdata.framework.gateway;

import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import com.netflix.governator.InjectorBuilder;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewayBootstrap {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GatewayBootstrap.class);

    public static void main(String[] args) {
        new GatewayBootstrap().start();
    }


    public void start() {

        logger.info("启动 Gateway ");
        long startTime = System.currentTimeMillis();
        int exitCode = 0;

        Server server = null;

        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("application");
            Injector injector = InjectorBuilder.fromModule(new GatewayModule()).createInjector();
            BaseServerStartup serverStartup = injector.getInstance(BaseServerStartup.class);
            server = serverStartup.server();

            long startupDuration = System.currentTimeMillis() - startTime;
            logger.info("Gateway Zuul2: finished startup. Duration = {} ms", startupDuration);

            server.start(true);
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error("网关启动错误", t);
            logger.error("###############");
            logger.error("Gateway Zuul2 : initialization failed. Forcing shutdown now.");
            logger.error("###############");
            exitCode = 1;
        } finally {
            // server shutdown
            if (server != null) {
                server.stop();
            }
            System.exit(exitCode);
        }
    }

}

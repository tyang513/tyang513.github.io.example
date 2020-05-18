package com.talkingdata.framework.gateway;


import com.google.common.collect.Maps;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.config.DynamicIntProperty;
import com.netflix.discovery.EurekaClient;
import com.netflix.netty.common.accesslog.AccessLogPublisher;
import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.netty.common.channel.config.CommonChannelConfigKeys;
import com.netflix.netty.common.metrics.EventLoopGroupMetrics;
import com.netflix.netty.common.proxyprotocol.StripUntrustedProxyHeadersHandler;
import com.netflix.netty.common.ssl.ServerSslConfig;
import com.netflix.netty.common.status.ServerStatusManager;
import com.netflix.spectator.api.Registry;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.FilterUsageNotifier;
import com.netflix.zuul.RequestCompleteHandler;
import com.netflix.zuul.context.SessionContextDecorator;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.DirectMemoryMonitor;
import com.netflix.zuul.netty.server.Http1MutualSslChannelInitializer;
import com.netflix.zuul.netty.server.ZuulDependencyKeys;
import com.netflix.zuul.netty.server.ZuulServerChannelInitializer;
import com.netflix.zuul.netty.server.http2.Http2SslChannelInitializer;
import com.netflix.zuul.netty.server.push.PushConnectionRegistry;
import com.netflix.zuul.netty.ssl.BaseSslContextFactory;
import com.talkingdata.framework.gateway.protocol.GatewayPushMessageSenderInitializer;
import com.talkingdata.framework.gateway.protocol.SSEPushChannelInitializer;
import com.talkingdata.framework.gateway.protocol.WebSocketPushChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.ClientAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Map;

/**
 * @author tao.yang
 * @date 2019-10-25
 */
@Singleton
public class GatewayServerStartup extends BaseServerStartup {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GatewayServerStartup.class);

    /**
     * 支持的协议类型 HTTP HTTP2 HTTP_MUTUAL_TLS WEB_SOCKET SSE
     * HTTP_MUTUAL_TLS 传输层安全(transport layer security)
     * WEB SOCKET
     * SSE 可以理解为轻量级的web socket协议
     */
    enum ServerType {
        HTTP,
        HTTP2,
        HTTP_MUTUAL_TLS,
        WEB_SOCKET,
        SSE
    }

    private static final String[] WWW_PROTOCOLS = new String[]{"TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3"};

    private static final ServerType SERVER_TYPE = ServerType.HTTP;

    private final PushConnectionRegistry pushConnectionRegistry;

    private final GatewayPushMessageSenderInitializer pushSenderInitializer;

    @Inject
    public GatewayServerStartup(ServerStatusManager serverStatusManager, FilterLoader filterLoader,
                                SessionContextDecorator sessionCtxDecorator, FilterUsageNotifier usageNotifier,
                                RequestCompleteHandler reqCompleteHandler, Registry registry,
                                DirectMemoryMonitor directMemoryMonitor, EventLoopGroupMetrics eventLoopGroupMetrics,
                                EurekaClient discoveryClient, ApplicationInfoManager applicationInfoManager,
                                AccessLogPublisher accessLogPublisher, PushConnectionRegistry pushConnectionRegistry,
                                GatewayPushMessageSenderInitializer pushSenderInitializer) {
        super(serverStatusManager, filterLoader, sessionCtxDecorator, usageNotifier, reqCompleteHandler, registry,
                directMemoryMonitor, eventLoopGroupMetrics, discoveryClient, applicationInfoManager,
                accessLogPublisher);
        this.pushConnectionRegistry = pushConnectionRegistry;
        this.pushSenderInitializer = pushSenderInitializer;
    }

    @Override
    protected Map<Integer, ChannelInitializer> choosePortsAndChannels(ChannelGroup clientChannels) {
        Map<Integer, ChannelInitializer> portsToChannels = Maps.newHashMap();

        String mainPortName = "main";
        int port = new DynamicIntProperty("zuul.server.port.main", 7001).get();

        ChannelConfig channelConfig = defaultChannelConfig(mainPortName);
        int pushPort = new DynamicIntProperty("zuul.server.port.http.protocol", 7008).get();
        ServerSslConfig sslConfig;
        ChannelConfig channelDependencies = defaultChannelDependencies(mainPortName);

        /* These settings may need to be tweaked depending if you're running behind an ELB HTTP listener, TCP listener,
         * or directly on the internet.
         */
        switch (SERVER_TYPE) {
            /* The below settings can be used when running behind an ELB HTTP listener that terminates SSL for you
             * and passes XFF headers.
             */
            case HTTP:
                channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.ALWAYS);
                channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, false);
                channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
                channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, false);

                portsToChannels.put(port, new GatewayServerChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
                logPortConfigured(port, null);
                break;

            /* The below settings can be used when running behind an ELB TCP listener with proxy protocol, terminating
             * SSL in Zuul.
             */
            case HTTP2:
                sslConfig = ServerSslConfig.withDefaultCiphers(loadFromResources("server.cert"), loadFromResources("server.key"), WWW_PROTOCOLS);

                channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.NEVER);
                channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, true);
                channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
                channelConfig.set(CommonChannelConfigKeys.serverSslConfig, sslConfig);
                channelConfig.set(CommonChannelConfigKeys.sslContextFactory, new BaseSslContextFactory(registry, sslConfig));

                addHttp2DefaultConfig(channelConfig, mainPortName);

                portsToChannels.put(port, new Http2SslChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
                logPortConfigured(port, sslConfig);
                break;

            /* The below settings can be used when running behind an ELB TCP listener with proxy protocol, terminating
             * SSL in Zuul.
             *
             * Can be tested using certs in resources directory:
             *  curl https://localhost:7001/test -vk --cert src/main/resources/ssl/client.cert:zuul123 --key src/main/resources/ssl/client.key
             */
            case HTTP_MUTUAL_TLS:
                sslConfig = new ServerSslConfig(WWW_PROTOCOLS, ServerSslConfig.getDefaultCiphers(),
                        loadFromResources("server.cert"), loadFromResources("server.key"), null,
                        ClientAuth.REQUIRE, loadFromResources("truststore.jks"), loadFromResources("truststore.key"), false);

                channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.NEVER);
                channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, true);
                channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
                channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, true);
                channelConfig.set(CommonChannelConfigKeys.serverSslConfig, sslConfig);
                channelConfig.set(CommonChannelConfigKeys.sslContextFactory, new BaseSslContextFactory(registry, sslConfig));

                portsToChannels.put(port, new Http1MutualSslChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
                logPortConfigured(port, sslConfig);
                break;

            /* Settings to be used when running behind an ELB TCP listener with proxy protocol as a Push notification
             * server using WebSockets */
            case WEB_SOCKET:
                channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.NEVER);
                channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, true);
                channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
                channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, true);

                channelDependencies.set(ZuulDependencyKeys.pushConnectionRegistry, pushConnectionRegistry);

                portsToChannels.put(port, new WebSocketPushChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
                logPortConfigured(port, null);

                // port to accept protocol message from the backend, should be accessible on internal network only.
                portsToChannels.put(pushPort, pushSenderInitializer);
                logPortConfigured(pushPort, null);
                break;

            /* Settings to be used when running behind an ELB TCP listener with proxy protocol as a Push notification
             * server using Server Sent Events (SSE) */
            case SSE:
                channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.NEVER);
                channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, true);
                channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
                channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, true);

                channelDependencies.set(ZuulDependencyKeys.pushConnectionRegistry, pushConnectionRegistry);

                portsToChannels.put(port, new SSEPushChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
                logPortConfigured(port, null);

                // port to accept protocol message from the backend, should be accessible on internal network only.
                portsToChannels.put(pushPort, pushSenderInitializer);
                logPortConfigured(pushPort, null);
                break;

            default:
                logger.error("不支持的协议");
                break;
        }

        return portsToChannels;
    }

    private File loadFromResources(String s) {
        return new File(ClassLoader.getSystemResource("ssl/" + s).getFile());
    }
}
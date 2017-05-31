package ru.studmanager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyLauncher {
    private static final Logger logger = LoggerFactory.getLogger(JettyLauncher.class);
    public static final int MAX_REQUEST_SIZE = 16 * 1024 * 1024;

    private final JettyConfig conf;

    private List<WebAppContext> webAppContexts = new ArrayList<>();

    public JettyLauncher(JettyConfig conf) {
        this.conf = conf;
        logger.debug("ru.studmanager.JettyLauncher initialized with config {}", conf);
    }

    public static JettyLauncher server(JettyConfig conf) {
        return new JettyLauncher(conf);
    }

    public JettyLauncher withWebApp(String webAppRoot, String path) {
        return withWebAppContext(new WebAppContext(webAppRoot, path));
    }

    public JettyLauncher withWebAppContext(WebAppContext context) {
        webAppContexts.add(context);
        return this;
    }

    public JettyLauncher withDefaultWebApp(ClassLoader classLoader, String path) {
        WebAppContext webapp = new WebAppContext();
        webapp.setResourceBase(classLoader.getResource("webapp").toExternalForm());
        webapp.setContextPath(path);
        webAppContexts.add(webapp);
        return this;
    }

    public void run() throws Exception {
        Server server = configureServer();

        HandlerCollection handlers = new HandlerCollection();
        for (WebAppContext ctx : webAppContexts) {
            // если не можем запустить приложение - не запускаем и jetty
            ctx.setThrowUnavailableOnStartupException(true);
            ctx.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            ctx.setMaxFormContentSize(MAX_REQUEST_SIZE);
            handlers.addHandler(ctx);
        }

        // Needed for graceful shutdown, see Server.setStopTimeout
        StatisticsHandler statsHandler = new StatisticsHandler();
        statsHandler.setHandler(handlers);
        server.setHandler(statsHandler);

        try {
            server.start();
            logger.info("jetty started, ports={}, {}", conf.port, conf.sslPort);
        } catch (Exception e) {
            server.stop();
            throw new JettyLaunchException("Can't start Jetty", e);
        }

        try {
            server.join();
            logger.info("jetty stopped");
        } finally {
            server.stop();
        }
    }

    private Server configureServer() {

        final QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(conf.threadPool.maxThreads);
        threadPool.setMinThreads(conf.threadPool.minThreads);
        threadPool.setIdleTimeout(conf.threadPool.idleTimeout);

        final Server server = new Server(threadPool);



        final ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(getHttpConfiguration()));
        connector.setHost(conf.host);
        connector.setPort(conf.port);
        connector.setIdleTimeout(conf.idleTimeout);

        server.addConnector(connector);

        if (conf.sslPort > 0) {
            SslContextFactory ctx = new SslContextFactory();
            ConnectionFactory sslFactory = new SslConnectionFactory(ctx, "http/1.1");

            HttpConfiguration sslHttpConfig = getHttpConfiguration();
            sslHttpConfig.addCustomizer(new SecureRequestCustomizer());
            ConnectionFactory httpFactory = new HttpConnectionFactory(sslHttpConfig);

            ServerConnector sslConnector = new ServerConnector(server, sslFactory, httpFactory);
            sslConnector.setPort(conf.sslPort);
            sslConnector.setIdleTimeout(conf.idleTimeout);

            server.addConnector(sslConnector);
        }

        configureServerShutdown(server);

        return server;
    }

    private void configureServerShutdown(Server server) {
        server.setStopTimeout(conf.stopTimeout);
        server.setStopAtShutdown(true);
    }

    private HttpConfiguration getHttpConfiguration() {
        final HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setRequestHeaderSize(65536);
        httpConfiguration.setResponseHeaderSize(65536);
        httpConfiguration.setSendServerVersion(false);
        httpConfiguration.setSendDateHeader(false);
        return httpConfiguration;
    }

    public static class JettyLaunchException extends RuntimeException {
        public JettyLaunchException(String message, Throwable t) {
            super(message, t);
        }
    }

    public static class JettyConfig {
        /**
         * network interface (IP or hostname) to bind to
         * if null, bind to all interfaces
         */
        public final String host;

        /**
         * http port
         */
        public final int port;

        /**
         * sslPort, no ssl initizlization if sslPort is 0
         */
        public final int sslPort;

        /**
         * The time in milliseconds that the connection can be idle before it is closed.
         */
        public final int idleTimeout;

        /**
         * The time in milliseconds for the graceful shutdown
         */
        public final int stopTimeout;

        public final ThreadPool threadPool;

        public JettyConfig(String host, int port, int sslPort, int idleTimeout, int stopTimeout,
                ThreadPool threadPool) {
            this.host = host;
            this.port = port;
            this.sslPort = sslPort;
            this.idleTimeout = idleTimeout;
            this.stopTimeout = stopTimeout;
            this.threadPool = threadPool;
        }
    }



    public static class ThreadPool {
        public final int minThreads;
        public final int maxThreads;
        public final int idleTimeout;

        public ThreadPool(int minThreads, int maxThreads, int idleTimeout) {
            this.minThreads = minThreads;
            this.maxThreads = maxThreads;
            this.idleTimeout = idleTimeout;
        }
    }

}

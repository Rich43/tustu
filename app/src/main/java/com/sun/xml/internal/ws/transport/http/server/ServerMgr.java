package com.sun.xml.internal.ws.transport.http.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.server.ServerRtException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerMgr.class */
final class ServerMgr {
    private static final ServerMgr serverMgr = new ServerMgr();
    private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.http");
    private final Map<InetSocketAddress, ServerState> servers = new HashMap();

    private ServerMgr() {
    }

    static ServerMgr getInstance() {
        return serverMgr;
    }

    HttpContext createContext(String address) {
        try {
            URL url = new URL(address);
            int port = url.getPort();
            if (port == -1) {
                port = url.getDefaultPort();
            }
            InetSocketAddress inetAddress = new InetSocketAddress(url.getHost(), port);
            synchronized (this.servers) {
                ServerState state = this.servers.get(inetAddress);
                if (state == null) {
                    int finalPortNum = port;
                    Iterator<ServerState> it = this.servers.values().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ServerState s2 = it.next();
                        if (s2.getServer().getAddress().getPort() == finalPortNum) {
                            state = s2;
                            break;
                        }
                    }
                    if (!inetAddress.getAddress().isAnyLocalAddress() || state == null) {
                        logger.fine("Creating new HTTP Server at " + ((Object) inetAddress));
                        HttpServer server = HttpServer.create(inetAddress, 0);
                        server.setExecutor(Executors.newCachedThreadPool());
                        String path = url.toURI().getPath();
                        logger.fine("Creating HTTP Context at = " + path);
                        HttpContext context = server.createContext(path);
                        server.start();
                        InetSocketAddress inetAddress2 = server.getAddress();
                        logger.fine("HTTP server started = " + ((Object) inetAddress2));
                        this.servers.put(inetAddress2, new ServerState(server, path));
                        return context;
                    }
                }
                HttpServer server2 = state.getServer();
                if (state.getPaths().contains(url.getPath())) {
                    String err = "Context with URL path " + url.getPath() + " already exists on the server " + ((Object) server2.getAddress());
                    logger.fine(err);
                    throw new IllegalArgumentException(err);
                }
                logger.fine("Creating HTTP Context at = " + url.getPath());
                HttpContext context2 = server2.createContext(url.getPath());
                state.oneMoreContext(url.getPath());
                return context2;
            }
        } catch (Exception e2) {
            throw new ServerRtException("server.rt.err", e2);
        }
    }

    void removeContext(HttpContext context) {
        InetSocketAddress inetAddress = context.getServer().getAddress();
        synchronized (this.servers) {
            ServerState state = this.servers.get(inetAddress);
            int instances = state.noOfContexts();
            if (instances < 2) {
                ((ExecutorService) state.getServer().getExecutor()).shutdown();
                state.getServer().stop(0);
                this.servers.remove(inetAddress);
            } else {
                state.getServer().removeContext(context);
                state.oneLessContext(context.getPath());
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerMgr$ServerState.class */
    private static final class ServerState {
        private final HttpServer server;
        private Set<String> paths = new HashSet();
        private int instances = 1;

        ServerState(HttpServer server, String path) {
            this.server = server;
            this.paths.add(path);
        }

        public HttpServer getServer() {
            return this.server;
        }

        public void oneMoreContext(String path) {
            this.instances++;
            this.paths.add(path);
        }

        public void oneLessContext(String path) {
            this.instances--;
            this.paths.remove(path);
        }

        public int noOfContexts() {
            return this.instances;
        }

        public Set<String> getPaths() {
            return this.paths;
        }
    }
}

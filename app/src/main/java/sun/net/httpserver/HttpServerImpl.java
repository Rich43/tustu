package sun.net.httpserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/* loaded from: rt.jar:sun/net/httpserver/HttpServerImpl.class */
public class HttpServerImpl extends HttpServer {
    ServerImpl server;

    HttpServerImpl() throws IOException {
        this(new InetSocketAddress(80), 0);
    }

    HttpServerImpl(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        this.server = new ServerImpl(this, "http", inetSocketAddress, i2);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void bind(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        this.server.bind(inetSocketAddress, i2);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void start() {
        this.server.start();
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void setExecutor(Executor executor) {
        this.server.setExecutor(executor);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public Executor getExecutor() {
        return this.server.getExecutor();
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void stop(int i2) {
        this.server.stop(i2);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public HttpContextImpl createContext(String str, HttpHandler httpHandler) {
        return this.server.createContext(str, httpHandler);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public HttpContextImpl createContext(String str) {
        return this.server.createContext(str);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void removeContext(String str) throws IllegalArgumentException {
        this.server.removeContext(str);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public void removeContext(HttpContext httpContext) throws IllegalArgumentException {
        this.server.removeContext(httpContext);
    }

    @Override // com.sun.net.httpserver.HttpServer
    public InetSocketAddress getAddress() {
        return this.server.getAddress();
    }
}

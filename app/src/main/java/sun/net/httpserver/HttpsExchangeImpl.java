package sun.net.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpsExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import javax.net.ssl.SSLSession;

/* loaded from: rt.jar:sun/net/httpserver/HttpsExchangeImpl.class */
class HttpsExchangeImpl extends HttpsExchange {
    ExchangeImpl impl;

    HttpsExchangeImpl(ExchangeImpl exchangeImpl) throws IOException {
        this.impl = exchangeImpl;
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public Headers getRequestHeaders() {
        return this.impl.getRequestHeaders();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public Headers getResponseHeaders() {
        return this.impl.getResponseHeaders();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public URI getRequestURI() {
        return this.impl.getRequestURI();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public String getRequestMethod() {
        return this.impl.getRequestMethod();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public HttpContextImpl getHttpContext() {
        return this.impl.getHttpContext();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public void close() {
        this.impl.close();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public InputStream getRequestBody() {
        return this.impl.getRequestBody();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public int getResponseCode() {
        return this.impl.getResponseCode();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public OutputStream getResponseBody() {
        return this.impl.getResponseBody();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public void sendResponseHeaders(int i2, long j2) throws IOException {
        this.impl.sendResponseHeaders(i2, j2);
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public InetSocketAddress getRemoteAddress() {
        return this.impl.getRemoteAddress();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public InetSocketAddress getLocalAddress() {
        return this.impl.getLocalAddress();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public String getProtocol() {
        return this.impl.getProtocol();
    }

    @Override // com.sun.net.httpserver.HttpsExchange
    public SSLSession getSSLSession() {
        return this.impl.getSSLSession();
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public Object getAttribute(String str) {
        return this.impl.getAttribute(str);
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public void setAttribute(String str, Object obj) {
        this.impl.setAttribute(str, obj);
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public void setStreams(InputStream inputStream, OutputStream outputStream) {
        this.impl.setStreams(inputStream, outputStream);
    }

    @Override // com.sun.net.httpserver.HttpExchange
    public HttpPrincipal getPrincipal() {
        return this.impl.getPrincipal();
    }

    ExchangeImpl getExchangeImpl() {
        return this.impl;
    }
}

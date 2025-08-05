package sun.net.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/* loaded from: rt.jar:sun/net/httpserver/HttpConnection.class */
class HttpConnection {
    HttpContextImpl context;
    SSLEngine engine;
    SSLContext sslContext;
    SSLStreams sslStreams;

    /* renamed from: i, reason: collision with root package name */
    InputStream f13581i;
    InputStream raw;
    OutputStream rawout;
    SocketChannel chan;
    SelectionKey selectionKey;
    String protocol;
    long idleStartTime;
    volatile long reqStartedTime;
    volatile long rspStartedTime;
    int remaining;
    boolean closed = false;
    Logger logger;
    volatile State state;

    /* loaded from: rt.jar:sun/net/httpserver/HttpConnection$State.class */
    public enum State {
        IDLE,
        REQUEST,
        RESPONSE,
        NEWLY_ACCEPTED
    }

    public String toString() {
        String string = null;
        if (this.chan != null) {
            string = this.chan.toString();
        }
        return string;
    }

    HttpConnection() {
    }

    void setChannel(SocketChannel socketChannel) {
        this.chan = socketChannel;
    }

    void setContext(HttpContextImpl httpContextImpl) {
        this.context = httpContextImpl;
    }

    State getState() {
        return this.state;
    }

    void setState(State state) {
        this.state = state;
    }

    void setParameters(InputStream inputStream, OutputStream outputStream, SocketChannel socketChannel, SSLEngine sSLEngine, SSLStreams sSLStreams, SSLContext sSLContext, String str, HttpContextImpl httpContextImpl, InputStream inputStream2) {
        this.context = httpContextImpl;
        this.f13581i = inputStream;
        this.rawout = outputStream;
        this.raw = inputStream2;
        this.protocol = str;
        this.engine = sSLEngine;
        this.chan = socketChannel;
        this.sslContext = sSLContext;
        this.sslStreams = sSLStreams;
        this.logger = httpContextImpl.getLogger();
    }

    SocketChannel getChannel() {
        return this.chan;
    }

    synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.logger != null && this.chan != null) {
            this.logger.finest("Closing connection: " + this.chan.toString());
        }
        if (!this.chan.isOpen()) {
            ServerImpl.dprint("Channel already closed");
            return;
        }
        try {
            if (this.raw != null) {
                this.raw.close();
            }
        } catch (IOException e2) {
            ServerImpl.dprint(e2);
        }
        try {
            if (this.rawout != null) {
                this.rawout.close();
            }
        } catch (IOException e3) {
            ServerImpl.dprint(e3);
        }
        try {
            if (this.sslStreams != null) {
                this.sslStreams.close();
            }
        } catch (IOException e4) {
            ServerImpl.dprint(e4);
        }
        try {
            this.chan.close();
        } catch (IOException e5) {
            ServerImpl.dprint(e5);
        }
    }

    void setRemaining(int i2) {
        this.remaining = i2;
    }

    int getRemaining() {
        return this.remaining;
    }

    SelectionKey getSelectionKey() {
        return this.selectionKey;
    }

    InputStream getInputStream() {
        return this.f13581i;
    }

    OutputStream getRawOutputStream() {
        return this.rawout;
    }

    String getProtocol() {
        return this.protocol;
    }

    SSLEngine getSSLEngine() {
        return this.engine;
    }

    SSLContext getSSLContext() {
        return this.sslContext;
    }

    HttpContextImpl getHttpContext() {
        return this.context;
    }
}

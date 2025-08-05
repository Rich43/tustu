package com.sun.xml.internal.ws.transport.http.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/WSHttpHandler.class */
final class WSHttpHandler implements HttpHandler {
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String HEAD_METHOD = "HEAD";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";
    private static final Logger LOGGER;
    private static final boolean fineTraceEnabled;
    private final HttpAdapter adapter;
    private final Executor executor;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WSHttpHandler.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger("com.sun.xml.internal.ws.server.http");
        fineTraceEnabled = LOGGER.isLoggable(Level.FINE);
    }

    public WSHttpHandler(@NotNull HttpAdapter adapter, @Nullable Executor executor) {
        if (!$assertionsDisabled && adapter == null) {
            throw new AssertionError();
        }
        this.adapter = adapter;
        this.executor = executor;
    }

    @Override // com.sun.net.httpserver.HttpHandler
    public void handle(HttpExchange msg) {
        try {
            if (fineTraceEnabled) {
                LOGGER.log(Level.FINE, "Received HTTP request:{0}", msg.getRequestURI());
            }
            if (this.executor != null) {
                this.executor.execute(new HttpHandlerRunnable(msg));
            } else {
                handleExchange(msg);
            }
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleExchange(HttpExchange msg) throws IOException {
        WSHTTPConnection con = new ServerConnectionImpl(this.adapter, msg);
        try {
            if (fineTraceEnabled) {
                LOGGER.log(Level.FINE, "Received HTTP request:{0}", msg.getRequestURI());
            }
            String method = msg.getRequestMethod();
            if (method.equals(GET_METHOD) || method.equals(POST_METHOD) || method.equals(HEAD_METHOD) || method.equals(PUT_METHOD) || method.equals(DELETE_METHOD)) {
                this.adapter.handle(con);
            } else if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(HttpserverMessages.UNEXPECTED_HTTP_METHOD(method));
            }
        } finally {
            msg.close();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/WSHttpHandler$HttpHandlerRunnable.class */
    class HttpHandlerRunnable implements Runnable {
        final HttpExchange msg;

        HttpHandlerRunnable(HttpExchange msg) {
            this.msg = msg;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                WSHttpHandler.this.handleExchange(this.msg);
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
    }
}

package sun.net.httpserver;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import sun.net.httpserver.HttpConnection;
import sun.net.httpserver.Request;

/* loaded from: rt.jar:sun/net/httpserver/ServerImpl.class */
class ServerImpl {
    private String protocol;
    private boolean https;
    private Executor executor;
    private HttpsConfigurator httpsConfig;
    private SSLContext sslContext;
    private ContextList contexts;
    private InetSocketAddress address;
    private ServerSocketChannel schan;
    private Selector selector;
    private SelectionKey listenerKey;
    private final Set<HttpConnection> idleConnections;
    private final Set<HttpConnection> newlyAcceptedConnections;
    private final Set<HttpConnection> allConnections;
    private final Set<HttpConnection> reqConnections;
    private final Set<HttpConnection> rspConnections;
    private List<Event> events;
    private boolean bound;
    private HttpServer wrapper;
    static final long IDLE_TIMER_TASK_SCHEDULE;
    static final int MAX_CONNECTIONS;
    static final int MAX_IDLE_CONNECTIONS;
    static final long REQ_RSP_TIMER_SCHEDULE;
    static final long MAX_REQ_TIME;
    static final long MAX_RSP_TIME;
    static final boolean reqRspTimeoutEnabled;
    static final long IDLE_INTERVAL;
    static final long NEWLY_ACCEPTED_CONN_IDLE_INTERVAL;
    private Timer timer;
    private Timer timer1;
    Dispatcher dispatcher;
    static boolean debug;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Object lolock = new Object();
    private volatile boolean finished = false;
    private volatile boolean terminating = false;
    private boolean started = false;
    private int exchangeCount = 0;
    private Logger logger = Logger.getLogger("com.sun.net.httpserver");

    static {
        $assertionsDisabled = !ServerImpl.class.desiredAssertionStatus();
        IDLE_TIMER_TASK_SCHEDULE = ServerConfig.getIdleTimerScheduleMillis();
        MAX_CONNECTIONS = ServerConfig.getMaxConnections();
        MAX_IDLE_CONNECTIONS = ServerConfig.getMaxIdleConnections();
        REQ_RSP_TIMER_SCHEDULE = ServerConfig.getReqRspTimerScheduleMillis();
        MAX_REQ_TIME = getTimeMillis(ServerConfig.getMaxReqTime());
        MAX_RSP_TIME = getTimeMillis(ServerConfig.getMaxRspTime());
        reqRspTimeoutEnabled = (MAX_REQ_TIME == -1 && MAX_RSP_TIME == -1) ? false : true;
        IDLE_INTERVAL = ServerConfig.getIdleIntervalMillis();
        NEWLY_ACCEPTED_CONN_IDLE_INTERVAL = MAX_REQ_TIME > 0 ? Math.min(IDLE_INTERVAL, MAX_REQ_TIME) : IDLE_INTERVAL;
        debug = ServerConfig.debugEnabled();
    }

    ServerImpl(HttpServer httpServer, String str, InetSocketAddress inetSocketAddress, int i2) throws IOException {
        this.bound = false;
        this.protocol = str;
        this.wrapper = httpServer;
        ServerConfig.checkLegacyProperties(this.logger);
        this.https = str.equalsIgnoreCase("https");
        this.address = inetSocketAddress;
        this.contexts = new ContextList();
        this.schan = ServerSocketChannel.open();
        if (inetSocketAddress != null) {
            this.schan.socket().bind(inetSocketAddress, i2);
            this.bound = true;
        }
        this.selector = Selector.open();
        this.schan.configureBlocking(false);
        this.listenerKey = this.schan.register(this.selector, 16);
        this.dispatcher = new Dispatcher();
        this.idleConnections = Collections.synchronizedSet(new HashSet());
        this.allConnections = Collections.synchronizedSet(new HashSet());
        this.reqConnections = Collections.synchronizedSet(new HashSet());
        this.rspConnections = Collections.synchronizedSet(new HashSet());
        this.newlyAcceptedConnections = Collections.synchronizedSet(new HashSet());
        this.timer = new Timer("idle-timeout-task", true);
        this.timer.schedule(new IdleTimeoutTask(), IDLE_TIMER_TASK_SCHEDULE, IDLE_TIMER_TASK_SCHEDULE);
        if (reqRspTimeoutEnabled) {
            this.timer1 = new Timer("req-rsp-timeout-task", true);
            this.timer1.schedule(new ReqRspTimeoutTask(), REQ_RSP_TIMER_SCHEDULE, REQ_RSP_TIMER_SCHEDULE);
            this.logger.config("HttpServer request/response timeout task schedule ms: " + REQ_RSP_TIMER_SCHEDULE);
            this.logger.config("MAX_REQ_TIME:  " + MAX_REQ_TIME);
            this.logger.config("MAX_RSP_TIME:  " + MAX_RSP_TIME);
        }
        this.events = new LinkedList();
        this.logger.config("HttpServer created " + str + " " + ((Object) inetSocketAddress));
    }

    public void bind(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        if (this.bound) {
            throw new BindException("HttpServer already bound");
        }
        if (inetSocketAddress == null) {
            throw new NullPointerException("null address");
        }
        this.schan.socket().bind(inetSocketAddress, i2);
        this.bound = true;
    }

    public void start() {
        if (!this.bound || this.started || this.finished) {
            throw new IllegalStateException("server in wrong state");
        }
        if (this.executor == null) {
            this.executor = new DefaultExecutor();
        }
        Thread thread = new Thread(this.dispatcher);
        this.started = true;
        thread.start();
    }

    public void setExecutor(Executor executor) {
        if (this.started) {
            throw new IllegalStateException("server already started");
        }
        this.executor = executor;
    }

    /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$DefaultExecutor.class */
    private static class DefaultExecutor implements Executor {
        private DefaultExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public void setHttpsConfigurator(HttpsConfigurator httpsConfigurator) {
        if (httpsConfigurator == null) {
            throw new NullPointerException("null HttpsConfigurator");
        }
        if (this.started) {
            throw new IllegalStateException("server already started");
        }
        this.httpsConfig = httpsConfigurator;
        this.sslContext = httpsConfigurator.getSSLContext();
    }

    public HttpsConfigurator getHttpsConfigurator() {
        return this.httpsConfig;
    }

    public void stop(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("negative delay parameter");
        }
        this.terminating = true;
        try {
            this.schan.close();
        } catch (IOException e2) {
        }
        this.selector.wakeup();
        long jCurrentTimeMillis = System.currentTimeMillis() + (i2 * 1000);
        while (System.currentTimeMillis() < jCurrentTimeMillis) {
            delay();
            if (this.finished) {
                break;
            }
        }
        this.finished = true;
        this.selector.wakeup();
        synchronized (this.allConnections) {
            Iterator<HttpConnection> it = this.allConnections.iterator();
            while (it.hasNext()) {
                it.next().close();
            }
        }
        this.allConnections.clear();
        this.idleConnections.clear();
        this.newlyAcceptedConnections.clear();
        this.timer.cancel();
        if (reqRspTimeoutEnabled) {
            this.timer1.cancel();
        }
    }

    public synchronized HttpContextImpl createContext(String str, HttpHandler httpHandler) {
        if (httpHandler == null || str == null) {
            throw new NullPointerException("null handler, or path parameter");
        }
        HttpContextImpl httpContextImpl = new HttpContextImpl(this.protocol, str, httpHandler, this);
        this.contexts.add(httpContextImpl);
        this.logger.config("context created: " + str);
        return httpContextImpl;
    }

    public synchronized HttpContextImpl createContext(String str) {
        if (str == null) {
            throw new NullPointerException("null path parameter");
        }
        HttpContextImpl httpContextImpl = new HttpContextImpl(this.protocol, str, null, this);
        this.contexts.add(httpContextImpl);
        this.logger.config("context created: " + str);
        return httpContextImpl;
    }

    public synchronized void removeContext(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new NullPointerException("null path parameter");
        }
        this.contexts.remove(this.protocol, str);
        this.logger.config("context removed: " + str);
    }

    public synchronized void removeContext(HttpContext httpContext) throws IllegalArgumentException {
        if (!(httpContext instanceof HttpContextImpl)) {
            throw new IllegalArgumentException("wrong HttpContext type");
        }
        this.contexts.remove((HttpContextImpl) httpContext);
        this.logger.config("context removed: " + httpContext.getPath());
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) AccessController.doPrivileged(new PrivilegedAction<InetSocketAddress>() { // from class: sun.net.httpserver.ServerImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InetSocketAddress run2() {
                return (InetSocketAddress) ServerImpl.this.schan.socket().getLocalSocketAddress();
            }
        });
    }

    void addEvent(Event event) {
        synchronized (this.lolock) {
            this.events.add(event);
            this.selector.wakeup();
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$Dispatcher.class */
    class Dispatcher implements Runnable {
        final LinkedList<HttpConnection> connsToRegister = new LinkedList<>();
        static final /* synthetic */ boolean $assertionsDisabled;

        Dispatcher() {
        }

        static {
            $assertionsDisabled = !ServerImpl.class.desiredAssertionStatus();
        }

        private void handleEvent(Event event) {
            ExchangeImpl exchangeImpl = event.exchange;
            HttpConnection connection = exchangeImpl.getConnection();
            try {
                if (event instanceof WriteFinishedEvent) {
                    int iEndExchange = ServerImpl.this.endExchange();
                    if (ServerImpl.this.terminating && iEndExchange == 0) {
                        ServerImpl.this.finished = true;
                    }
                    ServerImpl.this.responseCompleted(connection);
                    LeftOverInputStream originalInputStream = exchangeImpl.getOriginalInputStream();
                    if (!originalInputStream.isEOF()) {
                        exchangeImpl.close = true;
                    }
                    if (exchangeImpl.close || ServerImpl.this.idleConnections.size() >= ServerImpl.MAX_IDLE_CONNECTIONS) {
                        connection.close();
                        ServerImpl.this.allConnections.remove(connection);
                    } else if (originalInputStream.isDataBuffered()) {
                        ServerImpl.this.requestStarted(connection);
                        handle(connection.getChannel(), connection);
                    } else {
                        this.connsToRegister.add(connection);
                    }
                }
            } catch (IOException e2) {
                ServerImpl.this.logger.log(Level.FINER, "Dispatcher (1)", (Throwable) e2);
                connection.close();
            }
        }

        void reRegister(HttpConnection httpConnection) {
            try {
                SocketChannel channel = httpConnection.getChannel();
                channel.configureBlocking(false);
                SelectionKey selectionKeyRegister = channel.register(ServerImpl.this.selector, 1);
                selectionKeyRegister.attach(httpConnection);
                httpConnection.selectionKey = selectionKeyRegister;
                ServerImpl.this.markIdle(httpConnection);
            } catch (IOException e2) {
                ServerImpl.dprint(e2);
                ServerImpl.this.logger.log(Level.FINER, "Dispatcher(8)", (Throwable) e2);
                httpConnection.close();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!ServerImpl.this.finished) {
                try {
                    List list = null;
                    synchronized (ServerImpl.this.lolock) {
                        if (ServerImpl.this.events.size() > 0) {
                            list = ServerImpl.this.events;
                            ServerImpl.this.events = new LinkedList();
                        }
                    }
                    if (list != null) {
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            handleEvent((Event) it.next());
                        }
                    }
                    Iterator<HttpConnection> it2 = this.connsToRegister.iterator();
                    while (it2.hasNext()) {
                        reRegister(it2.next());
                    }
                    this.connsToRegister.clear();
                    ServerImpl.this.selector.select(1000L);
                    Iterator<SelectionKey> it3 = ServerImpl.this.selector.selectedKeys().iterator();
                    while (it3.hasNext()) {
                        SelectionKey next = it3.next();
                        it3.remove();
                        if (next.equals(ServerImpl.this.listenerKey)) {
                            if (!ServerImpl.this.terminating) {
                                SocketChannel socketChannelAccept = ServerImpl.this.schan.accept();
                                if (socketChannelAccept != null) {
                                    if (ServerImpl.MAX_CONNECTIONS > 0 && ServerImpl.this.allConnections.size() >= ServerImpl.MAX_CONNECTIONS) {
                                        try {
                                            socketChannelAccept.close();
                                        } catch (IOException e2) {
                                        }
                                    } else {
                                        if (ServerConfig.noDelay()) {
                                            socketChannelAccept.socket().setTcpNoDelay(true);
                                        }
                                        socketChannelAccept.configureBlocking(false);
                                        SelectionKey selectionKeyRegister = socketChannelAccept.register(ServerImpl.this.selector, 1);
                                        HttpConnection httpConnection = new HttpConnection();
                                        httpConnection.selectionKey = selectionKeyRegister;
                                        httpConnection.setChannel(socketChannelAccept);
                                        selectionKeyRegister.attach(httpConnection);
                                        ServerImpl.this.markNewlyAccepted(httpConnection);
                                        ServerImpl.this.allConnections.add(httpConnection);
                                    }
                                }
                            }
                        } else {
                            try {
                                if (next.isReadable()) {
                                    SocketChannel socketChannel = (SocketChannel) next.channel();
                                    HttpConnection httpConnection2 = (HttpConnection) next.attachment();
                                    next.cancel();
                                    socketChannel.configureBlocking(true);
                                    if (ServerImpl.this.newlyAcceptedConnections.remove(httpConnection2) || ServerImpl.this.idleConnections.remove(httpConnection2)) {
                                        ServerImpl.this.requestStarted(httpConnection2);
                                    }
                                    handle(socketChannel, httpConnection2);
                                } else if (!$assertionsDisabled) {
                                    throw new AssertionError();
                                }
                            } catch (IOException e3) {
                                handleException(next, e3);
                            } catch (CancelledKeyException e4) {
                                handleException(next, null);
                            }
                        }
                    }
                    ServerImpl.this.selector.selectNow();
                } catch (IOException e5) {
                    ServerImpl.this.logger.log(Level.FINER, "Dispatcher (4)", (Throwable) e5);
                } catch (Exception e6) {
                    ServerImpl.this.logger.log(Level.FINER, "Dispatcher (7)", (Throwable) e6);
                }
            }
            try {
                ServerImpl.this.selector.close();
            } catch (Exception e7) {
            }
        }

        private void handleException(SelectionKey selectionKey, Exception exc) {
            HttpConnection httpConnection = (HttpConnection) selectionKey.attachment();
            if (exc != null) {
                ServerImpl.this.logger.log(Level.FINER, "Dispatcher (2)", (Throwable) exc);
            }
            ServerImpl.this.closeConnection(httpConnection);
        }

        public void handle(SocketChannel socketChannel, HttpConnection httpConnection) throws IOException {
            try {
                ServerImpl.this.executor.execute(ServerImpl.this.new Exchange(socketChannel, ServerImpl.this.protocol, httpConnection));
            } catch (IOException e2) {
                ServerImpl.this.logger.log(Level.FINER, "Dispatcher (5)", (Throwable) e2);
                ServerImpl.this.closeConnection(httpConnection);
            } catch (HttpError e3) {
                ServerImpl.this.logger.log(Level.FINER, "Dispatcher (4)", (Throwable) e3);
                ServerImpl.this.closeConnection(httpConnection);
            }
        }
    }

    static synchronized void dprint(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    static synchronized void dprint(Exception exc) {
        if (debug) {
            System.out.println(exc);
            exc.printStackTrace();
        }
    }

    Logger getLogger() {
        return this.logger;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeConnection(HttpConnection httpConnection) {
        httpConnection.close();
        this.allConnections.remove(httpConnection);
        switch (httpConnection.getState()) {
            case REQUEST:
                this.reqConnections.remove(httpConnection);
                break;
            case RESPONSE:
                this.rspConnections.remove(httpConnection);
                break;
            case IDLE:
                this.idleConnections.remove(httpConnection);
                break;
            case NEWLY_ACCEPTED:
                this.newlyAcceptedConnections.remove(httpConnection);
                break;
        }
        if (!$assertionsDisabled && this.reqConnections.remove(httpConnection)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.rspConnections.remove(httpConnection)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.idleConnections.remove(httpConnection)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.newlyAcceptedConnections.remove(httpConnection)) {
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$Exchange.class */
    class Exchange implements Runnable {
        SocketChannel chan;
        HttpConnection connection;
        HttpContextImpl context;
        InputStream rawin;
        OutputStream rawout;
        String protocol;
        ExchangeImpl tx;
        HttpContextImpl ctx;
        boolean rejected = false;

        Exchange(SocketChannel socketChannel, String str, HttpConnection httpConnection) throws IOException {
            this.chan = socketChannel;
            this.connection = httpConnection;
            this.protocol = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z2;
            this.context = this.connection.getHttpContext();
            SSLEngine sSLEngine = null;
            SSLStreams sSLStreams = null;
            try {
                if (this.context != null) {
                    this.rawin = this.connection.getInputStream();
                    this.rawout = this.connection.getRawOutputStream();
                    z2 = false;
                } else {
                    z2 = true;
                    if (!ServerImpl.this.https) {
                        this.rawin = new BufferedInputStream(new Request.ReadStream(ServerImpl.this, this.chan));
                        this.rawout = new Request.WriteStream(ServerImpl.this, this.chan);
                    } else {
                        if (ServerImpl.this.sslContext == null) {
                            ServerImpl.this.logger.warning("SSL connection received. No https contxt created");
                            throw new HttpError("No SSL context established");
                        }
                        sSLStreams = new SSLStreams(ServerImpl.this, ServerImpl.this.sslContext, this.chan);
                        this.rawin = sSLStreams.getInputStream();
                        this.rawout = sSLStreams.getOutputStream();
                        sSLEngine = sSLStreams.getSSLEngine();
                        this.connection.sslStreams = sSLStreams;
                    }
                    this.connection.raw = this.rawin;
                    this.connection.rawout = this.rawout;
                }
                Request request = new Request(this.rawin, this.rawout);
                String strRequestLine = request.requestLine();
                if (strRequestLine == null) {
                    ServerImpl.this.closeConnection(this.connection);
                    return;
                }
                int iIndexOf = strRequestLine.indexOf(32);
                if (iIndexOf == -1) {
                    reject(400, strRequestLine, "Bad request line");
                    return;
                }
                String strSubstring = strRequestLine.substring(0, iIndexOf);
                int i2 = iIndexOf + 1;
                int iIndexOf2 = strRequestLine.indexOf(32, i2);
                if (iIndexOf2 == -1) {
                    reject(400, strRequestLine, "Bad request line");
                    return;
                }
                URI uri = new URI(strRequestLine.substring(i2, iIndexOf2));
                String strSubstring2 = strRequestLine.substring(iIndexOf2 + 1);
                Headers headers = request.headers();
                Iterator<String> it = headers.keySet().iterator();
                while (it.hasNext()) {
                    if (!ServerImpl.isValidHeaderKey(it.next())) {
                        reject(400, strRequestLine, "Header key contains illegal characters");
                        return;
                    }
                }
                if (headers.containsKey("Content-Length") && (headers.containsKey("Transfer-encoding") || headers.get((Object) "Content-Length").size() > 1)) {
                    reject(400, strRequestLine, "Conflicting or malformed headers detected");
                    return;
                }
                long j2 = 0;
                String str = null;
                List<String> list = headers.get((Object) "Transfer-encoding");
                if (list != null && !list.isEmpty()) {
                    str = list.get(0);
                }
                if (str == null) {
                    String first = headers.getFirst("Content-Length");
                    if (first != null) {
                        j2 = Long.parseLong(first);
                        if (j2 < 0) {
                            reject(400, strRequestLine, "Illegal Content-Length value");
                            return;
                        }
                    }
                    if (j2 == 0) {
                        ServerImpl.this.requestCompleted(this.connection);
                    }
                } else {
                    if (!str.equalsIgnoreCase("chunked") || list.size() != 1) {
                        reject(501, strRequestLine, "Unsupported Transfer-Encoding value");
                        return;
                    }
                    j2 = -1;
                }
                this.ctx = ServerImpl.this.contexts.findContext(this.protocol, uri.getPath());
                if (this.ctx == null) {
                    reject(404, strRequestLine, "No context found for request");
                    return;
                }
                this.connection.setContext(this.ctx);
                if (this.ctx.getHandler() == null) {
                    reject(500, strRequestLine, "No handler for context");
                    return;
                }
                this.tx = new ExchangeImpl(strSubstring, uri, request, j2, this.connection);
                String first2 = headers.getFirst("Connection");
                Headers responseHeaders = this.tx.getResponseHeaders();
                if (first2 != null && first2.equalsIgnoreCase("close")) {
                    this.tx.close = true;
                }
                if (strSubstring2.equalsIgnoreCase("http/1.0")) {
                    this.tx.http10 = true;
                    if (first2 == null) {
                        this.tx.close = true;
                        responseHeaders.set("Connection", "close");
                    } else if (first2.equalsIgnoreCase("keep-alive")) {
                        responseHeaders.set("Connection", "keep-alive");
                        responseHeaders.set("Keep-Alive", "timeout=" + ((int) (ServerConfig.getIdleIntervalMillis() / 1000)) + ", max=" + ServerConfig.getMaxIdleConnections());
                    }
                }
                if (z2) {
                    this.connection.setParameters(this.rawin, this.rawout, this.chan, sSLEngine, sSLStreams, ServerImpl.this.sslContext, this.protocol, this.ctx, this.rawin);
                }
                String first3 = headers.getFirst("Expect");
                if (first3 != null && first3.equalsIgnoreCase("100-continue")) {
                    ServerImpl.this.logReply(100, strRequestLine, null);
                    sendReply(100, false, null);
                }
                Filter.Chain chain = new Filter.Chain(this.ctx.getFilters(), new LinkHandler(new Filter.Chain(this.ctx.getSystemFilters(), this.ctx.getHandler())));
                this.tx.getRequestBody();
                this.tx.getResponseBody();
                if (ServerImpl.this.https) {
                    chain.doFilter(new HttpsExchangeImpl(this.tx));
                } else {
                    chain.doFilter(new HttpExchangeImpl(this.tx));
                }
            } catch (IOException e2) {
                ServerImpl.this.logger.log(Level.FINER, "ServerImpl.Exchange (1)", (Throwable) e2);
                ServerImpl.this.closeConnection(this.connection);
            } catch (NumberFormatException e3) {
                reject(400, null, "NumberFormatException thrown");
            } catch (URISyntaxException e4) {
                reject(400, null, "URISyntaxException thrown");
            } catch (Exception e5) {
                ServerImpl.this.logger.log(Level.FINER, "ServerImpl.Exchange (2)", (Throwable) e5);
                ServerImpl.this.closeConnection(this.connection);
            }
        }

        /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$Exchange$LinkHandler.class */
        class LinkHandler implements HttpHandler {
            Filter.Chain nextChain;

            LinkHandler(Filter.Chain chain) {
                this.nextChain = chain;
            }

            @Override // com.sun.net.httpserver.HttpHandler
            public void handle(HttpExchange httpExchange) throws IOException {
                this.nextChain.doFilter(httpExchange);
            }
        }

        void reject(int i2, String str, String str2) {
            this.rejected = true;
            ServerImpl.this.logReply(i2, str, str2);
            sendReply(i2, false, "<h1>" + i2 + Code.msg(i2) + "</h1>" + str2);
            ServerImpl.this.closeConnection(this.connection);
        }

        void sendReply(int i2, boolean z2, String str) {
            try {
                StringBuilder sb = new StringBuilder(512);
                sb.append("HTTP/1.1 ").append(i2).append(Code.msg(i2)).append("\r\n");
                if (str != null && str.length() != 0) {
                    sb.append("Content-Length: ").append(str.length()).append("\r\n").append("Content-Type: text/html\r\n");
                } else {
                    sb.append("Content-Length: 0\r\n");
                    str = "";
                }
                if (z2) {
                    sb.append("Connection: close\r\n");
                }
                sb.append("\r\n").append(str);
                this.rawout.write(sb.toString().getBytes("ISO8859_1"));
                this.rawout.flush();
                if (z2) {
                    ServerImpl.this.closeConnection(this.connection);
                }
            } catch (IOException e2) {
                ServerImpl.this.logger.log(Level.FINER, "ServerImpl.sendReply", (Throwable) e2);
                ServerImpl.this.closeConnection(this.connection);
            }
        }
    }

    void logReply(int i2, String str, String str2) {
        String str3;
        if (!this.logger.isLoggable(Level.FINE)) {
            return;
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str.length() > 80) {
            str3 = str.substring(0, 80) + "<TRUNCATED>";
        } else {
            str3 = str;
        }
        this.logger.fine(str3 + " [" + i2 + " " + Code.msg(i2) + "] (" + str2 + ")");
    }

    void delay() {
        Thread.yield();
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e2) {
        }
    }

    synchronized void startExchange() {
        this.exchangeCount++;
    }

    synchronized int endExchange() {
        this.exchangeCount--;
        if ($assertionsDisabled || this.exchangeCount >= 0) {
            return this.exchangeCount;
        }
        throw new AssertionError();
    }

    HttpServer getWrapper() {
        return this.wrapper;
    }

    void requestStarted(HttpConnection httpConnection) {
        httpConnection.reqStartedTime = System.currentTimeMillis();
        httpConnection.setState(HttpConnection.State.REQUEST);
        this.reqConnections.add(httpConnection);
    }

    void markIdle(HttpConnection httpConnection) {
        httpConnection.idleStartTime = System.currentTimeMillis();
        httpConnection.setState(HttpConnection.State.IDLE);
        this.idleConnections.add(httpConnection);
    }

    void markNewlyAccepted(HttpConnection httpConnection) {
        httpConnection.idleStartTime = System.currentTimeMillis();
        httpConnection.setState(HttpConnection.State.NEWLY_ACCEPTED);
        this.newlyAcceptedConnections.add(httpConnection);
    }

    void requestCompleted(HttpConnection httpConnection) {
        if (!$assertionsDisabled && httpConnection.getState() != HttpConnection.State.REQUEST) {
            throw new AssertionError();
        }
        this.reqConnections.remove(httpConnection);
        httpConnection.rspStartedTime = System.currentTimeMillis();
        this.rspConnections.add(httpConnection);
        httpConnection.setState(HttpConnection.State.RESPONSE);
    }

    void responseCompleted(HttpConnection httpConnection) {
        if (!$assertionsDisabled && httpConnection.getState() != HttpConnection.State.RESPONSE) {
            throw new AssertionError();
        }
        this.rspConnections.remove(httpConnection);
        httpConnection.setState(HttpConnection.State.IDLE);
    }

    /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$IdleTimeoutTask.class */
    class IdleTimeoutTask extends TimerTask {
        IdleTimeoutTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            LinkedList linkedList = new LinkedList();
            long jCurrentTimeMillis = System.currentTimeMillis();
            synchronized (ServerImpl.this.idleConnections) {
                Iterator it = ServerImpl.this.idleConnections.iterator();
                while (it.hasNext()) {
                    HttpConnection httpConnection = (HttpConnection) it.next();
                    if (jCurrentTimeMillis - httpConnection.idleStartTime >= ServerImpl.IDLE_INTERVAL) {
                        linkedList.add(httpConnection);
                        it.remove();
                    }
                }
            }
            synchronized (ServerImpl.this.newlyAcceptedConnections) {
                Iterator it2 = ServerImpl.this.newlyAcceptedConnections.iterator();
                while (it2.hasNext()) {
                    HttpConnection httpConnection2 = (HttpConnection) it2.next();
                    if (jCurrentTimeMillis - httpConnection2.idleStartTime >= ServerImpl.NEWLY_ACCEPTED_CONN_IDLE_INTERVAL) {
                        linkedList.add(httpConnection2);
                        it2.remove();
                    }
                }
            }
            Iterator<E> it3 = linkedList.iterator();
            while (it3.hasNext()) {
                HttpConnection httpConnection3 = (HttpConnection) it3.next();
                ServerImpl.this.allConnections.remove(httpConnection3);
                httpConnection3.close();
                if (ServerImpl.this.logger.isLoggable(Level.FINER)) {
                    ServerImpl.this.logger.log(Level.FINER, "Closed idle connection " + ((Object) httpConnection3));
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/ServerImpl$ReqRspTimeoutTask.class */
    class ReqRspTimeoutTask extends TimerTask {
        ReqRspTimeoutTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            LinkedList linkedList = new LinkedList();
            long jCurrentTimeMillis = System.currentTimeMillis();
            synchronized (ServerImpl.this.reqConnections) {
                if (ServerImpl.MAX_REQ_TIME != -1) {
                    for (HttpConnection httpConnection : ServerImpl.this.reqConnections) {
                        if (jCurrentTimeMillis - httpConnection.reqStartedTime >= ServerImpl.MAX_REQ_TIME) {
                            linkedList.add(httpConnection);
                        }
                    }
                    Iterator<E> it = linkedList.iterator();
                    while (it.hasNext()) {
                        HttpConnection httpConnection2 = (HttpConnection) it.next();
                        ServerImpl.this.logger.log(Level.FINE, "closing: no request: " + ((Object) httpConnection2));
                        ServerImpl.this.reqConnections.remove(httpConnection2);
                        ServerImpl.this.allConnections.remove(httpConnection2);
                        httpConnection2.close();
                    }
                }
            }
            LinkedList linkedList2 = new LinkedList();
            synchronized (ServerImpl.this.rspConnections) {
                if (ServerImpl.MAX_RSP_TIME != -1) {
                    for (HttpConnection httpConnection3 : ServerImpl.this.rspConnections) {
                        if (jCurrentTimeMillis - httpConnection3.rspStartedTime >= ServerImpl.MAX_RSP_TIME) {
                            linkedList2.add(httpConnection3);
                        }
                    }
                    Iterator<E> it2 = linkedList2.iterator();
                    while (it2.hasNext()) {
                        HttpConnection httpConnection4 = (HttpConnection) it2.next();
                        ServerImpl.this.logger.log(Level.FINE, "closing: no response: " + ((Object) httpConnection4));
                        ServerImpl.this.rspConnections.remove(httpConnection4);
                        ServerImpl.this.allConnections.remove(httpConnection4);
                        httpConnection4.close();
                    }
                }
            }
        }
    }

    private static long getTimeMillis(long j2) {
        if (j2 <= 0) {
            return -1L;
        }
        long j3 = j2 * 1000;
        if (j3 > 0) {
            return j3;
        }
        return -1L;
    }

    static boolean isValidHeaderKey(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = charArray[i2];
            if (!((c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z') || (c2 >= '0' && c2 <= '9')) && "!#$%&'*+-.^_`|~".indexOf(c2) == -1) {
                return false;
            }
        }
        return true;
    }
}

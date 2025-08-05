package com.sun.webkit.network;

import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

/* loaded from: jfxrt.jar:com/sun/webkit/network/SocketStreamHandle.class */
final class SocketStreamHandle {
    private static final Pattern FIRST_LINE_PATTERN = Pattern.compile("^HTTP/1.[01]\\s+(\\d{3})(?:\\s.*)?$");
    private static final Logger logger = Logger.getLogger(SocketStreamHandle.class.getName());
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS, new SynchronousQueue(), new CustomThreadFactory());
    private final String host;
    private final int port;
    private final boolean ssl;
    private final WebPage webPage;
    private final long data;
    private volatile Socket socket;
    private volatile State state = State.ACTIVE;
    private volatile boolean connected;

    /* loaded from: jfxrt.jar:com/sun/webkit/network/SocketStreamHandle$State.class */
    private enum State {
        ACTIVE,
        CLOSE_REQUESTED,
        DISPOSED
    }

    private static native void twkDidOpen(long j2);

    private static native void twkDidReceiveData(byte[] bArr, int i2, long j2);

    private static native void twkDidFail(int i2, String str, long j2);

    private static native void twkDidClose(long j2);

    private SocketStreamHandle(String host, int port, boolean ssl, WebPage webPage, long data) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.webPage = webPage;
        this.data = data;
    }

    private static SocketStreamHandle fwkCreate(String host, int port, boolean ssl, WebPage webPage, long data) {
        SocketStreamHandle ssh = new SocketStreamHandle(host, port, ssl, webPage, data);
        logger.log(Level.FINEST, "Starting {0}", ssh);
        threadPool.submit(() -> {
            ssh.run();
        });
        return ssh;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void run() {
        if (this.webPage == null) {
            logger.log(Level.FINEST, "{0} is not associated with any web page, aborted", this);
            didFail(0, "Web socket is not associated with any web page");
            didClose();
            return;
        }
        AccessController.doPrivileged(() -> {
            doRun();
            return null;
        }, this.webPage.getAccessControlContext());
    }

    private void doRun() {
        Throwable error = null;
        String errorDescription = null;
        try {
            logger.log(Level.FINEST, "{0} started", this);
            connect();
            this.connected = true;
            logger.log(Level.FINEST, "{0} connected", this);
            didOpen();
            InputStream is = this.socket.getInputStream();
            while (true) {
                byte[] buffer = new byte[8192];
                int n2 = is.read(buffer);
                if (n2 <= 0) {
                    break;
                }
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s received len: [%d], data:%s", this, Integer.valueOf(n2), dump(buffer, n2)));
                }
                didReceiveData(buffer, n2);
            }
            logger.log(Level.FINEST, "{0} connection closed by remote host", this);
        } catch (SecurityException ex) {
            error = ex;
            errorDescription = "Security error";
        } catch (ConnectException ex2) {
            error = ex2;
            errorDescription = "Unable to connect";
        } catch (NoRouteToHostException ex3) {
            error = ex3;
            errorDescription = "No route to host";
        } catch (PortUnreachableException ex4) {
            error = ex4;
            errorDescription = "Port unreachable";
        } catch (SocketException ex5) {
            if (this.state == State.ACTIVE) {
                error = ex5;
                errorDescription = "Socket error";
            } else if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, String.format("%s exception (most likely caused by local close)", this), (Throwable) ex5);
            }
        } catch (UnknownHostException ex6) {
            error = ex6;
            errorDescription = "Unknown host";
        } catch (SSLException ex7) {
            error = ex7;
            errorDescription = "SSL error";
        } catch (IOException ex8) {
            error = ex8;
            errorDescription = "I/O error";
        } catch (Throwable th) {
            error = th;
        }
        if (error != null) {
            if (errorDescription == null) {
                errorDescription = "Unknown error";
                logger.log(Level.WARNING, String.format("%s unexpected error", this), error);
            } else {
                logger.log(Level.FINEST, String.format("%s exception", this), error);
            }
            didFail(0, errorDescription);
        }
        try {
            this.socket.close();
        } catch (IOException e2) {
        }
        didClose();
        logger.log(Level.FINEST, "{0} finished", this);
    }

    private void connect() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(this.host, this.port);
        }
        boolean success = false;
        IOException lastException = null;
        boolean triedDirectConnection = false;
        ProxySelector proxySelector = (ProxySelector) AccessController.doPrivileged(() -> {
            return ProxySelector.getDefault();
        });
        if (proxySelector != null) {
            try {
                URI uri = new URI((this.ssl ? "https" : "http") + "://" + this.host);
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s selecting proxies for: [%s]", this, uri));
                }
                List<Proxy> proxies = proxySelector.select(uri);
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, String.format("%s selected proxies: %s", this, proxies));
                }
                for (Proxy proxy : proxies) {
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.log(Level.FINEST, String.format("%s trying proxy: [%s]", this, proxy));
                    }
                    if (proxy.type() == Proxy.Type.DIRECT) {
                        triedDirectConnection = true;
                    }
                    try {
                        connect(proxy);
                        success = true;
                        break;
                    } catch (IOException ex) {
                        logger.log(Level.FINEST, String.format("%s exception", this), (Throwable) ex);
                        lastException = ex;
                        if (proxy.address() != null) {
                            proxySelector.connectFailed(uri, proxy.address(), ex);
                        }
                    }
                }
            } catch (URISyntaxException ex2) {
                throw new IOException(ex2);
            }
        }
        if (!success && !triedDirectConnection) {
            logger.log(Level.FINEST, "{0} trying direct connection", this);
            connect(Proxy.NO_PROXY);
            success = true;
        }
        if (!success) {
            throw lastException;
        }
    }

    private void connect(Proxy proxy) throws IOException {
        synchronized (this) {
            if (this.state != State.ACTIVE) {
                throw new SocketException("Close requested");
            }
            this.socket = new Socket(proxy);
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s connecting to: [%s:%d]", this, this.host, Integer.valueOf(this.port)));
        }
        this.socket.connect(new InetSocketAddress(this.host, this.port));
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s connected to: [%s:%d]", this, this.host, Integer.valueOf(this.port)));
        }
        if (this.ssl) {
            synchronized (this) {
                if (this.state != State.ACTIVE) {
                    throw new SocketException("Close requested");
                }
                logger.log(Level.FINEST, "{0} starting SSL handshake", this);
                this.socket = HttpsURLConnection.getDefaultSSLSocketFactory().createSocket(this.socket, this.host, this.port, true);
            }
            ((SSLSocket) this.socket).startHandshake();
        }
    }

    private int fwkSend(byte[] buffer) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s sending len: [%d], data:%s", this, Integer.valueOf(buffer.length), dump(buffer, buffer.length)));
        }
        if (this.connected) {
            try {
                this.socket.getOutputStream().write(buffer);
                return buffer.length;
            } catch (IOException ex) {
                logger.log(Level.FINEST, String.format("%s exception", this), (Throwable) ex);
                didFail(0, "I/O error");
                return 0;
            }
        }
        logger.log(Level.FINEST, "{0} not connected", this);
        didFail(0, "Not connected");
        return 0;
    }

    private void fwkClose() {
        synchronized (this) {
            logger.log(Level.FINEST, "{0}", this);
            this.state = State.CLOSE_REQUESTED;
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e2) {
            }
        }
    }

    private void fwkNotifyDisposed() {
        logger.log(Level.FINEST, "{0}", this);
        this.state = State.DISPOSED;
    }

    private void didOpen() {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                notifyDidOpen();
            }
        });
    }

    private void didReceiveData(byte[] buffer, int len) {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                notifyDidReceiveData(buffer, len);
            }
        });
    }

    private void didFail(int errorCode, String errorDescription) {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state == State.ACTIVE) {
                notifyDidFail(errorCode, errorDescription);
            }
        });
    }

    private void didClose() {
        Invoker.getInvoker().postOnEventThread(() -> {
            if (this.state != State.DISPOSED) {
                notifyDidClose();
            }
        });
    }

    private void notifyDidOpen() {
        logger.log(Level.FINEST, "{0}", this);
        twkDidOpen(this.data);
    }

    private void notifyDidReceiveData(byte[] buffer, int len) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s, len: [%d], data:%s", this, Integer.valueOf(len), dump(buffer, len)));
        }
        twkDidReceiveData(buffer, len, this.data);
    }

    private void notifyDidFail(int errorCode, String errorDescription) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("%s, errorCode: %d, errorDescription: %s", this, Integer.valueOf(errorCode), errorDescription));
        }
        twkDidFail(errorCode, errorDescription, this.data);
    }

    private void notifyDidClose() {
        logger.log(Level.FINEST, "{0}", this);
        twkDidClose(this.data);
    }

    private static String dump(byte[] buffer, int len) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (i2 < len) {
            StringBuilder c1 = new StringBuilder();
            StringBuilder c2 = new StringBuilder();
            int k2 = 0;
            while (k2 < 16) {
                if (i2 < len) {
                    int b2 = buffer[i2] & 255;
                    c1.append(String.format("%02x ", Integer.valueOf(b2)));
                    c2.append((b2 < 32 || b2 > 126) ? '.' : (char) b2);
                } else {
                    c1.append("   ");
                }
                k2++;
                i2++;
            }
            sb.append(String.format("%n  ", new Object[0])).append((CharSequence) c1).append(' ').append((CharSequence) c2);
        }
        return sb.toString();
    }

    public String toString() {
        return String.format("SocketStreamHandle{host=%s, port=%d, ssl=%s, data=0x%016X, state=%s, connected=%s}", this.host, Integer.valueOf(this.port), Boolean.valueOf(this.ssl), Long.valueOf(this.data), this.state, Boolean.valueOf(this.connected));
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/SocketStreamHandle$CustomThreadFactory.class */
    private static final class CustomThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index;

        private CustomThreadFactory() {
            this.index = new AtomicInteger(1);
            SecurityManager sm = System.getSecurityManager();
            this.group = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r2) {
            Thread t2 = new Thread(this.group, r2, "SocketStreamHandle-" + this.index.getAndIncrement());
            t2.setDaemon(true);
            if (t2.getPriority() != 5) {
                t2.setPriority(5);
            }
            return t2;
        }
    }
}

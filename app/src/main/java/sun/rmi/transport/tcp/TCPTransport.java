package sun.rmi.transport.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.LogStream;
import java.rmi.server.RMIFailureHandler;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UID;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.java2d.marlin.MarlinConst;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.NewThreadAction;
import sun.rmi.transport.Connection;
import sun.rmi.transport.DGCAckHandler;
import sun.rmi.transport.Endpoint;
import sun.rmi.transport.StreamRemoteCall;
import sun.rmi.transport.Target;
import sun.rmi.transport.Transport;
import sun.rmi.transport.proxy.HttpReceiveSocket;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetLongAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/tcp/TCPTransport.class */
public class TCPTransport extends Transport {
    static final Log tcpLog;
    private static final int maxConnectionThreads;
    private static final long threadKeepAliveTime;
    private static final ExecutorService connectionThreadPool;
    private static final boolean disableIncomingHttp;
    private static final AtomicInteger connectionCount;
    private static final ThreadLocal<ConnectionHandler> threadConnectionHandler;
    private static final AccessControlContext NOPERMS_ACC;
    private final LinkedList<TCPEndpoint> epList;
    private int exportCount = 0;
    private ServerSocket server = null;
    private final Map<TCPEndpoint, Reference<TCPChannel>> channelTable = new WeakHashMap();
    static final RMISocketFactory defaultSocketFactory;
    private static final int connectionReadTimeout;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TCPTransport.class.desiredAssertionStatus();
        tcpLog = Log.getLog("sun.rmi.transport.tcp", "tcp", LogStream.parseLevel((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.tcp.logLevel"))));
        maxConnectionThreads = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.maxConnectionThreads", Integer.MAX_VALUE))).intValue();
        threadKeepAliveTime = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.tcp.threadKeepAliveTime", 60000L))).longValue();
        connectionThreadPool = new ThreadPoolExecutor(0, maxConnectionThreads, threadKeepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue(), new ThreadFactory() { // from class: sun.rmi.transport.tcp.TCPTransport.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return (Thread) AccessController.doPrivileged(new NewThreadAction(runnable, "TCP Connection(idle)", true, true));
            }
        });
        disableIncomingHttp = ((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.server.disableIncomingHttp", "true"))).equalsIgnoreCase("true");
        connectionCount = new AtomicInteger(0);
        threadConnectionHandler = new ThreadLocal<>();
        NOPERMS_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, new Permissions())});
        defaultSocketFactory = RMISocketFactory.getDefaultSocketFactory();
        connectionReadTimeout = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.readTimeout", 7200000))).intValue();
    }

    TCPTransport(LinkedList<TCPEndpoint> linkedList) {
        this.epList = linkedList;
        if (tcpLog.isLoggable(Log.BRIEF)) {
            tcpLog.log(Log.BRIEF, "Version = 2, ep = " + ((Object) getEndpoint()));
        }
    }

    public void shedConnectionCaches() {
        ArrayList arrayList;
        synchronized (this.channelTable) {
            arrayList = new ArrayList(this.channelTable.values().size());
            Iterator<Reference<TCPChannel>> it = this.channelTable.values().iterator();
            while (it.hasNext()) {
                TCPChannel tCPChannel = it.next().get();
                if (tCPChannel != null) {
                    arrayList.add(tCPChannel);
                }
            }
        }
        Iterator<E> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ((TCPChannel) it2.next()).shedCache();
        }
    }

    @Override // sun.rmi.transport.Transport
    public TCPChannel getChannel(Endpoint endpoint) {
        TCPChannel tCPChannel = null;
        if (endpoint instanceof TCPEndpoint) {
            synchronized (this.channelTable) {
                Reference<TCPChannel> reference = this.channelTable.get(endpoint);
                if (reference != null) {
                    tCPChannel = reference.get();
                }
                if (tCPChannel == null) {
                    TCPEndpoint tCPEndpoint = (TCPEndpoint) endpoint;
                    tCPChannel = new TCPChannel(this, tCPEndpoint);
                    this.channelTable.put(tCPEndpoint, new WeakReference(tCPChannel));
                }
            }
        }
        return tCPChannel;
    }

    @Override // sun.rmi.transport.Transport
    public void free(Endpoint endpoint) {
        TCPChannel tCPChannel;
        if (endpoint instanceof TCPEndpoint) {
            synchronized (this.channelTable) {
                Reference<TCPChannel> referenceRemove = this.channelTable.remove(endpoint);
                if (referenceRemove != null && (tCPChannel = referenceRemove.get()) != null) {
                    tCPChannel.shedCache();
                }
            }
        }
    }

    @Override // sun.rmi.transport.Transport
    public void exportObject(Target target) throws RemoteException {
        synchronized (this) {
            listen();
            this.exportCount++;
        }
        boolean z2 = false;
        try {
            super.exportObject(target);
            z2 = true;
            if (1 == 0) {
                synchronized (this) {
                    decrementExportCount();
                }
            }
        } catch (Throwable th) {
            if (!z2) {
                synchronized (this) {
                    decrementExportCount();
                }
            }
            throw th;
        }
    }

    @Override // sun.rmi.transport.Transport
    protected synchronized void targetUnexported() {
        decrementExportCount();
    }

    private void decrementExportCount() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        this.exportCount--;
        if (this.exportCount == 0 && getEndpoint().getListenPort() != 0) {
            ServerSocket serverSocket = this.server;
            this.server = null;
            try {
                serverSocket.close();
            } catch (IOException e2) {
            }
        }
    }

    @Override // sun.rmi.transport.Transport
    protected void checkAcceptPermission(AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        ConnectionHandler connectionHandler = threadConnectionHandler.get();
        if (connectionHandler == null) {
            throw new Error("checkAcceptPermission not in ConnectionHandler thread");
        }
        connectionHandler.checkAcceptPermission(securityManager, accessControlContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TCPEndpoint getEndpoint() {
        TCPEndpoint last;
        synchronized (this.epList) {
            last = this.epList.getLast();
        }
        return last;
    }

    private void listen() throws RemoteException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        TCPEndpoint endpoint = getEndpoint();
        int port = endpoint.getPort();
        if (this.server == null) {
            if (tcpLog.isLoggable(Log.BRIEF)) {
                tcpLog.log(Log.BRIEF, "(port " + port + ") create server socket");
            }
            try {
                this.server = endpoint.newServerSocket();
                ((Thread) AccessController.doPrivileged(new NewThreadAction(new AcceptLoop(this.server), "TCP Accept-" + port, true))).start();
                return;
            } catch (BindException e2) {
                throw new ExportException("Port already in use: " + port, e2);
            } catch (IOException e3) {
                throw new ExportException("Listen failed on port: " + port, e3);
            }
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkListen(port);
        }
    }

    /* loaded from: rt.jar:sun/rmi/transport/tcp/TCPTransport$AcceptLoop.class */
    private class AcceptLoop implements Runnable {
        private final ServerSocket serverSocket;
        private long lastExceptionTime = 0;
        private int recentExceptionCount;

        AcceptLoop(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                executeAcceptLoop();
            } finally {
                try {
                    this.serverSocket.close();
                } catch (IOException e2) {
                }
            }
        }

        private void executeAcceptLoop() {
            if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                TCPTransport.tcpLog.log(Log.BRIEF, "listening on port " + TCPTransport.this.getEndpoint().getPort());
            }
            while (true) {
                Socket socketAccept = null;
                try {
                    socketAccept = this.serverSocket.accept();
                    InetAddress inetAddress = socketAccept.getInetAddress();
                    String hostAddress = inetAddress != null ? inetAddress.getHostAddress() : "0.0.0.0";
                    try {
                        TCPTransport.connectionThreadPool.execute(TCPTransport.this.new ConnectionHandler(socketAccept, hostAddress));
                    } catch (RejectedExecutionException e2) {
                        TCPTransport.closeSocket(socketAccept);
                        TCPTransport.tcpLog.log(Log.BRIEF, "rejected connection from " + hostAddress);
                    }
                } catch (Throwable th) {
                    try {
                        if (!this.serverSocket.isClosed()) {
                            try {
                                if (TCPTransport.tcpLog.isLoggable(Level.WARNING)) {
                                    TCPTransport.tcpLog.log(Level.WARNING, "accept loop for " + ((Object) this.serverSocket) + " throws", th);
                                }
                            } catch (Throwable th2) {
                            }
                            if (!(th instanceof SecurityException)) {
                                try {
                                    TCPEndpoint.shedConnectionCaches();
                                } catch (Throwable th3) {
                                }
                            }
                            if ((th instanceof Exception) || (th instanceof OutOfMemoryError) || (th instanceof NoClassDefFoundError)) {
                                if (!continueAfterAcceptFailure(th)) {
                                    return;
                                }
                            } else {
                                if (th instanceof Error) {
                                    throw ((Error) th);
                                }
                                throw new UndeclaredThrowableException(th);
                            }
                        } else {
                            if (socketAccept != null) {
                                TCPTransport.closeSocket(socketAccept);
                                return;
                            }
                            return;
                        }
                    } finally {
                        if (socketAccept != null) {
                            TCPTransport.closeSocket(socketAccept);
                        }
                    }
                }
            }
        }

        private boolean continueAfterAcceptFailure(Throwable th) {
            RMIFailureHandler failureHandler = RMISocketFactory.getFailureHandler();
            if (failureHandler != null) {
                return failureHandler.failure(th instanceof Exception ? (Exception) th : new InvocationTargetException(th));
            }
            throttleLoopOnException();
            return true;
        }

        private void throttleLoopOnException() {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (this.lastExceptionTime == 0 || jCurrentTimeMillis - this.lastExceptionTime > MarlinConst.statDump) {
                this.lastExceptionTime = jCurrentTimeMillis;
                this.recentExceptionCount = 0;
                return;
            }
            int i2 = this.recentExceptionCount + 1;
            this.recentExceptionCount = i2;
            if (i2 >= 10) {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e2) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e2) {
        }
    }

    void handleMessages(Connection connection, boolean z2) {
        int port = getEndpoint().getPort();
        try {
            try {
                DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
                while (true) {
                    int i2 = dataInputStream.read();
                    if (i2 != -1) {
                        if (tcpLog.isLoggable(Log.BRIEF)) {
                            tcpLog.log(Log.BRIEF, "(port " + port + ") op = " + i2);
                        }
                        switch (i2) {
                            case 80:
                                if (!serviceCall(new StreamRemoteCall(connection))) {
                                    try {
                                        connection.close();
                                        return;
                                    } catch (IOException e2) {
                                        return;
                                    }
                                }
                                break;
                            case 81:
                            case 83:
                            default:
                                throw new IOException("unknown transport op " + i2);
                            case 82:
                                new DataOutputStream(connection.getOutputStream()).writeByte(83);
                                connection.releaseOutputStream();
                                break;
                            case 84:
                                DGCAckHandler.received(UID.read(dataInputStream));
                                break;
                        }
                        if (!z2) {
                        }
                    } else if (tcpLog.isLoggable(Log.BRIEF)) {
                        tcpLog.log(Log.BRIEF, "(port " + port + ") connection closed");
                    }
                }
            } catch (IOException e3) {
                if (tcpLog.isLoggable(Log.BRIEF)) {
                    tcpLog.log(Log.BRIEF, "(port " + port + ") exception: ", e3);
                }
                try {
                    connection.close();
                } catch (IOException e4) {
                }
            }
        } finally {
            try {
                connection.close();
            } catch (IOException e5) {
            }
        }
    }

    public static String getClientHost() throws ServerNotActiveException {
        ConnectionHandler connectionHandler = threadConnectionHandler.get();
        if (connectionHandler != null) {
            return connectionHandler.getClientHost();
        }
        throw new ServerNotActiveException("not in a remote call");
    }

    /* loaded from: rt.jar:sun/rmi/transport/tcp/TCPTransport$ConnectionHandler.class */
    private class ConnectionHandler implements Runnable {
        private static final int POST = 1347375956;
        private AccessControlContext okContext;
        private Map<AccessControlContext, Reference<AccessControlContext>> authCache;
        private SecurityManager cacheSecurityManager = null;
        private Socket socket;
        private String remoteHost;

        ConnectionHandler(Socket socket, String str) {
            this.socket = socket;
            this.remoteHost = str;
        }

        String getClientHost() {
            return this.remoteHost;
        }

        void checkAcceptPermission(SecurityManager securityManager, AccessControlContext accessControlContext) {
            if (securityManager != this.cacheSecurityManager) {
                this.okContext = null;
                this.authCache = new WeakHashMap();
                this.cacheSecurityManager = securityManager;
            }
            if (accessControlContext.equals(this.okContext) || this.authCache.containsKey(accessControlContext)) {
                return;
            }
            InetAddress inetAddress = this.socket.getInetAddress();
            securityManager.checkAccept(inetAddress != null ? inetAddress.getHostAddress() : "*", this.socket.getPort());
            this.authCache.put(accessControlContext, new SoftReference(accessControlContext));
            this.okContext = accessControlContext;
        }

        @Override // java.lang.Runnable
        public void run() {
            Thread threadCurrentThread = Thread.currentThread();
            String name = threadCurrentThread.getName();
            try {
                threadCurrentThread.setName("RMI TCP Connection(" + TCPTransport.connectionCount.incrementAndGet() + ")-" + this.remoteHost);
                AccessController.doPrivileged(() -> {
                    run0();
                    return null;
                }, TCPTransport.NOPERMS_ACC);
            } finally {
                threadCurrentThread.setName(name);
            }
        }

        private void run0() {
            ConnectionMultiplexer connectionMultiplexer;
            TCPEndpoint endpoint = TCPTransport.this.getEndpoint();
            int port = endpoint.getPort();
            TCPTransport.threadConnectionHandler.set(this);
            try {
                this.socket.setTcpNoDelay(true);
            } catch (Exception e2) {
            }
            try {
                if (TCPTransport.connectionReadTimeout > 0) {
                    this.socket.setSoTimeout(TCPTransport.connectionReadTimeout);
                }
            } catch (Exception e3) {
            }
            try {
                try {
                    InputStream inputStream = this.socket.getInputStream();
                    InputStream bufferedInputStream = inputStream.markSupported() ? inputStream : new BufferedInputStream(inputStream);
                    bufferedInputStream.mark(4);
                    DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
                    int i2 = dataInputStream.readInt();
                    if (i2 == POST) {
                        if (TCPTransport.disableIncomingHttp) {
                            throw new RemoteException("RMI over HTTP is disabled");
                        }
                        TCPTransport.tcpLog.log(Log.BRIEF, "decoding HTTP-wrapped call");
                        bufferedInputStream.reset();
                        try {
                            this.socket = new HttpReceiveSocket(this.socket, bufferedInputStream, null);
                            this.remoteHost = "0.0.0.0";
                            bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
                            dataInputStream = new DataInputStream(bufferedInputStream);
                            i2 = dataInputStream.readInt();
                        } catch (IOException e4) {
                            throw new RemoteException("Error HTTP-unwrapping call", e4);
                        }
                    }
                    short s2 = dataInputStream.readShort();
                    if (i2 != 1246907721 || s2 != 2) {
                        TCPTransport.closeSocket(this.socket);
                        TCPTransport.closeSocket(this.socket);
                        return;
                    }
                    OutputStream outputStream = this.socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
                    int port2 = this.socket.getPort();
                    if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                        TCPTransport.tcpLog.log(Log.BRIEF, "accepted socket from [" + this.remoteHost + CallSiteDescriptor.TOKEN_DELIMITER + port2 + "]");
                    }
                    switch (dataInputStream.readByte()) {
                        case 75:
                            dataOutputStream.writeByte(78);
                            if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                                TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + port + ") suggesting " + this.remoteHost + CallSiteDescriptor.TOKEN_DELIMITER + port2);
                            }
                            dataOutputStream.writeUTF(this.remoteHost);
                            dataOutputStream.writeInt(port2);
                            dataOutputStream.flush();
                            String utf = dataInputStream.readUTF();
                            int i3 = dataInputStream.readInt();
                            if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                                TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + port + ") client using " + utf + CallSiteDescriptor.TOKEN_DELIMITER + i3);
                            }
                            TCPTransport.this.handleMessages(new TCPConnection(new TCPChannel(TCPTransport.this, new TCPEndpoint(this.remoteHost, this.socket.getLocalPort(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory())), this.socket, bufferedInputStream, bufferedOutputStream), true);
                            break;
                        case 76:
                            TCPTransport.this.handleMessages(new TCPConnection(new TCPChannel(TCPTransport.this, new TCPEndpoint(this.remoteHost, this.socket.getLocalPort(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory())), this.socket, bufferedInputStream, bufferedOutputStream), false);
                            break;
                        case 77:
                            if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                                TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + port + ") accepting multiplex protocol");
                            }
                            dataOutputStream.writeByte(78);
                            if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                                TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + port + ") suggesting " + this.remoteHost + CallSiteDescriptor.TOKEN_DELIMITER + port2);
                            }
                            dataOutputStream.writeUTF(this.remoteHost);
                            dataOutputStream.writeInt(port2);
                            dataOutputStream.flush();
                            TCPEndpoint tCPEndpoint = new TCPEndpoint(dataInputStream.readUTF(), dataInputStream.readInt(), endpoint.getClientSocketFactory(), endpoint.getServerSocketFactory());
                            if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                                TCPTransport.tcpLog.log(Log.VERBOSE, "(port " + port + ") client using " + tCPEndpoint.getHost() + CallSiteDescriptor.TOKEN_DELIMITER + tCPEndpoint.getPort());
                            }
                            synchronized (TCPTransport.this.channelTable) {
                                TCPChannel channel = TCPTransport.this.getChannel((Endpoint) tCPEndpoint);
                                connectionMultiplexer = new ConnectionMultiplexer(channel, bufferedInputStream, outputStream, false);
                                channel.useMultiplexer(connectionMultiplexer);
                            }
                            connectionMultiplexer.run();
                            break;
                        default:
                            dataOutputStream.writeByte(79);
                            dataOutputStream.flush();
                            break;
                    }
                    TCPTransport.closeSocket(this.socket);
                } catch (IOException e5) {
                    TCPTransport.tcpLog.log(Log.BRIEF, "terminated with exception:", e5);
                    TCPTransport.closeSocket(this.socket);
                }
            } catch (Throwable th) {
                TCPTransport.closeSocket(this.socket);
                throw th;
            }
        }
    }
}

package sun.rmi.transport.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.Socket;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.RuntimeUtil;
import sun.rmi.transport.Channel;
import sun.rmi.transport.Connection;
import sun.rmi.transport.Endpoint;
import sun.rmi.transport.TransportConstants;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetLongAction;

/* loaded from: rt.jar:sun/rmi/transport/tcp/TCPChannel.class */
public class TCPChannel implements Channel {
    private final TCPEndpoint ep;
    private final TCPTransport tr;
    private ConnectionAcceptor acceptor;
    private AccessControlContext okContext;
    private WeakHashMap<AccessControlContext, Reference<AccessControlContext>> authcache;
    private static final long idleTimeout = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.connectionTimeout", 15000))).longValue();
    private static final int handshakeTimeout = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.handshakeTimeout", 60000))).intValue();
    private static final int responseTimeout = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.rmi.transport.tcp.responseTimeout", 0))).intValue();
    private static final ScheduledExecutorService scheduler = ((RuntimeUtil) AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
    private final List<TCPConnection> freeList = new ArrayList();
    private Future<?> reaper = null;
    private boolean usingMultiplexer = false;
    private ConnectionMultiplexer multiplexer = null;
    private SecurityManager cacheSecurityManager = null;

    TCPChannel(TCPTransport tCPTransport, TCPEndpoint tCPEndpoint) {
        this.tr = tCPTransport;
        this.ep = tCPEndpoint;
    }

    @Override // sun.rmi.transport.Channel
    public Endpoint getEndpoint() {
        return this.ep;
    }

    private void checkConnectPermission() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        if (securityManager != this.cacheSecurityManager) {
            this.okContext = null;
            this.authcache = new WeakHashMap<>();
            this.cacheSecurityManager = securityManager;
        }
        AccessControlContext context = AccessController.getContext();
        if (this.okContext == null || (!this.okContext.equals(context) && !this.authcache.containsKey(context))) {
            securityManager.checkConnect(this.ep.getHost(), this.ep.getPort());
            this.authcache.put(context, new SoftReference(context));
        }
        this.okContext = context;
    }

    @Override // sun.rmi.transport.Channel
    public Connection newConnection() throws RemoteException {
        TCPConnection tCPConnection;
        do {
            tCPConnection = null;
            synchronized (this.freeList) {
                int size = this.freeList.size() - 1;
                if (size >= 0) {
                    checkConnectPermission();
                    tCPConnection = this.freeList.get(size);
                    this.freeList.remove(size);
                }
            }
            if (tCPConnection != null) {
                if (!tCPConnection.isDead()) {
                    TCPTransport.tcpLog.log(Log.BRIEF, "reuse connection");
                    return tCPConnection;
                }
                free(tCPConnection, false);
            }
        } while (tCPConnection != null);
        return createConnection();
    }

    private Connection createConnection() throws RemoteException {
        TCPConnection tCPConnectionOpenConnection;
        int i2;
        TCPTransport.tcpLog.log(Log.BRIEF, "create connection");
        if (!this.usingMultiplexer) {
            Socket socketNewSocket = this.ep.newSocket();
            tCPConnectionOpenConnection = new TCPConnection(this, socketNewSocket);
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(tCPConnectionOpenConnection.getOutputStream());
                writeTransportHeader(dataOutputStream);
                if (!tCPConnectionOpenConnection.isReusable()) {
                    dataOutputStream.writeByte(76);
                } else {
                    dataOutputStream.writeByte(75);
                    dataOutputStream.flush();
                    int soTimeout = 0;
                    try {
                        soTimeout = socketNewSocket.getSoTimeout();
                        socketNewSocket.setSoTimeout(handshakeTimeout);
                    } catch (Exception e2) {
                    }
                    DataInputStream dataInputStream = new DataInputStream(tCPConnectionOpenConnection.getInputStream());
                    byte b2 = dataInputStream.readByte();
                    if (b2 != 78) {
                        throw new ConnectIOException(b2 == 79 ? "JRMP StreamProtocol not supported by server" : "non-JRMP server at remote endpoint");
                    }
                    String utf = dataInputStream.readUTF();
                    int i3 = dataInputStream.readInt();
                    if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                        TCPTransport.tcpLog.log(Log.VERBOSE, "server suggested " + utf + CallSiteDescriptor.TOKEN_DELIMITER + i3);
                    }
                    TCPEndpoint.setLocalHost(utf);
                    TCPEndpoint localEndpoint = TCPEndpoint.getLocalEndpoint(0, null, null);
                    dataOutputStream.writeUTF(localEndpoint.getHost());
                    dataOutputStream.writeInt(localEndpoint.getPort());
                    if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
                        TCPTransport.tcpLog.log(Log.VERBOSE, "using " + localEndpoint.getHost() + CallSiteDescriptor.TOKEN_DELIMITER + localEndpoint.getPort());
                    }
                    if (soTimeout != 0) {
                        i2 = soTimeout;
                    } else {
                        try {
                            i2 = responseTimeout;
                        } catch (Exception e3) {
                        }
                    }
                    socketNewSocket.setSoTimeout(i2);
                    dataOutputStream.flush();
                }
            } catch (IOException e4) {
                try {
                    tCPConnectionOpenConnection.close();
                } catch (Exception e5) {
                }
                if (e4 instanceof RemoteException) {
                    throw ((RemoteException) e4);
                }
                throw new ConnectIOException("error during JRMP connection establishment", e4);
            }
        } else {
            try {
                tCPConnectionOpenConnection = this.multiplexer.openConnection();
            } catch (IOException e6) {
                synchronized (this) {
                    this.usingMultiplexer = false;
                    this.multiplexer = null;
                    throw new ConnectIOException("error opening virtual connection over multiplexed connection", e6);
                }
            }
        }
        return tCPConnectionOpenConnection;
    }

    @Override // sun.rmi.transport.Channel
    public void free(Connection connection, boolean z2) {
        if (connection == null) {
            return;
        }
        if (z2 && connection.isReusable()) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            TCPConnection tCPConnection = (TCPConnection) connection;
            TCPTransport.tcpLog.log(Log.BRIEF, "reuse connection");
            synchronized (this.freeList) {
                this.freeList.add(tCPConnection);
                if (this.reaper == null) {
                    TCPTransport.tcpLog.log(Log.BRIEF, "create reaper");
                    this.reaper = scheduler.scheduleWithFixedDelay(new Runnable() { // from class: sun.rmi.transport.tcp.TCPChannel.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TCPTransport.tcpLog.log(Log.VERBOSE, "wake up");
                            TCPChannel.this.freeCachedConnections();
                        }
                    }, idleTimeout, idleTimeout, TimeUnit.MILLISECONDS);
                }
            }
            tCPConnection.setLastUseTime(jCurrentTimeMillis);
            tCPConnection.setExpiration(jCurrentTimeMillis + idleTimeout);
            return;
        }
        TCPTransport.tcpLog.log(Log.BRIEF, "close connection");
        try {
            connection.close();
        } catch (IOException e2) {
        }
    }

    private void writeTransportHeader(DataOutputStream dataOutputStream) throws RemoteException {
        try {
            DataOutputStream dataOutputStream2 = new DataOutputStream(dataOutputStream);
            dataOutputStream2.writeInt(TransportConstants.Magic);
            dataOutputStream2.writeShort(2);
        } catch (IOException e2) {
            throw new ConnectIOException("error writing JRMP transport header", e2);
        }
    }

    synchronized void useMultiplexer(ConnectionMultiplexer connectionMultiplexer) {
        this.multiplexer = connectionMultiplexer;
        this.usingMultiplexer = true;
    }

    void acceptMultiplexConnection(Connection connection) {
        if (this.acceptor == null) {
            this.acceptor = new ConnectionAcceptor(this.tr);
            this.acceptor.startNewAcceptor();
        }
        this.acceptor.accept(connection);
    }

    public void shedCache() {
        Connection[] connectionArr;
        synchronized (this.freeList) {
            connectionArr = (Connection[]) this.freeList.toArray(new Connection[this.freeList.size()]);
            this.freeList.clear();
        }
        int length = connectionArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                Connection connection = connectionArr[length];
                connectionArr[length] = null;
                try {
                    connection.close();
                } catch (IOException e2) {
                }
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void freeCachedConnections() {
        synchronized (this.freeList) {
            int size = this.freeList.size();
            if (size > 0) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                ListIterator<TCPConnection> listIterator = this.freeList.listIterator(size);
                while (listIterator.hasPrevious()) {
                    TCPConnection tCPConnectionPrevious = listIterator.previous();
                    if (tCPConnectionPrevious.expired(jCurrentTimeMillis)) {
                        TCPTransport.tcpLog.log(Log.VERBOSE, "connection timeout expired");
                        try {
                            tCPConnectionPrevious.close();
                        } catch (IOException e2) {
                        }
                        listIterator.remove();
                    }
                }
            }
            if (this.freeList.isEmpty()) {
                this.reaper.cancel(false);
                this.reaper = null;
            }
        }
    }
}

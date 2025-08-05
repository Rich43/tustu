package java.net;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/DatagramSocket.class */
public class DatagramSocket implements Closeable {
    private boolean created;
    private boolean bound;
    private boolean closed;
    private Object closeLock;
    DatagramSocketImpl impl;
    boolean oldImpl;
    private boolean explicitFilter;
    private int bytesLeftToFilter;
    static final int ST_NOT_CONNECTED = 0;
    static final int ST_CONNECTED = 1;
    static final int ST_CONNECTED_NO_IMPL = 2;
    int connectState;
    InetAddress connectedAddress;
    int connectedPort;
    static Class<?> implClass = null;
    static DatagramSocketImplFactory factory;

    private synchronized void connectInternal(InetAddress inetAddress, int i2) throws SocketException {
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException("connect: " + i2);
        }
        if (inetAddress == null) {
            throw new IllegalArgumentException("connect: null address");
        }
        checkAddress(inetAddress, SecurityConstants.SOCKET_CONNECT_ACTION);
        if (isClosed()) {
            return;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (inetAddress.isMulticastAddress()) {
                securityManager.checkMulticast(inetAddress);
            } else {
                securityManager.checkConnect(inetAddress.getHostAddress(), i2);
                securityManager.checkAccept(inetAddress.getHostAddress(), i2);
            }
        }
        if (!isBound()) {
            bind(new InetSocketAddress(0));
        }
        if (this.oldImpl || ((this.impl instanceof AbstractPlainDatagramSocketImpl) && ((AbstractPlainDatagramSocketImpl) this.impl).nativeConnectDisabled())) {
            this.connectState = 2;
        } else {
            try {
                getImpl().connect(inetAddress, i2);
                this.connectState = 1;
                int iDataAvailable = getImpl().dataAvailable();
                if (iDataAvailable == -1) {
                    throw new SocketException();
                }
                this.explicitFilter = iDataAvailable > 0;
                if (this.explicitFilter) {
                    this.bytesLeftToFilter = getReceiveBufferSize();
                }
            } catch (SocketException e2) {
                this.connectState = 2;
            }
        }
        this.connectedAddress = inetAddress;
        this.connectedPort = i2;
    }

    public DatagramSocket() throws SocketException {
        this(new InetSocketAddress(0));
    }

    protected DatagramSocket(DatagramSocketImpl datagramSocketImpl) {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        this.explicitFilter = false;
        this.connectState = 0;
        this.connectedAddress = null;
        this.connectedPort = -1;
        if (datagramSocketImpl == null) {
            throw new NullPointerException();
        }
        this.impl = datagramSocketImpl;
        checkOldImpl();
    }

    public DatagramSocket(SocketAddress socketAddress) throws SocketException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        this.explicitFilter = false;
        this.connectState = 0;
        this.connectedAddress = null;
        this.connectedPort = -1;
        createImpl();
        if (socketAddress != null) {
            try {
                bind(socketAddress);
            } finally {
                if (!isBound()) {
                    close();
                }
            }
        }
    }

    public DatagramSocket(int i2) throws SocketException {
        this(i2, null);
    }

    public DatagramSocket(int i2, InetAddress inetAddress) throws SocketException {
        this(new InetSocketAddress(inetAddress, i2));
    }

    private void checkOldImpl() {
        if (this.impl == null) {
            return;
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.net.DatagramSocket.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws NoSuchMethodException, SecurityException {
                    DatagramSocket.this.impl.getClass().getDeclaredMethod("peekData", DatagramPacket.class);
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            this.oldImpl = true;
        }
    }

    void createImpl() throws SocketException {
        if (this.impl == null) {
            if (factory != null) {
                this.impl = factory.createDatagramSocketImpl();
                checkOldImpl();
            } else {
                this.impl = DefaultDatagramSocketImplFactory.createDatagramSocketImpl(this instanceof MulticastSocket);
                checkOldImpl();
            }
        }
        this.impl.create();
        this.impl.setDatagramSocket(this);
        this.created = true;
    }

    DatagramSocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl();
        }
        return this.impl;
    }

    public synchronized void bind(SocketAddress socketAddress) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (isBound()) {
            throw new SocketException("already bound");
        }
        if (socketAddress == null) {
            socketAddress = new InetSocketAddress(0);
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type!");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress.isUnresolved()) {
            throw new SocketException("Unresolved address");
        }
        InetAddress address = inetSocketAddress.getAddress();
        int port = inetSocketAddress.getPort();
        checkAddress(address, "bind");
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkListen(port);
        }
        try {
            getImpl().bind(port, address);
            this.bound = true;
        } catch (SocketException e2) {
            getImpl().close();
            throw e2;
        }
    }

    void checkAddress(InetAddress inetAddress, String str) {
        if (inetAddress != null && !(inetAddress instanceof Inet4Address) && !(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(str + ": invalid address type");
        }
    }

    public void connect(InetAddress inetAddress, int i2) {
        try {
            connectInternal(inetAddress, i2);
        } catch (SocketException e2) {
            throw new Error("connect failed", e2);
        }
    }

    public void connect(SocketAddress socketAddress) throws SocketException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Address can't be null");
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress.isUnresolved()) {
            throw new SocketException("Unresolved address");
        }
        connectInternal(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
    }

    public void disconnect() {
        synchronized (this) {
            if (isClosed()) {
                return;
            }
            if (this.connectState == 1) {
                this.impl.disconnect();
            }
            this.connectedAddress = null;
            this.connectedPort = -1;
            this.connectState = 0;
            this.explicitFilter = false;
        }
    }

    public boolean isBound() {
        return this.bound;
    }

    public boolean isConnected() {
        return this.connectState != 0;
    }

    public InetAddress getInetAddress() {
        return this.connectedAddress;
    }

    public int getPort() {
        return this.connectedPort;
    }

    public SocketAddress getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getPort());
    }

    public SocketAddress getLocalSocketAddress() {
        if (isClosed() || !isBound()) {
            return null;
        }
        return new InetSocketAddress(getLocalAddress(), getLocalPort());
    }

    public void send(DatagramPacket datagramPacket) throws IOException {
        synchronized (datagramPacket) {
            if (isClosed()) {
                throw new SocketException("Socket is closed");
            }
            checkAddress(datagramPacket.getAddress(), "send");
            if (this.connectState == 0) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    if (datagramPacket.getAddress().isMulticastAddress()) {
                        securityManager.checkMulticast(datagramPacket.getAddress());
                    } else {
                        securityManager.checkConnect(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort());
                    }
                }
            } else {
                InetAddress address = datagramPacket.getAddress();
                if (address == null) {
                    datagramPacket.setAddress(this.connectedAddress);
                    datagramPacket.setPort(this.connectedPort);
                } else if (!address.equals(this.connectedAddress) || datagramPacket.getPort() != this.connectedPort) {
                    throw new IllegalArgumentException("connected address and packet address differ");
                }
            }
            if (!isBound()) {
                bind(new InetSocketAddress(0));
            }
            getImpl().send(datagramPacket);
        }
    }

    /* JADX WARN: In synchronized method top region not synchronized by 'this': (r6v0 java.net.DatagramPacket) */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00b2 A[Catch: all -> 0x015b, TryCatch #0 {, blocks: (B:4:0x0004, B:6:0x000b, B:7:0x0017, B:9:0x001e, B:11:0x0026, B:13:0x0033, B:15:0x0073, B:17:0x0080, B:14:0x0058, B:18:0x0099, B:20:0x00a3, B:40:0x013d, B:44:0x0150, B:46:0x0157, B:25:0x00b2, B:27:0x00bf, B:29:0x00f5, B:31:0x0101, B:33:0x010a, B:35:0x0129, B:28:0x00e1), top: B:54:0x0004, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void receive(java.net.DatagramPacket r6) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 355
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.DatagramSocket.receive(java.net.DatagramPacket):void");
    }

    private boolean checkFiltering(DatagramPacket datagramPacket) throws SocketException {
        this.bytesLeftToFilter -= datagramPacket.getLength();
        if (this.bytesLeftToFilter <= 0 || getImpl().dataAvailable() <= 0) {
            this.explicitFilter = false;
            return true;
        }
        return false;
    }

    public InetAddress getLocalAddress() {
        InetAddress inetAddressAnyLocalAddress;
        if (isClosed()) {
            return null;
        }
        try {
            inetAddressAnyLocalAddress = (InetAddress) getImpl().getOption(15);
            if (inetAddressAnyLocalAddress.isAnyLocalAddress()) {
                inetAddressAnyLocalAddress = InetAddress.anyLocalAddress();
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(inetAddressAnyLocalAddress.getHostAddress(), -1);
            }
        } catch (Exception e2) {
            inetAddressAnyLocalAddress = InetAddress.anyLocalAddress();
        }
        return inetAddressAnyLocalAddress;
    }

    public int getLocalPort() {
        if (isClosed()) {
            return -1;
        }
        try {
            return getImpl().getLocalPort();
        } catch (Exception e2) {
            return 0;
        }
    }

    public synchronized void setSoTimeout(int i2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(SocketOptions.SO_TIMEOUT, new Integer(i2));
    }

    public synchronized int getSoTimeout() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (getImpl() == null) {
            return 0;
        }
        Object option = getImpl().getOption(SocketOptions.SO_TIMEOUT);
        if (option instanceof Integer) {
            return ((Integer) option).intValue();
        }
        return 0;
    }

    public synchronized void setSendBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("negative send size");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(4097, new Integer(i2));
    }

    public synchronized int getSendBufferSize() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        int iIntValue = 0;
        Object option = getImpl().getOption(4097);
        if (option instanceof Integer) {
            iIntValue = ((Integer) option).intValue();
        }
        return iIntValue;
    }

    public synchronized void setReceiveBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("invalid receive size");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(SocketOptions.SO_RCVBUF, new Integer(i2));
    }

    public synchronized int getReceiveBufferSize() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        int iIntValue = 0;
        Object option = getImpl().getOption(SocketOptions.SO_RCVBUF);
        if (option instanceof Integer) {
            iIntValue = ((Integer) option).intValue();
        }
        return iIntValue;
    }

    public synchronized void setReuseAddress(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.oldImpl) {
            getImpl().setOption(4, new Integer(z2 ? -1 : 0));
        } else {
            getImpl().setOption(4, Boolean.valueOf(z2));
        }
    }

    public synchronized boolean getReuseAddress() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(4)).booleanValue();
    }

    public synchronized void setBroadcast(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(32, Boolean.valueOf(z2));
    }

    public synchronized boolean getBroadcast() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(32)).booleanValue();
    }

    public synchronized void setTrafficClass(int i2) throws SocketException {
        if (i2 < 0 || i2 > 255) {
            throw new IllegalArgumentException("tc is not in range 0 -- 255");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        try {
            getImpl().setOption(3, Integer.valueOf(i2));
        } catch (SocketException e2) {
            if (!isConnected()) {
                throw e2;
            }
        }
    }

    public synchronized int getTrafficClass() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Integer) getImpl().getOption(3)).intValue();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.closeLock) {
            if (isClosed()) {
                return;
            }
            this.impl.close();
            this.closed = true;
        }
    }

    public boolean isClosed() {
        boolean z2;
        synchronized (this.closeLock) {
            z2 = this.closed;
        }
        return z2;
    }

    public DatagramChannel getChannel() {
        return null;
    }

    public static synchronized void setDatagramSocketImplFactory(DatagramSocketImplFactory datagramSocketImplFactory) throws IOException {
        if (factory != null) {
            throw new SocketException("factory already defined");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        factory = datagramSocketImplFactory;
    }
}

package java.net;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.net.ApplicationProxy;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/Socket.class */
public class Socket implements Closeable {
    private boolean created;
    private boolean bound;
    private boolean connected;
    private boolean closed;
    private Object closeLock;
    private boolean shutIn;
    private boolean shutOut;
    SocketImpl impl;
    private boolean oldImpl;
    private static SocketImplFactory factory = null;

    public Socket() {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        setImpl();
    }

    public Socket(Proxy proxy) {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        if (proxy == null) {
            throw new IllegalArgumentException("Invalid Proxy");
        }
        Proxy proxyCreate = proxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(proxy);
        Proxy.Type type = proxyCreate.type();
        if (type == Proxy.Type.SOCKS || type == Proxy.Type.HTTP) {
            SecurityManager securityManager = System.getSecurityManager();
            InetSocketAddress inetSocketAddress = (InetSocketAddress) proxyCreate.address();
            if (inetSocketAddress.getAddress() != null) {
                checkAddress(inetSocketAddress.getAddress(), ORBConstants.SOCKET);
            }
            if (securityManager != null) {
                inetSocketAddress = inetSocketAddress.isUnresolved() ? new InetSocketAddress(inetSocketAddress.getHostName(), inetSocketAddress.getPort()) : inetSocketAddress;
                if (inetSocketAddress.isUnresolved()) {
                    securityManager.checkConnect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                } else {
                    securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                }
            }
            this.impl = type == Proxy.Type.SOCKS ? new SocksSocketImpl(proxyCreate) : new HttpConnectSocketImpl(proxyCreate);
            this.impl.setSocket(this);
            return;
        }
        if (proxyCreate == Proxy.NO_PROXY) {
            if (factory == null) {
                this.impl = new PlainSocketImpl();
                this.impl.setSocket(this);
                return;
            } else {
                setImpl();
                return;
            }
        }
        throw new IllegalArgumentException("Invalid Proxy");
    }

    protected Socket(SocketImpl socketImpl) throws SocketException {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        checkPermission(socketImpl);
        this.impl = socketImpl;
        if (socketImpl != null) {
            checkOldImpl();
            this.impl.setSocket(this);
        }
    }

    private static Void checkPermission(SocketImpl socketImpl) {
        SecurityManager securityManager;
        if (socketImpl != null && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SecurityConstants.SET_SOCKETIMPL_PERMISSION);
            return null;
        }
        return null;
    }

    public Socket(String str, int i2) throws IOException {
        this((SocketAddress) (str != null ? new InetSocketAddress(str, i2) : new InetSocketAddress(InetAddress.getByName(null), i2)), (SocketAddress) null, true);
    }

    public Socket(InetAddress inetAddress, int i2) throws IOException {
        this((SocketAddress) (inetAddress != null ? new InetSocketAddress(inetAddress, i2) : null), (SocketAddress) null, true);
    }

    public Socket(String str, int i2, InetAddress inetAddress, int i3) throws IOException {
        this((SocketAddress) (str != null ? new InetSocketAddress(str, i2) : new InetSocketAddress(InetAddress.getByName(null), i2)), (SocketAddress) new InetSocketAddress(inetAddress, i3), true);
    }

    public Socket(InetAddress inetAddress, int i2, InetAddress inetAddress2, int i3) throws IOException {
        this((SocketAddress) (inetAddress != null ? new InetSocketAddress(inetAddress, i2) : null), (SocketAddress) new InetSocketAddress(inetAddress2, i3), true);
    }

    @Deprecated
    public Socket(String str, int i2, boolean z2) throws IOException {
        this(str != null ? new InetSocketAddress(str, i2) : new InetSocketAddress(InetAddress.getByName(null), i2), (SocketAddress) null, z2);
    }

    @Deprecated
    public Socket(InetAddress inetAddress, int i2, boolean z2) throws IOException {
        this(inetAddress != null ? new InetSocketAddress(inetAddress, i2) : null, new InetSocketAddress(0), z2);
    }

    private Socket(SocketAddress socketAddress, SocketAddress socketAddress2, boolean z2) throws IOException {
        this.created = false;
        this.bound = false;
        this.connected = false;
        this.closed = false;
        this.closeLock = new Object();
        this.shutIn = false;
        this.shutOut = false;
        this.oldImpl = false;
        setImpl();
        if (socketAddress == null) {
            throw new NullPointerException();
        }
        try {
            createImpl(z2);
            if (socketAddress2 != null) {
                bind(socketAddress2);
            }
            connect(socketAddress);
        } catch (IOException | IllegalArgumentException | SecurityException e2) {
            try {
                close();
            } catch (IOException e3) {
                e2.addSuppressed(e3);
            }
            throw e2;
        }
    }

    void createImpl(boolean z2) throws SocketException {
        if (this.impl == null) {
            setImpl();
        }
        try {
            this.impl.create(z2);
            this.created = true;
        } catch (IOException e2) {
            throw new SocketException(e2.getMessage());
        }
    }

    private void checkOldImpl() {
        if (this.impl == null) {
            return;
        }
        this.oldImpl = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.net.Socket.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() throws SecurityException {
                Class<?> superclass = Socket.this.impl.getClass();
                do {
                    try {
                        superclass.getDeclaredMethod(SecurityConstants.SOCKET_CONNECT_ACTION, SocketAddress.class, Integer.TYPE);
                        return Boolean.FALSE;
                    } catch (NoSuchMethodException e2) {
                        superclass = superclass.getSuperclass();
                    }
                } while (!superclass.equals(SocketImpl.class));
                return Boolean.TRUE;
            }
        })).booleanValue();
    }

    void setImpl() {
        if (factory != null) {
            this.impl = factory.createSocketImpl();
            checkOldImpl();
        } else {
            this.impl = new SocksSocketImpl();
        }
        if (this.impl != null) {
            this.impl.setSocket(this);
        }
    }

    SocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl(true);
        }
        return this.impl;
    }

    public void connect(SocketAddress socketAddress) throws IOException {
        connect(socketAddress, 0);
    }

    public void connect(SocketAddress socketAddress, int i2) throws IOException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("connect: The address can't be null");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        }
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.oldImpl && isConnected()) {
            throw new SocketException("already connected");
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        InetAddress address = inetSocketAddress.getAddress();
        int port = inetSocketAddress.getPort();
        checkAddress(address, SecurityConstants.SOCKET_CONNECT_ACTION);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (inetSocketAddress.isUnresolved()) {
                securityManager.checkConnect(inetSocketAddress.getHostName(), port);
            } else {
                securityManager.checkConnect(address.getHostAddress(), port);
            }
        }
        if (!this.created) {
            createImpl(true);
        }
        if (!this.oldImpl) {
            this.impl.connect(inetSocketAddress, i2);
        } else if (i2 == 0) {
            if (inetSocketAddress.isUnresolved()) {
                this.impl.connect(address.getHostName(), port);
            } else {
                this.impl.connect(address, port);
            }
        } else {
            throw new UnsupportedOperationException("SocketImpl.connect(addr, timeout)");
        }
        this.connected = true;
        this.bound = true;
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.oldImpl && isBound()) {
            throw new SocketException("Already bound");
        }
        if (socketAddress != null && !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress != null && inetSocketAddress.isUnresolved()) {
            throw new SocketException("Unresolved address");
        }
        if (inetSocketAddress == null) {
            inetSocketAddress = new InetSocketAddress(0);
        }
        InetAddress address = inetSocketAddress.getAddress();
        int port = inetSocketAddress.getPort();
        checkAddress(address, "bind");
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkListen(port);
        }
        getImpl().bind(address, port);
        this.bound = true;
    }

    private void checkAddress(InetAddress inetAddress, String str) {
        if (inetAddress != null && !(inetAddress instanceof Inet4Address) && !(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(str + ": invalid address type");
        }
    }

    final void postAccept() {
        this.connected = true;
        this.created = true;
        this.bound = true;
    }

    void setCreated() {
        this.created = true;
    }

    void setBound() {
        this.bound = true;
    }

    void setConnected() {
        this.connected = true;
    }

    public InetAddress getInetAddress() {
        if (!isConnected()) {
            return null;
        }
        try {
            return getImpl().getInetAddress();
        } catch (SocketException e2) {
            return null;
        }
    }

    public InetAddress getLocalAddress() {
        InetAddress inetAddressAnyLocalAddress;
        if (!isBound()) {
            return InetAddress.anyLocalAddress();
        }
        try {
            inetAddressAnyLocalAddress = (InetAddress) getImpl().getOption(15);
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(inetAddressAnyLocalAddress.getHostAddress(), -1);
            }
            if (inetAddressAnyLocalAddress.isAnyLocalAddress()) {
                inetAddressAnyLocalAddress = InetAddress.anyLocalAddress();
            }
        } catch (SecurityException e2) {
            inetAddressAnyLocalAddress = InetAddress.getLoopbackAddress();
        } catch (Exception e3) {
            inetAddressAnyLocalAddress = InetAddress.anyLocalAddress();
        }
        return inetAddressAnyLocalAddress;
    }

    public int getPort() {
        if (!isConnected()) {
            return 0;
        }
        try {
            return getImpl().getPort();
        } catch (SocketException e2) {
            return -1;
        }
    }

    public int getLocalPort() {
        if (!isBound()) {
            return -1;
        }
        try {
            return getImpl().getLocalPort();
        } catch (SocketException e2) {
            return -1;
        }
    }

    public SocketAddress getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getPort());
    }

    public SocketAddress getLocalSocketAddress() {
        if (!isBound()) {
            return null;
        }
        return new InetSocketAddress(getLocalAddress(), getLocalPort());
    }

    public SocketChannel getChannel() {
        return null;
    }

    public InputStream getInputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isInputShutdown()) {
            throw new SocketException("Socket input is shutdown");
        }
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: java.net.Socket.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public InputStream run() throws IOException {
                    return Socket.this.impl.getInputStream();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isOutputShutdown()) {
            throw new SocketException("Socket output is shutdown");
        }
        try {
            return (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() { // from class: java.net.Socket.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public OutputStream run() throws IOException {
                    return Socket.this.impl.getOutputStream();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    public void setTcpNoDelay(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(1, Boolean.valueOf(z2));
    }

    public boolean getTcpNoDelay() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(1)).booleanValue();
    }

    public void setSoLinger(boolean z2, int i2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!z2) {
            getImpl().setOption(128, new Boolean(z2));
        } else {
            if (i2 < 0) {
                throw new IllegalArgumentException("invalid value for SO_LINGER");
            }
            if (i2 > 65535) {
                i2 = 65535;
            }
            getImpl().setOption(128, new Integer(i2));
        }
    }

    public int getSoLinger() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object option = getImpl().getOption(128);
        if (option instanceof Integer) {
            return ((Integer) option).intValue();
        }
        return -1;
    }

    public void sendUrgentData(int i2) throws IOException {
        if (!getImpl().supportsUrgentData()) {
            throw new SocketException("Urgent data not supported");
        }
        getImpl().sendUrgentData(i2);
    }

    public void setOOBInline(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(SocketOptions.SO_OOBINLINE, Boolean.valueOf(z2));
    }

    public boolean getOOBInline() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(SocketOptions.SO_OOBINLINE)).booleanValue();
    }

    public synchronized void setSoTimeout(int i2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        }
        getImpl().setOption(SocketOptions.SO_TIMEOUT, new Integer(i2));
    }

    public synchronized int getSoTimeout() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
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

    public void setKeepAlive(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(8, Boolean.valueOf(z2));
    }

    public boolean getKeepAlive() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(8)).booleanValue();
    }

    public void setTrafficClass(int i2) throws SocketException {
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

    public int getTrafficClass() throws SocketException {
        return ((Integer) getImpl().getOption(3)).intValue();
    }

    public void setReuseAddress(boolean z2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(4, Boolean.valueOf(z2));
    }

    public boolean getReuseAddress() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        return ((Boolean) getImpl().getOption(4)).booleanValue();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        synchronized (this.closeLock) {
            if (isClosed()) {
                return;
            }
            if (this.created) {
                this.impl.close();
            }
            this.closed = true;
        }
    }

    public void shutdownInput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isInputShutdown()) {
            throw new SocketException("Socket input is already shutdown");
        }
        getImpl().shutdownInput();
        this.shutIn = true;
    }

    public void shutdownOutput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isOutputShutdown()) {
            throw new SocketException("Socket output is already shutdown");
        }
        getImpl().shutdownOutput();
        this.shutOut = true;
    }

    public String toString() {
        try {
            if (isConnected()) {
                return "Socket[addr=" + ((Object) getImpl().getInetAddress()) + ",port=" + getImpl().getPort() + ",localport=" + getImpl().getLocalPort() + "]";
            }
            return "Socket[unconnected]";
        } catch (SocketException e2) {
            return "Socket[unconnected]";
        }
    }

    public boolean isConnected() {
        return this.connected || this.oldImpl;
    }

    public boolean isBound() {
        return this.bound || this.oldImpl;
    }

    public boolean isClosed() {
        boolean z2;
        synchronized (this.closeLock) {
            z2 = this.closed;
        }
        return z2;
    }

    public boolean isInputShutdown() {
        return this.shutIn;
    }

    public boolean isOutputShutdown() {
        return this.shutOut;
    }

    public static synchronized void setSocketImplFactory(SocketImplFactory socketImplFactory) throws IOException {
        if (factory != null) {
            throw new SocketException("factory already defined");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        factory = socketImplFactory;
    }

    public void setPerformancePreferences(int i2, int i3, int i4) {
    }
}

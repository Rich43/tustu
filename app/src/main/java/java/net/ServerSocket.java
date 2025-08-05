package java.net;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/ServerSocket.class */
public class ServerSocket implements Closeable {
    private boolean created;
    private boolean bound;
    private boolean closed;
    private Object closeLock;
    private SocketImpl impl;
    private boolean oldImpl;
    private static SocketImplFactory factory = null;

    ServerSocket(SocketImpl socketImpl) {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        checkPermission();
        this.impl = socketImpl;
        socketImpl.setServerSocket(this);
    }

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.SET_SOCKETIMPL_PERMISSION);
            return null;
        }
        return null;
    }

    public ServerSocket() throws IOException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        setImpl();
    }

    public ServerSocket(int i2) throws IOException {
        this(i2, 50, null);
    }

    public ServerSocket(int i2, int i3) throws IOException {
        this(i2, i3, null);
    }

    public ServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException {
        this.created = false;
        this.bound = false;
        this.closed = false;
        this.closeLock = new Object();
        this.oldImpl = false;
        setImpl();
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException("Port value out of range: " + i2);
        }
        try {
            bind(new InetSocketAddress(inetAddress, i2), i3 < 1 ? 50 : i3);
        } catch (IOException e2) {
            close();
            throw e2;
        } catch (SecurityException e3) {
            close();
            throw e3;
        }
    }

    SocketImpl getImpl() throws SocketException {
        if (!this.created) {
            createImpl();
        }
        return this.impl;
    }

    private void checkOldImpl() {
        if (this.impl == null) {
            return;
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.net.ServerSocket.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws NoSuchMethodException, SecurityException {
                    ServerSocket.this.impl.getClass().getDeclaredMethod(SecurityConstants.SOCKET_CONNECT_ACTION, SocketAddress.class, Integer.TYPE);
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            this.oldImpl = true;
        }
    }

    private void setImpl() {
        if (factory != null) {
            this.impl = factory.createSocketImpl();
            checkOldImpl();
        } else {
            this.impl = new SocksSocketImpl();
        }
        if (this.impl != null) {
            this.impl.setServerSocket(this);
        }
    }

    void createImpl() throws SocketException {
        if (this.impl == null) {
            setImpl();
        }
        try {
            this.impl.create(true);
            this.created = true;
        } catch (IOException e2) {
            throw new SocketException(e2.getMessage());
        }
    }

    public void bind(SocketAddress socketAddress) throws IOException {
        bind(socketAddress, 50);
    }

    public void bind(SocketAddress socketAddress, int i2) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.oldImpl && isBound()) {
            throw new SocketException("Already bound");
        }
        if (socketAddress == null) {
            socketAddress = new InetSocketAddress(0);
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress.isUnresolved()) {
            throw new SocketException("Unresolved address");
        }
        if (i2 < 1) {
            i2 = 50;
        }
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkListen(inetSocketAddress.getPort());
            }
            getImpl().bind(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            getImpl().listen(i2);
            this.bound = true;
        } catch (IOException e2) {
            this.bound = false;
            throw e2;
        } catch (SecurityException e3) {
            this.bound = false;
            throw e3;
        }
    }

    public InetAddress getInetAddress() {
        if (!isBound()) {
            return null;
        }
        try {
            InetAddress inetAddress = getImpl().getInetAddress();
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(inetAddress.getHostAddress(), -1);
            }
            return inetAddress;
        } catch (SecurityException e2) {
            return InetAddress.getLoopbackAddress();
        } catch (SocketException e3) {
            return null;
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

    public SocketAddress getLocalSocketAddress() {
        if (!isBound()) {
            return null;
        }
        return new InetSocketAddress(getInetAddress(), getLocalPort());
    }

    public Socket accept() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (!isBound()) {
            throw new SocketException("Socket is not bound yet");
        }
        Socket socket = new Socket((SocketImpl) null);
        implAccept(socket);
        return socket;
    }

    protected final void implAccept(Socket socket) throws IOException {
        SocketImpl socketImpl = null;
        try {
            if (socket.impl == null) {
                socket.setImpl();
            } else {
                socket.impl.reset();
            }
            socketImpl = socket.impl;
            socket.impl = null;
            socketImpl.address = new InetAddress();
            socketImpl.fd = new FileDescriptor();
            getImpl().accept(socketImpl);
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkAccept(socketImpl.getInetAddress().getHostAddress(), socketImpl.getPort());
            }
            socket.impl = socketImpl;
            socket.postAccept();
        } catch (IOException e2) {
            if (socketImpl != null) {
                socketImpl.reset();
            }
            socket.impl = socketImpl;
            throw e2;
        } catch (SecurityException e3) {
            if (socketImpl != null) {
                socketImpl.reset();
            }
            socket.impl = socketImpl;
            throw e3;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
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

    public ServerSocketChannel getChannel() {
        return null;
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

    public synchronized void setSoTimeout(int i2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        getImpl().setOption(SocketOptions.SO_TIMEOUT, new Integer(i2));
    }

    public synchronized int getSoTimeout() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        }
        Object option = getImpl().getOption(SocketOptions.SO_TIMEOUT);
        if (option instanceof Integer) {
            return ((Integer) option).intValue();
        }
        return 0;
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

    public String toString() {
        InetAddress inetAddress;
        if (!isBound()) {
            return "ServerSocket[unbound]";
        }
        if (System.getSecurityManager() != null) {
            inetAddress = InetAddress.getLoopbackAddress();
        } else {
            inetAddress = this.impl.getInetAddress();
        }
        return "ServerSocket[addr=" + ((Object) inetAddress) + ",localport=" + this.impl.getLocalPort() + "]";
    }

    void setBound() {
        this.bound = true;
    }

    void setCreated() {
        this.created = true;
    }

    public static synchronized void setSocketFactory(SocketImplFactory socketImplFactory) throws IOException {
        if (factory != null) {
            throw new SocketException("factory already defined");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        factory = socketImplFactory;
    }

    public synchronized void setReceiveBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("negative receive size");
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

    public void setPerformancePreferences(int i2, int i3, int i4) {
    }
}

package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.net.ConnectionResetException;
import sun.net.NetHooks;
import sun.net.ResourceManager;

/* loaded from: rt.jar:java/net/AbstractPlainSocketImpl.class */
abstract class AbstractPlainSocketImpl extends SocketImpl {
    int timeout;
    private int trafficClass;
    private int resetState;
    protected boolean stream;
    public static final int SHUT_RD = 0;
    public static final int SHUT_WR = 1;
    private boolean shut_rd = false;
    private boolean shut_wr = false;
    private SocketInputStream socketInputStream = null;
    private SocketOutputStream socketOutputStream = null;
    protected int fdUseCount = 0;
    protected final Object fdLock = new Object();
    protected boolean closePending = false;
    private int CONNECTION_NOT_RESET = 0;
    private int CONNECTION_RESET_PENDING = 1;
    private int CONNECTION_RESET = 2;
    private final Object resetLock = new Object();

    abstract void socketCreate(boolean z2) throws IOException;

    abstract void socketConnect(InetAddress inetAddress, int i2, int i3) throws IOException;

    abstract void socketBind(InetAddress inetAddress, int i2) throws IOException;

    abstract void socketListen(int i2) throws IOException;

    abstract void socketAccept(SocketImpl socketImpl) throws IOException;

    abstract int socketAvailable() throws IOException;

    abstract void socketClose0(boolean z2) throws IOException;

    abstract void socketShutdown(int i2) throws IOException;

    abstract void socketSetOption(int i2, boolean z2, Object obj) throws SocketException;

    abstract int socketGetOption(int i2, Object obj) throws SocketException;

    abstract void socketSendUrgentData(int i2) throws IOException;

    AbstractPlainSocketImpl() {
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.AbstractPlainSocketImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("net");
                return null;
            }
        });
    }

    @Override // java.net.SocketImpl
    protected synchronized void create(boolean z2) throws IOException {
        this.stream = z2;
        if (!z2) {
            ResourceManager.beforeUdpCreate();
            this.fd = new FileDescriptor();
            try {
                socketCreate(false);
            } catch (IOException e2) {
                ResourceManager.afterUdpClose();
                this.fd = null;
                throw e2;
            }
        } else {
            this.fd = new FileDescriptor();
            socketCreate(true);
        }
        if (this.socket != null) {
            this.socket.setCreated();
        }
        if (this.serverSocket != null) {
            this.serverSocket.setCreated();
        }
    }

    @Override // java.net.SocketImpl
    protected void connect(String str, int i2) throws IOException {
        boolean z2 = false;
        try {
            InetAddress byName = InetAddress.getByName(str);
            this.port = i2;
            this.address = byName;
            connectToAddress(byName, i2, this.timeout);
            z2 = true;
            if (1 == 0) {
                try {
                    close();
                } catch (IOException e2) {
                }
            }
        } catch (Throwable th) {
            if (!z2) {
                try {
                    close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    @Override // java.net.SocketImpl
    protected void connect(InetAddress inetAddress, int i2) throws IOException {
        this.port = i2;
        this.address = inetAddress;
        try {
            connectToAddress(inetAddress, i2, this.timeout);
        } catch (IOException e2) {
            close();
            throw e2;
        }
    }

    @Override // java.net.SocketImpl
    protected void connect(SocketAddress socketAddress, int i2) throws IOException {
        if (socketAddress != null) {
            try {
                if (socketAddress instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                    if (inetSocketAddress.isUnresolved()) {
                        throw new UnknownHostException(inetSocketAddress.getHostName());
                    }
                    this.port = inetSocketAddress.getPort();
                    this.address = inetSocketAddress.getAddress();
                    connectToAddress(this.address, this.port, i2);
                    if (1 == 0) {
                        try {
                            close();
                            return;
                        } catch (IOException e2) {
                            return;
                        }
                    }
                    return;
                }
            } catch (Throwable th) {
                if (0 == 0) {
                    try {
                        close();
                    } catch (IOException e3) {
                    }
                }
                throw th;
            }
        }
        throw new IllegalArgumentException("unsupported address type");
    }

    private void connectToAddress(InetAddress inetAddress, int i2, int i3) throws IOException {
        if (inetAddress.isAnyLocalAddress()) {
            doConnect(InetAddress.getLocalHost(), i2, i3);
        } else {
            doConnect(inetAddress, i2, i3);
        }
    }

    @Override // java.net.SocketOptions
    public void setOption(int i2, Object obj) throws SocketException {
        if (isClosedOrPending()) {
            throw new SocketException("Socket Closed");
        }
        boolean zBooleanValue = true;
        switch (i2) {
            case 1:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad parameter for TCP_NODELAY");
                }
                zBooleanValue = ((Boolean) obj).booleanValue();
                break;
                break;
            case 3:
                if (obj == null || !(obj instanceof Integer)) {
                    throw new SocketException("bad argument for IP_TOS");
                }
                this.trafficClass = ((Integer) obj).intValue();
                break;
            case 4:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad parameter for SO_REUSEADDR");
                }
                zBooleanValue = ((Boolean) obj).booleanValue();
                break;
                break;
            case 8:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad parameter for SO_KEEPALIVE");
                }
                zBooleanValue = ((Boolean) obj).booleanValue();
                break;
                break;
            case 15:
                throw new SocketException("Cannot re-bind socket");
            case 128:
                if (obj == null || (!(obj instanceof Integer) && !(obj instanceof Boolean))) {
                    throw new SocketException("Bad parameter for option");
                }
                if (obj instanceof Boolean) {
                    zBooleanValue = false;
                    break;
                }
                break;
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                if (obj == null || !(obj instanceof Integer) || ((Integer) obj).intValue() <= 0) {
                    throw new SocketException("bad parameter for SO_SNDBUF or SO_RCVBUF");
                }
                break;
            case SocketOptions.SO_OOBINLINE /* 4099 */:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad parameter for SO_OOBINLINE");
                }
                zBooleanValue = ((Boolean) obj).booleanValue();
                break;
            case SocketOptions.SO_TIMEOUT /* 4102 */:
                if (obj == null || !(obj instanceof Integer)) {
                    throw new SocketException("Bad parameter for SO_TIMEOUT");
                }
                int iIntValue = ((Integer) obj).intValue();
                if (iIntValue < 0) {
                    throw new IllegalArgumentException("timeout < 0");
                }
                this.timeout = iIntValue;
                break;
                break;
            default:
                throw new SocketException("unrecognized TCP option: " + i2);
        }
        socketSetOption(i2, zBooleanValue, obj);
    }

    @Override // java.net.SocketOptions
    public Object getOption(int i2) throws SocketException {
        if (isClosedOrPending()) {
            throw new SocketException("Socket Closed");
        }
        if (i2 == 4102) {
            return new Integer(this.timeout);
        }
        switch (i2) {
            case 1:
                return Boolean.valueOf(socketGetOption(i2, null) != -1);
            case 3:
                try {
                    int iSocketGetOption = socketGetOption(i2, null);
                    if (iSocketGetOption == -1) {
                        return Integer.valueOf(this.trafficClass);
                    }
                    return Integer.valueOf(iSocketGetOption);
                } catch (SocketException e2) {
                    return Integer.valueOf(this.trafficClass);
                }
            case 4:
                return Boolean.valueOf(socketGetOption(i2, null) != -1);
            case 8:
                return Boolean.valueOf(socketGetOption(i2, null) != -1);
            case 15:
                InetAddressContainer inetAddressContainer = new InetAddressContainer();
                socketGetOption(i2, inetAddressContainer);
                return inetAddressContainer.addr;
            case 128:
                int iSocketGetOption2 = socketGetOption(i2, null);
                return iSocketGetOption2 == -1 ? Boolean.FALSE : new Integer(iSocketGetOption2);
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                return new Integer(socketGetOption(i2, null));
            case SocketOptions.SO_OOBINLINE /* 4099 */:
                return Boolean.valueOf(socketGetOption(i2, null) != -1);
            default:
                return null;
        }
    }

    /* JADX WARN: Finally extract failed */
    synchronized void doConnect(InetAddress inetAddress, int i2, int i3) throws IOException {
        synchronized (this.fdLock) {
            if (!this.closePending && (this.socket == null || !this.socket.isBound())) {
                NetHooks.beforeTcpConnect(this.fd, inetAddress, i2);
            }
        }
        try {
            acquireFD();
            try {
                socketConnect(inetAddress, i2, i3);
                synchronized (this.fdLock) {
                    if (this.closePending) {
                        throw new SocketException("Socket closed");
                    }
                }
                if (this.socket != null) {
                    this.socket.setBound();
                    this.socket.setConnected();
                }
                releaseFD();
            } catch (Throwable th) {
                releaseFD();
                throw th;
            }
        } catch (IOException e2) {
            close();
            throw e2;
        }
    }

    @Override // java.net.SocketImpl
    protected synchronized void bind(InetAddress inetAddress, int i2) throws IOException {
        synchronized (this.fdLock) {
            if (!this.closePending && (this.socket == null || !this.socket.isBound())) {
                NetHooks.beforeTcpBind(this.fd, inetAddress, i2);
            }
        }
        socketBind(inetAddress, i2);
        if (this.socket != null) {
            this.socket.setBound();
        }
        if (this.serverSocket != null) {
            this.serverSocket.setBound();
        }
    }

    @Override // java.net.SocketImpl
    protected synchronized void listen(int i2) throws IOException {
        socketListen(i2);
    }

    @Override // java.net.SocketImpl
    protected void accept(SocketImpl socketImpl) throws IOException {
        acquireFD();
        try {
            socketAccept(socketImpl);
        } finally {
            releaseFD();
        }
    }

    @Override // java.net.SocketImpl
    protected synchronized InputStream getInputStream() throws IOException {
        synchronized (this.fdLock) {
            if (isClosedOrPending()) {
                throw new IOException("Socket Closed");
            }
            if (this.shut_rd) {
                throw new IOException("Socket input is shutdown");
            }
            if (this.socketInputStream == null) {
                this.socketInputStream = new SocketInputStream(this);
            }
        }
        return this.socketInputStream;
    }

    void setInputStream(SocketInputStream socketInputStream) {
        this.socketInputStream = socketInputStream;
    }

    @Override // java.net.SocketImpl
    protected synchronized OutputStream getOutputStream() throws IOException {
        synchronized (this.fdLock) {
            if (isClosedOrPending()) {
                throw new IOException("Socket Closed");
            }
            if (this.shut_wr) {
                throw new IOException("Socket output is shutdown");
            }
            if (this.socketOutputStream == null) {
                this.socketOutputStream = new SocketOutputStream(this);
            }
        }
        return this.socketOutputStream;
    }

    void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.fd = fileDescriptor;
    }

    void setAddress(InetAddress inetAddress) {
        this.address = inetAddress;
    }

    void setPort(int i2) {
        this.port = i2;
    }

    void setLocalPort(int i2) {
        this.localport = i2;
    }

    @Override // java.net.SocketImpl
    protected synchronized int available() throws IOException {
        if (isClosedOrPending()) {
            throw new IOException("Stream closed.");
        }
        if (isConnectionReset() || this.shut_rd) {
            return 0;
        }
        int iSocketAvailable = 0;
        try {
            iSocketAvailable = socketAvailable();
            if (iSocketAvailable == 0 && isConnectionResetPending()) {
                setConnectionReset();
            }
        } catch (ConnectionResetException e2) {
            setConnectionResetPending();
            try {
                iSocketAvailable = socketAvailable();
                if (iSocketAvailable == 0) {
                    setConnectionReset();
                }
            } catch (ConnectionResetException e3) {
            }
        }
        return iSocketAvailable;
    }

    @Override // java.net.SocketImpl
    protected void close() throws IOException {
        synchronized (this.fdLock) {
            if (this.fd != null) {
                if (!this.stream) {
                    ResourceManager.afterUdpClose();
                }
                if (this.fdUseCount == 0) {
                    if (this.closePending) {
                        return;
                    }
                    this.closePending = true;
                    try {
                        socketPreClose();
                        socketClose();
                        this.fd = null;
                        return;
                    } catch (Throwable th) {
                        socketClose();
                        throw th;
                    }
                }
                if (!this.closePending) {
                    this.closePending = true;
                    this.fdUseCount--;
                    socketPreClose();
                }
            }
        }
    }

    @Override // java.net.SocketImpl
    void reset() throws IOException {
        if (this.fd != null) {
            socketClose();
        }
        this.fd = null;
        super.reset();
    }

    @Override // java.net.SocketImpl
    protected void shutdownInput() throws IOException {
        if (this.fd != null) {
            socketShutdown(0);
            if (this.socketInputStream != null) {
                this.socketInputStream.setEOF(true);
            }
            this.shut_rd = true;
        }
    }

    @Override // java.net.SocketImpl
    protected void shutdownOutput() throws IOException {
        if (this.fd != null) {
            socketShutdown(1);
            this.shut_wr = true;
        }
    }

    @Override // java.net.SocketImpl
    protected boolean supportsUrgentData() {
        return true;
    }

    @Override // java.net.SocketImpl
    protected void sendUrgentData(int i2) throws IOException {
        if (this.fd == null) {
            throw new IOException("Socket Closed");
        }
        socketSendUrgentData(i2);
    }

    protected void finalize() throws IOException {
        close();
    }

    FileDescriptor acquireFD() {
        FileDescriptor fileDescriptor;
        synchronized (this.fdLock) {
            this.fdUseCount++;
            fileDescriptor = this.fd;
        }
        return fileDescriptor;
    }

    void releaseFD() {
        synchronized (this.fdLock) {
            this.fdUseCount--;
            if (this.fdUseCount == -1 && this.fd != null) {
                try {
                    try {
                        socketClose();
                        this.fd = null;
                    } catch (IOException e2) {
                        this.fd = null;
                    }
                } catch (Throwable th) {
                    this.fd = null;
                    throw th;
                }
            }
        }
    }

    public boolean isConnectionReset() {
        boolean z2;
        synchronized (this.resetLock) {
            z2 = this.resetState == this.CONNECTION_RESET;
        }
        return z2;
    }

    public boolean isConnectionResetPending() {
        boolean z2;
        synchronized (this.resetLock) {
            z2 = this.resetState == this.CONNECTION_RESET_PENDING;
        }
        return z2;
    }

    public void setConnectionReset() {
        synchronized (this.resetLock) {
            this.resetState = this.CONNECTION_RESET;
        }
    }

    public void setConnectionResetPending() {
        synchronized (this.resetLock) {
            if (this.resetState == this.CONNECTION_NOT_RESET) {
                this.resetState = this.CONNECTION_RESET_PENDING;
            }
        }
    }

    public boolean isClosedOrPending() {
        synchronized (this.fdLock) {
            if (this.closePending || this.fd == null) {
                return true;
            }
            return false;
        }
    }

    public int getTimeout() {
        return this.timeout;
    }

    private void socketPreClose() throws IOException {
        socketClose0(true);
    }

    protected void socketClose() throws IOException {
        socketClose0(false);
    }
}

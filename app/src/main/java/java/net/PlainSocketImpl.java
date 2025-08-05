package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:java/net/PlainSocketImpl.class */
class PlainSocketImpl extends AbstractPlainSocketImpl {
    private AbstractPlainSocketImpl impl;
    private static float version;
    private static boolean preferIPv4Stack = false;
    private static boolean useDualStackImpl;
    private static String exclBindProp;
    private static boolean exclusiveBind;

    static {
        useDualStackImpl = false;
        exclusiveBind = true;
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.net.PlainSocketImpl.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !PlainSocketImpl.class.desiredAssertionStatus();
            }

            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                float unused = PlainSocketImpl.version = 0.0f;
                try {
                    float unused2 = PlainSocketImpl.version = Float.parseFloat(System.getProperties().getProperty("os.version"));
                    boolean unused3 = PlainSocketImpl.preferIPv4Stack = Boolean.parseBoolean(System.getProperties().getProperty("java.net.preferIPv4Stack"));
                    String unused4 = PlainSocketImpl.exclBindProp = System.getProperty("sun.net.useExclusiveBind");
                    return null;
                } catch (NumberFormatException e2) {
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError(e2);
                }
            }
        });
        if (version >= 6.0d && !preferIPv4Stack) {
            useDualStackImpl = true;
        }
        if (exclBindProp != null) {
            exclusiveBind = exclBindProp.length() == 0 ? true : Boolean.parseBoolean(exclBindProp);
        } else if (version < 6.0d) {
            exclusiveBind = false;
        }
    }

    PlainSocketImpl() {
        if (useDualStackImpl) {
            this.impl = new DualStackPlainSocketImpl(exclusiveBind);
        } else {
            this.impl = new TwoStacksPlainSocketImpl(exclusiveBind);
        }
    }

    PlainSocketImpl(FileDescriptor fileDescriptor) {
        if (useDualStackImpl) {
            this.impl = new DualStackPlainSocketImpl(fileDescriptor, exclusiveBind);
        } else {
            this.impl = new TwoStacksPlainSocketImpl(fileDescriptor, exclusiveBind);
        }
    }

    @Override // java.net.SocketImpl
    protected FileDescriptor getFileDescriptor() {
        return this.impl.getFileDescriptor();
    }

    @Override // java.net.SocketImpl
    protected InetAddress getInetAddress() {
        return this.impl.getInetAddress();
    }

    @Override // java.net.SocketImpl
    protected int getPort() {
        return this.impl.getPort();
    }

    @Override // java.net.SocketImpl
    protected int getLocalPort() {
        return this.impl.getLocalPort();
    }

    @Override // java.net.SocketImpl
    void setSocket(Socket socket) {
        this.impl.setSocket(socket);
    }

    @Override // java.net.SocketImpl
    Socket getSocket() {
        return this.impl.getSocket();
    }

    @Override // java.net.SocketImpl
    void setServerSocket(ServerSocket serverSocket) {
        this.impl.setServerSocket(serverSocket);
    }

    @Override // java.net.SocketImpl
    ServerSocket getServerSocket() {
        return this.impl.getServerSocket();
    }

    @Override // java.net.SocketImpl
    public String toString() {
        return this.impl.toString();
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized void create(boolean z2) throws IOException {
        this.impl.create(z2);
        this.fd = this.impl.fd;
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void connect(String str, int i2) throws IOException {
        this.impl.connect(str, i2);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void connect(InetAddress inetAddress, int i2) throws IOException {
        this.impl.connect(inetAddress, i2);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void connect(SocketAddress socketAddress, int i2) throws IOException {
        this.impl.connect(socketAddress, i2);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketOptions
    public void setOption(int i2, Object obj) throws SocketException {
        this.impl.setOption(i2, obj);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketOptions
    public Object getOption(int i2) throws SocketException {
        return this.impl.getOption(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    synchronized void doConnect(InetAddress inetAddress, int i2, int i3) throws IOException {
        this.impl.doConnect(inetAddress, i2, i3);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized void bind(InetAddress inetAddress, int i2) throws IOException {
        this.impl.bind(inetAddress, i2);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized void accept(SocketImpl socketImpl) throws IOException {
        if (socketImpl instanceof PlainSocketImpl) {
            AbstractPlainSocketImpl abstractPlainSocketImpl = ((PlainSocketImpl) socketImpl).impl;
            abstractPlainSocketImpl.address = new InetAddress();
            abstractPlainSocketImpl.fd = new FileDescriptor();
            this.impl.accept(abstractPlainSocketImpl);
            socketImpl.fd = abstractPlainSocketImpl.fd;
            return;
        }
        this.impl.accept(socketImpl);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.impl.setFileDescriptor(fileDescriptor);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void setAddress(InetAddress inetAddress) {
        this.impl.setAddress(inetAddress);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void setPort(int i2) {
        this.impl.setPort(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void setLocalPort(int i2) {
        this.impl.setLocalPort(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized InputStream getInputStream() throws IOException {
        return this.impl.getInputStream();
    }

    @Override // java.net.AbstractPlainSocketImpl
    void setInputStream(SocketInputStream socketInputStream) {
        this.impl.setInputStream(socketInputStream);
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized OutputStream getOutputStream() throws IOException {
        return this.impl.getOutputStream();
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void close() throws IOException {
        try {
            this.impl.close();
        } finally {
            this.fd = null;
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    void reset() throws IOException {
        try {
            this.impl.reset();
        } finally {
            this.fd = null;
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void shutdownInput() throws IOException {
        this.impl.shutdownInput();
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void shutdownOutput() throws IOException {
        this.impl.shutdownOutput();
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void sendUrgentData(int i2) throws IOException {
        this.impl.sendUrgentData(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    FileDescriptor acquireFD() {
        return this.impl.acquireFD();
    }

    @Override // java.net.AbstractPlainSocketImpl
    void releaseFD() {
        this.impl.releaseFD();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public boolean isConnectionReset() {
        return this.impl.isConnectionReset();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public boolean isConnectionResetPending() {
        return this.impl.isConnectionResetPending();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public void setConnectionReset() {
        this.impl.setConnectionReset();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public void setConnectionResetPending() {
        this.impl.setConnectionResetPending();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public boolean isClosedOrPending() {
        return this.impl.isClosedOrPending();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public int getTimeout() {
        return this.impl.getTimeout();
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketCreate(boolean z2) throws IOException {
        this.impl.socketCreate(z2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketConnect(InetAddress inetAddress, int i2, int i3) throws IOException {
        this.impl.socketConnect(inetAddress, i2, i3);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketBind(InetAddress inetAddress, int i2) throws IOException {
        this.impl.socketBind(inetAddress, i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketListen(int i2) throws IOException {
        this.impl.socketListen(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketAccept(SocketImpl socketImpl) throws IOException {
        this.impl.socketAccept(socketImpl);
    }

    @Override // java.net.AbstractPlainSocketImpl
    int socketAvailable() throws IOException {
        return this.impl.socketAvailable();
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketClose0(boolean z2) throws IOException {
        this.impl.socketClose0(z2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketShutdown(int i2) throws IOException {
        this.impl.socketShutdown(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketSetOption(int i2, boolean z2, Object obj) throws SocketException {
        this.impl.socketSetOption(i2, z2, obj);
    }

    @Override // java.net.AbstractPlainSocketImpl
    int socketGetOption(int i2, Object obj) throws SocketException {
        return this.impl.socketGetOption(i2, obj);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketSendUrgentData(int i2) throws IOException {
        this.impl.socketSendUrgentData(i2);
    }
}

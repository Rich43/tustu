package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.net.ResourceManager;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/net/AbstractPlainDatagramSocketImpl.class */
abstract class AbstractPlainDatagramSocketImpl extends DatagramSocketImpl {
    int timeout = 0;
    boolean connected = false;
    private int trafficClass = 0;
    protected InetAddress connectedAddress = null;
    private int connectedPort = -1;
    private static final String os = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
    private static final boolean connectDisabled = os.contains("OS X");

    protected abstract void bind0(int i2, InetAddress inetAddress) throws SocketException;

    @Override // java.net.DatagramSocketImpl
    protected abstract void send(DatagramPacket datagramPacket) throws IOException;

    @Override // java.net.DatagramSocketImpl
    protected abstract int peek(InetAddress inetAddress) throws IOException;

    @Override // java.net.DatagramSocketImpl
    protected abstract int peekData(DatagramPacket datagramPacket) throws IOException;

    protected abstract void receive0(DatagramPacket datagramPacket) throws IOException;

    @Override // java.net.DatagramSocketImpl
    protected abstract void setTimeToLive(int i2) throws IOException;

    @Override // java.net.DatagramSocketImpl
    protected abstract int getTimeToLive() throws IOException;

    @Override // java.net.DatagramSocketImpl
    @Deprecated
    protected abstract void setTTL(byte b2) throws IOException;

    @Override // java.net.DatagramSocketImpl
    @Deprecated
    protected abstract byte getTTL() throws IOException;

    protected abstract void join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    protected abstract void leave(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    protected abstract void datagramSocketCreate() throws SocketException;

    protected abstract void datagramSocketClose();

    protected abstract void socketSetOption(int i2, Object obj) throws SocketException;

    protected abstract Object socketGetOption(int i2) throws SocketException;

    protected abstract void connect0(InetAddress inetAddress, int i2) throws SocketException;

    protected abstract void disconnect0(int i2);

    @Override // java.net.DatagramSocketImpl
    abstract int dataAvailable();

    AbstractPlainDatagramSocketImpl() {
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.AbstractPlainDatagramSocketImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("net");
                return null;
            }
        });
    }

    @Override // java.net.DatagramSocketImpl
    protected synchronized void create() throws SocketException {
        ResourceManager.beforeUdpCreate();
        this.fd = new FileDescriptor();
        try {
            datagramSocketCreate();
        } catch (SocketException e2) {
            ResourceManager.afterUdpClose();
            this.fd = null;
            throw e2;
        }
    }

    @Override // java.net.DatagramSocketImpl
    protected synchronized void bind(int i2, InetAddress inetAddress) throws SocketException {
        bind0(i2, inetAddress);
    }

    @Override // java.net.DatagramSocketImpl
    protected void connect(InetAddress inetAddress, int i2) throws SocketException {
        connect0(inetAddress, i2);
        this.connectedAddress = inetAddress;
        this.connectedPort = i2;
        this.connected = true;
    }

    @Override // java.net.DatagramSocketImpl
    protected void disconnect() {
        disconnect0(this.connectedAddress.holder().getFamily());
        this.connected = false;
        this.connectedAddress = null;
        this.connectedPort = -1;
    }

    @Override // java.net.DatagramSocketImpl
    protected synchronized void receive(DatagramPacket datagramPacket) throws IOException {
        receive0(datagramPacket);
    }

    @Override // java.net.DatagramSocketImpl
    protected void join(InetAddress inetAddress) throws IOException {
        join(inetAddress, null);
    }

    @Override // java.net.DatagramSocketImpl
    protected void leave(InetAddress inetAddress) throws IOException {
        leave(inetAddress, null);
    }

    @Override // java.net.DatagramSocketImpl
    protected void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        join(((InetSocketAddress) socketAddress).getAddress(), networkInterface);
    }

    @Override // java.net.DatagramSocketImpl
    protected void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        leave(((InetSocketAddress) socketAddress).getAddress(), networkInterface);
    }

    @Override // java.net.DatagramSocketImpl
    protected void close() {
        if (this.fd != null) {
            datagramSocketClose();
            ResourceManager.afterUdpClose();
            this.fd = null;
        }
    }

    protected boolean isClosed() {
        return this.fd == null;
    }

    protected void finalize() {
        close();
    }

    @Override // java.net.SocketOptions
    public void setOption(int i2, Object obj) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket Closed");
        }
        switch (i2) {
            case 3:
                if (obj == null || !(obj instanceof Integer)) {
                    throw new SocketException("bad argument for IP_TOS");
                }
                this.trafficClass = ((Integer) obj).intValue();
                break;
                break;
            case 4:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad argument for SO_REUSEADDR");
                }
                break;
            case 15:
                throw new SocketException("Cannot re-bind Socket");
            case 16:
                if (obj == null || !(obj instanceof InetAddress)) {
                    throw new SocketException("bad argument for IP_MULTICAST_IF");
                }
                break;
            case 18:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad argument for IP_MULTICAST_LOOP");
                }
                break;
            case 31:
                if (obj == null || !(obj instanceof NetworkInterface)) {
                    throw new SocketException("bad argument for IP_MULTICAST_IF2");
                }
                break;
            case 32:
                if (obj == null || !(obj instanceof Boolean)) {
                    throw new SocketException("bad argument for SO_BROADCAST");
                }
                break;
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                if (obj == null || !(obj instanceof Integer) || ((Integer) obj).intValue() < 0) {
                    throw new SocketException("bad argument for SO_SNDBUF or SO_RCVBUF");
                }
                break;
            case SocketOptions.SO_TIMEOUT /* 4102 */:
                if (obj == null || !(obj instanceof Integer)) {
                    throw new SocketException("bad argument for SO_TIMEOUT");
                }
                int iIntValue = ((Integer) obj).intValue();
                if (iIntValue < 0) {
                    throw new IllegalArgumentException("timeout < 0");
                }
                this.timeout = iIntValue;
                return;
            default:
                throw new SocketException("invalid option: " + i2);
        }
        socketSetOption(i2, obj);
    }

    @Override // java.net.SocketOptions
    public Object getOption(int i2) throws SocketException {
        Object objSocketGetOption;
        if (isClosed()) {
            throw new SocketException("Socket Closed");
        }
        switch (i2) {
            case 3:
                objSocketGetOption = socketGetOption(i2);
                if (((Integer) objSocketGetOption).intValue() == -1) {
                    objSocketGetOption = new Integer(this.trafficClass);
                    break;
                }
                break;
            case 4:
            case 15:
            case 16:
            case 18:
            case 31:
            case 32:
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                objSocketGetOption = socketGetOption(i2);
                break;
            case SocketOptions.SO_TIMEOUT /* 4102 */:
                objSocketGetOption = new Integer(this.timeout);
                break;
            default:
                throw new SocketException("invalid option: " + i2);
        }
        return objSocketGetOption;
    }

    protected boolean nativeConnectDisabled() {
        return connectDisabled;
    }
}

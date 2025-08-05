package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import sun.net.ResourceManager;

/* loaded from: rt.jar:java/net/TwoStacksPlainDatagramSocketImpl.class */
class TwoStacksPlainDatagramSocketImpl extends AbstractPlainDatagramSocketImpl {
    private FileDescriptor fd1;
    private InetAddress anyLocalBoundAddr = null;
    private int fduse = -1;
    private int lastfd = -1;
    private final boolean exclusiveBind;
    private boolean reuseAddressEmulated;
    private boolean isReuseAddress;

    protected native synchronized void bind0(int i2, InetAddress inetAddress, boolean z2) throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected native void send(DatagramPacket datagramPacket) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected native synchronized int peek(InetAddress inetAddress) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected native synchronized int peekData(DatagramPacket datagramPacket) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native synchronized void receive0(DatagramPacket datagramPacket) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected native void setTimeToLive(int i2) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected native int getTimeToLive() throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    @Deprecated
    protected native void setTTL(byte b2) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    @Deprecated
    protected native byte getTTL() throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void leave(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void datagramSocketCreate() throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void datagramSocketClose();

    protected native void socketNativeSetOption(int i2, Object obj) throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native Object socketGetOption(int i2) throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void connect0(InetAddress inetAddress, int i2) throws SocketException;

    protected native Object socketLocalAddress(int i2) throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected native void disconnect0(int i2);

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    native int dataAvailable();

    private static native void init();

    static {
        init();
    }

    TwoStacksPlainDatagramSocketImpl(boolean z2) {
        this.exclusiveBind = z2;
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected synchronized void create() throws SocketException {
        this.fd1 = new FileDescriptor();
        try {
            super.create();
        } catch (SocketException e2) {
            this.fd1 = null;
            throw e2;
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected synchronized void bind(int i2, InetAddress inetAddress) throws SocketException {
        super.bind(i2, inetAddress);
        if (inetAddress.isAnyLocalAddress()) {
            this.anyLocalBoundAddr = inetAddress;
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected synchronized void bind0(int i2, InetAddress inetAddress) throws SocketException {
        bind0(i2, inetAddress, this.exclusiveBind);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected synchronized void receive(DatagramPacket datagramPacket) throws IOException {
        try {
            receive0(datagramPacket);
        } finally {
            this.fduse = -1;
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.SocketOptions
    public Object getOption(int i2) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket Closed");
        }
        if (i2 == 15) {
            if (this.fd != null && this.fd1 != null && !this.connected) {
                return this.anyLocalBoundAddr;
            }
            return socketLocalAddress(this.connectedAddress == null ? -1 : this.connectedAddress.holder().getFamily());
        }
        if (i2 == 4 && this.reuseAddressEmulated) {
            return Boolean.valueOf(this.isReuseAddress);
        }
        return super.getOption(i2);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void socketSetOption(int i2, Object obj) throws SocketException {
        if (i2 == 4 && this.exclusiveBind && this.localPort != 0) {
            this.reuseAddressEmulated = true;
            this.isReuseAddress = ((Boolean) obj).booleanValue();
        } else {
            socketNativeSetOption(i2, obj);
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected boolean isClosed() {
        return this.fd == null && this.fd1 == null;
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected void close() {
        if (this.fd != null || this.fd1 != null) {
            datagramSocketClose();
            ResourceManager.afterUdpClose();
            this.fd = null;
            this.fd1 = null;
        }
    }
}

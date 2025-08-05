package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import sun.net.ResourceManager;

/* loaded from: rt.jar:java/net/TwoStacksPlainSocketImpl.class */
class TwoStacksPlainSocketImpl extends AbstractPlainSocketImpl {
    private FileDescriptor fd1;
    private InetAddress anyLocalBoundAddr = null;
    private int lastfd = -1;
    private final boolean exclusiveBind;
    private boolean isReuseAddress;

    static native void initProto();

    @Override // java.net.AbstractPlainSocketImpl
    native void socketCreate(boolean z2) throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketConnect(InetAddress inetAddress, int i2, int i3) throws IOException;

    native void socketBind(InetAddress inetAddress, int i2, boolean z2) throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketListen(int i2) throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketAccept(SocketImpl socketImpl) throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native int socketAvailable() throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketClose0(boolean z2) throws IOException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketShutdown(int i2) throws IOException;

    native void socketNativeSetOption(int i2, boolean z2, Object obj) throws SocketException;

    @Override // java.net.AbstractPlainSocketImpl
    native int socketGetOption(int i2, Object obj) throws SocketException;

    @Override // java.net.AbstractPlainSocketImpl
    native void socketSendUrgentData(int i2) throws IOException;

    static {
        initProto();
    }

    public TwoStacksPlainSocketImpl(boolean z2) {
        this.exclusiveBind = z2;
    }

    public TwoStacksPlainSocketImpl(FileDescriptor fileDescriptor, boolean z2) {
        this.fd = fileDescriptor;
        this.exclusiveBind = z2;
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized void create(boolean z2) throws IOException {
        this.fd1 = new FileDescriptor();
        try {
            super.create(z2);
        } catch (IOException e2) {
            this.fd1 = null;
            throw e2;
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected synchronized void bind(InetAddress inetAddress, int i2) throws IOException {
        super.bind(inetAddress, i2);
        if (inetAddress.isAnyLocalAddress()) {
            this.anyLocalBoundAddr = inetAddress;
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketOptions
    public Object getOption(int i2) throws SocketException {
        if (isClosedOrPending()) {
            throw new SocketException("Socket Closed");
        }
        if (i2 == 15) {
            if (this.fd != null && this.fd1 != null) {
                return this.anyLocalBoundAddr;
            }
            InetAddressContainer inetAddressContainer = new InetAddressContainer();
            socketGetOption(i2, inetAddressContainer);
            return inetAddressContainer.addr;
        }
        if (i2 == 4 && this.exclusiveBind) {
            return Boolean.valueOf(this.isReuseAddress);
        }
        return super.getOption(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketBind(InetAddress inetAddress, int i2) throws IOException {
        socketBind(inetAddress, i2, this.exclusiveBind);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketSetOption(int i2, boolean z2, Object obj) throws SocketException {
        if (i2 == 4 && this.exclusiveBind) {
            this.isReuseAddress = z2;
        } else {
            socketNativeSetOption(i2, z2, obj);
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void close() throws IOException {
        synchronized (this.fdLock) {
            if (this.fd != null || this.fd1 != null) {
                if (!this.stream) {
                    ResourceManager.afterUdpClose();
                }
                if (this.fdUseCount == 0) {
                    if (this.closePending) {
                        return;
                    }
                    this.closePending = true;
                    socketClose();
                    this.fd = null;
                    this.fd1 = null;
                    return;
                }
                if (!this.closePending) {
                    this.closePending = true;
                    this.fdUseCount--;
                    socketClose();
                }
            }
        }
    }

    @Override // java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    void reset() throws IOException {
        if (this.fd != null || this.fd1 != null) {
            socketClose();
        }
        this.fd = null;
        this.fd1 = null;
        super.reset();
    }

    @Override // java.net.AbstractPlainSocketImpl
    public boolean isClosedOrPending() {
        synchronized (this.fdLock) {
            if (this.closePending || (this.fd == null && this.fd1 == null)) {
                return true;
            }
            return false;
        }
    }
}

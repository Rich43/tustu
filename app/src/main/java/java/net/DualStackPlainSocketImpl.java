package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/net/DualStackPlainSocketImpl.class */
class DualStackPlainSocketImpl extends AbstractPlainSocketImpl {
    static JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
    private final boolean exclusiveBind;
    private boolean isReuseAddress;
    static final int WOULDBLOCK = -2;

    static native void initIDs();

    static native int socket0(boolean z2, boolean z3) throws IOException;

    static native void bind0(int i2, InetAddress inetAddress, int i3, boolean z2) throws IOException;

    static native int connect0(int i2, InetAddress inetAddress, int i3) throws IOException;

    static native void waitForConnect(int i2, int i3) throws IOException;

    static native int localPort0(int i2) throws IOException;

    static native void localAddress(int i2, InetAddressContainer inetAddressContainer) throws SocketException;

    static native void listen0(int i2, int i3) throws IOException;

    static native int accept0(int i2, InetSocketAddress[] inetSocketAddressArr) throws IOException;

    static native void waitForNewConnection(int i2, int i3) throws IOException;

    static native int available0(int i2) throws IOException;

    static native void close0(int i2) throws IOException;

    static native void shutdown0(int i2, int i3) throws IOException;

    static native void setIntOption(int i2, int i3, int i4) throws SocketException;

    static native int getIntOption(int i2, int i3) throws SocketException;

    static native void sendOOB(int i2, int i3) throws IOException;

    static native void configureBlocking(int i2, boolean z2) throws IOException;

    static {
        initIDs();
    }

    public DualStackPlainSocketImpl(boolean z2) {
        this.exclusiveBind = z2;
    }

    public DualStackPlainSocketImpl(FileDescriptor fileDescriptor, boolean z2) {
        this.fd = fileDescriptor;
        this.exclusiveBind = z2;
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketCreate(boolean z2) throws IOException {
        if (this.fd == null) {
            throw new SocketException("Socket closed");
        }
        fdAccess.set(this.fd, socket0(z2, false));
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketConnect(InetAddress inetAddress, int i2, int i3) throws IOException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (inetAddress == null) {
            throw new NullPointerException("inet address argument is null.");
        }
        if (i3 <= 0) {
            connect0(iCheckAndReturnNativeFD, inetAddress, i2);
        } else {
            configureBlocking(iCheckAndReturnNativeFD, false);
            try {
                if (connect0(iCheckAndReturnNativeFD, inetAddress, i2) == -2) {
                    waitForConnect(iCheckAndReturnNativeFD, i3);
                }
            } finally {
                configureBlocking(iCheckAndReturnNativeFD, true);
            }
        }
        if (this.localport == 0) {
            this.localport = localPort0(iCheckAndReturnNativeFD);
        }
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketBind(InetAddress inetAddress, int i2) throws IOException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (inetAddress == null) {
            throw new NullPointerException("inet address argument is null.");
        }
        bind0(iCheckAndReturnNativeFD, inetAddress, i2, this.exclusiveBind);
        if (i2 == 0) {
            this.localport = localPort0(iCheckAndReturnNativeFD);
        } else {
            this.localport = i2;
        }
        this.address = inetAddress;
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketListen(int i2) throws IOException {
        listen0(checkAndReturnNativeFD(), i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketAccept(SocketImpl socketImpl) throws IOException {
        int iAccept0;
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (socketImpl == null) {
            throw new NullPointerException("socket is null");
        }
        InetSocketAddress[] inetSocketAddressArr = new InetSocketAddress[1];
        if (this.timeout <= 0) {
            iAccept0 = accept0(iCheckAndReturnNativeFD, inetSocketAddressArr);
        } else {
            configureBlocking(iCheckAndReturnNativeFD, false);
            try {
                waitForNewConnection(iCheckAndReturnNativeFD, this.timeout);
                iAccept0 = accept0(iCheckAndReturnNativeFD, inetSocketAddressArr);
                if (iAccept0 != -1) {
                    configureBlocking(iAccept0, true);
                }
            } finally {
                configureBlocking(iCheckAndReturnNativeFD, true);
            }
        }
        fdAccess.set(socketImpl.fd, iAccept0);
        InetSocketAddress inetSocketAddress = inetSocketAddressArr[0];
        socketImpl.port = inetSocketAddress.getPort();
        socketImpl.address = inetSocketAddress.getAddress();
        socketImpl.localport = this.localport;
    }

    @Override // java.net.AbstractPlainSocketImpl
    int socketAvailable() throws IOException {
        return available0(checkAndReturnNativeFD());
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketClose0(boolean z2) throws IOException {
        if (this.fd == null) {
            throw new SocketException("Socket closed");
        }
        if (!this.fd.valid()) {
            return;
        }
        int i2 = fdAccess.get(this.fd);
        fdAccess.set(this.fd, -1);
        close0(i2);
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketShutdown(int i2) throws IOException {
        shutdown0(checkAndReturnNativeFD(), i2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0071  */
    @Override // java.net.AbstractPlainSocketImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void socketSetOption(int r5, boolean r6, java.lang.Object r7) throws java.net.SocketException {
        /*
            r4 = this;
            r0 = r4
            int r0 = r0.checkAndReturnNativeFD()
            r8 = r0
            r0 = r5
            r1 = 4102(0x1006, float:5.748E-42)
            if (r0 != r1) goto Le
            return
        Le:
            r0 = 0
            r9 = r0
            r0 = r5
            switch(r0) {
                case 1: goto L69;
                case 3: goto L77;
                case 4: goto L5c;
                case 8: goto L69;
                case 128: goto L83;
                case 4097: goto L77;
                case 4098: goto L77;
                case 4099: goto L69;
                default: goto L99;
            }
        L5c:
            r0 = r4
            boolean r0 = r0.exclusiveBind
            if (r0 == 0) goto L69
            r0 = r4
            r1 = r6
            r0.isReuseAddress = r1
            return
        L69:
            r0 = r6
            if (r0 == 0) goto L71
            r0 = 1
            goto L72
        L71:
            r0 = 0
        L72:
            r9 = r0
            goto La3
        L77:
            r0 = r7
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r9 = r0
            goto La3
        L83:
            r0 = r6
            if (r0 == 0) goto L93
            r0 = r7
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r9 = r0
            goto La3
        L93:
            r0 = -1
            r9 = r0
            goto La3
        L99:
            java.net.SocketException r0 = new java.net.SocketException
            r1 = r0
            java.lang.String r2 = "Option not supported"
            r1.<init>(r2)
            throw r0
        La3:
            r0 = r8
            r1 = r5
            r2 = r9
            setIntOption(r0, r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.DualStackPlainSocketImpl.socketSetOption(int, boolean, java.lang.Object):void");
    }

    @Override // java.net.AbstractPlainSocketImpl
    int socketGetOption(int i2, Object obj) throws SocketException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (i2 == 15) {
            localAddress(iCheckAndReturnNativeFD, (InetAddressContainer) obj);
            return 0;
        }
        if (i2 == 4 && this.exclusiveBind) {
            return this.isReuseAddress ? 1 : -1;
        }
        int intOption = getIntOption(iCheckAndReturnNativeFD, i2);
        switch (i2) {
            case 1:
            case 4:
            case 8:
            case SocketOptions.SO_OOBINLINE /* 4099 */:
                return intOption == 0 ? -1 : 1;
            default:
                return intOption;
        }
    }

    @Override // java.net.AbstractPlainSocketImpl
    void socketSendUrgentData(int i2) throws IOException {
        sendOOB(checkAndReturnNativeFD(), i2);
    }

    private int checkAndReturnNativeFD() throws SocketException {
        if (this.fd == null || !this.fd.valid()) {
            throw new SocketException("Socket closed");
        }
        return fdAccess.get(this.fd);
    }
}

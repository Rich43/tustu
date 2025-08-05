package java.net;

import java.io.IOException;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/net/DualStackPlainDatagramSocketImpl.class */
class DualStackPlainDatagramSocketImpl extends AbstractPlainDatagramSocketImpl {
    static JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
    private final boolean exclusiveBind;
    private boolean reuseAddressEmulated;
    private boolean isReuseAddress;

    private static native void initIDs();

    private static native int socketCreate(boolean z2);

    private static native void socketBind(int i2, InetAddress inetAddress, int i3, boolean z2) throws SocketException;

    private static native void socketConnect(int i2, InetAddress inetAddress, int i3) throws SocketException;

    private static native void socketDisconnect(int i2);

    private static native void socketClose(int i2);

    private static native int socketLocalPort(int i2) throws SocketException;

    private static native Object socketLocalAddress(int i2) throws SocketException;

    private static native int socketReceiveOrPeekData(int i2, DatagramPacket datagramPacket, int i3, boolean z2, boolean z3) throws IOException;

    private static native void socketSend(int i2, byte[] bArr, int i3, int i4, InetAddress inetAddress, int i5, boolean z2) throws IOException;

    private static native void socketSetIntOption(int i2, int i3, int i4) throws SocketException;

    private static native int socketGetIntOption(int i2, int i3) throws SocketException;

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    native int dataAvailable();

    static {
        initIDs();
    }

    DualStackPlainDatagramSocketImpl(boolean z2) {
        this.exclusiveBind = z2;
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void datagramSocketCreate() throws SocketException {
        if (this.fd == null) {
            throw new SocketException("Socket closed");
        }
        fdAccess.set(this.fd, socketCreate(false));
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected synchronized void bind0(int i2, InetAddress inetAddress) throws SocketException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (inetAddress == null) {
            throw new NullPointerException("argument address");
        }
        socketBind(iCheckAndReturnNativeFD, inetAddress, i2, this.exclusiveBind);
        if (i2 == 0) {
            this.localPort = socketLocalPort(iCheckAndReturnNativeFD);
        } else {
            this.localPort = i2;
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected synchronized int peek(InetAddress inetAddress) throws IOException {
        checkAndReturnNativeFD();
        if (inetAddress == null) {
            throw new NullPointerException("Null address in peek()");
        }
        DatagramPacket datagramPacket = new DatagramPacket(new byte[1], 1);
        int iPeekData = peekData(datagramPacket);
        datagramPacket.getAddress();
        return iPeekData;
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected synchronized int peekData(DatagramPacket datagramPacket) throws IOException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (datagramPacket == null) {
            throw new NullPointerException("packet");
        }
        if (datagramPacket.getData() == null) {
            throw new NullPointerException("packet buffer");
        }
        return socketReceiveOrPeekData(iCheckAndReturnNativeFD, datagramPacket, this.timeout, this.connected, true);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected synchronized void receive0(DatagramPacket datagramPacket) throws IOException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (datagramPacket == null) {
            throw new NullPointerException("packet");
        }
        if (datagramPacket.getData() == null) {
            throw new NullPointerException("packet buffer");
        }
        socketReceiveOrPeekData(iCheckAndReturnNativeFD, datagramPacket, this.timeout, this.connected, false);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected void send(DatagramPacket datagramPacket) throws IOException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (datagramPacket == null) {
            throw new NullPointerException("null packet");
        }
        if (datagramPacket.getAddress() == null || datagramPacket.getData() == null) {
            throw new NullPointerException("null address || null buffer");
        }
        socketSend(iCheckAndReturnNativeFD, datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), datagramPacket.getAddress(), datagramPacket.getPort(), this.connected);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void connect0(InetAddress inetAddress, int i2) throws SocketException {
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (inetAddress == null) {
            throw new NullPointerException("address");
        }
        socketConnect(iCheckAndReturnNativeFD, inetAddress, i2);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void disconnect0(int i2) {
        if (this.fd == null || !this.fd.valid()) {
            return;
        }
        socketDisconnect(fdAccess.get(this.fd));
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void datagramSocketClose() {
        if (this.fd == null || !this.fd.valid()) {
            return;
        }
        socketClose(fdAccess.get(this.fd));
        fdAccess.set(this.fd, -1);
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void socketSetOption(int i2, Object obj) throws SocketException {
        int iIntValue;
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        switch (i2) {
            case 3:
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                iIntValue = ((Integer) obj).intValue();
                socketSetIntOption(iCheckAndReturnNativeFD, i2, iIntValue);
                return;
            case 4:
                if (this.exclusiveBind && this.localPort != 0) {
                    this.reuseAddressEmulated = true;
                    this.isReuseAddress = ((Boolean) obj).booleanValue();
                    return;
                }
                break;
            case 32:
                iIntValue = ((Boolean) obj).booleanValue() ? 1 : 0;
                socketSetIntOption(iCheckAndReturnNativeFD, i2, iIntValue);
                return;
            default:
                throw new SocketException("Option not supported");
        }
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected Object socketGetOption(int i2) throws SocketException {
        Object num;
        int iCheckAndReturnNativeFD = checkAndReturnNativeFD();
        if (i2 == 15) {
            return socketLocalAddress(iCheckAndReturnNativeFD);
        }
        if (i2 == 4 && this.reuseAddressEmulated) {
            return Boolean.valueOf(this.isReuseAddress);
        }
        int iSocketGetIntOption = socketGetIntOption(iCheckAndReturnNativeFD, i2);
        switch (i2) {
            case 3:
            case 4097:
            case SocketOptions.SO_RCVBUF /* 4098 */:
                num = new Integer(iSocketGetIntOption);
                break;
            case 4:
            case 32:
                num = iSocketGetIntOption == 0 ? Boolean.FALSE : Boolean.TRUE;
                break;
            default:
                throw new SocketException("Option not supported");
        }
        return num;
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException {
        throw new IOException("Method not implemented!");
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl
    protected void leave(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException {
        throw new IOException("Method not implemented!");
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected void setTimeToLive(int i2) throws IOException {
        throw new IOException("Method not implemented!");
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    protected int getTimeToLive() throws IOException {
        throw new IOException("Method not implemented!");
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    @Deprecated
    protected void setTTL(byte b2) throws IOException {
        throw new IOException("Method not implemented!");
    }

    @Override // java.net.AbstractPlainDatagramSocketImpl, java.net.DatagramSocketImpl
    @Deprecated
    protected byte getTTL() throws IOException {
        throw new IOException("Method not implemented!");
    }

    private int checkAndReturnNativeFD() throws SocketException {
        if (this.fd == null || !this.fd.valid()) {
            throw new SocketException("Socket closed");
        }
        return fdAccess.get(this.fd);
    }
}

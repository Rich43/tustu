package java.net;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:java/net/DatagramPacket.class */
public final class DatagramPacket {
    byte[] buf;
    int offset;
    int length;
    int bufLength;
    InetAddress address;
    int port;

    private static native void init();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.DatagramPacket.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                return null;
            }
        });
        init();
    }

    public DatagramPacket(byte[] bArr, int i2, int i3) {
        setData(bArr, i2, i3);
        this.address = null;
        this.port = -1;
    }

    public DatagramPacket(byte[] bArr, int i2) {
        this(bArr, 0, i2);
    }

    public DatagramPacket(byte[] bArr, int i2, int i3, InetAddress inetAddress, int i4) {
        setData(bArr, i2, i3);
        setAddress(inetAddress);
        setPort(i4);
    }

    public DatagramPacket(byte[] bArr, int i2, int i3, SocketAddress socketAddress) {
        setData(bArr, i2, i3);
        setSocketAddress(socketAddress);
    }

    public DatagramPacket(byte[] bArr, int i2, InetAddress inetAddress, int i3) {
        this(bArr, 0, i2, inetAddress, i3);
    }

    public DatagramPacket(byte[] bArr, int i2, SocketAddress socketAddress) {
        this(bArr, 0, i2, socketAddress);
    }

    public synchronized InetAddress getAddress() {
        return this.address;
    }

    public synchronized int getPort() {
        return this.port;
    }

    public synchronized byte[] getData() {
        return this.buf;
    }

    public synchronized int getOffset() {
        return this.offset;
    }

    public synchronized int getLength() {
        return this.length;
    }

    public synchronized void setData(byte[] bArr, int i2, int i3) {
        if (i3 < 0 || i2 < 0 || i3 + i2 < 0 || i3 + i2 > bArr.length) {
            throw new IllegalArgumentException("illegal length or offset");
        }
        this.buf = bArr;
        this.length = i3;
        this.bufLength = i3;
        this.offset = i2;
    }

    public synchronized void setAddress(InetAddress inetAddress) {
        this.address = inetAddress;
    }

    public synchronized void setPort(int i2) {
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException("Port out of range:" + i2);
        }
        this.port = i2;
    }

    public synchronized void setSocketAddress(SocketAddress socketAddress) {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (inetSocketAddress.isUnresolved()) {
            throw new IllegalArgumentException("unresolved address");
        }
        setAddress(inetSocketAddress.getAddress());
        setPort(inetSocketAddress.getPort());
    }

    public synchronized SocketAddress getSocketAddress() {
        return new InetSocketAddress(getAddress(), getPort());
    }

    public synchronized void setData(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("null packet buffer");
        }
        this.buf = bArr;
        this.offset = 0;
        this.length = bArr.length;
        this.bufLength = bArr.length;
    }

    public synchronized void setLength(int i2) {
        if (i2 + this.offset > this.buf.length || i2 < 0 || i2 + this.offset < 0) {
            throw new IllegalArgumentException("illegal length");
        }
        this.length = i2;
        this.bufLength = this.length;
    }
}

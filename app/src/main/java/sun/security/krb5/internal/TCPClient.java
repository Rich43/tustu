package sun.security.krb5.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import sun.misc.IOUtils;

/* compiled from: NetClient.java */
/* loaded from: rt.jar:sun/security/krb5/internal/TCPClient.class */
class TCPClient extends NetClient {
    private Socket tcpSocket = new Socket();
    private BufferedOutputStream out;
    private BufferedInputStream in;

    TCPClient(String str, int i2, int i3) throws IOException {
        this.tcpSocket.connect(new InetSocketAddress(str, i2), i3);
        this.out = new BufferedOutputStream(this.tcpSocket.getOutputStream());
        this.in = new BufferedInputStream(this.tcpSocket.getInputStream());
        this.tcpSocket.setSoTimeout(i3);
    }

    @Override // sun.security.krb5.internal.NetClient
    public void send(byte[] bArr) throws IOException {
        byte[] bArr2 = new byte[4];
        intToNetworkByteOrder(bArr.length, bArr2, 0, 4);
        this.out.write(bArr2);
        this.out.write(bArr);
        this.out.flush();
    }

    @Override // sun.security.krb5.internal.NetClient
    public byte[] receive() throws IOException {
        byte[] bArr = new byte[4];
        int fully = readFully(bArr, 4);
        if (fully != 4) {
            if (Krb5.DEBUG) {
                System.out.println(">>>DEBUG: TCPClient could not read length field");
                return null;
            }
            return null;
        }
        int iNetworkByteOrderToInt = networkByteOrderToInt(bArr, 0, 4);
        if (Krb5.DEBUG) {
            System.out.println(">>>DEBUG: TCPClient reading " + iNetworkByteOrderToInt + " bytes");
        }
        if (iNetworkByteOrderToInt <= 0) {
            if (Krb5.DEBUG) {
                System.out.println(">>>DEBUG: TCPClient zero or negative length field: " + iNetworkByteOrderToInt);
                return null;
            }
            return null;
        }
        try {
            return IOUtils.readExactlyNBytes(this.in, iNetworkByteOrderToInt);
        } catch (IOException e2) {
            if (Krb5.DEBUG) {
                System.out.println(">>>DEBUG: TCPClient could not read complete packet (" + iNetworkByteOrderToInt + "/" + fully + ")");
                return null;
            }
            return null;
        }
    }

    @Override // sun.security.krb5.internal.NetClient, java.lang.AutoCloseable
    public void close() throws IOException {
        this.tcpSocket.close();
    }

    private int readFully(byte[] bArr, int i2) throws IOException {
        int i3 = 0;
        while (i2 > 0) {
            int i4 = this.in.read(bArr, i3, i2);
            if (i4 == -1) {
                if (i3 == 0) {
                    return -1;
                }
                return i3;
            }
            i3 += i4;
            i2 -= i4;
        }
        return i3;
    }

    private static int networkByteOrderToInt(byte[] bArr, int i2, int i3) {
        if (i3 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            i4 = (i4 << 8) | (bArr[i2 + i5] & 255);
        }
        return i4;
    }

    private static void intToNetworkByteOrder(int i2, byte[] bArr, int i3, int i4) {
        if (i4 > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }
        for (int i5 = i4 - 1; i5 >= 0; i5--) {
            bArr[i3 + i5] = (byte) (i2 & 255);
            i2 >>>= 8;
        }
    }
}

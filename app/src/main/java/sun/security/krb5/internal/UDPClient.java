package sun.security.krb5.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.UnknownHostException;

/* compiled from: NetClient.java */
/* loaded from: rt.jar:sun/security/krb5/internal/UDPClient.class */
class UDPClient extends NetClient {
    InetAddress iaddr;
    int iport;
    int bufSize = 65507;
    DatagramSocket dgSocket = new DatagramSocket();
    DatagramPacket dgPacketIn;

    UDPClient(String str, int i2, int i3) throws SocketException, UnknownHostException {
        this.iaddr = InetAddress.getByName(str);
        this.iport = i2;
        this.dgSocket.setSoTimeout(i3);
        this.dgSocket.connect(this.iaddr, this.iport);
    }

    @Override // sun.security.krb5.internal.NetClient
    public void send(byte[] bArr) throws IOException {
        this.dgSocket.send(new DatagramPacket(bArr, bArr.length, this.iaddr, this.iport));
    }

    @Override // sun.security.krb5.internal.NetClient
    public byte[] receive() throws IOException {
        byte[] bArr = new byte[this.bufSize];
        this.dgPacketIn = new DatagramPacket(bArr, bArr.length);
        try {
            this.dgSocket.receive(this.dgPacketIn);
        } catch (SocketException e2) {
            if (e2 instanceof PortUnreachableException) {
                throw e2;
            }
            this.dgSocket.receive(this.dgPacketIn);
        }
        byte[] bArr2 = new byte[this.dgPacketIn.getLength()];
        System.arraycopy(this.dgPacketIn.getData(), 0, bArr2, 0, this.dgPacketIn.getLength());
        return bArr2;
    }

    @Override // sun.security.krb5.internal.NetClient, java.lang.AutoCloseable
    public void close() {
        this.dgSocket.close();
    }
}

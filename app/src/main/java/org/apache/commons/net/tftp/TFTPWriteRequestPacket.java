package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/tftp/TFTPWriteRequestPacket.class */
public final class TFTPWriteRequestPacket extends TFTPRequestPacket {
    public TFTPWriteRequestPacket(InetAddress destination, int port, String filename, int mode) {
        super(destination, port, 2, filename, mode);
    }

    TFTPWriteRequestPacket(DatagramPacket datagram) throws TFTPPacketException {
        super(2, datagram);
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " WRQ " + getFilename() + " " + TFTP.getModeName(getMode());
    }
}

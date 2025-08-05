package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/tftp/TFTPPacket.class */
public abstract class TFTPPacket {
    static final int MIN_PACKET_SIZE = 4;
    public static final int READ_REQUEST = 1;
    public static final int WRITE_REQUEST = 2;
    public static final int DATA = 3;
    public static final int ACKNOWLEDGEMENT = 4;
    public static final int ERROR = 5;
    public static final int SEGMENT_SIZE = 512;
    int _type;
    int _port;
    InetAddress _address;

    abstract DatagramPacket _newDatagram(DatagramPacket datagramPacket, byte[] bArr);

    public abstract DatagramPacket newDatagram();

    public static final TFTPPacket newTFTPPacket(DatagramPacket datagram) throws TFTPPacketException {
        TFTPPacket packet;
        if (datagram.getLength() < 4) {
            throw new TFTPPacketException("Bad packet. Datagram data length is too short.");
        }
        byte[] data = datagram.getData();
        switch (data[1]) {
            case 1:
                packet = new TFTPReadRequestPacket(datagram);
                break;
            case 2:
                packet = new TFTPWriteRequestPacket(datagram);
                break;
            case 3:
                packet = new TFTPDataPacket(datagram);
                break;
            case 4:
                packet = new TFTPAckPacket(datagram);
                break;
            case 5:
                packet = new TFTPErrorPacket(datagram);
                break;
            default:
                throw new TFTPPacketException("Bad packet.  Invalid TFTP operator code.");
        }
        return packet;
    }

    TFTPPacket(int type, InetAddress address, int port) {
        this._type = type;
        this._address = address;
        this._port = port;
    }

    public final int getType() {
        return this._type;
    }

    public final InetAddress getAddress() {
        return this._address;
    }

    public final int getPort() {
        return this._port;
    }

    public final void setPort(int port) {
        this._port = port;
    }

    public final void setAddress(InetAddress address) {
        this._address = address;
    }

    public String toString() {
        return ((Object) this._address) + " " + this._port + " " + this._type;
    }
}

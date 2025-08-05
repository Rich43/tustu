package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/tftp/TFTPAckPacket.class */
public final class TFTPAckPacket extends TFTPPacket {
    int _blockNumber;

    public TFTPAckPacket(InetAddress destination, int port, int blockNumber) {
        super(4, destination, port);
        this._blockNumber = blockNumber;
    }

    TFTPAckPacket(DatagramPacket datagram) throws TFTPPacketException {
        super(4, datagram.getAddress(), datagram.getPort());
        byte[] data = datagram.getData();
        if (getType() != data[1]) {
            throw new TFTPPacketException("TFTP operator code does not match type.");
        }
        this._blockNumber = ((data[2] & 255) << 8) | (data[3] & 255);
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
        data[0] = 0;
        data[1] = (byte) this._type;
        data[2] = (byte) ((this._blockNumber & 65535) >> 8);
        data[3] = (byte) (this._blockNumber & 255);
        datagram.setAddress(this._address);
        datagram.setPort(this._port);
        datagram.setData(data);
        datagram.setLength(4);
        return datagram;
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public DatagramPacket newDatagram() {
        byte[] data = {0, (byte) this._type, (byte) ((this._blockNumber & 65535) >> 8), (byte) (this._blockNumber & 255)};
        return new DatagramPacket(data, data.length, this._address, this._port);
    }

    public int getBlockNumber() {
        return this._blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this._blockNumber = blockNumber;
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " ACK " + this._blockNumber;
    }
}

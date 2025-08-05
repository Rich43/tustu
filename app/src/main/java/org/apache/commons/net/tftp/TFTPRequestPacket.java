package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Locale;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/tftp/TFTPRequestPacket.class */
public abstract class TFTPRequestPacket extends TFTPPacket {
    static final String[] _modeStrings = {"netascii", "octet"};
    private static final byte[][] _modeBytes = {new byte[]{110, 101, 116, 97, 115, 99, 105, 105, 0}, new byte[]{111, 99, 116, 101, 116, 0}};
    private final int _mode;
    private final String _filename;

    TFTPRequestPacket(InetAddress destination, int port, int type, String filename, int mode) {
        super(type, destination, port);
        this._filename = filename;
        this._mode = mode;
    }

    TFTPRequestPacket(int type, DatagramPacket datagram) throws TFTPPacketException {
        super(type, datagram.getAddress(), datagram.getPort());
        byte[] data = datagram.getData();
        if (getType() != data[1]) {
            throw new TFTPPacketException("TFTP operator code does not match type.");
        }
        StringBuilder buffer = new StringBuilder();
        int index = 2;
        int length = datagram.getLength();
        while (index < length && data[index] != 0) {
            buffer.append((char) data[index]);
            index++;
        }
        this._filename = buffer.toString();
        if (index >= length) {
            throw new TFTPPacketException("Bad filename and mode format.");
        }
        buffer.setLength(0);
        while (true) {
            index++;
            if (index >= length || data[index] == 0) {
                break;
            } else {
                buffer.append((char) data[index]);
            }
        }
        String modeString = buffer.toString().toLowerCase(Locale.ENGLISH);
        int length2 = _modeStrings.length;
        int mode = 0;
        int index2 = 0;
        while (true) {
            if (index2 >= length2) {
                break;
            }
            if (!modeString.equals(_modeStrings[index2])) {
                index2++;
            } else {
                mode = index2;
                break;
            }
        }
        this._mode = mode;
        if (index2 >= length2) {
            throw new TFTPPacketException("Unrecognized TFTP transfer mode: " + modeString);
        }
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    final DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
        int fileLength = this._filename.length();
        int modeLength = _modeBytes[this._mode].length;
        data[0] = 0;
        data[1] = (byte) this._type;
        System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
        data[fileLength + 2] = 0;
        System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, modeLength);
        datagram.setAddress(this._address);
        datagram.setPort(this._port);
        datagram.setData(data);
        datagram.setLength(fileLength + modeLength + 3);
        return datagram;
    }

    @Override // org.apache.commons.net.tftp.TFTPPacket
    public final DatagramPacket newDatagram() {
        int fileLength = this._filename.length();
        int modeLength = _modeBytes[this._mode].length;
        byte[] data = new byte[fileLength + modeLength + 4];
        data[0] = 0;
        data[1] = (byte) this._type;
        System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
        data[fileLength + 2] = 0;
        System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, modeLength);
        return new DatagramPacket(data, data.length, this._address, this._port);
    }

    public final int getMode() {
        return this._mode;
    }

    public final String getFilename() {
        return this._filename;
    }
}

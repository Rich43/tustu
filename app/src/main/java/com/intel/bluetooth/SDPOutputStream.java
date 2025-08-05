package com.intel.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.bluetooth.DataElement;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SDPOutputStream.class */
class SDPOutputStream extends OutputStream {
    OutputStream dst;

    public SDPOutputStream(OutputStream out) {
        this.dst = out;
    }

    @Override // java.io.OutputStream
    public void write(int oneByte) throws IOException {
        this.dst.write(oneByte);
    }

    private void writeLong(long l2, int size) throws IOException {
        for (int i2 = 0; i2 < size; i2++) {
            write((int) (l2 >> ((size - 1) << 3)));
            l2 <<= 8;
        }
    }

    private void writeBytes(byte[] b2) throws IOException {
        for (byte b3 : b2) {
            write(b3);
        }
    }

    static int getLength(DataElement d2) {
        int result;
        byte[] b2;
        switch (d2.getDataType()) {
            case 0:
                return 1;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalArgumentException();
            case 8:
            case 16:
            case 40:
                return 2;
            case 9:
            case 17:
                return 3;
            case 10:
            case 18:
                return 5;
            case 11:
            case 19:
                return 9;
            case 12:
            case 20:
                return 17;
            case 24:
                long uuid = Utils.UUIDTo32Bit((UUID) d2.getValue());
                if (uuid == -1) {
                    return 17;
                }
                if (uuid <= 65535) {
                    return 3;
                }
                return 5;
            case 32:
                if (BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_SDP_STRING_ENCODING_ASCII, false)) {
                    b2 = Utils.getASCIIBytes((String) d2.getValue());
                } else {
                    b2 = Utils.getUTF8Bytes((String) d2.getValue());
                }
                if (b2.length < 256) {
                    return b2.length + 2;
                }
                if (b2.length < 65536) {
                    return b2.length + 3;
                }
                return b2.length + 5;
            case 48:
            case 56:
                int result2 = 1;
                Enumeration e2 = (Enumeration) d2.getValue();
                while (e2.hasMoreElements()) {
                    result2 += getLength((DataElement) e2.nextElement2());
                }
                if (result2 < 255) {
                    result = result2 + 1;
                } else if (result2 < 65535) {
                    result = result2 + 2;
                } else {
                    result = result2 + 4;
                }
                return result;
            case 64:
                byte[] b3 = Utils.getASCIIBytes((String) d2.getValue());
                if (b3.length < 256) {
                    return b3.length + 2;
                }
                if (b3.length < 65536) {
                    return b3.length + 3;
                }
                return b3.length + 5;
        }
    }

    void writeElement(DataElement d2) throws IOException {
        int sizeDescriptor;
        int lenSize;
        int sizeDescriptor2;
        int lenSize2;
        byte[] b2;
        switch (d2.getDataType()) {
            case 0:
                write(0);
                return;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IOException();
            case 8:
                write(8);
                writeLong(d2.getLong(), 1);
                return;
            case 9:
                write(9);
                writeLong(d2.getLong(), 2);
                return;
            case 10:
                write(10);
                writeLong(d2.getLong(), 4);
                return;
            case 11:
                write(11);
                writeBytes((byte[]) d2.getValue());
                return;
            case 12:
                write(12);
                writeBytes((byte[]) d2.getValue());
                return;
            case 16:
                write(16);
                writeLong(d2.getLong(), 1);
                return;
            case 17:
                write(17);
                writeLong(d2.getLong(), 2);
                return;
            case 18:
                write(18);
                writeLong(d2.getLong(), 4);
                return;
            case 19:
                write(19);
                writeLong(d2.getLong(), 8);
                return;
            case 20:
                write(20);
                writeBytes((byte[]) d2.getValue());
                return;
            case 24:
                long uuid = Utils.UUIDTo32Bit((UUID) d2.getValue());
                if (uuid == -1) {
                    write(28);
                    writeBytes(Utils.UUIDToByteArray((UUID) d2.getValue()));
                    return;
                } else if (uuid <= 65535) {
                    write(25);
                    writeLong(uuid, 2);
                    return;
                } else {
                    write(26);
                    writeLong(uuid, 4);
                    return;
                }
            case 32:
                if (BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_SDP_STRING_ENCODING_ASCII, false)) {
                    b2 = Utils.getASCIIBytes((String) d2.getValue());
                } else {
                    b2 = Utils.getUTF8Bytes((String) d2.getValue());
                }
                if (b2.length < 256) {
                    write(37);
                    writeLong(b2.length, 1);
                } else if (b2.length < 65536) {
                    write(38);
                    writeLong(b2.length, 2);
                } else {
                    write(39);
                    writeLong(b2.length, 4);
                }
                writeBytes(b2);
                return;
            case 40:
                write(40);
                writeLong(d2.getBoolean() ? 1L : 0L, 1);
                return;
            case 48:
                int len = getLength(d2);
                if (len < 257) {
                    sizeDescriptor2 = 5;
                    lenSize2 = 1;
                } else if (len < 65538) {
                    sizeDescriptor2 = 6;
                    lenSize2 = 2;
                } else {
                    sizeDescriptor2 = 7;
                    lenSize2 = 4;
                }
                write(48 | sizeDescriptor2);
                writeLong(len - (1 + lenSize2), lenSize2);
                Enumeration e2 = (Enumeration) d2.getValue();
                while (e2.hasMoreElements()) {
                    writeElement((DataElement) e2.nextElement2());
                }
                return;
            case 56:
                int len2 = getLength(d2) - 5;
                if (len2 < 255) {
                    sizeDescriptor = 5;
                    lenSize = 1;
                } else if (len2 < 65535) {
                    sizeDescriptor = 6;
                    lenSize = 2;
                } else {
                    sizeDescriptor = 7;
                    lenSize = 4;
                }
                write(56 | sizeDescriptor);
                writeLong(len2, lenSize);
                Enumeration e3 = (Enumeration) d2.getValue();
                while (e3.hasMoreElements()) {
                    writeElement((DataElement) e3.nextElement2());
                }
                return;
            case 64:
                byte[] b3 = Utils.getASCIIBytes((String) d2.getValue());
                if (b3.length < 256) {
                    write(69);
                    writeLong(b3.length, 1);
                } else if (b3.length < 65536) {
                    write(70);
                    writeLong(b3.length, 2);
                } else {
                    write(71);
                    writeLong(b3.length, 4);
                }
                writeBytes(b3);
                return;
        }
    }
}

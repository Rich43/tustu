package com.intel.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.DataElement;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SDPInputStream.class */
class SDPInputStream extends InputStream {
    private InputStream source;
    private int pos = 0;

    public SDPInputStream(InputStream in) {
        this.source = in;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.source.read();
    }

    private long readLong(int size) throws IOException {
        long result = 0;
        for (int i2 = 0; i2 < size; i2++) {
            result = (result << 8) | read();
        }
        this.pos += size;
        return result;
    }

    private int readInteger(int size) throws IOException {
        int result = 0;
        for (int i2 = 0; i2 < size; i2++) {
            result = (result << 8) | read();
        }
        this.pos += size;
        return result;
    }

    private String hexString(byte[] b2) throws IOException {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < b2.length; i2++) {
            buf.append(Integer.toHexString((b2[i2] >> 4) & 15));
            buf.append(Integer.toHexString(b2[i2] & 15));
        }
        return buf.toString();
    }

    private byte[] readBytes(int size) throws IOException {
        byte[] result = new byte[size];
        for (int i2 = 0; i2 < size; i2++) {
            result[i2] = (byte) read();
        }
        this.pos += size;
        return result;
    }

    public DataElement readElement() throws IOException {
        int length;
        int length2;
        int length3;
        int length4;
        UUID uuid;
        int header = read();
        int type = (header >> 3) & 31;
        int sizeDescriptor = header & 7;
        this.pos++;
        switch (type) {
            case 0:
                return new DataElement(0);
            case 1:
                switch (sizeDescriptor) {
                    case 0:
                        return new DataElement(8, readLong(1));
                    case 1:
                        return new DataElement(9, readLong(2));
                    case 2:
                        return new DataElement(10, readLong(4));
                    case 3:
                        return new DataElement(11, readBytes(8));
                    case 4:
                        return new DataElement(12, readBytes(16));
                    default:
                        throw new IOException();
                }
            case 2:
                switch (sizeDescriptor) {
                    case 0:
                        return new DataElement(16, (byte) readLong(1));
                    case 1:
                        return new DataElement(17, (short) readLong(2));
                    case 2:
                        return new DataElement(18, (int) readLong(4));
                    case 3:
                        return new DataElement(19, readLong(8));
                    case 4:
                        return new DataElement(20, readBytes(16));
                    default:
                        throw new IOException();
                }
            case 3:
                switch (sizeDescriptor) {
                    case 1:
                        uuid = new UUID(readLong(2));
                        break;
                    case 2:
                        uuid = new UUID(readLong(4));
                        break;
                    case 3:
                    default:
                        throw new IOException();
                    case 4:
                        uuid = new UUID(hexString(readBytes(16)), false);
                        break;
                }
                return new DataElement(24, uuid);
            case 4:
                switch (sizeDescriptor) {
                    case 5:
                        length4 = readInteger(1);
                        break;
                    case 6:
                        length4 = readInteger(2);
                        break;
                    case 7:
                        length4 = readInteger(4);
                        break;
                    default:
                        throw new IOException();
                }
                String strValue = Utils.newStringUTF8(readBytes(length4));
                DebugLog.debug("DataElement.STRING", strValue, Integer.toString(length4 - strValue.length()));
                return new DataElement(32, strValue);
            case 5:
                return new DataElement(readLong(1) != 0);
            case 6:
                switch (sizeDescriptor) {
                    case 5:
                        length3 = readInteger(1);
                        break;
                    case 6:
                        length3 = readInteger(2);
                        break;
                    case 7:
                        length3 = readInteger(4);
                        break;
                    default:
                        throw new IOException();
                }
                DataElement element = new DataElement(48);
                int started = this.pos;
                int end = this.pos + length3;
                while (this.pos < end) {
                    element.addElement(readElement());
                }
                if (started + length3 != this.pos) {
                    throw new IOException(new StringBuffer().append("DATSEQ size corruption ").append((started + length3) - this.pos).toString());
                }
                return element;
            case 7:
                switch (sizeDescriptor) {
                    case 5:
                        length2 = readInteger(1);
                        break;
                    case 6:
                        length2 = readInteger(2);
                        break;
                    case 7:
                        length2 = readInteger(4);
                        break;
                    default:
                        throw new IOException();
                }
                DataElement element2 = new DataElement(56);
                int started2 = this.pos;
                long end2 = this.pos + length2;
                while (this.pos < end2) {
                    element2.addElement(readElement());
                }
                if (started2 + length2 != this.pos) {
                    throw new IOException(new StringBuffer().append("DATALT size corruption ").append((started2 + length2) - this.pos).toString());
                }
                return element2;
            case 8:
                switch (sizeDescriptor) {
                    case 5:
                        length = readInteger(1);
                        break;
                    case 6:
                        length = readInteger(2);
                        break;
                    case 7:
                        length = readInteger(4);
                        break;
                    default:
                        throw new IOException();
                }
                return new DataElement(64, Utils.newStringASCII(readBytes(length)));
            default:
                throw new IOException(new StringBuffer().append("Unknown type ").append(type).toString());
        }
    }
}

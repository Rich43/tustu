package com.intel.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.DataElement;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackWIDCOMMSDPInputStream.class */
class BluetoothStackWIDCOMMSDPInputStream extends InputStream {
    public static final boolean debug = false;
    private InputStream source;
    static final int ATTR_TYPE_INT = 0;
    static final int ATTR_TYPE_TWO_COMP = 1;
    static final int ATTR_TYPE_UUID = 2;
    static final int ATTR_TYPE_BOOL = 3;
    static final int ATTR_TYPE_ARRAY = 4;
    static final int MAX_SEQ_ENTRIES = 20;
    static final int MAX_ATTR_LEN_OLD = 256;
    private int valueSize = 0;

    protected BluetoothStackWIDCOMMSDPInputStream(InputStream in) throws IOException {
        this.source = in;
        readVersionInfo();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.source.read();
    }

    private long readLong(int size) throws IOException {
        long result = 0;
        for (int i2 = 0; i2 < size; i2++) {
            result += read() << (8 * i2);
        }
        return result;
    }

    private long readLongDebug(int size) throws IOException {
        long result = 0;
        for (int i2 = 0; i2 < size; i2++) {
            int data = read();
            result += data << (8 * i2);
        }
        return result;
    }

    private int readInt() throws IOException {
        return (int) readLong(4);
    }

    private byte[] readBytes(int size) throws IOException {
        byte[] result = new byte[size];
        for (int i2 = 0; i2 < size; i2++) {
            result[i2] = (byte) read();
        }
        return result;
    }

    static String hexString(byte[] b2) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < b2.length; i2++) {
            buf.append(Integer.toHexString((b2[i2] >> 4) & 15));
            buf.append(Integer.toHexString(b2[i2] & 15));
        }
        return buf.toString();
    }

    static byte[] getUUIDHexBytes(UUID uuid) {
        return Utils.UUIDToByteArray(uuid);
    }

    private void readVersionInfo() throws IOException {
        this.valueSize = readInt();
    }

    public DataElement readElement() throws IOException {
        DataElement dataElement;
        UUID uuid;
        DataElement result = null;
        DataElement mainSeq = null;
        DataElement currentSeq = null;
        int elements = readInt();
        if (elements < 0 || elements > 20) {
            throw new IOException(new StringBuffer().append("Unexpected number of elements ").append(elements).toString());
        }
        for (int i2 = 0; i2 < elements; i2++) {
            int type = readInt();
            int length = readInt();
            boolean start_of_seq = readInt() != 0;
            if (length < 0 || this.valueSize < length) {
                throw new IOException(new StringBuffer().append("Unexpected length ").append(length).toString());
            }
            switch (type) {
                case 0:
                    switch (length) {
                        case 1:
                            dataElement = new DataElement(8, readLong(1));
                            break;
                        case 2:
                            dataElement = new DataElement(9, readLong(2));
                            break;
                        case 4:
                            dataElement = new DataElement(10, readLong(4));
                            break;
                        case 8:
                            dataElement = new DataElement(11, readBytes(8));
                            break;
                        case 16:
                            dataElement = new DataElement(12, readBytes(16));
                            break;
                        default:
                            throw new IOException(new StringBuffer().append("Unknown U_INT length ").append(length).toString());
                    }
                case 1:
                    switch (length) {
                        case 1:
                            dataElement = new DataElement(16, (byte) readLong(1));
                            break;
                        case 2:
                            dataElement = new DataElement(17, (short) readLong(2));
                            break;
                        case 4:
                            dataElement = new DataElement(18, (int) readLong(4));
                            break;
                        case 8:
                            dataElement = new DataElement(19, readLongDebug(8));
                            break;
                        case 16:
                            dataElement = new DataElement(20, readBytes(16));
                            break;
                        default:
                            throw new IOException(new StringBuffer().append("Unknown INT length ").append(length).toString());
                    }
                case 2:
                    switch (length) {
                        case 2:
                            uuid = new UUID(readLong(2));
                            break;
                        case 4:
                            uuid = new UUID(readLong(4));
                            break;
                        case 16:
                            uuid = new UUID(hexString(readBytes(16)), false);
                            break;
                        default:
                            throw new IOException(new StringBuffer().append("Unknown UUID length ").append(length).toString());
                    }
                    dataElement = new DataElement(24, uuid);
                    break;
                case 3:
                    dataElement = new DataElement(readLong(length) != 0);
                    break;
                case 4:
                    dataElement = new DataElement(32, Utils.newStringUTF8(readBytes(length)));
                    break;
                default:
                    throw new IOException(new StringBuffer().append("Unknown data type ").append(type).toString());
            }
            if (start_of_seq) {
                DataElement newSeq = new DataElement(48);
                newSeq.addElement(dataElement);
                dataElement = newSeq;
                if (i2 != 0) {
                    if (mainSeq != null) {
                        mainSeq.addElement(newSeq);
                    } else {
                        mainSeq = new DataElement(48);
                        result = mainSeq;
                        mainSeq.addElement(currentSeq);
                        mainSeq.addElement(newSeq);
                    }
                }
                currentSeq = newSeq;
            } else if (currentSeq != null) {
                currentSeq.addElement(dataElement);
            }
            if (result == null) {
                result = dataElement;
            }
            if (i2 < elements - 1 && skip(this.valueSize - length) != this.valueSize - length) {
                throw new IOException("Unexpected end of data");
            }
        }
        return result;
    }
}

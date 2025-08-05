package javax.bluetooth;

import com.intel.bluetooth.Utils;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/DataElement.class */
public class DataElement {
    public static final int NULL = 0;
    public static final int U_INT_1 = 8;
    public static final int U_INT_2 = 9;
    public static final int U_INT_4 = 10;
    public static final int U_INT_8 = 11;
    public static final int U_INT_16 = 12;
    public static final int INT_1 = 16;
    public static final int INT_2 = 17;
    public static final int INT_4 = 18;
    public static final int INT_8 = 19;
    public static final int INT_16 = 20;
    public static final int URL = 64;
    public static final int UUID = 24;
    public static final int BOOL = 40;
    public static final int STRING = 32;
    public static final int DATSEQ = 48;
    public static final int DATALT = 56;
    private Object value;
    private int valueType;

    public DataElement(int valueType) {
        switch (valueType) {
            case 0:
                this.value = null;
                break;
            case 48:
            case 56:
                this.value = new Vector();
                break;
            default:
                throw new IllegalArgumentException(new StringBuffer().append("valueType ").append(typeToString(valueType)).append(" is not DATSEQ, DATALT or NULL").toString());
        }
        this.valueType = valueType;
    }

    public DataElement(boolean bool) {
        this.value = bool ? Boolean.TRUE : Boolean.FALSE;
        this.valueType = 40;
    }

    public DataElement(int valueType, long value) {
        switch (valueType) {
            case 8:
                if (value < 0 || value > 255) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not U_INT_1").toString());
                }
                break;
            case 9:
                if (value < 0 || value > 65535) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not U_INT_2").toString());
                }
                break;
            case 10:
                if (value < 0 || value > 4294967295L) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not U_INT_4").toString());
                }
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                throw new IllegalArgumentException(new StringBuffer().append("type ").append(typeToString(valueType)).append(" can't be represented long").toString());
            case 16:
                if (value < -128 || value > 127) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not INT_1").toString());
                }
                break;
            case 17:
                if (value < -32768 || value > 32767) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not INT_2").toString());
                }
                break;
            case 18:
                if (value < -2147483648L || value > 2147483647L) {
                    throw new IllegalArgumentException(new StringBuffer().append(value).append(" not INT_4").toString());
                }
                break;
            case 19:
                break;
        }
        this.value = new Long(value);
        this.valueType = valueType;
    }

    public DataElement(int valueType, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value param is null");
        }
        switch (valueType) {
            case 11:
                if (!(value instanceof byte[]) || ((byte[]) value).length != 8) {
                    throw new IllegalArgumentException("value param should be byte[8]");
                }
                break;
            case 12:
            case 20:
                if (!(value instanceof byte[]) || ((byte[]) value).length != 16) {
                    throw new IllegalArgumentException("value param should be byte[16]");
                }
                break;
            case 24:
                if (!(value instanceof UUID)) {
                    throw new IllegalArgumentException("value param should be UUID");
                }
                break;
            case 32:
            case 64:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("value param should be String");
                }
                break;
            default:
                throw new IllegalArgumentException(new StringBuffer().append("type ").append(typeToString(valueType)).append(" can't be represented by Object").toString());
        }
        this.value = value;
        this.valueType = valueType;
    }

    public void addElement(DataElement elem) {
        if (elem == null) {
            throw new NullPointerException("elem param is null");
        }
        switch (this.valueType) {
            case 48:
            case 56:
                ((Vector) this.value).addElement(elem);
                return;
            default:
                throw new ClassCastException("DataType is not DATSEQ or DATALT");
        }
    }

    public void insertElementAt(DataElement elem, int index) {
        if (elem == null) {
            throw new NullPointerException("elem param is null");
        }
        switch (this.valueType) {
            case 48:
            case 56:
                ((Vector) this.value).insertElementAt(elem, index);
                return;
            default:
                throw new ClassCastException("DataType is not DATSEQ or DATALT");
        }
    }

    public int getSize() {
        switch (this.valueType) {
            case 48:
            case 56:
                return ((Vector) this.value).size();
            default:
                throw new ClassCastException("DataType is not DATSEQ or DATALT");
        }
    }

    public boolean removeElement(DataElement elem) {
        if (elem == null) {
            throw new NullPointerException("elem param is null");
        }
        switch (this.valueType) {
            case 48:
            case 56:
                return ((Vector) this.value).removeElement(elem);
            default:
                throw new ClassCastException("DataType is not DATSEQ or DATALT");
        }
    }

    public int getDataType() {
        return this.valueType;
    }

    public long getLong() {
        switch (this.valueType) {
            case 8:
            case 9:
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
                return ((Long) this.value).longValue();
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                throw new ClassCastException("DataType is not INT");
        }
    }

    public boolean getBoolean() {
        if (this.valueType == 40) {
            return ((Boolean) this.value).booleanValue();
        }
        throw new ClassCastException("DataType is not BOOL");
    }

    public Object getValue() {
        switch (this.valueType) {
            case 11:
            case 12:
            case 20:
                return Utils.clone((byte[]) this.value);
            case 24:
            case 32:
            case 64:
                return this.value;
            case 48:
            case 56:
                return ((Vector) this.value).elements();
            default:
                throw new ClassCastException("DataType is simple java type");
        }
    }

    private static String typeToString(int type) {
        switch (type) {
            case 0:
                return "NULL";
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
                return new StringBuffer().append("Unknown").append(type).toString();
            case 8:
                return "U_INT_1";
            case 9:
                return "U_INT_2";
            case 10:
                return "U_INT_4";
            case 11:
                return "U_INT_8";
            case 12:
                return "U_INT_16";
            case 16:
                return "INT_1";
            case 17:
                return "INT_2";
            case 18:
                return "INT_4";
            case 19:
                return "INT_8";
            case 20:
                return "INT_16";
            case 24:
                return "UUID";
            case 32:
                return "STRING";
            case 40:
                return "BOOL";
            case 48:
                return "DATSEQ";
            case 56:
                return "DATALT";
            case 64:
                return "URL";
        }
    }

    public String toString() {
        switch (this.valueType) {
            case 8:
            case 9:
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
                return new StringBuffer().append(typeToString(this.valueType)).append(" 0x").append(Utils.toHexString(((Long) this.value).longValue())).toString();
            case 11:
            case 12:
            case 20:
                byte[] b2 = (byte[]) this.value;
                StringBuffer buf = new StringBuffer();
                buf.append(typeToString(this.valueType)).append(" ");
                for (int i2 = 0; i2 < b2.length; i2++) {
                    buf.append(Integer.toHexString((b2[i2] >> 4) & 15));
                    buf.append(Integer.toHexString(b2[i2] & 15));
                }
                return buf.toString();
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
                return new StringBuffer().append("Unknown").append(this.valueType).toString();
            case 24:
            case 32:
            case 40:
            case 64:
                return new StringBuffer().append(typeToString(this.valueType)).append(" ").append(this.value.toString()).toString();
            case 48:
                StringBuffer buf2 = new StringBuffer("DATSEQ {\n");
                Enumeration e2 = ((Vector) this.value).elements();
                while (e2.hasMoreElements()) {
                    buf2.append(e2.nextElement2());
                    buf2.append("\n");
                }
                buf2.append("}");
                return buf2.toString();
            case 56:
                StringBuffer buf3 = new StringBuffer("DATALT {\n");
                Enumeration e3 = ((Vector) this.value).elements();
                while (e3.hasMoreElements()) {
                    buf3.append(e3.nextElement2());
                    buf3.append("\n");
                }
                buf3.append("}");
                return buf3.toString();
        }
    }
}

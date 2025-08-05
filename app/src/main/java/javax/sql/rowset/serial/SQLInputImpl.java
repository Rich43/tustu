package javax.sql.rowset.serial;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/sql/rowset/serial/SQLInputImpl.class */
public class SQLInputImpl implements SQLInput {
    private boolean lastValueWasNull;
    private int idx;
    private Object[] attrib;
    private Map<String, Class<?>> map;

    public SQLInputImpl(Object[] objArr, Map<String, Class<?>> map) throws SQLException {
        if (objArr == null || map == null) {
            throw new SQLException("Cannot instantiate a SQLInputImpl object with null parameters");
        }
        this.attrib = Arrays.copyOf(objArr, objArr.length);
        this.idx = -1;
        this.map = map;
    }

    private Object getNextAttribute() throws SQLException {
        int i2 = this.idx + 1;
        this.idx = i2;
        if (i2 >= this.attrib.length) {
            throw new SQLException("SQLInputImpl exception: Invalid read position");
        }
        this.lastValueWasNull = this.attrib[this.idx] == null;
        return this.attrib[this.idx];
    }

    @Override // java.sql.SQLInput
    public String readString() throws SQLException {
        return (String) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public boolean readBoolean() throws SQLException {
        Boolean bool = (Boolean) getNextAttribute();
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // java.sql.SQLInput
    public byte readByte() throws SQLException {
        Byte b2 = (Byte) getNextAttribute();
        if (b2 == null) {
            return (byte) 0;
        }
        return b2.byteValue();
    }

    @Override // java.sql.SQLInput
    public short readShort() throws SQLException {
        Short sh = (Short) getNextAttribute();
        if (sh == null) {
            return (short) 0;
        }
        return sh.shortValue();
    }

    @Override // java.sql.SQLInput
    public int readInt() throws SQLException {
        Integer num = (Integer) getNextAttribute();
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    @Override // java.sql.SQLInput
    public long readLong() throws SQLException {
        Long l2 = (Long) getNextAttribute();
        if (l2 == null) {
            return 0L;
        }
        return l2.longValue();
    }

    @Override // java.sql.SQLInput
    public float readFloat() throws SQLException {
        Float f2 = (Float) getNextAttribute();
        if (f2 == null) {
            return 0.0f;
        }
        return f2.floatValue();
    }

    @Override // java.sql.SQLInput
    public double readDouble() throws SQLException {
        Double d2 = (Double) getNextAttribute();
        if (d2 == null) {
            return 0.0d;
        }
        return d2.doubleValue();
    }

    @Override // java.sql.SQLInput
    public BigDecimal readBigDecimal() throws SQLException {
        return (BigDecimal) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public byte[] readBytes() throws SQLException {
        return (byte[]) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Date readDate() throws SQLException {
        return (Date) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Time readTime() throws SQLException {
        return (Time) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Timestamp readTimestamp() throws SQLException {
        return (Timestamp) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Reader readCharacterStream() throws SQLException {
        return (Reader) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public InputStream readAsciiStream() throws SQLException {
        return (InputStream) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public InputStream readBinaryStream() throws SQLException {
        return (InputStream) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Object readObject() throws SQLException {
        Object nextAttribute = getNextAttribute();
        if (nextAttribute instanceof Struct) {
            Struct struct = (Struct) nextAttribute;
            Class<?> cls = this.map.get(struct.getSQLTypeName());
            if (cls != null) {
                try {
                    SQLData sQLData = (SQLData) ReflectUtil.newInstance(cls);
                    sQLData.readSQL(new SQLInputImpl(struct.getAttributes(this.map), this.map), struct.getSQLTypeName());
                    return sQLData;
                } catch (Exception e2) {
                    throw new SQLException("Unable to Instantiate: ", e2);
                }
            }
        }
        return nextAttribute;
    }

    @Override // java.sql.SQLInput
    public Ref readRef() throws SQLException {
        return (Ref) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Blob readBlob() throws SQLException {
        return (Blob) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Clob readClob() throws SQLException {
        return (Clob) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public Array readArray() throws SQLException {
        return (Array) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public boolean wasNull() throws SQLException {
        return this.lastValueWasNull;
    }

    @Override // java.sql.SQLInput
    public URL readURL() throws SQLException {
        return (URL) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public NClob readNClob() throws SQLException {
        return (NClob) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public String readNString() throws SQLException {
        return (String) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public SQLXML readSQLXML() throws SQLException {
        return (SQLXML) getNextAttribute();
    }

    @Override // java.sql.SQLInput
    public RowId readRowId() throws SQLException {
        return (RowId) getNextAttribute();
    }
}

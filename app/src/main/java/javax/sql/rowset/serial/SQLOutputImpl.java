package javax.sql.rowset.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.sql.SQLOutput;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;

/* loaded from: rt.jar:javax/sql/rowset/serial/SQLOutputImpl.class */
public class SQLOutputImpl implements SQLOutput {
    private Vector attribs;
    private Map map;

    public SQLOutputImpl(Vector<?> vector, Map<String, ?> map) throws SQLException {
        if (vector == null || map == null) {
            throw new SQLException("Cannot instantiate a SQLOutputImpl instance with null parameters");
        }
        this.attribs = vector;
        this.map = map;
    }

    @Override // java.sql.SQLOutput
    public void writeString(String str) throws SQLException {
        this.attribs.add(str);
    }

    @Override // java.sql.SQLOutput
    public void writeBoolean(boolean z2) throws SQLException {
        this.attribs.add(Boolean.valueOf(z2));
    }

    @Override // java.sql.SQLOutput
    public void writeByte(byte b2) throws SQLException {
        this.attribs.add(Byte.valueOf(b2));
    }

    @Override // java.sql.SQLOutput
    public void writeShort(short s2) throws SQLException {
        this.attribs.add(Short.valueOf(s2));
    }

    @Override // java.sql.SQLOutput
    public void writeInt(int i2) throws SQLException {
        this.attribs.add(Integer.valueOf(i2));
    }

    @Override // java.sql.SQLOutput
    public void writeLong(long j2) throws SQLException {
        this.attribs.add(Long.valueOf(j2));
    }

    @Override // java.sql.SQLOutput
    public void writeFloat(float f2) throws SQLException {
        this.attribs.add(Float.valueOf(f2));
    }

    @Override // java.sql.SQLOutput
    public void writeDouble(double d2) throws SQLException {
        this.attribs.add(Double.valueOf(d2));
    }

    @Override // java.sql.SQLOutput
    public void writeBigDecimal(BigDecimal bigDecimal) throws SQLException {
        this.attribs.add(bigDecimal);
    }

    @Override // java.sql.SQLOutput
    public void writeBytes(byte[] bArr) throws SQLException {
        this.attribs.add(bArr);
    }

    @Override // java.sql.SQLOutput
    public void writeDate(Date date) throws SQLException {
        this.attribs.add(date);
    }

    @Override // java.sql.SQLOutput
    public void writeTime(Time time) throws SQLException {
        this.attribs.add(time);
    }

    @Override // java.sql.SQLOutput
    public void writeTimestamp(Timestamp timestamp) throws SQLException {
        this.attribs.add(timestamp);
    }

    @Override // java.sql.SQLOutput
    public void writeCharacterStream(Reader reader) throws SQLException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (true) {
            try {
                int i2 = bufferedReader.read();
                if (i2 != -1) {
                    char c2 = (char) i2;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(c2);
                    writeString(new String(stringBuffer).concat(bufferedReader.readLine()));
                } else {
                    return;
                }
            } catch (IOException e2) {
                return;
            }
        }
    }

    @Override // java.sql.SQLOutput
    public void writeAsciiStream(InputStream inputStream) throws SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            try {
                int i2 = bufferedReader.read();
                if (i2 != -1) {
                    char c2 = (char) i2;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(c2);
                    writeString(new String(stringBuffer).concat(bufferedReader.readLine()));
                } else {
                    return;
                }
            } catch (IOException e2) {
                throw new SQLException(e2.getMessage());
            }
        }
    }

    @Override // java.sql.SQLOutput
    public void writeBinaryStream(InputStream inputStream) throws SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            try {
                int i2 = bufferedReader.read();
                if (i2 != -1) {
                    char c2 = (char) i2;
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(c2);
                    writeString(new String(stringBuffer).concat(bufferedReader.readLine()));
                } else {
                    return;
                }
            } catch (IOException e2) {
                throw new SQLException(e2.getMessage());
            }
        }
    }

    @Override // java.sql.SQLOutput
    public void writeObject(SQLData sQLData) throws SQLException {
        if (sQLData == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialStruct(sQLData, (Map<String, Class<?>>) this.map));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeRef(Ref ref) throws SQLException {
        if (ref == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialRef(ref));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeBlob(Blob blob) throws SQLException {
        if (blob == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialBlob(blob));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeClob(Clob clob) throws SQLException {
        if (clob == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialClob(clob));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeStruct(Struct struct) throws SQLException {
        this.attribs.add(new SerialStruct(struct, (Map<String, Class<?>>) this.map));
    }

    @Override // java.sql.SQLOutput
    public void writeArray(Array array) throws SQLException {
        if (array == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialArray(array, this.map));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeURL(URL url) throws SQLException {
        if (url == null) {
            this.attribs.add(null);
        } else {
            this.attribs.add(new SerialDatalink(url));
        }
    }

    @Override // java.sql.SQLOutput
    public void writeNString(String str) throws SQLException {
        this.attribs.add(str);
    }

    @Override // java.sql.SQLOutput
    public void writeNClob(NClob nClob) throws SQLException {
        this.attribs.add(nClob);
    }

    @Override // java.sql.SQLOutput
    public void writeRowId(RowId rowId) throws SQLException {
        this.attribs.add(rowId);
    }

    @Override // java.sql.SQLOutput
    public void writeSQLXML(SQLXML sqlxml) throws SQLException {
        this.attribs.add(sqlxml);
    }
}

package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

/* loaded from: rt.jar:java/sql/CallableStatement.class */
public interface CallableStatement extends PreparedStatement {
    void registerOutParameter(int i2, int i3) throws SQLException;

    void registerOutParameter(int i2, int i3, int i4) throws SQLException;

    boolean wasNull() throws SQLException;

    String getString(int i2) throws SQLException;

    boolean getBoolean(int i2) throws SQLException;

    byte getByte(int i2) throws SQLException;

    short getShort(int i2) throws SQLException;

    int getInt(int i2) throws SQLException;

    long getLong(int i2) throws SQLException;

    float getFloat(int i2) throws SQLException;

    double getDouble(int i2) throws SQLException;

    @Deprecated
    BigDecimal getBigDecimal(int i2, int i3) throws SQLException;

    byte[] getBytes(int i2) throws SQLException;

    Date getDate(int i2) throws SQLException;

    Time getTime(int i2) throws SQLException;

    Timestamp getTimestamp(int i2) throws SQLException;

    Object getObject(int i2) throws SQLException;

    BigDecimal getBigDecimal(int i2) throws SQLException;

    Object getObject(int i2, Map<String, Class<?>> map) throws SQLException;

    Ref getRef(int i2) throws SQLException;

    Blob getBlob(int i2) throws SQLException;

    Clob getClob(int i2) throws SQLException;

    Array getArray(int i2) throws SQLException;

    Date getDate(int i2, Calendar calendar) throws SQLException;

    Time getTime(int i2, Calendar calendar) throws SQLException;

    Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException;

    void registerOutParameter(int i2, int i3, String str) throws SQLException;

    void registerOutParameter(String str, int i2) throws SQLException;

    void registerOutParameter(String str, int i2, int i3) throws SQLException;

    void registerOutParameter(String str, int i2, String str2) throws SQLException;

    URL getURL(int i2) throws SQLException;

    void setURL(String str, URL url) throws SQLException;

    void setNull(String str, int i2) throws SQLException;

    void setBoolean(String str, boolean z2) throws SQLException;

    void setByte(String str, byte b2) throws SQLException;

    void setShort(String str, short s2) throws SQLException;

    void setInt(String str, int i2) throws SQLException;

    void setLong(String str, long j2) throws SQLException;

    void setFloat(String str, float f2) throws SQLException;

    void setDouble(String str, double d2) throws SQLException;

    void setBigDecimal(String str, BigDecimal bigDecimal) throws SQLException;

    void setString(String str, String str2) throws SQLException;

    void setBytes(String str, byte[] bArr) throws SQLException;

    void setDate(String str, Date date) throws SQLException;

    void setTime(String str, Time time) throws SQLException;

    void setTimestamp(String str, Timestamp timestamp) throws SQLException;

    void setAsciiStream(String str, InputStream inputStream, int i2) throws SQLException;

    void setBinaryStream(String str, InputStream inputStream, int i2) throws SQLException;

    void setObject(String str, Object obj, int i2, int i3) throws SQLException;

    void setObject(String str, Object obj, int i2) throws SQLException;

    void setObject(String str, Object obj) throws SQLException;

    void setCharacterStream(String str, Reader reader, int i2) throws SQLException;

    void setDate(String str, Date date, Calendar calendar) throws SQLException;

    void setTime(String str, Time time, Calendar calendar) throws SQLException;

    void setTimestamp(String str, Timestamp timestamp, Calendar calendar) throws SQLException;

    void setNull(String str, int i2, String str2) throws SQLException;

    String getString(String str) throws SQLException;

    boolean getBoolean(String str) throws SQLException;

    byte getByte(String str) throws SQLException;

    short getShort(String str) throws SQLException;

    int getInt(String str) throws SQLException;

    long getLong(String str) throws SQLException;

    float getFloat(String str) throws SQLException;

    double getDouble(String str) throws SQLException;

    byte[] getBytes(String str) throws SQLException;

    Date getDate(String str) throws SQLException;

    Time getTime(String str) throws SQLException;

    Timestamp getTimestamp(String str) throws SQLException;

    Object getObject(String str) throws SQLException;

    BigDecimal getBigDecimal(String str) throws SQLException;

    Object getObject(String str, Map<String, Class<?>> map) throws SQLException;

    Ref getRef(String str) throws SQLException;

    Blob getBlob(String str) throws SQLException;

    Clob getClob(String str) throws SQLException;

    Array getArray(String str) throws SQLException;

    Date getDate(String str, Calendar calendar) throws SQLException;

    Time getTime(String str, Calendar calendar) throws SQLException;

    Timestamp getTimestamp(String str, Calendar calendar) throws SQLException;

    URL getURL(String str) throws SQLException;

    RowId getRowId(int i2) throws SQLException;

    RowId getRowId(String str) throws SQLException;

    void setRowId(String str, RowId rowId) throws SQLException;

    void setNString(String str, String str2) throws SQLException;

    void setNCharacterStream(String str, Reader reader, long j2) throws SQLException;

    void setNClob(String str, NClob nClob) throws SQLException;

    void setClob(String str, Reader reader, long j2) throws SQLException;

    void setBlob(String str, InputStream inputStream, long j2) throws SQLException;

    void setNClob(String str, Reader reader, long j2) throws SQLException;

    NClob getNClob(int i2) throws SQLException;

    NClob getNClob(String str) throws SQLException;

    void setSQLXML(String str, SQLXML sqlxml) throws SQLException;

    SQLXML getSQLXML(int i2) throws SQLException;

    SQLXML getSQLXML(String str) throws SQLException;

    String getNString(int i2) throws SQLException;

    String getNString(String str) throws SQLException;

    Reader getNCharacterStream(int i2) throws SQLException;

    Reader getNCharacterStream(String str) throws SQLException;

    Reader getCharacterStream(int i2) throws SQLException;

    Reader getCharacterStream(String str) throws SQLException;

    void setBlob(String str, Blob blob) throws SQLException;

    void setClob(String str, Clob clob) throws SQLException;

    void setAsciiStream(String str, InputStream inputStream, long j2) throws SQLException;

    void setBinaryStream(String str, InputStream inputStream, long j2) throws SQLException;

    void setCharacterStream(String str, Reader reader, long j2) throws SQLException;

    void setAsciiStream(String str, InputStream inputStream) throws SQLException;

    void setBinaryStream(String str, InputStream inputStream) throws SQLException;

    void setCharacterStream(String str, Reader reader) throws SQLException;

    void setNCharacterStream(String str, Reader reader) throws SQLException;

    void setClob(String str, Reader reader) throws SQLException;

    void setBlob(String str, InputStream inputStream) throws SQLException;

    void setNClob(String str, Reader reader) throws SQLException;

    <T> T getObject(int i2, Class<T> cls) throws SQLException;

    <T> T getObject(String str, Class<T> cls) throws SQLException;

    default void setObject(String str, Object obj, SQLType sQLType, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("setObject not implemented");
    }

    default void setObject(String str, Object obj, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("setObject not implemented");
    }

    default void registerOutParameter(int i2, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }

    default void registerOutParameter(int i2, SQLType sQLType, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }

    default void registerOutParameter(int i2, SQLType sQLType, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }

    default void registerOutParameter(String str, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }

    default void registerOutParameter(String str, SQLType sQLType, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }

    default void registerOutParameter(String str, SQLType sQLType, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException("registerOutParameter not implemented");
    }
}

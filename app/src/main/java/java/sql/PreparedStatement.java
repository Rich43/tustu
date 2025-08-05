package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;

/* loaded from: rt.jar:java/sql/PreparedStatement.class */
public interface PreparedStatement extends Statement {
    ResultSet executeQuery() throws SQLException;

    int executeUpdate() throws SQLException;

    void setNull(int i2, int i3) throws SQLException;

    void setBoolean(int i2, boolean z2) throws SQLException;

    void setByte(int i2, byte b2) throws SQLException;

    void setShort(int i2, short s2) throws SQLException;

    void setInt(int i2, int i3) throws SQLException;

    void setLong(int i2, long j2) throws SQLException;

    void setFloat(int i2, float f2) throws SQLException;

    void setDouble(int i2, double d2) throws SQLException;

    void setBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException;

    void setString(int i2, String str) throws SQLException;

    void setBytes(int i2, byte[] bArr) throws SQLException;

    void setDate(int i2, Date date) throws SQLException;

    void setTime(int i2, Time time) throws SQLException;

    void setTimestamp(int i2, Timestamp timestamp) throws SQLException;

    void setAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException;

    @Deprecated
    void setUnicodeStream(int i2, InputStream inputStream, int i3) throws SQLException;

    void setBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException;

    void clearParameters() throws SQLException;

    void setObject(int i2, Object obj, int i3) throws SQLException;

    void setObject(int i2, Object obj) throws SQLException;

    boolean execute() throws SQLException;

    void addBatch() throws SQLException;

    void setCharacterStream(int i2, Reader reader, int i3) throws SQLException;

    void setRef(int i2, Ref ref) throws SQLException;

    void setBlob(int i2, Blob blob) throws SQLException;

    void setClob(int i2, Clob clob) throws SQLException;

    void setArray(int i2, Array array) throws SQLException;

    ResultSetMetaData getMetaData() throws SQLException;

    void setDate(int i2, Date date, Calendar calendar) throws SQLException;

    void setTime(int i2, Time time, Calendar calendar) throws SQLException;

    void setTimestamp(int i2, Timestamp timestamp, Calendar calendar) throws SQLException;

    void setNull(int i2, int i3, String str) throws SQLException;

    void setURL(int i2, URL url) throws SQLException;

    ParameterMetaData getParameterMetaData() throws SQLException;

    void setRowId(int i2, RowId rowId) throws SQLException;

    void setNString(int i2, String str) throws SQLException;

    void setNCharacterStream(int i2, Reader reader, long j2) throws SQLException;

    void setNClob(int i2, NClob nClob) throws SQLException;

    void setClob(int i2, Reader reader, long j2) throws SQLException;

    void setBlob(int i2, InputStream inputStream, long j2) throws SQLException;

    void setNClob(int i2, Reader reader, long j2) throws SQLException;

    void setSQLXML(int i2, SQLXML sqlxml) throws SQLException;

    void setObject(int i2, Object obj, int i3, int i4) throws SQLException;

    void setAsciiStream(int i2, InputStream inputStream, long j2) throws SQLException;

    void setBinaryStream(int i2, InputStream inputStream, long j2) throws SQLException;

    void setCharacterStream(int i2, Reader reader, long j2) throws SQLException;

    void setAsciiStream(int i2, InputStream inputStream) throws SQLException;

    void setBinaryStream(int i2, InputStream inputStream) throws SQLException;

    void setCharacterStream(int i2, Reader reader) throws SQLException;

    void setNCharacterStream(int i2, Reader reader) throws SQLException;

    void setClob(int i2, Reader reader) throws SQLException;

    void setBlob(int i2, InputStream inputStream) throws SQLException;

    void setNClob(int i2, Reader reader) throws SQLException;

    default void setObject(int i2, Object obj, SQLType sQLType, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException("setObject not implemented");
    }

    default void setObject(int i2, Object obj, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("setObject not implemented");
    }

    default long executeLargeUpdate() throws SQLException {
        throw new UnsupportedOperationException("executeLargeUpdate not implemented");
    }
}

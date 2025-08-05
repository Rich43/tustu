package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

/* loaded from: rt.jar:java/sql/ResultSet.class */
public interface ResultSet extends Wrapper, AutoCloseable {
    public static final int FETCH_FORWARD = 1000;
    public static final int FETCH_REVERSE = 1001;
    public static final int FETCH_UNKNOWN = 1002;
    public static final int TYPE_FORWARD_ONLY = 1003;
    public static final int TYPE_SCROLL_INSENSITIVE = 1004;
    public static final int TYPE_SCROLL_SENSITIVE = 1005;
    public static final int CONCUR_READ_ONLY = 1007;
    public static final int CONCUR_UPDATABLE = 1008;
    public static final int HOLD_CURSORS_OVER_COMMIT = 1;
    public static final int CLOSE_CURSORS_AT_COMMIT = 2;

    boolean next() throws SQLException;

    void close() throws SQLException;

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

    InputStream getAsciiStream(int i2) throws SQLException;

    @Deprecated
    InputStream getUnicodeStream(int i2) throws SQLException;

    InputStream getBinaryStream(int i2) throws SQLException;

    String getString(String str) throws SQLException;

    boolean getBoolean(String str) throws SQLException;

    byte getByte(String str) throws SQLException;

    short getShort(String str) throws SQLException;

    int getInt(String str) throws SQLException;

    long getLong(String str) throws SQLException;

    float getFloat(String str) throws SQLException;

    double getDouble(String str) throws SQLException;

    @Deprecated
    BigDecimal getBigDecimal(String str, int i2) throws SQLException;

    byte[] getBytes(String str) throws SQLException;

    Date getDate(String str) throws SQLException;

    Time getTime(String str) throws SQLException;

    Timestamp getTimestamp(String str) throws SQLException;

    InputStream getAsciiStream(String str) throws SQLException;

    @Deprecated
    InputStream getUnicodeStream(String str) throws SQLException;

    InputStream getBinaryStream(String str) throws SQLException;

    SQLWarning getWarnings() throws SQLException;

    void clearWarnings() throws SQLException;

    String getCursorName() throws SQLException;

    ResultSetMetaData getMetaData() throws SQLException;

    Object getObject(int i2) throws SQLException;

    Object getObject(String str) throws SQLException;

    int findColumn(String str) throws SQLException;

    Reader getCharacterStream(int i2) throws SQLException;

    Reader getCharacterStream(String str) throws SQLException;

    BigDecimal getBigDecimal(int i2) throws SQLException;

    BigDecimal getBigDecimal(String str) throws SQLException;

    boolean isBeforeFirst() throws SQLException;

    boolean isAfterLast() throws SQLException;

    boolean isFirst() throws SQLException;

    boolean isLast() throws SQLException;

    void beforeFirst() throws SQLException;

    void afterLast() throws SQLException;

    boolean first() throws SQLException;

    boolean last() throws SQLException;

    int getRow() throws SQLException;

    boolean absolute(int i2) throws SQLException;

    boolean relative(int i2) throws SQLException;

    boolean previous() throws SQLException;

    void setFetchDirection(int i2) throws SQLException;

    int getFetchDirection() throws SQLException;

    void setFetchSize(int i2) throws SQLException;

    int getFetchSize() throws SQLException;

    int getType() throws SQLException;

    int getConcurrency() throws SQLException;

    boolean rowUpdated() throws SQLException;

    boolean rowInserted() throws SQLException;

    boolean rowDeleted() throws SQLException;

    void updateNull(int i2) throws SQLException;

    void updateBoolean(int i2, boolean z2) throws SQLException;

    void updateByte(int i2, byte b2) throws SQLException;

    void updateShort(int i2, short s2) throws SQLException;

    void updateInt(int i2, int i3) throws SQLException;

    void updateLong(int i2, long j2) throws SQLException;

    void updateFloat(int i2, float f2) throws SQLException;

    void updateDouble(int i2, double d2) throws SQLException;

    void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException;

    void updateString(int i2, String str) throws SQLException;

    void updateBytes(int i2, byte[] bArr) throws SQLException;

    void updateDate(int i2, Date date) throws SQLException;

    void updateTime(int i2, Time time) throws SQLException;

    void updateTimestamp(int i2, Timestamp timestamp) throws SQLException;

    void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException;

    void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException;

    void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException;

    void updateObject(int i2, Object obj, int i3) throws SQLException;

    void updateObject(int i2, Object obj) throws SQLException;

    void updateNull(String str) throws SQLException;

    void updateBoolean(String str, boolean z2) throws SQLException;

    void updateByte(String str, byte b2) throws SQLException;

    void updateShort(String str, short s2) throws SQLException;

    void updateInt(String str, int i2) throws SQLException;

    void updateLong(String str, long j2) throws SQLException;

    void updateFloat(String str, float f2) throws SQLException;

    void updateDouble(String str, double d2) throws SQLException;

    void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException;

    void updateString(String str, String str2) throws SQLException;

    void updateBytes(String str, byte[] bArr) throws SQLException;

    void updateDate(String str, Date date) throws SQLException;

    void updateTime(String str, Time time) throws SQLException;

    void updateTimestamp(String str, Timestamp timestamp) throws SQLException;

    void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException;

    void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException;

    void updateCharacterStream(String str, Reader reader, int i2) throws SQLException;

    void updateObject(String str, Object obj, int i2) throws SQLException;

    void updateObject(String str, Object obj) throws SQLException;

    void insertRow() throws SQLException;

    void updateRow() throws SQLException;

    void deleteRow() throws SQLException;

    void refreshRow() throws SQLException;

    void cancelRowUpdates() throws SQLException;

    void moveToInsertRow() throws SQLException;

    void moveToCurrentRow() throws SQLException;

    Statement getStatement() throws SQLException;

    Object getObject(int i2, Map<String, Class<?>> map) throws SQLException;

    Ref getRef(int i2) throws SQLException;

    Blob getBlob(int i2) throws SQLException;

    Clob getClob(int i2) throws SQLException;

    Array getArray(int i2) throws SQLException;

    Object getObject(String str, Map<String, Class<?>> map) throws SQLException;

    Ref getRef(String str) throws SQLException;

    Blob getBlob(String str) throws SQLException;

    Clob getClob(String str) throws SQLException;

    Array getArray(String str) throws SQLException;

    Date getDate(int i2, Calendar calendar) throws SQLException;

    Date getDate(String str, Calendar calendar) throws SQLException;

    Time getTime(int i2, Calendar calendar) throws SQLException;

    Time getTime(String str, Calendar calendar) throws SQLException;

    Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException;

    Timestamp getTimestamp(String str, Calendar calendar) throws SQLException;

    URL getURL(int i2) throws SQLException;

    URL getURL(String str) throws SQLException;

    void updateRef(int i2, Ref ref) throws SQLException;

    void updateRef(String str, Ref ref) throws SQLException;

    void updateBlob(int i2, Blob blob) throws SQLException;

    void updateBlob(String str, Blob blob) throws SQLException;

    void updateClob(int i2, Clob clob) throws SQLException;

    void updateClob(String str, Clob clob) throws SQLException;

    void updateArray(int i2, Array array) throws SQLException;

    void updateArray(String str, Array array) throws SQLException;

    RowId getRowId(int i2) throws SQLException;

    RowId getRowId(String str) throws SQLException;

    void updateRowId(int i2, RowId rowId) throws SQLException;

    void updateRowId(String str, RowId rowId) throws SQLException;

    int getHoldability() throws SQLException;

    boolean isClosed() throws SQLException;

    void updateNString(int i2, String str) throws SQLException;

    void updateNString(String str, String str2) throws SQLException;

    void updateNClob(int i2, NClob nClob) throws SQLException;

    void updateNClob(String str, NClob nClob) throws SQLException;

    NClob getNClob(int i2) throws SQLException;

    NClob getNClob(String str) throws SQLException;

    SQLXML getSQLXML(int i2) throws SQLException;

    SQLXML getSQLXML(String str) throws SQLException;

    void updateSQLXML(int i2, SQLXML sqlxml) throws SQLException;

    void updateSQLXML(String str, SQLXML sqlxml) throws SQLException;

    String getNString(int i2) throws SQLException;

    String getNString(String str) throws SQLException;

    Reader getNCharacterStream(int i2) throws SQLException;

    Reader getNCharacterStream(String str) throws SQLException;

    void updateNCharacterStream(int i2, Reader reader, long j2) throws SQLException;

    void updateNCharacterStream(String str, Reader reader, long j2) throws SQLException;

    void updateAsciiStream(int i2, InputStream inputStream, long j2) throws SQLException;

    void updateBinaryStream(int i2, InputStream inputStream, long j2) throws SQLException;

    void updateCharacterStream(int i2, Reader reader, long j2) throws SQLException;

    void updateAsciiStream(String str, InputStream inputStream, long j2) throws SQLException;

    void updateBinaryStream(String str, InputStream inputStream, long j2) throws SQLException;

    void updateCharacterStream(String str, Reader reader, long j2) throws SQLException;

    void updateBlob(int i2, InputStream inputStream, long j2) throws SQLException;

    void updateBlob(String str, InputStream inputStream, long j2) throws SQLException;

    void updateClob(int i2, Reader reader, long j2) throws SQLException;

    void updateClob(String str, Reader reader, long j2) throws SQLException;

    void updateNClob(int i2, Reader reader, long j2) throws SQLException;

    void updateNClob(String str, Reader reader, long j2) throws SQLException;

    void updateNCharacterStream(int i2, Reader reader) throws SQLException;

    void updateNCharacterStream(String str, Reader reader) throws SQLException;

    void updateAsciiStream(int i2, InputStream inputStream) throws SQLException;

    void updateBinaryStream(int i2, InputStream inputStream) throws SQLException;

    void updateCharacterStream(int i2, Reader reader) throws SQLException;

    void updateAsciiStream(String str, InputStream inputStream) throws SQLException;

    void updateBinaryStream(String str, InputStream inputStream) throws SQLException;

    void updateCharacterStream(String str, Reader reader) throws SQLException;

    void updateBlob(int i2, InputStream inputStream) throws SQLException;

    void updateBlob(String str, InputStream inputStream) throws SQLException;

    void updateClob(int i2, Reader reader) throws SQLException;

    void updateClob(String str, Reader reader) throws SQLException;

    void updateNClob(int i2, Reader reader) throws SQLException;

    void updateNClob(String str, Reader reader) throws SQLException;

    <T> T getObject(int i2, Class<T> cls) throws SQLException;

    <T> T getObject(String str, Class<T> cls) throws SQLException;

    default void updateObject(int i2, Object obj, SQLType sQLType, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    default void updateObject(String str, Object obj, SQLType sQLType, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    default void updateObject(int i2, Object obj, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    default void updateObject(String str, Object obj, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }
}

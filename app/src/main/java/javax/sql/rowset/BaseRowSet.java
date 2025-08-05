package javax.sql.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialRef;

/* loaded from: rt.jar:javax/sql/rowset/BaseRowSet.class */
public abstract class BaseRowSet implements Serializable, Cloneable {
    public static final int UNICODE_STREAM_PARAM = 0;
    public static final int BINARY_STREAM_PARAM = 1;
    public static final int ASCII_STREAM_PARAM = 2;
    protected InputStream binaryStream;
    protected InputStream unicodeStream;
    protected InputStream asciiStream;
    protected Reader charStream;
    private String command;
    private String URL;
    private String dataSource;
    private transient String username;
    private transient String password;
    private boolean readOnly;
    private int isolation;
    private Map<String, Class<?>> map;
    private Hashtable<Integer, Object> params;
    static final long serialVersionUID = 4886719666485113312L;
    private int rowSetType = 1004;
    private boolean showDeleted = false;
    private int queryTimeout = 0;
    private int maxRows = 0;
    private int maxFieldSize = 0;
    private int concurrency = 1008;
    private boolean escapeProcessing = true;
    private int fetchDir = 1000;
    private int fetchSize = 0;
    private Vector<RowSetListener> listeners = new Vector<>();

    protected void initParams() {
        this.params = new Hashtable<>();
    }

    public void addRowSetListener(RowSetListener rowSetListener) {
        this.listeners.add(rowSetListener);
    }

    public void removeRowSetListener(RowSetListener rowSetListener) {
        this.listeners.remove(rowSetListener);
    }

    private void checkforRowSetInterface() throws SQLException {
        if (!(this instanceof RowSet)) {
            throw new SQLException("The class extending abstract class BaseRowSet must implement javax.sql.RowSet or one of it's sub-interfaces.");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void notifyCursorMoved() throws SQLException {
        checkforRowSetInterface();
        if (!this.listeners.isEmpty()) {
            RowSetEvent rowSetEvent = new RowSetEvent((RowSet) this);
            Iterator<RowSetListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().cursorMoved(rowSetEvent);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void notifyRowChanged() throws SQLException {
        checkforRowSetInterface();
        if (!this.listeners.isEmpty()) {
            RowSetEvent rowSetEvent = new RowSetEvent((RowSet) this);
            Iterator<RowSetListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().rowChanged(rowSetEvent);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void notifyRowSetChanged() throws SQLException {
        checkforRowSetInterface();
        if (!this.listeners.isEmpty()) {
            RowSetEvent rowSetEvent = new RowSetEvent((RowSet) this);
            Iterator<RowSetListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().rowSetChanged(rowSetEvent);
            }
        }
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String str) throws SQLException {
        if (str == null) {
            this.command = null;
        } else {
            if (str.length() == 0) {
                throw new SQLException("Invalid command string detected. Cannot be of length less than 0");
            }
            if (this.params == null) {
                throw new SQLException("Set initParams() before setCommand");
            }
            this.params.clear();
            this.command = str;
        }
    }

    public String getUrl() throws SQLException {
        return this.URL;
    }

    public void setUrl(String str) throws SQLException {
        if (str != null) {
            if (str.length() < 1) {
                throw new SQLException("Invalid url string detected. Cannot be of length less than 1");
            }
            this.URL = str;
        }
        this.dataSource = null;
    }

    public String getDataSourceName() {
        return this.dataSource;
    }

    public void setDataSourceName(String str) throws SQLException {
        if (str == null) {
            this.dataSource = null;
        } else {
            if (str.equals("")) {
                throw new SQLException("DataSource name cannot be empty string");
            }
            this.dataSource = str;
        }
        this.URL = null;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        if (str == null) {
            this.username = null;
        } else {
            this.username = str;
        }
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        if (str == null) {
            this.password = null;
        } else {
            this.password = str;
        }
    }

    public void setType(int i2) throws SQLException {
        if (i2 != 1003 && i2 != 1004 && i2 != 1005) {
            throw new SQLException("Invalid type of RowSet set. Must be either ResultSet.TYPE_FORWARD_ONLY or ResultSet.TYPE_SCROLL_INSENSITIVE or ResultSet.TYPE_SCROLL_SENSITIVE.");
        }
        this.rowSetType = i2;
    }

    public int getType() throws SQLException {
        return this.rowSetType;
    }

    public void setConcurrency(int i2) throws SQLException {
        if (i2 != 1007 && i2 != 1008) {
            throw new SQLException("Invalid concurrency set. Must be either ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE.");
        }
        this.concurrency = i2;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean z2) {
        this.readOnly = z2;
    }

    public int getTransactionIsolation() {
        return this.isolation;
    }

    public void setTransactionIsolation(int i2) throws SQLException {
        if (i2 != 0 && i2 != 2 && i2 != 1 && i2 != 4 && i2 != 8) {
            throw new SQLException("Invalid transaction isolation set. Must be either Connection.TRANSACTION_NONE or Connection.TRANSACTION_READ_UNCOMMITTED or Connection.TRANSACTION_READ_COMMITTED or Connection.RRANSACTION_REPEATABLE_READ or Connection.TRANSACTION_SERIALIZABLE");
        }
        this.isolation = i2;
    }

    public Map<String, Class<?>> getTypeMap() {
        return this.map;
    }

    public void setTypeMap(Map<String, Class<?>> map) {
        this.map = map;
    }

    public int getMaxFieldSize() throws SQLException {
        return this.maxFieldSize;
    }

    public void setMaxFieldSize(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException("Invalid max field size set. Cannot be of value: " + i2);
        }
        this.maxFieldSize = i2;
    }

    public int getMaxRows() throws SQLException {
        return this.maxRows;
    }

    public void setMaxRows(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException("Invalid max row size set. Cannot be of value: " + i2);
        }
        if (i2 < getFetchSize()) {
            throw new SQLException("Invalid max row size set. Cannot be less than the fetchSize.");
        }
        this.maxRows = i2;
    }

    public void setEscapeProcessing(boolean z2) throws SQLException {
        this.escapeProcessing = z2;
    }

    public int getQueryTimeout() throws SQLException {
        return this.queryTimeout;
    }

    public void setQueryTimeout(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException("Invalid query timeout value set. Cannot be of value: " + i2);
        }
        this.queryTimeout = i2;
    }

    public boolean getShowDeleted() throws SQLException {
        return this.showDeleted;
    }

    public void setShowDeleted(boolean z2) throws SQLException {
        this.showDeleted = z2;
    }

    public boolean getEscapeProcessing() throws SQLException {
        return this.escapeProcessing;
    }

    public void setFetchDirection(int i2) throws SQLException {
        if ((getType() == 1003 && i2 != 1000) || (i2 != 1000 && i2 != 1001 && i2 != 1002)) {
            throw new SQLException("Invalid Fetch Direction");
        }
        this.fetchDir = i2;
    }

    public int getFetchDirection() throws SQLException {
        return this.fetchDir;
    }

    public void setFetchSize(int i2) throws SQLException {
        if (getMaxRows() == 0 && i2 >= 0) {
            this.fetchSize = i2;
        } else {
            if (i2 < 0 || i2 > getMaxRows()) {
                throw new SQLException("Invalid fetch size set. Cannot be of value: " + i2);
            }
            this.fetchSize = i2;
        }
    }

    public int getFetchSize() throws SQLException {
        return this.fetchSize;
    }

    public int getConcurrency() throws SQLException {
        return this.concurrency;
    }

    private void checkParamIndex(int i2) throws SQLException {
        if (i2 < 1) {
            throw new SQLException("Invalid Parameter Index");
        }
    }

    public void setNull(int i2, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {null, Integer.valueOf(i3)};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setNull");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setNull(int i2, int i3, String str) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {null, Integer.valueOf(i3), str};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setNull");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setBoolean(int i2, boolean z2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setNull");
        }
        this.params.put(Integer.valueOf(i2 - 1), Boolean.valueOf(z2));
    }

    public void setByte(int i2, byte b2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setByte");
        }
        this.params.put(Integer.valueOf(i2 - 1), Byte.valueOf(b2));
    }

    public void setShort(int i2, short s2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setShort");
        }
        this.params.put(Integer.valueOf(i2 - 1), Short.valueOf(s2));
    }

    public void setInt(int i2, int i3) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setInt");
        }
        this.params.put(Integer.valueOf(i2 - 1), Integer.valueOf(i3));
    }

    public void setLong(int i2, long j2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setLong");
        }
        this.params.put(Integer.valueOf(i2 - 1), Long.valueOf(j2));
    }

    public void setFloat(int i2, float f2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setFloat");
        }
        this.params.put(Integer.valueOf(i2 - 1), Float.valueOf(f2));
    }

    public void setDouble(int i2, double d2) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setDouble");
        }
        this.params.put(Integer.valueOf(i2 - 1), Double.valueOf(d2));
    }

    public void setBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setBigDecimal");
        }
        this.params.put(Integer.valueOf(i2 - 1), bigDecimal);
    }

    public void setString(int i2, String str) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setString");
        }
        this.params.put(Integer.valueOf(i2 - 1), str);
    }

    public void setBytes(int i2, byte[] bArr) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setBytes");
        }
        this.params.put(Integer.valueOf(i2 - 1), bArr);
    }

    public void setDate(int i2, Date date) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setDate");
        }
        this.params.put(Integer.valueOf(i2 - 1), date);
    }

    public void setTime(int i2, Time time) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setTime");
        }
        this.params.put(Integer.valueOf(i2 - 1), time);
    }

    public void setTimestamp(int i2, Timestamp timestamp) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setTimestamp");
        }
        this.params.put(Integer.valueOf(i2 - 1), timestamp);
    }

    public void setAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {inputStream, Integer.valueOf(i3), 2};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setAsciiStream");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setAsciiStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {inputStream, Integer.valueOf(i3), 1};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setBinaryStream");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setBinaryStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    @Deprecated
    public void setUnicodeStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {inputStream, Integer.valueOf(i3), 0};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setUnicodeStream");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {reader, Integer.valueOf(i3)};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setCharacterStream");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setObject(int i2, Object obj, int i3, int i4) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {obj, Integer.valueOf(i3), Integer.valueOf(i4)};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setObject");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setObject(int i2, Object obj, int i3) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {obj, Integer.valueOf(i3)};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setObject");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setObject(int i2, Object obj) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setObject");
        }
        this.params.put(Integer.valueOf(i2 - 1), obj);
    }

    public void setRef(int i2, Ref ref) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setRef");
        }
        this.params.put(Integer.valueOf(i2 - 1), new SerialRef(ref));
    }

    public void setBlob(int i2, Blob blob) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setBlob");
        }
        this.params.put(Integer.valueOf(i2 - 1), new SerialBlob(blob));
    }

    public void setClob(int i2, Clob clob) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setClob");
        }
        this.params.put(Integer.valueOf(i2 - 1), new SerialClob(clob));
    }

    public void setArray(int i2, Array array) throws SQLException {
        checkParamIndex(i2);
        if (this.params == null) {
            throw new SQLException("Set initParams() before setArray");
        }
        this.params.put(Integer.valueOf(i2 - 1), new SerialArray(array));
    }

    public void setDate(int i2, Date date, Calendar calendar) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {date, calendar};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setDate");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setTime(int i2, Time time, Calendar calendar) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {time, calendar};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setTime");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void setTimestamp(int i2, Timestamp timestamp, Calendar calendar) throws SQLException {
        checkParamIndex(i2);
        Object[] objArr = {timestamp, calendar};
        if (this.params == null) {
            throw new SQLException("Set initParams() before setTimestamp");
        }
        this.params.put(Integer.valueOf(i2 - 1), objArr);
    }

    public void clearParameters() throws SQLException {
        this.params.clear();
    }

    public Object[] getParams() throws SQLException {
        if (this.params == null) {
            initParams();
            return new Object[this.params.size()];
        }
        Object[] objArr = new Object[this.params.size()];
        for (int i2 = 0; i2 < this.params.size(); i2++) {
            objArr[i2] = this.params.get(Integer.valueOf(i2));
            if (objArr[i2] == null) {
                throw new SQLException("missing parameter: " + (i2 + 1));
            }
        }
        return objArr;
    }

    public void setNull(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNull(String str, int i2, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBoolean(String str, boolean z2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setByte(String str, byte b2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setShort(String str, short s2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setInt(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setLong(String str, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setFloat(String str, float f2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setDouble(String str, double d2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBytes(String str, byte[] bArr) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setTimestamp(String str, Timestamp timestamp) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setCharacterStream(String str, Reader reader, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setAsciiStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBinaryStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setObject(String str, Object obj, int i2, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setObject(String str, Object obj, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setObject(String str, Object obj) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBlob(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBlob(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBlob(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBlob(String str, Blob blob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setBlob(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setClob(String str, Clob clob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setDate(String str, Date date) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setDate(String str, Date date, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setTime(String str, Time time) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setTime(String str, Time time, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setTimestamp(String str, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setSQLXML(int i2, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setSQLXML(String str, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setRowId(int i2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setRowId(String str, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNString(int i2, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(String str, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(int i2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setNClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    public void setURL(int i2, URL url) throws SQLException {
        throw new SQLFeatureNotSupportedException("Feature not supported");
    }
}

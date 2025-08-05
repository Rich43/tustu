package com.sun.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.BaseRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.Joinable;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetWarning;

/* loaded from: rt.jar:com/sun/rowset/JdbcRowSetImpl.class */
public class JdbcRowSetImpl extends BaseRowSet implements JdbcRowSet, Joinable {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private RowSetMetaDataImpl rowsMD;
    private ResultSetMetaData resMD;
    private Vector<Integer> iMatchColumns;
    private Vector<String> strMatchColumns;
    protected transient JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = -3591946023893483003L;

    public JdbcRowSetImpl() {
        this.conn = null;
        this.ps = null;
        this.rs = null;
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            initParams();
            try {
                setShowDeleted(false);
            } catch (SQLException e2) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setshowdeleted").toString() + e2.getLocalizedMessage());
            }
            try {
                setQueryTimeout(0);
            } catch (SQLException e3) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setquerytimeout").toString() + e3.getLocalizedMessage());
            }
            try {
                setMaxRows(0);
            } catch (SQLException e4) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxrows").toString() + e4.getLocalizedMessage());
            }
            try {
                setMaxFieldSize(0);
            } catch (SQLException e5) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxfieldsize").toString() + e5.getLocalizedMessage());
            }
            try {
                setEscapeProcessing(true);
            } catch (SQLException e6) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setescapeprocessing").toString() + e6.getLocalizedMessage());
            }
            try {
                setConcurrency(1008);
            } catch (SQLException e7) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setconcurrency").toString() + e7.getLocalizedMessage());
            }
            setTypeMap(null);
            try {
                setType(1004);
            } catch (SQLException e8) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.settype").toString() + e8.getLocalizedMessage());
            }
            setReadOnly(true);
            try {
                setTransactionIsolation(2);
            } catch (SQLException e9) {
                System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.settransactionisolation").toString() + e9.getLocalizedMessage());
            }
            this.iMatchColumns = new Vector<>(10);
            for (int i2 = 0; i2 < 10; i2++) {
                this.iMatchColumns.add(i2, -1);
            }
            this.strMatchColumns = new Vector<>(10);
            for (int i3 = 0; i3 < 10; i3++) {
                this.strMatchColumns.add(i3, null);
            }
        } catch (IOException e10) {
            throw new RuntimeException(e10);
        }
    }

    public JdbcRowSetImpl(Connection connection) throws SQLException {
        this.conn = connection;
        this.ps = null;
        this.rs = null;
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            initParams();
            setShowDeleted(false);
            setQueryTimeout(0);
            setMaxRows(0);
            setMaxFieldSize(0);
            setParams();
            setReadOnly(true);
            setTransactionIsolation(2);
            setEscapeProcessing(true);
            setTypeMap(null);
            this.iMatchColumns = new Vector<>(10);
            for (int i2 = 0; i2 < 10; i2++) {
                this.iMatchColumns.add(i2, -1);
            }
            this.strMatchColumns = new Vector<>(10);
            for (int i3 = 0; i3 < 10; i3++) {
                this.strMatchColumns.add(i3, null);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public JdbcRowSetImpl(String str, String str2, String str3) throws SQLException {
        this.conn = null;
        this.ps = null;
        this.rs = null;
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            initParams();
            setUsername(str2);
            setPassword(str3);
            setUrl(str);
            setShowDeleted(false);
            setQueryTimeout(0);
            setMaxRows(0);
            setMaxFieldSize(0);
            setParams();
            setReadOnly(true);
            setTransactionIsolation(2);
            setEscapeProcessing(true);
            setTypeMap(null);
            this.iMatchColumns = new Vector<>(10);
            for (int i2 = 0; i2 < 10; i2++) {
                this.iMatchColumns.add(i2, -1);
            }
            this.strMatchColumns = new Vector<>(10);
            for (int i3 = 0; i3 < 10; i3++) {
                this.strMatchColumns.add(i3, null);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public JdbcRowSetImpl(ResultSet resultSet) throws SQLException {
        this.conn = null;
        this.ps = null;
        this.rs = resultSet;
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            initParams();
            setShowDeleted(false);
            setQueryTimeout(0);
            setMaxRows(0);
            setMaxFieldSize(0);
            setParams();
            setReadOnly(true);
            setTransactionIsolation(2);
            setEscapeProcessing(true);
            setTypeMap(null);
            this.resMD = this.rs.getMetaData();
            this.rowsMD = new RowSetMetaDataImpl();
            initMetaData(this.rowsMD, this.resMD);
            this.iMatchColumns = new Vector<>(10);
            for (int i2 = 0; i2 < 10; i2++) {
                this.iMatchColumns.add(i2, -1);
            }
            this.strMatchColumns = new Vector<>(10);
            for (int i3 = 0; i3 < 10; i3++) {
                this.strMatchColumns.add(i3, null);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    protected void initMetaData(RowSetMetaData rowSetMetaData, ResultSetMetaData resultSetMetaData) throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();
        rowSetMetaData.setColumnCount(columnCount);
        for (int i2 = 1; i2 <= columnCount; i2++) {
            rowSetMetaData.setAutoIncrement(i2, resultSetMetaData.isAutoIncrement(i2));
            rowSetMetaData.setCaseSensitive(i2, resultSetMetaData.isCaseSensitive(i2));
            rowSetMetaData.setCurrency(i2, resultSetMetaData.isCurrency(i2));
            rowSetMetaData.setNullable(i2, resultSetMetaData.isNullable(i2));
            rowSetMetaData.setSigned(i2, resultSetMetaData.isSigned(i2));
            rowSetMetaData.setSearchable(i2, resultSetMetaData.isSearchable(i2));
            rowSetMetaData.setColumnDisplaySize(i2, resultSetMetaData.getColumnDisplaySize(i2));
            rowSetMetaData.setColumnLabel(i2, resultSetMetaData.getColumnLabel(i2));
            rowSetMetaData.setColumnName(i2, resultSetMetaData.getColumnName(i2));
            rowSetMetaData.setSchemaName(i2, resultSetMetaData.getSchemaName(i2));
            rowSetMetaData.setPrecision(i2, resultSetMetaData.getPrecision(i2));
            rowSetMetaData.setScale(i2, resultSetMetaData.getScale(i2));
            rowSetMetaData.setTableName(i2, resultSetMetaData.getTableName(i2));
            rowSetMetaData.setCatalogName(i2, resultSetMetaData.getCatalogName(i2));
            rowSetMetaData.setColumnType(i2, resultSetMetaData.getColumnType(i2));
            rowSetMetaData.setColumnTypeName(i2, resultSetMetaData.getColumnTypeName(i2));
        }
    }

    protected void checkState() throws SQLException {
        if (this.conn == null && this.ps == null && this.rs == null) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.invalstate").toString());
        }
    }

    @Override // javax.sql.RowSet
    public void execute() throws SQLException {
        prepare();
        setProperties(this.ps);
        decodeParams(getParams(), this.ps);
        this.rs = this.ps.executeQuery();
        notifyRowSetChanged();
    }

    protected void setProperties(PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement.setEscapeProcessing(getEscapeProcessing());
        } catch (SQLException e2) {
            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setescapeprocessing").toString() + e2.getLocalizedMessage());
        }
        try {
            preparedStatement.setMaxFieldSize(getMaxFieldSize());
        } catch (SQLException e3) {
            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxfieldsize").toString() + e3.getLocalizedMessage());
        }
        try {
            preparedStatement.setMaxRows(getMaxRows());
        } catch (SQLException e4) {
            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setmaxrows").toString() + e4.getLocalizedMessage());
        }
        try {
            preparedStatement.setQueryTimeout(getQueryTimeout());
        } catch (SQLException e5) {
            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.setquerytimeout").toString() + e5.getLocalizedMessage());
        }
    }

    private Connection connect() throws SQLException {
        if (this.conn != null) {
            return this.conn;
        }
        if (getDataSourceName() != null) {
            try {
                DataSource dataSource = (DataSource) new InitialContext().lookup(getDataSourceName());
                if (getUsername() != null && !getUsername().equals("")) {
                    return dataSource.getConnection(getUsername(), getPassword());
                }
                return dataSource.getConnection();
            } catch (NamingException e2) {
                throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.connect").toString());
            }
        }
        if (getUrl() != null) {
            return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
        }
        return null;
    }

    protected PreparedStatement prepare() throws SQLException {
        this.conn = connect();
        try {
            Map<String, Class<?>> typeMap = getTypeMap();
            if (typeMap != null) {
                this.conn.setTypeMap(typeMap);
            }
            this.ps = this.conn.prepareStatement(getCommand(), 1004, 1008);
            return this.ps;
        } catch (SQLException e2) {
            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.prepare").toString() + e2.getLocalizedMessage());
            if (this.ps != null) {
                this.ps.close();
            }
            if (this.conn != null) {
                this.conn.close();
            }
            throw new SQLException(e2.getMessage());
        }
    }

    private void decodeParams(Object[] objArr, PreparedStatement preparedStatement) throws SQLException {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] instanceof Object[]) {
                Object[] objArr2 = (Object[]) objArr[i2];
                if (objArr2.length == 2) {
                    if (objArr2[0] == null) {
                        preparedStatement.setNull(i2 + 1, ((Integer) objArr2[1]).intValue());
                    } else if ((objArr2[0] instanceof Date) || (objArr2[0] instanceof Time) || (objArr2[0] instanceof Timestamp)) {
                        System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.detecteddate"));
                        if (objArr2[1] instanceof Calendar) {
                            System.err.println(this.resBundle.handleGetObject("jdbcrowsetimpl.detectedcalendar"));
                            preparedStatement.setDate(i2 + 1, (Date) objArr2[0], (Calendar) objArr2[1]);
                        } else {
                            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
                        }
                    } else if (objArr2[0] instanceof Reader) {
                        preparedStatement.setCharacterStream(i2 + 1, (Reader) objArr2[0], ((Integer) objArr2[1]).intValue());
                    } else if (objArr2[1] instanceof Integer) {
                        preparedStatement.setObject(i2 + 1, objArr2[0], ((Integer) objArr2[1]).intValue());
                    }
                } else if (objArr2.length == 3) {
                    if (objArr2[0] == null) {
                        preparedStatement.setNull(i2 + 1, ((Integer) objArr2[1]).intValue(), (String) objArr2[2]);
                    } else {
                        if (objArr2[0] instanceof InputStream) {
                            switch (((Integer) objArr2[2]).intValue()) {
                                case 0:
                                    preparedStatement.setUnicodeStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                case 1:
                                    preparedStatement.setBinaryStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                case 2:
                                    preparedStatement.setAsciiStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                default:
                                    throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
                            }
                        }
                        if ((objArr2[1] instanceof Integer) && (objArr2[2] instanceof Integer)) {
                            preparedStatement.setObject(i2 + 1, objArr2[0], ((Integer) objArr2[1]).intValue(), ((Integer) objArr2[2]).intValue());
                        } else {
                            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.paramtype").toString());
                        }
                    }
                } else {
                    preparedStatement.setObject(i2 + 1, objArr[i2]);
                }
            } else {
                preparedStatement.setObject(i2 + 1, objArr[i2]);
            }
        }
    }

    @Override // java.sql.ResultSet
    public boolean next() throws SQLException {
        checkState();
        boolean next = this.rs.next();
        notifyCursorMoved();
        return next;
    }

    @Override // java.sql.ResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        if (this.rs != null) {
            this.rs.close();
        }
        if (this.ps != null) {
            this.ps.close();
        }
        if (this.conn != null) {
            this.conn.close();
        }
    }

    @Override // java.sql.ResultSet
    public boolean wasNull() throws SQLException {
        checkState();
        return this.rs.wasNull();
    }

    @Override // java.sql.ResultSet
    public String getString(int i2) throws SQLException {
        checkState();
        return this.rs.getString(i2);
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(int i2) throws SQLException {
        checkState();
        return this.rs.getBoolean(i2);
    }

    @Override // java.sql.ResultSet
    public byte getByte(int i2) throws SQLException {
        checkState();
        return this.rs.getByte(i2);
    }

    @Override // java.sql.ResultSet
    public short getShort(int i2) throws SQLException {
        checkState();
        return this.rs.getShort(i2);
    }

    @Override // java.sql.ResultSet
    public int getInt(int i2) throws SQLException {
        checkState();
        return this.rs.getInt(i2);
    }

    @Override // java.sql.ResultSet
    public long getLong(int i2) throws SQLException {
        checkState();
        return this.rs.getLong(i2);
    }

    @Override // java.sql.ResultSet
    public float getFloat(int i2) throws SQLException {
        checkState();
        return this.rs.getFloat(i2);
    }

    @Override // java.sql.ResultSet
    public double getDouble(int i2) throws SQLException {
        checkState();
        return this.rs.getDouble(i2);
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int i2, int i3) throws SQLException {
        checkState();
        return this.rs.getBigDecimal(i2, i3);
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(int i2) throws SQLException {
        checkState();
        return this.rs.getBytes(i2);
    }

    @Override // java.sql.ResultSet
    public Date getDate(int i2) throws SQLException {
        checkState();
        return this.rs.getDate(i2);
    }

    @Override // java.sql.ResultSet
    public Time getTime(int i2) throws SQLException {
        checkState();
        return this.rs.getTime(i2);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int i2) throws SQLException {
        checkState();
        return this.rs.getTimestamp(i2);
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(int i2) throws SQLException {
        checkState();
        return this.rs.getAsciiStream(i2);
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(int i2) throws SQLException {
        checkState();
        return this.rs.getUnicodeStream(i2);
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(int i2) throws SQLException {
        checkState();
        return this.rs.getBinaryStream(i2);
    }

    @Override // java.sql.ResultSet
    public String getString(String str) throws SQLException {
        return getString(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(String str) throws SQLException {
        return getBoolean(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public byte getByte(String str) throws SQLException {
        return getByte(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public short getShort(String str) throws SQLException {
        return getShort(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public int getInt(String str) throws SQLException {
        return getInt(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public long getLong(String str) throws SQLException {
        return getLong(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public float getFloat(String str) throws SQLException {
        return getFloat(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public double getDouble(String str) throws SQLException {
        return getDouble(findColumn(str));
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String str, int i2) throws SQLException {
        return getBigDecimal(findColumn(str), i2);
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(String str) throws SQLException {
        return getBytes(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Date getDate(String str) throws SQLException {
        return getDate(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Time getTime(String str) throws SQLException {
        return getTime(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String str) throws SQLException {
        return getTimestamp(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(String str) throws SQLException {
        return getAsciiStream(findColumn(str));
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(String str) throws SQLException {
        return getUnicodeStream(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(String str) throws SQLException {
        return getBinaryStream(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public SQLWarning getWarnings() throws SQLException {
        checkState();
        return this.rs.getWarnings();
    }

    @Override // java.sql.ResultSet
    public void clearWarnings() throws SQLException {
        checkState();
        this.rs.clearWarnings();
    }

    @Override // java.sql.ResultSet
    public String getCursorName() throws SQLException {
        checkState();
        return this.rs.getCursorName();
    }

    @Override // java.sql.ResultSet
    public ResultSetMetaData getMetaData() throws SQLException {
        checkState();
        try {
            checkState();
            return this.rs.getMetaData();
        } catch (SQLException e2) {
            prepare();
            return this.ps.getMetaData();
        }
    }

    @Override // java.sql.ResultSet
    public Object getObject(int i2) throws SQLException {
        checkState();
        return this.rs.getObject(i2);
    }

    @Override // java.sql.ResultSet
    public Object getObject(String str) throws SQLException {
        return getObject(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public int findColumn(String str) throws SQLException {
        checkState();
        return this.rs.findColumn(str);
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(int i2) throws SQLException {
        checkState();
        return this.rs.getCharacterStream(i2);
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(String str) throws SQLException {
        return getCharacterStream(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(int i2) throws SQLException {
        checkState();
        return this.rs.getBigDecimal(i2);
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(String str) throws SQLException {
        return getBigDecimal(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        checkState();
        return this.rs.isBeforeFirst();
    }

    @Override // java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        checkState();
        return this.rs.isAfterLast();
    }

    @Override // java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        checkState();
        return this.rs.isFirst();
    }

    @Override // java.sql.ResultSet
    public boolean isLast() throws SQLException {
        checkState();
        return this.rs.isLast();
    }

    @Override // java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        checkState();
        this.rs.beforeFirst();
        notifyCursorMoved();
    }

    @Override // java.sql.ResultSet
    public void afterLast() throws SQLException {
        checkState();
        this.rs.afterLast();
        notifyCursorMoved();
    }

    @Override // java.sql.ResultSet
    public boolean first() throws SQLException {
        checkState();
        boolean zFirst = this.rs.first();
        notifyCursorMoved();
        return zFirst;
    }

    @Override // java.sql.ResultSet
    public boolean last() throws SQLException {
        checkState();
        boolean zLast = this.rs.last();
        notifyCursorMoved();
        return zLast;
    }

    @Override // java.sql.ResultSet
    public int getRow() throws SQLException {
        checkState();
        return this.rs.getRow();
    }

    @Override // java.sql.ResultSet
    public boolean absolute(int i2) throws SQLException {
        checkState();
        boolean zAbsolute = this.rs.absolute(i2);
        notifyCursorMoved();
        return zAbsolute;
    }

    @Override // java.sql.ResultSet
    public boolean relative(int i2) throws SQLException {
        checkState();
        boolean zRelative = this.rs.relative(i2);
        notifyCursorMoved();
        return zRelative;
    }

    @Override // java.sql.ResultSet
    public boolean previous() throws SQLException {
        checkState();
        boolean zPrevious = this.rs.previous();
        notifyCursorMoved();
        return zPrevious;
    }

    @Override // javax.sql.rowset.BaseRowSet, java.sql.ResultSet
    public void setFetchDirection(int i2) throws SQLException {
        checkState();
        this.rs.setFetchDirection(i2);
    }

    @Override // javax.sql.rowset.BaseRowSet, java.sql.ResultSet
    public int getFetchDirection() throws SQLException {
        try {
            checkState();
        } catch (SQLException e2) {
            super.getFetchDirection();
        }
        return this.rs.getFetchDirection();
    }

    @Override // javax.sql.rowset.BaseRowSet, java.sql.ResultSet
    public void setFetchSize(int i2) throws SQLException {
        checkState();
        this.rs.setFetchSize(i2);
    }

    @Override // javax.sql.rowset.BaseRowSet, java.sql.ResultSet
    public int getType() throws SQLException {
        try {
            checkState();
            if (this.rs == null) {
                return super.getType();
            }
            return this.rs.getType();
        } catch (SQLException e2) {
            return super.getType();
        }
    }

    @Override // javax.sql.rowset.BaseRowSet, java.sql.ResultSet
    public int getConcurrency() throws SQLException {
        try {
            checkState();
        } catch (SQLException e2) {
            super.getConcurrency();
        }
        return this.rs.getConcurrency();
    }

    @Override // java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        checkState();
        return this.rs.rowUpdated();
    }

    @Override // java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        checkState();
        return this.rs.rowInserted();
    }

    @Override // java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        checkState();
        return this.rs.rowDeleted();
    }

    @Override // java.sql.ResultSet
    public void updateNull(int i2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateNull(i2);
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(int i2, boolean z2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateBoolean(i2, z2);
    }

    @Override // java.sql.ResultSet
    public void updateByte(int i2, byte b2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateByte(i2, b2);
    }

    @Override // java.sql.ResultSet
    public void updateShort(int i2, short s2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateShort(i2, s2);
    }

    @Override // java.sql.ResultSet
    public void updateInt(int i2, int i3) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateInt(i2, i3);
    }

    @Override // java.sql.ResultSet
    public void updateLong(int i2, long j2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateLong(i2, j2);
    }

    @Override // java.sql.ResultSet
    public void updateFloat(int i2, float f2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateFloat(i2, f2);
    }

    @Override // java.sql.ResultSet
    public void updateDouble(int i2, double d2) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateDouble(i2, d2);
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateBigDecimal(i2, bigDecimal);
    }

    @Override // java.sql.ResultSet
    public void updateString(int i2, String str) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateString(i2, str);
    }

    @Override // java.sql.ResultSet
    public void updateBytes(int i2, byte[] bArr) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateBytes(i2, bArr);
    }

    @Override // java.sql.ResultSet
    public void updateDate(int i2, Date date) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateDate(i2, date);
    }

    @Override // java.sql.ResultSet
    public void updateTime(int i2, Time time) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateTime(i2, time);
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(int i2, Timestamp timestamp) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateTimestamp(i2, timestamp);
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateAsciiStream(i2, inputStream, i3);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateBinaryStream(i2, inputStream, i3);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateCharacterStream(i2, reader, i3);
    }

    @Override // java.sql.ResultSet
    public void updateObject(int i2, Object obj, int i3) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateObject(i2, obj, i3);
    }

    @Override // java.sql.ResultSet
    public void updateObject(int i2, Object obj) throws SQLException {
        checkState();
        checkTypeConcurrency();
        this.rs.updateObject(i2, obj);
    }

    @Override // java.sql.ResultSet
    public void updateNull(String str) throws SQLException {
        updateNull(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(String str, boolean z2) throws SQLException {
        updateBoolean(findColumn(str), z2);
    }

    @Override // java.sql.ResultSet
    public void updateByte(String str, byte b2) throws SQLException {
        updateByte(findColumn(str), b2);
    }

    @Override // java.sql.ResultSet
    public void updateShort(String str, short s2) throws SQLException {
        updateShort(findColumn(str), s2);
    }

    @Override // java.sql.ResultSet
    public void updateInt(String str, int i2) throws SQLException {
        updateInt(findColumn(str), i2);
    }

    @Override // java.sql.ResultSet
    public void updateLong(String str, long j2) throws SQLException {
        updateLong(findColumn(str), j2);
    }

    @Override // java.sql.ResultSet
    public void updateFloat(String str, float f2) throws SQLException {
        updateFloat(findColumn(str), f2);
    }

    @Override // java.sql.ResultSet
    public void updateDouble(String str, double d2) throws SQLException {
        updateDouble(findColumn(str), d2);
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        updateBigDecimal(findColumn(str), bigDecimal);
    }

    @Override // java.sql.ResultSet
    public void updateString(String str, String str2) throws SQLException {
        updateString(findColumn(str), str2);
    }

    @Override // java.sql.ResultSet
    public void updateBytes(String str, byte[] bArr) throws SQLException {
        updateBytes(findColumn(str), bArr);
    }

    @Override // java.sql.ResultSet
    public void updateDate(String str, Date date) throws SQLException {
        updateDate(findColumn(str), date);
    }

    @Override // java.sql.ResultSet
    public void updateTime(String str, Time time) throws SQLException {
        updateTime(findColumn(str), time);
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
        updateTimestamp(findColumn(str), timestamp);
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateAsciiStream(findColumn(str), inputStream, i2);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateBinaryStream(findColumn(str), inputStream, i2);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, int i2) throws SQLException {
        updateCharacterStream(findColumn(str), reader, i2);
    }

    @Override // java.sql.ResultSet
    public void updateObject(String str, Object obj, int i2) throws SQLException {
        updateObject(findColumn(str), obj, i2);
    }

    @Override // java.sql.ResultSet
    public void updateObject(String str, Object obj) throws SQLException {
        updateObject(findColumn(str), obj);
    }

    @Override // java.sql.ResultSet
    public void insertRow() throws SQLException {
        checkState();
        this.rs.insertRow();
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void updateRow() throws SQLException {
        checkState();
        this.rs.updateRow();
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void deleteRow() throws SQLException {
        checkState();
        this.rs.deleteRow();
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void refreshRow() throws SQLException {
        checkState();
        this.rs.refreshRow();
    }

    @Override // java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        checkState();
        this.rs.cancelRowUpdates();
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        checkState();
        this.rs.moveToInsertRow();
    }

    @Override // java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        checkState();
        this.rs.moveToCurrentRow();
    }

    @Override // java.sql.ResultSet
    public Statement getStatement() throws SQLException {
        if (this.rs != null) {
            return this.rs.getStatement();
        }
        return null;
    }

    @Override // java.sql.ResultSet
    public Object getObject(int i2, Map<String, Class<?>> map) throws SQLException {
        checkState();
        return this.rs.getObject(i2, map);
    }

    @Override // java.sql.ResultSet
    public Ref getRef(int i2) throws SQLException {
        checkState();
        return this.rs.getRef(i2);
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(int i2) throws SQLException {
        checkState();
        return this.rs.getBlob(i2);
    }

    @Override // java.sql.ResultSet
    public Clob getClob(int i2) throws SQLException {
        checkState();
        return this.rs.getClob(i2);
    }

    @Override // java.sql.ResultSet
    public Array getArray(int i2) throws SQLException {
        checkState();
        return this.rs.getArray(i2);
    }

    @Override // java.sql.ResultSet
    public Object getObject(String str, Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(str), map);
    }

    @Override // java.sql.ResultSet
    public Ref getRef(String str) throws SQLException {
        return getRef(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(String str) throws SQLException {
        return getBlob(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Clob getClob(String str) throws SQLException {
        return getClob(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Array getArray(String str) throws SQLException {
        return getArray(findColumn(str));
    }

    @Override // java.sql.ResultSet
    public Date getDate(int i2, Calendar calendar) throws SQLException {
        checkState();
        return this.rs.getDate(i2, calendar);
    }

    @Override // java.sql.ResultSet
    public Date getDate(String str, Calendar calendar) throws SQLException {
        return getDate(findColumn(str), calendar);
    }

    @Override // java.sql.ResultSet
    public Time getTime(int i2, Calendar calendar) throws SQLException {
        checkState();
        return this.rs.getTime(i2, calendar);
    }

    @Override // java.sql.ResultSet
    public Time getTime(String str, Calendar calendar) throws SQLException {
        return getTime(findColumn(str), calendar);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException {
        checkState();
        return this.rs.getTimestamp(i2, calendar);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String str, Calendar calendar) throws SQLException {
        return getTimestamp(findColumn(str), calendar);
    }

    @Override // java.sql.ResultSet
    public void updateRef(int i2, Ref ref) throws SQLException {
        checkState();
        this.rs.updateRef(i2, ref);
    }

    @Override // java.sql.ResultSet
    public void updateRef(String str, Ref ref) throws SQLException {
        updateRef(findColumn(str), ref);
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Clob clob) throws SQLException {
        checkState();
        this.rs.updateClob(i2, clob);
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Clob clob) throws SQLException {
        updateClob(findColumn(str), clob);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, Blob blob) throws SQLException {
        checkState();
        this.rs.updateBlob(i2, blob);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, Blob blob) throws SQLException {
        updateBlob(findColumn(str), blob);
    }

    @Override // java.sql.ResultSet
    public void updateArray(int i2, Array array) throws SQLException {
        checkState();
        this.rs.updateArray(i2, array);
    }

    @Override // java.sql.ResultSet
    public void updateArray(String str, Array array) throws SQLException {
        updateArray(findColumn(str), array);
    }

    @Override // java.sql.ResultSet
    public URL getURL(int i2) throws SQLException {
        checkState();
        return this.rs.getURL(i2);
    }

    @Override // java.sql.ResultSet
    public URL getURL(String str) throws SQLException {
        return getURL(findColumn(str));
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public RowSetWarning getRowSetWarnings() throws SQLException {
        return null;
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(int[] iArr) throws SQLException, NumberFormatException {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != Integer.parseInt(this.iMatchColumns.get(i2).toString())) {
                throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols").toString());
            }
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            this.iMatchColumns.set(i3, -1);
        }
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(String[] strArr) throws SQLException {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (!strArr[i2].equals(this.strMatchColumns.get(i2))) {
                throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols").toString());
            }
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            this.strMatchColumns.set(i3, null);
        }
    }

    @Override // javax.sql.rowset.Joinable
    public String[] getMatchColumnNames() throws SQLException {
        String[] strArr = new String[this.strMatchColumns.size()];
        if (this.strMatchColumns.get(0) == null) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.setmatchcols").toString());
        }
        this.strMatchColumns.copyInto(strArr);
        return strArr;
    }

    @Override // javax.sql.rowset.Joinable
    public int[] getMatchColumnIndexes() throws SQLException {
        Integer[] numArr = new Integer[this.iMatchColumns.size()];
        int[] iArr = new int[this.iMatchColumns.size()];
        if (this.iMatchColumns.get(0).intValue() == -1) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.setmatchcols").toString());
        }
        this.iMatchColumns.copyInto(numArr);
        for (int i2 = 0; i2 < numArr.length; i2++) {
            iArr[i2] = numArr[i2].intValue();
        }
        return iArr;
    }

    @Override // javax.sql.rowset.Joinable
    public void setMatchColumn(int[] iArr) throws SQLException {
        for (int i2 : iArr) {
            if (i2 < 0) {
                throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols1").toString());
            }
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            this.iMatchColumns.add(i3, Integer.valueOf(iArr[i3]));
        }
    }

    @Override // javax.sql.rowset.Joinable
    public void setMatchColumn(String[] strArr) throws SQLException {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] == null || strArr[i2].equals("")) {
                throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols2").toString());
            }
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            this.strMatchColumns.add(i3, strArr[i3]);
        }
    }

    @Override // javax.sql.rowset.Joinable
    public void setMatchColumn(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols1").toString());
        }
        this.iMatchColumns.set(0, Integer.valueOf(i2));
    }

    @Override // javax.sql.rowset.Joinable
    public void setMatchColumn(String str) throws SQLException {
        if (str != null) {
            String strTrim = str.trim();
            if (!strTrim.equals("")) {
                this.strMatchColumns.set(0, strTrim);
                return;
            }
        }
        throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.matchcols2").toString());
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(int i2) throws SQLException {
        if (!this.iMatchColumns.get(0).equals(Integer.valueOf(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.unsetmatch").toString());
        }
        if (this.strMatchColumns.get(0) != null) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.usecolname").toString());
        }
        this.iMatchColumns.set(0, -1);
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(String str) throws SQLException {
        if (!this.strMatchColumns.get(0).equals(str.trim())) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.unsetmatch").toString());
        }
        if (this.iMatchColumns.get(0).intValue() > 0) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.usecolid").toString());
        }
        this.strMatchColumns.set(0, null);
    }

    public DatabaseMetaData getDatabaseMetaData() throws SQLException {
        return connect().getMetaData();
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        prepare();
        return this.ps.getParameterMetaData();
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public void commit() throws SQLException {
        this.conn.commit();
        if (this.conn.getHoldability() != 1) {
            this.rs = null;
        }
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public void setAutoCommit(boolean z2) throws SQLException {
        if (this.conn != null) {
            this.conn.setAutoCommit(z2);
        } else {
            this.conn = connect();
            this.conn.setAutoCommit(z2);
        }
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public boolean getAutoCommit() throws SQLException {
        return this.conn.getAutoCommit();
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public void rollback() throws SQLException {
        this.conn.rollback();
        this.rs = null;
    }

    @Override // javax.sql.rowset.JdbcRowSet
    public void rollback(Savepoint savepoint) throws SQLException {
        this.conn.rollback(savepoint);
    }

    protected void setParams() throws SQLException {
        if (this.rs == null) {
            setType(1004);
            setConcurrency(1008);
        } else {
            setType(this.rs.getType());
            setConcurrency(this.rs.getConcurrency());
        }
    }

    private void checkTypeConcurrency() throws SQLException {
        if (this.rs.getType() == 1003 || this.rs.getConcurrency() == 1007) {
            throw new SQLException(this.resBundle.handleGetObject("jdbcrowsetimpl.resnotupd").toString());
        }
    }

    protected Connection getConnection() {
        return this.conn;
    }

    protected void setConnection(Connection connection) {
        this.conn = connection;
    }

    protected PreparedStatement getPreparedStatement() {
        return this.ps;
    }

    protected void setPreparedStatement(PreparedStatement preparedStatement) {
        this.ps = preparedStatement;
    }

    protected ResultSet getResultSet() throws SQLException {
        checkState();
        return this.rs;
    }

    protected void setResultSet(ResultSet resultSet) {
        this.rs = resultSet;
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCommand(String str) throws SQLException {
        if (getCommand() != null) {
            if (!getCommand().equals(str)) {
                super.setCommand(str);
                this.ps = null;
                this.rs = null;
                return;
            }
            return;
        }
        super.setCommand(str);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDataSourceName(String str) throws SQLException {
        if (getDataSourceName() != null) {
            if (!getDataSourceName().equals(str)) {
                super.setDataSourceName(str);
                this.conn = null;
                this.ps = null;
                this.rs = null;
                return;
            }
            return;
        }
        super.setDataSourceName(str);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setUrl(String str) throws SQLException {
        if (getUrl() != null) {
            if (!getUrl().equals(str)) {
                super.setUrl(str);
                this.conn = null;
                this.ps = null;
                this.rs = null;
                return;
            }
            return;
        }
        super.setUrl(str);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setUsername(String str) {
        if (getUsername() != null) {
            if (!getUsername().equals(str)) {
                super.setUsername(str);
                this.conn = null;
                this.ps = null;
                this.rs = null;
                return;
            }
            return;
        }
        super.setUsername(str);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setPassword(String str) {
        if (getPassword() != null) {
            if (!getPassword().equals(str)) {
                super.setPassword(str);
                this.conn = null;
                this.ps = null;
                this.rs = null;
                return;
            }
            return;
        }
        super.setPassword(str);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setType(int i2) throws SQLException {
        int type;
        try {
            type = getType();
        } catch (SQLException e2) {
            type = 0;
        }
        if (type != i2) {
            super.setType(i2);
        }
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setConcurrency(int i2) throws SQLException {
        int concurrency;
        try {
            concurrency = getConcurrency();
        } catch (NullPointerException e2) {
            concurrency = 0;
        }
        if (concurrency != i2) {
            super.setConcurrency(i2);
        }
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateRowId(int i2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateRowId(String str, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public boolean isClosed() throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNString(int i2, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        return null;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> cls) throws SQLException {
        return false;
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setSQLXML(int i2, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setSQLXML(String str, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setRowId(int i2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setRowId(String str, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNString(int i2, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(int i2, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(String str, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public String getNString(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public String getNString(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setURL(int i2, URL url) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTimestamp(String str, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Clob clob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDate(String str, Date date) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDate(String str, Date date, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTime(String str, Time time) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTime(String str, Time time, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, Blob blob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj, int i2, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCharacterStream(String str, Reader reader, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setAsciiStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBinaryStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBytes(String str, byte[] bArr) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTimestamp(String str, Timestamp timestamp) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNull(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNull(String str, int i2, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBoolean(String str, boolean z2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setByte(String str, byte b2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setShort(String str, short s2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setInt(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setLong(String str, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setFloat(String str, float f2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDouble(String str, double d2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("jdbcrowsetimpl.featnotsupp").toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
        }
    }

    @Override // java.sql.ResultSet
    public <T> T getObject(int i2, Class<T> cls) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }

    @Override // java.sql.ResultSet
    public <T> T getObject(String str, Class<T> cls) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not supported yet.");
    }
}

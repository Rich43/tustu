package com.sun.rowset.internal;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JdbcRowSetResourceBundle;
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
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;

/* loaded from: rt.jar:com/sun/rowset/internal/SyncResolverImpl.class */
public class SyncResolverImpl extends CachedRowSetImpl implements SyncResolver {
    private CachedRowSetImpl crsRes;
    private CachedRowSetImpl crsSync;
    private ArrayList<?> stats;
    private CachedRowSetWriter crw;
    private int rowStatus;
    private int sz;
    private transient Connection con;
    private CachedRowSet row;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = -3345004441725080251L;

    public SyncResolverImpl() throws SQLException {
        try {
            this.crsSync = new CachedRowSetImpl();
            this.crsRes = new CachedRowSetImpl();
            this.crw = new CachedRowSetWriter();
            this.row = new CachedRowSetImpl();
            this.rowStatus = 1;
            try {
                this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        } catch (SQLException e3) {
        }
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public int getStatus() {
        return ((Integer) this.stats.get(this.rowStatus - 1)).intValue();
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public Object getConflictValue(int i2) throws SQLException {
        try {
            return this.crsRes.getObject(i2);
        } catch (SQLException e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public Object getConflictValue(String str) throws SQLException {
        try {
            return this.crsRes.getObject(str);
        } catch (SQLException e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public void setResolvedValue(int i2, Object obj) throws SQLException {
        if (i2 > 0) {
            try {
                if (i2 <= this.crsSync.getMetaData().getColumnCount()) {
                    if (this.crsRes.getObject(i2) == null) {
                        throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.noconflict").toString());
                    }
                    try {
                        boolean z2 = true;
                        if (this.crsSync.getObject(i2).toString().equals(obj.toString()) || this.crsRes.getObject(i2).toString().equals(obj.toString())) {
                            this.crsRes.updateNull(i2);
                            this.crsRes.updateRow();
                            if (this.row.size() != 1) {
                                this.row = buildCachedRow();
                            }
                            this.row.updateObject(i2, obj);
                            this.row.updateRow();
                            int i3 = 1;
                            while (true) {
                                if (i3 >= this.crsRes.getMetaData().getColumnCount()) {
                                    break;
                                }
                                if (this.crsRes.getObject(i3) == null) {
                                    i3++;
                                } else {
                                    z2 = false;
                                    break;
                                }
                            }
                            if (z2) {
                                try {
                                    writeData(this.row);
                                } catch (SyncProviderException e2) {
                                    throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.syncnotpos").toString());
                                }
                            }
                            return;
                        }
                        throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.valtores").toString());
                    } catch (SQLException e3) {
                        throw new SQLException(e3.getMessage());
                    }
                }
            } catch (SQLException e4) {
                throw new SQLException(e4.getMessage());
            }
        }
        throw new SQLException(this.resBundle.handleGetObject("syncrsimpl.indexval").toString() + i2);
    }

    private void writeData(CachedRowSet cachedRowSet) throws SQLException {
        this.crw.updateResolvedConflictToDB(cachedRowSet, this.crw.getReader().connect(this.crsSync));
    }

    private CachedRowSet buildCachedRow() throws SQLException {
        CachedRowSetImpl cachedRowSetImpl = new CachedRowSetImpl();
        new RowSetMetaDataImpl();
        RowSetMetaDataImpl rowSetMetaDataImpl = (RowSetMetaDataImpl) this.crsSync.getMetaData();
        RowSetMetaDataImpl rowSetMetaDataImpl2 = new RowSetMetaDataImpl();
        int columnCount = rowSetMetaDataImpl.getColumnCount();
        rowSetMetaDataImpl2.setColumnCount(columnCount);
        for (int i2 = 1; i2 <= columnCount; i2++) {
            rowSetMetaDataImpl2.setColumnType(i2, rowSetMetaDataImpl.getColumnType(i2));
            rowSetMetaDataImpl2.setColumnName(i2, rowSetMetaDataImpl.getColumnName(i2));
            rowSetMetaDataImpl2.setNullable(i2, 2);
            try {
                rowSetMetaDataImpl2.setCatalogName(i2, rowSetMetaDataImpl.getCatalogName(i2));
                rowSetMetaDataImpl2.setSchemaName(i2, rowSetMetaDataImpl.getSchemaName(i2));
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        cachedRowSetImpl.setMetaData(rowSetMetaDataImpl2);
        cachedRowSetImpl.moveToInsertRow();
        for (int i3 = 1; i3 <= this.crsSync.getMetaData().getColumnCount(); i3++) {
            cachedRowSetImpl.updateObject(i3, this.crsSync.getObject(i3));
        }
        cachedRowSetImpl.insertRow();
        cachedRowSetImpl.moveToCurrentRow();
        cachedRowSetImpl.absolute(1);
        cachedRowSetImpl.setOriginalRow();
        try {
            cachedRowSetImpl.setUrl(this.crsSync.getUrl());
        } catch (SQLException e3) {
        }
        try {
            cachedRowSetImpl.setDataSourceName(this.crsSync.getCommand());
        } catch (SQLException e4) {
        }
        try {
            if (this.crsSync.getTableName() != null) {
                cachedRowSetImpl.setTableName(this.crsSync.getTableName());
            }
        } catch (SQLException e5) {
        }
        try {
            if (this.crsSync.getCommand() != null) {
                cachedRowSetImpl.setCommand(this.crsSync.getCommand());
            }
        } catch (SQLException e6) {
        }
        try {
            cachedRowSetImpl.setKeyColumns(this.crsSync.getKeyColumns());
        } catch (SQLException e7) {
        }
        return cachedRowSetImpl;
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public void setResolvedValue(String str, Object obj) throws SQLException {
    }

    void setCachedRowSet(CachedRowSet cachedRowSet) {
        this.crsSync = (CachedRowSetImpl) cachedRowSet;
    }

    void setCachedRowSetResolver(CachedRowSet cachedRowSet) {
        try {
            this.crsRes = (CachedRowSetImpl) cachedRowSet;
            this.crsRes.afterLast();
            this.sz = this.crsRes.size();
        } catch (SQLException e2) {
        }
    }

    void setStatus(ArrayList arrayList) {
        this.stats = arrayList;
    }

    void setCachedRowSetWriter(CachedRowSetWriter cachedRowSetWriter) {
        this.crw = cachedRowSetWriter;
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public boolean nextConflict() throws SQLException {
        boolean z2 = false;
        this.crsSync.setShowDeleted(true);
        while (true) {
            if (!this.crsSync.next()) {
                break;
            }
            this.crsRes.previous();
            this.rowStatus++;
            if (this.rowStatus - 1 >= this.stats.size()) {
                z2 = false;
                break;
            }
            if (((Integer) this.stats.get(this.rowStatus - 1)).intValue() != 3) {
                z2 = true;
                break;
            }
        }
        this.crsSync.setShowDeleted(false);
        return z2;
    }

    @Override // javax.sql.rowset.spi.SyncResolver
    public boolean previousConflict() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCommand(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void populate(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void execute(Connection connection) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void acceptChanges() throws SyncProviderException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void acceptChanges(Connection connection) throws SyncProviderException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void restoreOriginal() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void release() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void undoDelete() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void undoInsert() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void undoUpdate() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public RowSet createShared() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopy() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopySchema() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopyNoConstraints() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection toCollection() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection toCollection(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection toCollection(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public SyncProvider getSyncProvider() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setSyncProvider(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSet
    public void execute() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean next() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalNext() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean wasNull() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected BaseRow getCurrentRow() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected void removeCurrentRow() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getString(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean getBoolean(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte getByte(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public short getShort(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getInt(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public long getLong(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public float getFloat(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public double getDouble(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int i2, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte[] getBytes(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getAsciiStream(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getBinaryStream(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getString(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean getBoolean(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte getByte(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public short getShort(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getInt(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public long getLong(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public float getFloat(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public double getDouble(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String str, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte[] getBytes(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getAsciiStream(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getBinaryStream(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public SQLWarning getWarnings() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void clearWarnings() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getCursorName() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int findColumn(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Reader getCharacterStream(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Reader getCharacterStream(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public BigDecimal getBigDecimal(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public BigDecimal getBigDecimal(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isLast() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void afterLast() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean first() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalFirst() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean last() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalLast() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getRow() throws SQLException {
        return this.crsSync.getRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean absolute(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean relative(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean previous() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalPrevious() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public boolean columnUpdated(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public boolean columnUpdated(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateNull(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(int i2, boolean z2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(int i2, byte b2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(int i2, short s2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(int i2, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(int i2, long j2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(int i2, float f2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(int i2, double d2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(int i2, String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(int i2, byte[] bArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(int i2, Date date) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(int i2, Time time) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(int i2, Timestamp timestamp) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj, int i3) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateNull(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(String str, boolean z2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(String str, byte b2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(String str, short s2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(String str, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(String str, long j2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(String str, float f2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(String str, double d2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(String str, String str2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(String str, byte[] bArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(String str, Date date) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(String str, Time time) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void insertRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void deleteRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void refreshRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Statement getStatement() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(int i2, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Ref getRef(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Blob getBlob(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Clob getClob(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Array getArray(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(String str, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Ref getRef(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Blob getBlob(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Clob getClob(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Array getArray(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(int i2, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(String str, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(int i2, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(String str, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(String str, Calendar calendar) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public void setMetaData(RowSetMetaData rowSetMetaData) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginal() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginalRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setOriginalRow() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    public void setOriginal() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public String getTableName() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setTableName(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public int[] getKeyColumns() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setKeyColumns(int[] iArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRef(int i2, Ref ref) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRef(String str, Ref ref) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateClob(int i2, Clob clob) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateClob(String str, Clob clob) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBlob(int i2, Blob blob) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBlob(String str, Blob blob) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateArray(int i2, Array array) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateArray(String str, Array array) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public URL getURL(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public URL getURL(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public RowSetWarning getRowSetWarnings() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void commit() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void rollback() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void unsetMatchColumn(int[] iArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void unsetMatchColumn(String[] strArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public String[] getMatchColumnNames() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public int[] getMatchColumnIndexes() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void setMatchColumn(int[] iArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void setMatchColumn(String[] strArr) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void setMatchColumn(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void setMatchColumn(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void unsetMatchColumn(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.Joinable
    public void unsetMatchColumn(String str) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void rowSetPopulated(RowSetEvent rowSetEvent, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void populate(ResultSet resultSet, int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public boolean nextPage() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setPageSize(int i2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public boolean previousPage() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void updateNCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        throw new UnsupportedOperationException("Operation not yet supported");
    }

    public void updateNCharacterStream(String str, Reader reader, int i2) throws SQLException {
        throw new UnsupportedOperationException("Operation not yet supported");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }
}

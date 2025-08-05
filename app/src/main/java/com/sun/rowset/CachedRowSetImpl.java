package com.sun.rowset;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.rowset.internal.BaseRow;
import com.sun.rowset.internal.CachedRowSetReader;
import com.sun.rowset.internal.CachedRowSetWriter;
import com.sun.rowset.internal.InsertRow;
import com.sun.rowset.internal.Row;
import com.sun.rowset.providers.RIOptimisticProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetInternal;
import javax.sql.RowSetMetaData;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.BaseRowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.serial.SQLInputImpl;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialRef;
import javax.sql.rowset.serial.SerialStruct;
import javax.sql.rowset.spi.SyncFactory;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.TransactionalWriter;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/rowset/CachedRowSetImpl.class */
public class CachedRowSetImpl extends BaseRowSet implements RowSet, RowSetInternal, Serializable, Cloneable, CachedRowSet {
    private SyncProvider provider;
    private RowSetReader rowSetReader;
    private RowSetWriter rowSetWriter;
    private transient Connection conn;
    private transient ResultSetMetaData RSMD;
    private RowSetMetaDataImpl RowSetMD;
    private int[] keyCols;
    private String tableName;
    private Vector<Object> rvh;
    private int cursorPos;
    private int absolutePos;
    private int numDeleted;
    private int numRows;
    private InsertRow insertRow;
    private boolean onInsertRow;
    private int currentRow;
    private boolean lastValueNull;
    private SQLWarning sqlwarn;
    private RowSetWarning rowsetWarning;
    private boolean dbmslocatorsUpdateCopy;
    private transient ResultSet resultSet;
    private int endPos;
    private int prevEndPos;
    private int startPos;
    private int startPrev;
    private int pageSize;
    private int maxRowsreached;
    private boolean onFirstPage;
    private boolean onLastPage;
    private int populatecallcount;
    private int totalRows;
    private boolean callWithCon;
    private CachedRowSetReader crsReader;
    private Vector<Integer> iMatchColumns;
    private Vector<String> strMatchColumns;
    protected transient JdbcRowSetResourceBundle resBundle;
    private boolean updateOnInsert;
    static final long serialVersionUID = 1884577171200622428L;
    private String strMatchColumn = "";
    private int iMatchColumn = -1;
    private String DEFAULT_SYNC_PROVIDER = "com.sun.rowset.providers.RIOptimisticProvider";
    private boolean pagenotend = true;
    private boolean tXWriter = false;
    private TransactionalWriter tWriter = null;

    public CachedRowSetImpl() throws SQLException {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            this.provider = SyncFactory.getInstance(this.DEFAULT_SYNC_PROVIDER);
            if (!(this.provider instanceof RIOptimisticProvider)) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidp").toString());
            }
            this.rowSetReader = (CachedRowSetReader) this.provider.getRowSetReader();
            this.rowSetWriter = (CachedRowSetWriter) this.provider.getRowSetWriter();
            initParams();
            initContainer();
            initProperties();
            this.onInsertRow = false;
            this.insertRow = null;
            this.sqlwarn = new SQLWarning();
            this.rowsetWarning = new RowSetWarning();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public CachedRowSetImpl(Hashtable hashtable) throws SQLException {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            if (hashtable == null) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nullhash").toString());
            }
            this.provider = SyncFactory.getInstance((String) hashtable.get(SyncFactory.ROWSET_SYNC_PROVIDER));
            this.rowSetReader = this.provider.getRowSetReader();
            this.rowSetWriter = this.provider.getRowSetWriter();
            initParams();
            initContainer();
            initProperties();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void initContainer() {
        this.rvh = new Vector<>(100);
        this.cursorPos = 0;
        this.absolutePos = 0;
        this.numRows = 0;
        this.numDeleted = 0;
    }

    private void initProperties() throws SQLException {
        if (this.resBundle == null) {
            try {
                this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        setShowDeleted(false);
        setQueryTimeout(0);
        setMaxRows(0);
        setMaxFieldSize(0);
        setType(1004);
        setConcurrency(1008);
        if (this.rvh.size() > 0 && !isReadOnly()) {
            setReadOnly(false);
        } else {
            setReadOnly(true);
        }
        setTransactionIsolation(2);
        setEscapeProcessing(true);
        checkTransactionalWriter();
        this.iMatchColumns = new Vector<>(10);
        for (int i2 = 0; i2 < 10; i2++) {
            this.iMatchColumns.add(i2, -1);
        }
        this.strMatchColumns = new Vector<>(10);
        for (int i3 = 0; i3 < 10; i3++) {
            this.strMatchColumns.add(i3, null);
        }
    }

    private void checkTransactionalWriter() {
        Class<?> cls;
        if (this.rowSetWriter != null && (cls = this.rowSetWriter.getClass()) != null) {
            for (Class<?> cls2 : cls.getInterfaces()) {
                if (cls2.getName().indexOf("TransactionalWriter") > 0) {
                    this.tXWriter = true;
                    establishTransactionalWriter();
                }
            }
        }
    }

    private void establishTransactionalWriter() {
        this.tWriter = (TransactionalWriter) this.provider.getRowSetWriter();
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCommand(String str) throws SQLException {
        super.setCommand(str);
        if (!buildTableName(str).equals("")) {
            setTableName(buildTableName(str));
        }
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void populate(ResultSet resultSet) throws SQLException {
        Object object;
        Map<String, Class<?>> typeMap = getTypeMap();
        if (resultSet == null) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.populate").toString());
        }
        this.resultSet = resultSet;
        this.RSMD = resultSet.getMetaData();
        this.RowSetMD = new RowSetMetaDataImpl();
        initMetaData(this.RowSetMD, this.RSMD);
        this.RSMD = null;
        int columnCount = this.RowSetMD.getColumnCount();
        int maxRows = getMaxRows();
        int i2 = 0;
        while (resultSet.next()) {
            Row row = new Row(columnCount);
            if (i2 > maxRows && maxRows > 0) {
                this.rowsetWarning.setNextWarning(new RowSetWarning("Populating rows setting has exceeded max row setting"));
            }
            for (int i3 = 1; i3 <= columnCount; i3++) {
                if (typeMap == null || typeMap.isEmpty()) {
                    object = resultSet.getObject(i3);
                } else {
                    object = resultSet.getObject(i3, typeMap);
                }
                if (object instanceof Struct) {
                    object = new SerialStruct((Struct) object, typeMap);
                } else if (object instanceof SQLData) {
                    object = new SerialStruct((SQLData) object, typeMap);
                } else if (object instanceof Blob) {
                    object = new SerialBlob((Blob) object);
                } else if (object instanceof Clob) {
                    object = new SerialClob((Clob) object);
                } else if (object instanceof Array) {
                    if (typeMap != null) {
                        object = new SerialArray((Array) object, typeMap);
                    } else {
                        object = new SerialArray((Array) object);
                    }
                }
                row.initColumnObject(i3, object);
            }
            i2++;
            this.rvh.add(row);
        }
        this.numRows = i2;
        notifyRowSetChanged();
    }

    private void initMetaData(RowSetMetaDataImpl rowSetMetaDataImpl, ResultSetMetaData resultSetMetaData) throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();
        rowSetMetaDataImpl.setColumnCount(columnCount);
        for (int i2 = 1; i2 <= columnCount; i2++) {
            rowSetMetaDataImpl.setAutoIncrement(i2, resultSetMetaData.isAutoIncrement(i2));
            if (resultSetMetaData.isAutoIncrement(i2)) {
                this.updateOnInsert = true;
            }
            rowSetMetaDataImpl.setCaseSensitive(i2, resultSetMetaData.isCaseSensitive(i2));
            rowSetMetaDataImpl.setCurrency(i2, resultSetMetaData.isCurrency(i2));
            rowSetMetaDataImpl.setNullable(i2, resultSetMetaData.isNullable(i2));
            rowSetMetaDataImpl.setSigned(i2, resultSetMetaData.isSigned(i2));
            rowSetMetaDataImpl.setSearchable(i2, resultSetMetaData.isSearchable(i2));
            int columnDisplaySize = resultSetMetaData.getColumnDisplaySize(i2);
            if (columnDisplaySize < 0) {
                columnDisplaySize = 0;
            }
            rowSetMetaDataImpl.setColumnDisplaySize(i2, columnDisplaySize);
            rowSetMetaDataImpl.setColumnLabel(i2, resultSetMetaData.getColumnLabel(i2));
            rowSetMetaDataImpl.setColumnName(i2, resultSetMetaData.getColumnName(i2));
            rowSetMetaDataImpl.setSchemaName(i2, resultSetMetaData.getSchemaName(i2));
            int precision = resultSetMetaData.getPrecision(i2);
            if (precision < 0) {
                precision = 0;
            }
            rowSetMetaDataImpl.setPrecision(i2, precision);
            int scale = resultSetMetaData.getScale(i2);
            if (scale < 0) {
                scale = 0;
            }
            rowSetMetaDataImpl.setScale(i2, scale);
            rowSetMetaDataImpl.setTableName(i2, resultSetMetaData.getTableName(i2));
            rowSetMetaDataImpl.setCatalogName(i2, resultSetMetaData.getCatalogName(i2));
            rowSetMetaDataImpl.setColumnType(i2, resultSetMetaData.getColumnType(i2));
            rowSetMetaDataImpl.setColumnTypeName(i2, resultSetMetaData.getColumnTypeName(i2));
        }
        if (this.conn != null) {
            this.dbmslocatorsUpdateCopy = this.conn.getMetaData().locatorsUpdateCopy();
        }
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void execute(Connection connection) throws SQLException {
        setConnection(connection);
        if (getPageSize() != 0) {
            this.crsReader = (CachedRowSetReader) this.provider.getRowSetReader();
            this.crsReader.setStartPosition(1);
            this.callWithCon = true;
            this.crsReader.readData(this);
        } else {
            this.rowSetReader.readData(this);
        }
        this.RowSetMD = (RowSetMetaDataImpl) getMetaData();
        if (connection != null) {
            this.dbmslocatorsUpdateCopy = connection.getMetaData().locatorsUpdateCopy();
        }
    }

    private void setConnection(Connection connection) {
        this.conn = connection;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void acceptChanges() throws SyncProviderException {
        if (this.onInsertRow) {
            throw new SyncProviderException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        int i2 = this.cursorPos;
        boolean z2 = false;
        boolean zWriteData = false;
        try {
            if (this.rowSetWriter != null) {
                int i3 = this.cursorPos;
                zWriteData = this.rowSetWriter.writeData(this);
                this.cursorPos = i3;
            }
            if (this.tXWriter) {
                if (!zWriteData) {
                    this.tWriter = (TransactionalWriter) this.rowSetWriter;
                    this.tWriter.rollback();
                    z2 = false;
                } else {
                    this.tWriter = (TransactionalWriter) this.rowSetWriter;
                    if (this.tWriter instanceof CachedRowSetWriter) {
                        ((CachedRowSetWriter) this.tWriter).commit(this, this.updateOnInsert);
                    } else {
                        this.tWriter.commit();
                    }
                    z2 = true;
                }
            }
            if (z2) {
                setOriginal();
            } else if (!z2) {
                throw new SyncProviderException(this.resBundle.handleGetObject("cachedrowsetimpl.accfailed").toString());
            }
        } catch (SecurityException e2) {
            throw new SyncProviderException(e2.getMessage());
        } catch (SyncProviderException e3) {
            throw e3;
        } catch (SQLException e4) {
            e4.printStackTrace();
            throw new SyncProviderException(e4.getMessage());
        }
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void acceptChanges(Connection connection) throws SyncProviderException {
        setConnection(connection);
        acceptChanges();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void restoreOriginal() throws SQLException {
        Iterator<Object> it = this.rvh.iterator();
        while (it.hasNext()) {
            Row row = (Row) it.next();
            if (row.getInserted()) {
                it.remove();
                this.numRows--;
            } else {
                if (row.getDeleted()) {
                    row.clearDeleted();
                }
                if (row.getUpdated()) {
                    row.clearUpdated();
                }
            }
        }
        this.cursorPos = 0;
        notifyRowSetChanged();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void release() throws SQLException {
        initContainer();
        notifyRowSetChanged();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void undoDelete() throws SQLException {
        if (!getShowDeleted()) {
            return;
        }
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        Row row = (Row) getCurrentRow();
        if (row.getDeleted()) {
            row.clearDeleted();
            this.numDeleted--;
            notifyRowChanged();
        }
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void undoInsert() throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        if (((Row) getCurrentRow()).getInserted()) {
            this.rvh.remove(this.cursorPos - 1);
            this.numRows--;
            notifyRowChanged();
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.illegalop").toString());
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void undoUpdate() throws SQLException {
        moveToCurrentRow();
        undoDelete();
        undoInsert();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public RowSet createShared() throws SQLException {
        try {
            return (RowSet) clone();
        } catch (CloneNotSupportedException e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopy() throws SQLException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(this);
            try {
                try {
                    CachedRowSetImpl cachedRowSetImpl = (CachedRowSetImpl) new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
                    cachedRowSetImpl.resBundle = this.resBundle;
                    return cachedRowSetImpl;
                } catch (OptionalDataException e2) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e2.getMessage()));
                } catch (IOException e3) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e3.getMessage()));
                } catch (ClassNotFoundException e4) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e4.getMessage()));
                }
            } catch (StreamCorruptedException e5) {
                throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e5.getMessage()));
            } catch (IOException e6) {
                throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e6.getMessage()));
            }
        } catch (IOException e7) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.clonefail").toString(), e7.getMessage()));
        }
    }

    @Override // javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopySchema() throws SQLException {
        int i2 = this.numRows;
        this.numRows = 0;
        CachedRowSet cachedRowSetCreateCopy = createCopy();
        this.numRows = i2;
        return cachedRowSetCreateCopy;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopyNoConstraints() throws SQLException, NumberFormatException {
        CachedRowSetImpl cachedRowSetImpl = (CachedRowSetImpl) createCopy();
        cachedRowSetImpl.initProperties();
        try {
            cachedRowSetImpl.unsetMatchColumn(cachedRowSetImpl.getMatchColumnIndexes());
        } catch (SQLException e2) {
        }
        try {
            cachedRowSetImpl.unsetMatchColumn(cachedRowSetImpl.getMatchColumnNames());
        } catch (SQLException e3) {
        }
        return cachedRowSetImpl;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection() throws SQLException {
        TreeMap treeMap = new TreeMap();
        for (int i2 = 0; i2 < this.numRows; i2++) {
            treeMap.put(Integer.valueOf(i2), this.rvh.get(i2));
        }
        return treeMap.values();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection(int i2) throws SQLException {
        int i3 = this.numRows;
        Vector vector = new Vector(i3);
        CachedRowSetImpl cachedRowSetImpl = (CachedRowSetImpl) createCopy();
        while (i3 != 0) {
            cachedRowSetImpl.next();
            vector.add(cachedRowSetImpl.getObject(i2));
            i3--;
        }
        return vector;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection(String str) throws SQLException {
        return toCollection(getColIdxByName(str));
    }

    @Override // javax.sql.rowset.CachedRowSet
    public SyncProvider getSyncProvider() throws SQLException {
        return this.provider;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void setSyncProvider(String str) throws SQLException {
        this.provider = SyncFactory.getInstance(str);
        this.rowSetReader = this.provider.getRowSetReader();
        this.rowSetWriter = this.provider.getRowSetWriter();
    }

    @Override // javax.sql.RowSet
    public void execute() throws SQLException {
        execute(null);
    }

    @Override // java.sql.ResultSet
    public boolean next() throws SQLException {
        if (this.cursorPos < 0 || this.cursorPos >= this.numRows + 1) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        boolean zInternalNext = internalNext();
        notifyCursorMoved();
        return zInternalNext;
    }

    protected boolean internalNext() throws SQLException {
        boolean z2 = false;
        while (true) {
            if (this.cursorPos < this.numRows) {
                this.cursorPos++;
                z2 = true;
            } else if (this.cursorPos == this.numRows) {
                this.cursorPos++;
                z2 = false;
                break;
            }
            if (getShowDeleted() || !rowDeleted()) {
                break;
            }
        }
        if (z2) {
            this.absolutePos++;
        } else {
            this.absolutePos = 0;
        }
        return z2;
    }

    @Override // java.sql.ResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        this.cursorPos = 0;
        this.absolutePos = 0;
        this.numRows = 0;
        this.numDeleted = 0;
        initProperties();
        this.rvh.clear();
    }

    @Override // java.sql.ResultSet
    public boolean wasNull() throws SQLException {
        return this.lastValueNull;
    }

    private void setLastValueNull(boolean z2) {
        this.lastValueNull = z2;
    }

    private void checkIndex(int i2) throws SQLException {
        if (i2 < 1 || i2 > this.RowSetMD.getColumnCount()) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcol").toString());
        }
    }

    private void checkCursor() throws SQLException {
        if (isAfterLast() || isBeforeFirst()) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
    }

    private int getColIdxByName(String str) throws SQLException {
        this.RowSetMD = (RowSetMetaDataImpl) getMetaData();
        int columnCount = this.RowSetMD.getColumnCount();
        for (int i2 = 1; i2 <= columnCount; i2++) {
            String columnName = this.RowSetMD.getColumnName(i2);
            if (columnName != null && str.equalsIgnoreCase(columnName)) {
                return i2;
            }
        }
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalcolnm").toString());
    }

    protected BaseRow getCurrentRow() {
        if (this.onInsertRow) {
            return this.insertRow;
        }
        return (BaseRow) this.rvh.get(this.cursorPos - 1);
    }

    protected void removeCurrentRow() {
        ((Row) getCurrentRow()).setDeleted();
        this.rvh.remove(this.cursorPos - 1);
        this.numRows--;
    }

    @Override // java.sql.ResultSet
    public String getString(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        return columnObject.toString();
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return false;
        }
        if (columnObject instanceof Boolean) {
            return ((Boolean) columnObject).booleanValue();
        }
        try {
            return Double.compare(Double.parseDouble(columnObject.toString()), 0.0d) != 0;
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.boolfail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public byte getByte(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return (byte) 0;
        }
        try {
            return Byte.valueOf(columnObject.toString()).byteValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.bytefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public short getShort(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return (short) 0;
        }
        try {
            return Short.valueOf(columnObject.toString().trim()).shortValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.shortfail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public int getInt(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return 0;
        }
        try {
            return Integer.valueOf(columnObject.toString().trim()).intValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.intfail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public long getLong(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return 0L;
        }
        try {
            return Long.valueOf(columnObject.toString().trim()).longValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.longfail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public float getFloat(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return 0.0f;
        }
        try {
            return new Float(columnObject.toString()).floatValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.floatfail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public double getDouble(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return 0.0d;
        }
        try {
            return new Double(columnObject.toString().trim()).doubleValue();
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.doublefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int i2, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        if (getCurrentRow().getColumnObject(i2) == null) {
            setLastValueNull(true);
            return new BigDecimal(0);
        }
        return getBigDecimal(i2).setScale(i3);
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (!isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        return (byte[]) getCurrentRow().getColumnObject(i2);
    }

    @Override // java.sql.ResultSet
    public Date getDate(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        switch (this.RowSetMD.getColumnType(i2)) {
            case -1:
            case 1:
            case 12:
                try {
                    return (Date) DateFormat.getDateInstance().parse(columnObject.toString());
                } catch (ParseException e2) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.datefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
                }
            case 91:
                return new Date(((Date) columnObject).getTime());
            case 93:
                return new Date(((Timestamp) columnObject).getTime());
            default:
                throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.datefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public Time getTime(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        switch (this.RowSetMD.getColumnType(i2)) {
            case -1:
            case 1:
            case 12:
                try {
                    return (Time) DateFormat.getTimeInstance().parse(columnObject.toString());
                } catch (ParseException e2) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
                }
            case 92:
                return (Time) columnObject;
            case 93:
                return new Time(((Timestamp) columnObject).getTime());
            default:
                throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        switch (this.RowSetMD.getColumnType(i2)) {
            case -1:
            case 1:
            case 12:
                try {
                    return (Timestamp) DateFormat.getTimeInstance().parse(columnObject.toString());
                } catch (ParseException e2) {
                    throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
                }
            case 91:
                return new Timestamp(((Date) columnObject).getTime());
            case 92:
                return new Timestamp(((Time) columnObject).getTime());
            case 93:
                return (Timestamp) columnObject;
            default:
                throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.timefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(int i2) throws SQLException {
        this.asciiStream = null;
        checkIndex(i2);
        checkCursor();
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            this.lastValueNull = true;
            return null;
        }
        try {
            if (isString(this.RowSetMD.getColumnType(i2))) {
                this.asciiStream = new ByteArrayInputStream(((String) columnObject).getBytes("ASCII"));
                return this.asciiStream;
            }
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        } catch (UnsupportedEncodingException e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(int i2) throws SQLException {
        this.unicodeStream = null;
        checkIndex(i2);
        checkCursor();
        if (!isBinary(this.RowSetMD.getColumnType(i2)) && !isString(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            this.lastValueNull = true;
            return null;
        }
        this.unicodeStream = new StringBufferInputStream(columnObject.toString());
        return this.unicodeStream;
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(int i2) throws SQLException {
        this.binaryStream = null;
        checkIndex(i2);
        checkCursor();
        if (!isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            this.lastValueNull = true;
            return null;
        }
        this.binaryStream = new ByteArrayInputStream((byte[]) columnObject);
        return this.binaryStream;
    }

    @Override // java.sql.ResultSet
    public String getString(String str) throws SQLException {
        return getString(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(String str) throws SQLException {
        return getBoolean(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public byte getByte(String str) throws SQLException {
        return getByte(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public short getShort(String str) throws SQLException {
        return getShort(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public int getInt(String str) throws SQLException {
        return getInt(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public long getLong(String str) throws SQLException {
        return getLong(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public float getFloat(String str) throws SQLException {
        return getFloat(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public double getDouble(String str) throws SQLException {
        return getDouble(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String str, int i2) throws SQLException {
        return getBigDecimal(getColIdxByName(str), i2);
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(String str) throws SQLException {
        return getBytes(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Date getDate(String str) throws SQLException {
        return getDate(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Time getTime(String str) throws SQLException {
        return getTime(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String str) throws SQLException {
        return getTimestamp(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(String str) throws SQLException {
        return getAsciiStream(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(String str) throws SQLException {
        return getUnicodeStream(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(String str) throws SQLException {
        return getBinaryStream(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public SQLWarning getWarnings() {
        return this.sqlwarn;
    }

    @Override // java.sql.ResultSet
    public void clearWarnings() {
        this.sqlwarn = null;
    }

    @Override // java.sql.ResultSet
    public String getCursorName() throws SQLException {
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.posupdate").toString());
    }

    @Override // java.sql.ResultSet
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.RowSetMD;
    }

    @Override // java.sql.ResultSet
    public Object getObject(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        if (columnObject instanceof Struct) {
            Struct struct = (Struct) columnObject;
            Map<String, Class<?>> typeMap = getTypeMap();
            Class<?> cls = typeMap.get(struct.getSQLTypeName());
            if (cls != null) {
                try {
                    SQLData sQLData = (SQLData) ReflectUtil.newInstance(cls);
                    sQLData.readSQL(new SQLInputImpl(struct.getAttributes(typeMap), typeMap), struct.getSQLTypeName());
                    return sQLData;
                } catch (Exception e2) {
                    throw new SQLException("Unable to Instantiate: ", e2);
                }
            }
        }
        return columnObject;
    }

    @Override // java.sql.ResultSet
    public Object getObject(String str) throws SQLException {
        return getObject(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public int findColumn(String str) throws SQLException {
        return getColIdxByName(str);
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (isBinary(this.RowSetMD.getColumnType(i2))) {
            Object columnObject = getCurrentRow().getColumnObject(i2);
            if (columnObject == null) {
                this.lastValueNull = true;
                return null;
            }
            this.charStream = new InputStreamReader(new ByteArrayInputStream((byte[]) columnObject));
        } else if (isString(this.RowSetMD.getColumnType(i2))) {
            Object columnObject2 = getCurrentRow().getColumnObject(i2);
            if (columnObject2 == null) {
                this.lastValueNull = true;
                return null;
            }
            this.charStream = new StringReader(columnObject2.toString());
        } else {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        return this.charStream;
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(String str) throws SQLException {
        return getCharacterStream(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        try {
            return new BigDecimal(columnObject.toString().trim());
        } catch (NumberFormatException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.doublefail").toString(), columnObject.toString().trim(), Integer.valueOf(i2)));
        }
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(String str) throws SQLException {
        return getBigDecimal(getColIdxByName(str));
    }

    @Override // javax.sql.rowset.CachedRowSet
    public int size() {
        return this.numRows;
    }

    @Override // java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        if (this.cursorPos == 0 && this.numRows > 0) {
            return true;
        }
        return false;
    }

    @Override // java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        if (this.cursorPos == this.numRows + 1 && this.numRows > 0) {
            return true;
        }
        return false;
    }

    @Override // java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        int i2 = this.cursorPos;
        int i3 = this.absolutePos;
        internalFirst();
        if (this.cursorPos == i2) {
            return true;
        }
        this.cursorPos = i2;
        this.absolutePos = i3;
        return false;
    }

    @Override // java.sql.ResultSet
    public boolean isLast() throws SQLException {
        int i2 = this.cursorPos;
        int i3 = this.absolutePos;
        boolean showDeleted = getShowDeleted();
        setShowDeleted(true);
        internalLast();
        if (this.cursorPos == i2) {
            setShowDeleted(showDeleted);
            return true;
        }
        setShowDeleted(showDeleted);
        this.cursorPos = i2;
        this.absolutePos = i3;
        return false;
    }

    @Override // java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        if (getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.beforefirst").toString());
        }
        this.cursorPos = 0;
        this.absolutePos = 0;
        notifyCursorMoved();
    }

    @Override // java.sql.ResultSet
    public void afterLast() throws SQLException {
        if (this.numRows > 0) {
            this.cursorPos = this.numRows + 1;
            this.absolutePos = 0;
            notifyCursorMoved();
        }
    }

    @Override // java.sql.ResultSet
    public boolean first() throws SQLException {
        if (getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.first").toString());
        }
        boolean zInternalFirst = internalFirst();
        notifyCursorMoved();
        return zInternalFirst;
    }

    protected boolean internalFirst() throws SQLException {
        boolean zInternalNext = false;
        if (this.numRows > 0) {
            this.cursorPos = 1;
            if (!getShowDeleted() && rowDeleted()) {
                zInternalNext = internalNext();
            } else {
                zInternalNext = true;
            }
        }
        if (zInternalNext) {
            this.absolutePos = 1;
        } else {
            this.absolutePos = 0;
        }
        return zInternalNext;
    }

    @Override // java.sql.ResultSet
    public boolean last() throws SQLException {
        if (getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.last").toString());
        }
        boolean zInternalLast = internalLast();
        notifyCursorMoved();
        return zInternalLast;
    }

    protected boolean internalLast() throws SQLException {
        boolean zInternalPrevious = false;
        if (this.numRows > 0) {
            this.cursorPos = this.numRows;
            if (!getShowDeleted() && rowDeleted()) {
                zInternalPrevious = internalPrevious();
            } else {
                zInternalPrevious = true;
            }
        }
        if (zInternalPrevious) {
            this.absolutePos = this.numRows - this.numDeleted;
        } else {
            this.absolutePos = 0;
        }
        return zInternalPrevious;
    }

    @Override // java.sql.ResultSet
    public int getRow() throws SQLException {
        if (this.numRows > 0 && this.cursorPos > 0 && this.cursorPos < this.numRows + 1 && !getShowDeleted() && !rowDeleted()) {
            return this.absolutePos;
        }
        if (getShowDeleted()) {
            return this.cursorPos;
        }
        return 0;
    }

    @Override // java.sql.ResultSet
    public boolean absolute(int i2) throws SQLException {
        if (i2 == 0 || getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.absolute").toString());
        }
        if (i2 > 0) {
            if (i2 > this.numRows) {
                afterLast();
                return false;
            }
            if (this.absolutePos <= 0) {
                internalFirst();
            }
        } else {
            if (this.cursorPos + i2 < 0) {
                beforeFirst();
                return false;
            }
            if (this.absolutePos >= 0) {
                internalLast();
            }
        }
        while (this.absolutePos != i2) {
            if (this.absolutePos < i2) {
                if (!internalNext()) {
                    break;
                }
            } else if (!internalPrevious()) {
                break;
            }
        }
        notifyCursorMoved();
        if (isAfterLast() || isBeforeFirst()) {
            return false;
        }
        return true;
    }

    @Override // java.sql.ResultSet
    public boolean relative(int i2) throws SQLException {
        if (this.numRows == 0 || isBeforeFirst() || isAfterLast() || getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.relative").toString());
        }
        if (i2 == 0) {
            return true;
        }
        if (i2 > 0) {
            if (this.cursorPos + i2 > this.numRows) {
                afterLast();
            } else {
                for (int i3 = 0; i3 < i2 && internalNext(); i3++) {
                }
            }
        } else if (this.cursorPos + i2 < 0) {
            beforeFirst();
        } else {
            for (int i4 = i2; i4 < 0 && internalPrevious(); i4++) {
            }
        }
        notifyCursorMoved();
        if (isAfterLast() || isBeforeFirst()) {
            return false;
        }
        return true;
    }

    @Override // java.sql.ResultSet
    public boolean previous() throws SQLException {
        if (getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.last").toString());
        }
        if (this.cursorPos < 0 || this.cursorPos > this.numRows + 1) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        boolean zInternalPrevious = internalPrevious();
        notifyCursorMoved();
        return zInternalPrevious;
    }

    protected boolean internalPrevious() throws SQLException {
        boolean z2 = false;
        while (true) {
            if (this.cursorPos > 1) {
                this.cursorPos--;
                z2 = true;
            } else if (this.cursorPos == 1) {
                this.cursorPos--;
                z2 = false;
                break;
            }
            if (getShowDeleted() || !rowDeleted()) {
                break;
            }
        }
        if (z2) {
            this.absolutePos--;
        } else {
            this.absolutePos = 0;
        }
        return z2;
    }

    @Override // java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        return ((Row) getCurrentRow()).getUpdated();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public boolean columnUpdated(int i2) throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        return ((Row) getCurrentRow()).getColUpdated(i2 - 1);
    }

    @Override // javax.sql.rowset.CachedRowSet
    public boolean columnUpdated(String str) throws SQLException {
        return columnUpdated(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        return ((Row) getCurrentRow()).getInserted();
    }

    @Override // java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        if (isAfterLast() || isBeforeFirst() || this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        return ((Row) getCurrentRow()).getDeleted();
    }

    private boolean isNumeric(int i2) {
        switch (i2) {
            case -7:
            case -6:
            case -5:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            case -4:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            default:
                return false;
        }
    }

    private boolean isString(int i2) {
        switch (i2) {
            case -1:
            case 1:
            case 12:
                return true;
            default:
                return false;
        }
    }

    private boolean isBinary(int i2) {
        switch (i2) {
            case -4:
            case -3:
            case -2:
                return true;
            default:
                return false;
        }
    }

    private boolean isTemporal(int i2) {
        switch (i2) {
            case 91:
            case 92:
            case 93:
                return true;
            default:
                return false;
        }
    }

    private boolean isBoolean(int i2) {
        switch (i2) {
            case -7:
            case 16:
                return true;
            default:
                return false;
        }
    }

    private Object convertNumeric(Object obj, int i2, int i3) throws SQLException {
        if (i2 == i3) {
            return obj;
        }
        if (!isNumeric(i3) && !isString(i3)) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + i3);
        }
        try {
            switch (i3) {
                case -7:
                    if (Integer.valueOf(obj.toString().trim()).equals(0)) {
                        return false;
                    }
                    return true;
                case -6:
                    return Byte.valueOf(obj.toString().trim());
                case -5:
                    return Long.valueOf(obj.toString().trim());
                case -4:
                case -3:
                case -2:
                case 0:
                case 9:
                case 10:
                case 11:
                default:
                    throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + i3);
                case -1:
                case 1:
                case 12:
                    return obj.toString();
                case 2:
                case 3:
                    return new BigDecimal(obj.toString().trim());
                case 4:
                    return Integer.valueOf(obj.toString().trim());
                case 5:
                    return Short.valueOf(obj.toString().trim());
                case 6:
                case 7:
                    return new Float(obj.toString().trim());
                case 8:
                    return new Double(obj.toString().trim());
            }
        } catch (NumberFormatException e2) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + i3);
        }
    }

    private Object convertTemporal(Object obj, int i2, int i3) throws SQLException {
        if (i2 == i3) {
            return obj;
        }
        if (isNumeric(i3) || (!isString(i3) && !isTemporal(i3))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        try {
            switch (i3) {
                case -1:
                case 1:
                case 12:
                    return obj.toString();
                case 91:
                    if (i2 == 93) {
                        return new Date(((Timestamp) obj).getTime());
                    }
                    throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
                case 92:
                    if (i2 == 93) {
                        return new Time(((Timestamp) obj).getTime());
                    }
                    throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
                case 93:
                    if (i2 == 92) {
                        return new Timestamp(((Time) obj).getTime());
                    }
                    return new Timestamp(((Date) obj).getTime());
                default:
                    throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
            }
        } catch (NumberFormatException e2) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
    }

    private Object convertBoolean(Object obj, int i2, int i3) throws SQLException {
        if (i2 == i3) {
            return obj;
        }
        if (isNumeric(i3) || (!isString(i3) && !isBoolean(i3))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        try {
            switch (i3) {
                case -7:
                    if (Integer.valueOf(obj.toString().trim()).equals(0)) {
                        return false;
                    }
                    return true;
                case 16:
                    return Boolean.valueOf(obj.toString().trim());
                default:
                    throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + i3);
            }
        } catch (NumberFormatException e2) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString() + i3);
        }
    }

    @Override // java.sql.ResultSet
    public void updateNull(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, null);
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(int i2, boolean z2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertBoolean(Boolean.valueOf(z2), -7, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateByte(int i2, byte b2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Byte.valueOf(b2), -6, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateShort(int i2, short s2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Short.valueOf(s2), 5, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateInt(int i2, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Integer.valueOf(i3), 4, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateLong(int i2, long j2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Long.valueOf(j2), -5, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateFloat(int i2, float f2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Float.valueOf(f2), 7, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateDouble(int i2, double d2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(Double.valueOf(d2), 8, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertNumeric(bigDecimal, 2, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateString(int i2, String str) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, str);
    }

    @Override // java.sql.ResultSet
    public void updateBytes(int i2, byte[] bArr) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (!isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        getCurrentRow().setColumnObject(i2, bArr);
    }

    @Override // java.sql.ResultSet
    public void updateDate(int i2, Date date) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertTemporal(date, 91, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateTime(int i2, Time time) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertTemporal(time, 92, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(int i2, Timestamp timestamp) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, convertTemporal(timestamp, 93, this.RowSetMD.getColumnType(i2)));
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (!isString(this.RowSetMD.getColumnType(i2)) && !isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        byte[] bArr = new byte[i3];
        int i4 = 0;
        do {
            try {
                i4 += inputStream.read(bArr, i4, i3 - i4);
            } catch (IOException e2) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.asciistream").toString());
            }
        } while (i4 != i3);
        getCurrentRow().setColumnObject(i2, new String(bArr));
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (!isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        byte[] bArr = new byte[i3];
        int i4 = 0;
        do {
            try {
                i4 += inputStream.read(bArr, i4, i3 - i4);
            } catch (IOException e2) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.binstream").toString());
            }
        } while (i4 != -1);
        getCurrentRow().setColumnObject(i2, bArr);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (!isString(this.RowSetMD.getColumnType(i2)) && !isBinary(this.RowSetMD.getColumnType(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        char[] cArr = new char[i3];
        int i4 = 0;
        do {
            try {
                i4 += reader.read(cArr, i4, i3 - i4);
            } catch (IOException e2) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.binstream").toString());
            }
        } while (i4 != i3);
        getCurrentRow().setColumnObject(i2, new String(cArr));
    }

    @Override // java.sql.ResultSet
    public void updateObject(int i2, Object obj, int i3) throws SQLException {
        checkIndex(i2);
        checkCursor();
        int columnType = this.RowSetMD.getColumnType(i2);
        if (columnType == 3 || columnType == 2) {
            ((BigDecimal) obj).setScale(i3);
        }
        getCurrentRow().setColumnObject(i2, obj);
    }

    @Override // java.sql.ResultSet
    public void updateObject(int i2, Object obj) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, obj);
    }

    @Override // java.sql.ResultSet
    public void updateNull(String str) throws SQLException {
        updateNull(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(String str, boolean z2) throws SQLException {
        updateBoolean(getColIdxByName(str), z2);
    }

    @Override // java.sql.ResultSet
    public void updateByte(String str, byte b2) throws SQLException {
        updateByte(getColIdxByName(str), b2);
    }

    @Override // java.sql.ResultSet
    public void updateShort(String str, short s2) throws SQLException {
        updateShort(getColIdxByName(str), s2);
    }

    @Override // java.sql.ResultSet
    public void updateInt(String str, int i2) throws SQLException {
        updateInt(getColIdxByName(str), i2);
    }

    @Override // java.sql.ResultSet
    public void updateLong(String str, long j2) throws SQLException {
        updateLong(getColIdxByName(str), j2);
    }

    @Override // java.sql.ResultSet
    public void updateFloat(String str, float f2) throws SQLException {
        updateFloat(getColIdxByName(str), f2);
    }

    @Override // java.sql.ResultSet
    public void updateDouble(String str, double d2) throws SQLException {
        updateDouble(getColIdxByName(str), d2);
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        updateBigDecimal(getColIdxByName(str), bigDecimal);
    }

    @Override // java.sql.ResultSet
    public void updateString(String str, String str2) throws SQLException {
        updateString(getColIdxByName(str), str2);
    }

    @Override // java.sql.ResultSet
    public void updateBytes(String str, byte[] bArr) throws SQLException {
        updateBytes(getColIdxByName(str), bArr);
    }

    @Override // java.sql.ResultSet
    public void updateDate(String str, Date date) throws SQLException {
        updateDate(getColIdxByName(str), date);
    }

    @Override // java.sql.ResultSet
    public void updateTime(String str, Time time) throws SQLException {
        updateTime(getColIdxByName(str), time);
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
        updateTimestamp(getColIdxByName(str), timestamp);
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateAsciiStream(getColIdxByName(str), inputStream, i2);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateBinaryStream(getColIdxByName(str), inputStream, i2);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, int i2) throws SQLException {
        updateCharacterStream(getColIdxByName(str), reader, i2);
    }

    @Override // java.sql.ResultSet
    public void updateObject(String str, Object obj, int i2) throws SQLException {
        updateObject(getColIdxByName(str), obj, i2);
    }

    @Override // java.sql.ResultSet
    public void updateObject(String str, Object obj) throws SQLException {
        updateObject(getColIdxByName(str), obj);
    }

    @Override // java.sql.ResultSet
    public void insertRow() throws SQLException {
        int i2;
        if (!this.onInsertRow || !this.insertRow.isCompleteRow(this.RowSetMD)) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.failedins").toString());
        }
        Object[] params = getParams();
        for (int i3 = 0; i3 < params.length; i3++) {
            this.insertRow.setColumnObject(i3 + 1, params[i3]);
        }
        Row row = new Row(this.RowSetMD.getColumnCount(), this.insertRow.getOrigRow());
        row.setInserted();
        if (this.currentRow >= this.numRows || this.currentRow < 0) {
            i2 = this.numRows;
        } else {
            i2 = this.currentRow;
        }
        this.rvh.add(i2, row);
        this.numRows++;
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void updateRow() throws SQLException {
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.updateins").toString());
        }
        ((Row) getCurrentRow()).setUpdated();
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void deleteRow() throws SQLException {
        checkCursor();
        ((Row) getCurrentRow()).setDeleted();
        this.numDeleted++;
        notifyRowChanged();
    }

    @Override // java.sql.ResultSet
    public void refreshRow() throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        ((Row) getCurrentRow()).clearUpdated();
    }

    @Override // java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcp").toString());
        }
        Row row = (Row) getCurrentRow();
        if (row.getUpdated()) {
            row.clearUpdated();
            notifyRowChanged();
        }
    }

    @Override // java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        if (getConcurrency() == 1007) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins").toString());
        }
        if (this.insertRow == null) {
            if (this.RowSetMD == null) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins1").toString());
            }
            int columnCount = this.RowSetMD.getColumnCount();
            if (columnCount > 0) {
                this.insertRow = new InsertRow(columnCount);
            } else {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.movetoins2").toString());
            }
        }
        this.onInsertRow = true;
        this.currentRow = this.cursorPos;
        this.cursorPos = -1;
        this.insertRow.initInsertRow();
    }

    @Override // java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        if (!this.onInsertRow) {
            return;
        }
        this.cursorPos = this.currentRow;
        this.onInsertRow = false;
    }

    @Override // java.sql.ResultSet
    public Statement getStatement() throws SQLException {
        return null;
    }

    @Override // java.sql.ResultSet
    public Object getObject(int i2, Map<String, Class<?>> map) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        if (columnObject instanceof Struct) {
            Struct struct = (Struct) columnObject;
            Class<?> cls = map.get(struct.getSQLTypeName());
            if (cls != null) {
                try {
                    SQLData sQLData = (SQLData) ReflectUtil.newInstance(cls);
                    sQLData.readSQL(new SQLInputImpl(struct.getAttributes(map), map), struct.getSQLTypeName());
                    return sQLData;
                } catch (Exception e2) {
                    throw new SQLException("Unable to Instantiate: ", e2);
                }
            }
        }
        return columnObject;
    }

    @Override // java.sql.ResultSet
    public Ref getRef(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.RowSetMD.getColumnType(i2) != 2006) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        setLastValueNull(false);
        Ref ref = (Ref) getCurrentRow().getColumnObject(i2);
        if (ref == null) {
            setLastValueNull(true);
            return null;
        }
        return ref;
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.RowSetMD.getColumnType(i2) != 2004) {
            System.out.println(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.type").toString(), Integer.valueOf(this.RowSetMD.getColumnType(i2))));
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        setLastValueNull(false);
        Blob blob = (Blob) getCurrentRow().getColumnObject(i2);
        if (blob == null) {
            setLastValueNull(true);
            return null;
        }
        return blob;
    }

    @Override // java.sql.ResultSet
    public Clob getClob(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.RowSetMD.getColumnType(i2) != 2005) {
            System.out.println(MessageFormat.format(this.resBundle.handleGetObject("cachedrowsetimpl.type").toString(), Integer.valueOf(this.RowSetMD.getColumnType(i2))));
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        setLastValueNull(false);
        Clob clob = (Clob) getCurrentRow().getColumnObject(i2);
        if (clob == null) {
            setLastValueNull(true);
            return null;
        }
        return clob;
    }

    @Override // java.sql.ResultSet
    public Array getArray(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.RowSetMD.getColumnType(i2) != 2003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        setLastValueNull(false);
        Array array = (Array) getCurrentRow().getColumnObject(i2);
        if (array == null) {
            setLastValueNull(true);
            return null;
        }
        return array;
    }

    @Override // java.sql.ResultSet
    public Object getObject(String str, Map<String, Class<?>> map) throws SQLException {
        return getObject(getColIdxByName(str), map);
    }

    @Override // java.sql.ResultSet
    public Ref getRef(String str) throws SQLException {
        return getRef(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Blob getBlob(String str) throws SQLException {
        return getBlob(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Clob getClob(String str) throws SQLException {
        return getClob(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Array getArray(String str) throws SQLException {
        return getArray(getColIdxByName(str));
    }

    @Override // java.sql.ResultSet
    public Date getDate(int i2, Calendar calendar) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        Object objConvertTemporal = convertTemporal(columnObject, this.RowSetMD.getColumnType(i2), 91);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime((java.util.Date) objConvertTemporal);
        calendar.set(1, calendar2.get(1));
        calendar.set(2, calendar2.get(2));
        calendar.set(5, calendar2.get(5));
        return new Date(calendar.getTime().getTime());
    }

    @Override // java.sql.ResultSet
    public Date getDate(String str, Calendar calendar) throws SQLException {
        return getDate(getColIdxByName(str), calendar);
    }

    @Override // java.sql.ResultSet
    public Time getTime(int i2, Calendar calendar) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        Object objConvertTemporal = convertTemporal(columnObject, this.RowSetMD.getColumnType(i2), 92);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime((java.util.Date) objConvertTemporal);
        calendar.set(11, calendar2.get(11));
        calendar.set(12, calendar2.get(12));
        calendar.set(13, calendar2.get(13));
        return new Time(calendar.getTime().getTime());
    }

    @Override // java.sql.ResultSet
    public Time getTime(String str, Calendar calendar) throws SQLException {
        return getTime(getColIdxByName(str), calendar);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException {
        checkIndex(i2);
        checkCursor();
        setLastValueNull(false);
        Object columnObject = getCurrentRow().getColumnObject(i2);
        if (columnObject == null) {
            setLastValueNull(true);
            return null;
        }
        Object objConvertTemporal = convertTemporal(columnObject, this.RowSetMD.getColumnType(i2), 93);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime((java.util.Date) objConvertTemporal);
        calendar.set(1, calendar2.get(1));
        calendar.set(2, calendar2.get(2));
        calendar.set(5, calendar2.get(5));
        calendar.set(11, calendar2.get(11));
        calendar.set(12, calendar2.get(12));
        calendar.set(13, calendar2.get(13));
        return new Timestamp(calendar.getTime().getTime());
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String str, Calendar calendar) throws SQLException {
        return getTimestamp(getColIdxByName(str), calendar);
    }

    @Override // javax.sql.RowSetInternal
    public Connection getConnection() throws SQLException {
        return this.conn;
    }

    @Override // javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public void setMetaData(RowSetMetaData rowSetMetaData) throws SQLException {
        this.RowSetMD = (RowSetMetaDataImpl) rowSetMetaData;
    }

    @Override // javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginal() throws SQLException {
        CachedRowSetImpl cachedRowSetImpl = new CachedRowSetImpl();
        cachedRowSetImpl.RowSetMD = this.RowSetMD;
        cachedRowSetImpl.numRows = this.numRows;
        cachedRowSetImpl.cursorPos = 0;
        int columnCount = this.RowSetMD.getColumnCount();
        Iterator<Object> it = this.rvh.iterator();
        while (it.hasNext()) {
            cachedRowSetImpl.rvh.add(new Row(columnCount, ((Row) it.next()).getOrigRow()));
        }
        return cachedRowSetImpl;
    }

    @Override // javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginalRow() throws SQLException {
        CachedRowSetImpl cachedRowSetImpl = new CachedRowSetImpl();
        cachedRowSetImpl.RowSetMD = this.RowSetMD;
        cachedRowSetImpl.numRows = 1;
        cachedRowSetImpl.cursorPos = 0;
        cachedRowSetImpl.setTypeMap(getTypeMap());
        cachedRowSetImpl.rvh.add(new Row(this.RowSetMD.getColumnCount(), getCurrentRow().getOrigRow()));
        return cachedRowSetImpl;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void setOriginalRow() throws SQLException {
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        Row row = (Row) getCurrentRow();
        makeRowOriginal(row);
        if (row.getDeleted()) {
            removeCurrentRow();
        }
    }

    private void makeRowOriginal(Row row) {
        if (row.getInserted()) {
            row.clearInserted();
        }
        if (row.getUpdated()) {
            row.moveCurrentToOrig();
        }
    }

    public void setOriginal() throws SQLException {
        Iterator<Object> it = this.rvh.iterator();
        while (it.hasNext()) {
            Row row = (Row) it.next();
            makeRowOriginal(row);
            if (row.getDeleted()) {
                it.remove();
                this.numRows--;
            }
        }
        this.numDeleted = 0;
        notifyRowSetChanged();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public String getTableName() throws SQLException {
        return this.tableName;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void setTableName(String str) throws SQLException {
        if (str == null) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.tablename").toString());
        }
        this.tableName = str;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public int[] getKeyColumns() throws SQLException {
        int[] iArr = this.keyCols;
        if (iArr == null) {
            return null;
        }
        return Arrays.copyOf(iArr, iArr.length);
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void setKeyColumns(int[] iArr) throws SQLException {
        int columnCount = 0;
        if (this.RowSetMD != null) {
            columnCount = this.RowSetMD.getColumnCount();
            if (iArr.length > columnCount) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.keycols").toString());
            }
        }
        this.keyCols = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (this.RowSetMD != null && (iArr[i2] <= 0 || iArr[i2] > columnCount)) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidcol").toString() + iArr[i2]);
            }
            this.keyCols[i2] = iArr[i2];
        }
    }

    @Override // java.sql.ResultSet
    public void updateRef(int i2, Ref ref) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, new SerialRef(ref));
    }

    @Override // java.sql.ResultSet
    public void updateRef(String str, Ref ref) throws SQLException {
        updateRef(getColIdxByName(str), ref);
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Clob clob) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.dbmslocatorsUpdateCopy) {
            getCurrentRow().setColumnObject(i2, new SerialClob(clob));
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Clob clob) throws SQLException {
        updateClob(getColIdxByName(str), clob);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, Blob blob) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.dbmslocatorsUpdateCopy) {
            getCurrentRow().setColumnObject(i2, new SerialBlob(blob));
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, Blob blob) throws SQLException {
        updateBlob(getColIdxByName(str), blob);
    }

    @Override // java.sql.ResultSet
    public void updateArray(int i2, Array array) throws SQLException {
        checkIndex(i2);
        checkCursor();
        getCurrentRow().setColumnObject(i2, new SerialArray(array));
    }

    @Override // java.sql.ResultSet
    public void updateArray(String str, Array array) throws SQLException {
        updateArray(getColIdxByName(str), array);
    }

    @Override // java.sql.ResultSet
    public URL getURL(int i2) throws SQLException {
        checkIndex(i2);
        checkCursor();
        if (this.RowSetMD.getColumnType(i2) != 70) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.dtypemismt").toString());
        }
        setLastValueNull(false);
        URL url = (URL) getCurrentRow().getColumnObject(i2);
        if (url == null) {
            setLastValueNull(true);
            return null;
        }
        return url;
    }

    @Override // java.sql.ResultSet
    public URL getURL(String str) throws SQLException {
        return getURL(getColIdxByName(str));
    }

    @Override // javax.sql.rowset.CachedRowSet
    public RowSetWarning getRowSetWarnings() {
        try {
            notifyCursorMoved();
        } catch (SQLException e2) {
        }
        return this.rowsetWarning;
    }

    private String buildTableName(String str) throws SQLException {
        String str2 = "";
        String strTrim = str.trim();
        if (strTrim.toLowerCase().startsWith(Constants.ATTRNAME_SELECT)) {
            int iIndexOf = strTrim.toLowerCase().indexOf(Constants.ATTRNAME_FROM);
            if (strTrim.indexOf(",", iIndexOf) == -1) {
                String strTrim2 = strTrim.substring(iIndexOf + Constants.ATTRNAME_FROM.length(), strTrim.length()).trim();
                int iIndexOf2 = strTrim2.toLowerCase().indexOf("where");
                if (iIndexOf2 != -1) {
                    strTrim2 = strTrim2.substring(0, iIndexOf2).trim();
                }
                str2 = strTrim2;
            }
        } else if (!strTrim.toLowerCase().startsWith("insert") && strTrim.toLowerCase().startsWith("update")) {
        }
        return str2;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void commit() throws SQLException {
        this.conn.commit();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void rollback() throws SQLException {
        this.conn.rollback();
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void rollback(Savepoint savepoint) throws SQLException {
        this.conn.rollback(savepoint);
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(int[] iArr) throws SQLException, NumberFormatException {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != Integer.parseInt(this.iMatchColumns.get(i2).toString())) {
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols").toString());
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
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols").toString());
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
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.setmatchcols").toString());
        }
        this.strMatchColumns.copyInto(strArr);
        return strArr;
    }

    @Override // javax.sql.rowset.Joinable
    public int[] getMatchColumnIndexes() throws SQLException {
        Integer[] numArr = new Integer[this.iMatchColumns.size()];
        int[] iArr = new int[this.iMatchColumns.size()];
        if (this.iMatchColumns.get(0).intValue() == -1) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.setmatchcols").toString());
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
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols1").toString());
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
                throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols2").toString());
            }
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            this.strMatchColumns.add(i3, strArr[i3]);
        }
    }

    @Override // javax.sql.rowset.Joinable
    public void setMatchColumn(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols1").toString());
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
        throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.matchcols2").toString());
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(int i2) throws SQLException {
        if (!this.iMatchColumns.get(0).equals(Integer.valueOf(i2))) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch").toString());
        }
        if (this.strMatchColumns.get(0) != null) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch1").toString());
        }
        this.iMatchColumns.set(0, -1);
    }

    @Override // javax.sql.rowset.Joinable
    public void unsetMatchColumn(String str) throws SQLException {
        if (!this.strMatchColumns.get(0).equals(str.trim())) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch").toString());
        }
        if (this.iMatchColumns.get(0).intValue() > 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.unsetmatch2").toString());
        }
        this.strMatchColumns.set(0, null);
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void rowSetPopulated(RowSetEvent rowSetEvent, int i2) throws SQLException {
        if (i2 < 0 || i2 < getFetchSize()) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.numrows").toString());
        }
        if (size() % i2 == 0) {
            new RowSetEvent(this);
            notifyRowSetChanged();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x020f  */
    @Override // javax.sql.rowset.CachedRowSet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void populate(java.sql.ResultSet r6, int r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 736
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.rowset.CachedRowSetImpl.populate(java.sql.ResultSet, int):void");
    }

    @Override // javax.sql.rowset.CachedRowSet
    public boolean nextPage() throws SQLException {
        if (this.populatecallcount == 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nextpage").toString());
        }
        this.onFirstPage = false;
        if (this.callWithCon) {
            this.crsReader.setStartPosition(this.endPos);
            this.crsReader.readData(this);
            this.resultSet = null;
        } else {
            populate(this.resultSet, this.endPos);
        }
        return this.pagenotend;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public void setPageSize(int i2) throws SQLException {
        if (i2 < 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.pagesize").toString());
        }
        if (i2 > getMaxRows() && getMaxRows() != 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.pagesize1").toString());
        }
        this.pageSize = i2;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public int getPageSize() {
        return this.pageSize;
    }

    @Override // javax.sql.rowset.CachedRowSet
    public boolean previousPage() throws SQLException {
        int pageSize = getPageSize();
        int i2 = this.maxRowsreached;
        if (this.populatecallcount == 0) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.nextpage").toString());
        }
        if (!this.callWithCon && this.resultSet.getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.fwdonly").toString());
        }
        this.pagenotend = true;
        if (this.startPrev < this.startPos) {
            this.onFirstPage = true;
            return false;
        }
        if (this.onFirstPage) {
            return false;
        }
        int i3 = i2 % pageSize;
        if (i3 == 0) {
            this.maxRowsreached -= 2 * pageSize;
            if (this.callWithCon) {
                this.crsReader.setStartPosition(this.startPrev);
                this.crsReader.readData(this);
                this.resultSet = null;
                return true;
            }
            populate(this.resultSet, this.startPrev);
            return true;
        }
        this.maxRowsreached -= pageSize + i3;
        if (this.callWithCon) {
            this.crsReader.setStartPosition(this.startPrev);
            this.crsReader.readData(this);
            this.resultSet = null;
            return true;
        }
        populate(this.resultSet, this.startPrev);
        return true;
    }

    public void setRowInserted(boolean z2) throws SQLException {
        checkCursor();
        if (this.onInsertRow) {
            throw new SQLException(this.resBundle.handleGetObject("cachedrowsetimpl.invalidop").toString());
        }
        if (z2) {
            ((Row) getCurrentRow()).setInserted();
        } else {
            ((Row) getCurrentRow()).clearInserted();
        }
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateRowId(int i2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateRowId(String str, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public boolean isClosed() throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNString(int i2, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
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
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setSQLXML(String str, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setRowId(int i2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setRowId(String str, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(int i2, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(String str, SQLXML sqlxml) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public String getNString(int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public String getNString(String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.opnotysupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, long j2) throws SQLException {
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, long j2) throws SQLException {
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, long j2) throws SQLException {
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, long j2) throws SQLException {
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream) throws SQLException {
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setURL(int i2, URL url) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNClob(int i2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNString(int i2, String str) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTimestamp(String str, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Clob clob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDate(String str, Date date) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDate(String str, Date date, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTime(String str, Time time) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTime(String str, Time time, Calendar calendar) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(int i2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setClob(int i2, Reader reader, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(int i2, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(int i2, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, InputStream inputStream, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, Blob blob) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBlob(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj, int i2, int i3) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setObject(String str, Object obj) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCharacterStream(String str, Reader reader, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setAsciiStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBinaryStream(String str, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setCharacterStream(String str, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setString(String str, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBytes(String str, byte[] bArr) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setTimestamp(String str, Timestamp timestamp) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNull(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setNull(String str, int i2, String str2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setBoolean(String str, boolean z2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setByte(String str, byte b2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setShort(String str, short s2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setInt(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setLong(String str, long j2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setFloat(String str, float f2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void setDouble(String str, double d2) throws SQLException {
        throw new SQLFeatureNotSupportedException(this.resBundle.handleGetObject("cachedrowsetimpl.featnotsupp").toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
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

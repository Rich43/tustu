package com.sun.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.sql.RowSet;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.Joinable;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;

/* loaded from: rt.jar:com/sun/rowset/JoinRowSetImpl.class */
public class JoinRowSetImpl extends WebRowSetImpl implements JoinRowSet {
    private Vector<CachedRowSetImpl> vecRowSetsInJOIN = new Vector<>();
    private CachedRowSetImpl crsInternal = new CachedRowSetImpl();
    private Vector<Integer> vecJoinType = new Vector<>();
    private Vector<String> vecTableNames = new Vector<>();
    private int iMatchKey = -1;
    private String strMatchKey = null;
    boolean[] supportedJOINs = {false, true, false, false, false};
    private WebRowSet wrs;
    static final long serialVersionUID = -5590501621560008453L;

    public JoinRowSetImpl() throws SQLException {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void addRowSet(Joinable joinable) throws SQLException {
        CachedRowSetImpl cachedRowSetImpl;
        boolean z2 = false;
        boolean z3 = false;
        if (!(joinable instanceof RowSet)) {
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notinstance").toString());
        }
        if (joinable instanceof JdbcRowSetImpl) {
            cachedRowSetImpl = new CachedRowSetImpl();
            cachedRowSetImpl.populate((RowSet) joinable);
            if (cachedRowSetImpl.size() == 0) {
                throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.emptyrowset").toString());
            }
            int i2 = 0;
            for (int i3 = 0; i3 < joinable.getMatchColumnIndexes().length && joinable.getMatchColumnIndexes()[i3] != -1; i3++) {
                try {
                    i2++;
                } catch (SQLException e2) {
                }
            }
            int[] iArr = new int[i2];
            for (int i4 = 0; i4 < i2; i4++) {
                iArr[i4] = joinable.getMatchColumnIndexes()[i4];
            }
            cachedRowSetImpl.setMatchColumn(iArr);
        } else {
            cachedRowSetImpl = (CachedRowSetImpl) joinable;
            if (cachedRowSetImpl.size() == 0) {
                throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.emptyrowset").toString());
            }
        }
        try {
            this.iMatchKey = cachedRowSetImpl.getMatchColumnIndexes()[0];
        } catch (SQLException e3) {
            z2 = true;
        }
        try {
            this.strMatchKey = cachedRowSetImpl.getMatchColumnNames()[0];
        } catch (SQLException e4) {
            z3 = true;
        }
        if (z2 && z3) {
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.matchnotset").toString());
        }
        if (z2) {
            ArrayList arrayList = new ArrayList();
            for (int i5 = 0; i5 < cachedRowSetImpl.getMatchColumnNames().length; i5++) {
                String str = cachedRowSetImpl.getMatchColumnNames()[i5];
                this.strMatchKey = str;
                if (str == null) {
                    break;
                }
                this.iMatchKey = cachedRowSetImpl.findColumn(this.strMatchKey);
                arrayList.add(Integer.valueOf(this.iMatchKey));
            }
            int[] iArr2 = new int[arrayList.size()];
            for (int i6 = 0; i6 < arrayList.size(); i6++) {
                iArr2[i6] = ((Integer) arrayList.get(i6)).intValue();
            }
            cachedRowSetImpl.setMatchColumn(iArr2);
        }
        initJOIN(cachedRowSetImpl);
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void addRowSet(RowSet rowSet, int i2) throws SQLException {
        ((CachedRowSetImpl) rowSet).setMatchColumn(i2);
        addRowSet((Joinable) rowSet);
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void addRowSet(RowSet rowSet, String str) throws SQLException {
        ((CachedRowSetImpl) rowSet).setMatchColumn(str);
        addRowSet((Joinable) rowSet);
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void addRowSet(RowSet[] rowSetArr, int[] iArr) throws SQLException {
        if (rowSetArr.length != iArr.length) {
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.numnotequal").toString());
        }
        for (int i2 = 0; i2 < rowSetArr.length; i2++) {
            ((CachedRowSetImpl) rowSetArr[i2]).setMatchColumn(iArr[i2]);
            addRowSet((Joinable) rowSetArr[i2]);
        }
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void addRowSet(RowSet[] rowSetArr, String[] strArr) throws SQLException {
        if (rowSetArr.length != strArr.length) {
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.numnotequal").toString());
        }
        for (int i2 = 0; i2 < rowSetArr.length; i2++) {
            ((CachedRowSetImpl) rowSetArr[i2]).setMatchColumn(strArr[i2]);
            addRowSet((Joinable) rowSetArr[i2]);
        }
    }

    @Override // javax.sql.rowset.JoinRowSet
    public Collection getRowSets() throws SQLException {
        return this.vecRowSetsInJOIN;
    }

    @Override // javax.sql.rowset.JoinRowSet
    public String[] getRowSetNames() throws SQLException {
        Object[] array = this.vecTableNames.toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < array.length; i2++) {
            strArr[i2] = array[i2].toString();
        }
        return strArr;
    }

    @Override // javax.sql.rowset.JoinRowSet
    public CachedRowSet toCachedRowSet() throws SQLException {
        return this.crsInternal;
    }

    @Override // javax.sql.rowset.JoinRowSet
    public boolean supportsCrossJoin() {
        return this.supportedJOINs[0];
    }

    @Override // javax.sql.rowset.JoinRowSet
    public boolean supportsInnerJoin() {
        return this.supportedJOINs[1];
    }

    @Override // javax.sql.rowset.JoinRowSet
    public boolean supportsLeftOuterJoin() {
        return this.supportedJOINs[2];
    }

    @Override // javax.sql.rowset.JoinRowSet
    public boolean supportsRightOuterJoin() {
        return this.supportedJOINs[3];
    }

    @Override // javax.sql.rowset.JoinRowSet
    public boolean supportsFullJoin() {
        return this.supportedJOINs[4];
    }

    @Override // javax.sql.rowset.JoinRowSet
    public void setJoinType(int i2) throws SQLException {
        if (i2 >= 0 && i2 <= 4) {
            if (i2 != 1) {
                throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notsupported").toString());
            }
            this.vecJoinType.add(1);
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.notdefined").toString());
    }

    private boolean checkforMatchColumn(Joinable joinable) throws SQLException {
        if (joinable.getMatchColumnIndexes().length <= 0) {
            return false;
        }
        return true;
    }

    private void initJOIN(CachedRowSet cachedRowSet) throws SQLException {
        try {
            CachedRowSetImpl cachedRowSetImpl = (CachedRowSetImpl) cachedRowSet;
            CachedRowSetImpl cachedRowSetImpl2 = new CachedRowSetImpl();
            RowSetMetaDataImpl rowSetMetaDataImpl = new RowSetMetaDataImpl();
            if (this.vecRowSetsInJOIN.isEmpty()) {
                this.crsInternal = (CachedRowSetImpl) cachedRowSet.createCopy();
                this.crsInternal.setMetaData((RowSetMetaDataImpl) cachedRowSetImpl.getMetaData());
                this.vecRowSetsInJOIN.add(cachedRowSetImpl);
            } else {
                if (this.vecRowSetsInJOIN.size() - this.vecJoinType.size() == 2) {
                    setJoinType(1);
                } else if (this.vecRowSetsInJOIN.size() - this.vecJoinType.size() == 1) {
                }
                this.vecTableNames.add(this.crsInternal.getTableName());
                this.vecTableNames.add(cachedRowSetImpl.getTableName());
                int size = cachedRowSetImpl.size();
                int size2 = this.crsInternal.size();
                int i2 = 0;
                for (int i3 = 0; i3 < this.crsInternal.getMatchColumnIndexes().length && this.crsInternal.getMatchColumnIndexes()[i3] != -1; i3++) {
                    i2++;
                }
                rowSetMetaDataImpl.setColumnCount((this.crsInternal.getMetaData().getColumnCount() + cachedRowSetImpl.getMetaData().getColumnCount()) - i2);
                cachedRowSetImpl2.setMetaData(rowSetMetaDataImpl);
                this.crsInternal.beforeFirst();
                cachedRowSetImpl.beforeFirst();
                for (int i4 = 1; i4 <= size2 && !this.crsInternal.isAfterLast(); i4++) {
                    if (this.crsInternal.next()) {
                        cachedRowSetImpl.beforeFirst();
                        for (int i5 = 1; i5 <= size && !cachedRowSetImpl.isAfterLast(); i5++) {
                            if (cachedRowSetImpl.next()) {
                                boolean z2 = true;
                                int i6 = 0;
                                while (true) {
                                    if (i6 >= i2) {
                                        break;
                                    }
                                    if (this.crsInternal.getObject(this.crsInternal.getMatchColumnIndexes()[i6]).equals(cachedRowSetImpl.getObject(cachedRowSetImpl.getMatchColumnIndexes()[i6]))) {
                                        i6++;
                                    } else {
                                        z2 = false;
                                        break;
                                    }
                                }
                                if (z2) {
                                    int i7 = 0;
                                    cachedRowSetImpl2.moveToInsertRow();
                                    int i8 = 1;
                                    while (i8 <= this.crsInternal.getMetaData().getColumnCount()) {
                                        boolean z3 = false;
                                        int i9 = 0;
                                        while (true) {
                                            if (i9 >= i2) {
                                                break;
                                            }
                                            if (i8 != this.crsInternal.getMatchColumnIndexes()[i9]) {
                                                i9++;
                                            } else {
                                                z3 = true;
                                                break;
                                            }
                                        }
                                        if (!z3) {
                                            i7++;
                                            cachedRowSetImpl2.updateObject(i7, this.crsInternal.getObject(i8));
                                            rowSetMetaDataImpl.setColumnName(i7, this.crsInternal.getMetaData().getColumnName(i8));
                                            rowSetMetaDataImpl.setTableName(i7, this.crsInternal.getTableName());
                                            rowSetMetaDataImpl.setColumnType(i8, this.crsInternal.getMetaData().getColumnType(i8));
                                            rowSetMetaDataImpl.setAutoIncrement(i8, this.crsInternal.getMetaData().isAutoIncrement(i8));
                                            rowSetMetaDataImpl.setCaseSensitive(i8, this.crsInternal.getMetaData().isCaseSensitive(i8));
                                            rowSetMetaDataImpl.setCatalogName(i8, this.crsInternal.getMetaData().getCatalogName(i8));
                                            rowSetMetaDataImpl.setColumnDisplaySize(i8, this.crsInternal.getMetaData().getColumnDisplaySize(i8));
                                            rowSetMetaDataImpl.setColumnLabel(i8, this.crsInternal.getMetaData().getColumnLabel(i8));
                                            rowSetMetaDataImpl.setColumnType(i8, this.crsInternal.getMetaData().getColumnType(i8));
                                            rowSetMetaDataImpl.setColumnTypeName(i8, this.crsInternal.getMetaData().getColumnTypeName(i8));
                                            rowSetMetaDataImpl.setCurrency(i8, this.crsInternal.getMetaData().isCurrency(i8));
                                            rowSetMetaDataImpl.setNullable(i8, this.crsInternal.getMetaData().isNullable(i8));
                                            rowSetMetaDataImpl.setPrecision(i8, this.crsInternal.getMetaData().getPrecision(i8));
                                            rowSetMetaDataImpl.setScale(i8, this.crsInternal.getMetaData().getScale(i8));
                                            rowSetMetaDataImpl.setSchemaName(i8, this.crsInternal.getMetaData().getSchemaName(i8));
                                            rowSetMetaDataImpl.setSearchable(i8, this.crsInternal.getMetaData().isSearchable(i8));
                                            rowSetMetaDataImpl.setSigned(i8, this.crsInternal.getMetaData().isSigned(i8));
                                        } else {
                                            i7++;
                                            cachedRowSetImpl2.updateObject(i7, this.crsInternal.getObject(i8));
                                            rowSetMetaDataImpl.setColumnName(i7, this.crsInternal.getMetaData().getColumnName(i8));
                                            rowSetMetaDataImpl.setTableName(i7, this.crsInternal.getTableName() + FXMLLoader.CONTROLLER_METHOD_PREFIX + cachedRowSetImpl.getTableName());
                                            rowSetMetaDataImpl.setColumnType(i8, this.crsInternal.getMetaData().getColumnType(i8));
                                            rowSetMetaDataImpl.setAutoIncrement(i8, this.crsInternal.getMetaData().isAutoIncrement(i8));
                                            rowSetMetaDataImpl.setCaseSensitive(i8, this.crsInternal.getMetaData().isCaseSensitive(i8));
                                            rowSetMetaDataImpl.setCatalogName(i8, this.crsInternal.getMetaData().getCatalogName(i8));
                                            rowSetMetaDataImpl.setColumnDisplaySize(i8, this.crsInternal.getMetaData().getColumnDisplaySize(i8));
                                            rowSetMetaDataImpl.setColumnLabel(i8, this.crsInternal.getMetaData().getColumnLabel(i8));
                                            rowSetMetaDataImpl.setColumnType(i8, this.crsInternal.getMetaData().getColumnType(i8));
                                            rowSetMetaDataImpl.setColumnTypeName(i8, this.crsInternal.getMetaData().getColumnTypeName(i8));
                                            rowSetMetaDataImpl.setCurrency(i8, this.crsInternal.getMetaData().isCurrency(i8));
                                            rowSetMetaDataImpl.setNullable(i8, this.crsInternal.getMetaData().isNullable(i8));
                                            rowSetMetaDataImpl.setPrecision(i8, this.crsInternal.getMetaData().getPrecision(i8));
                                            rowSetMetaDataImpl.setScale(i8, this.crsInternal.getMetaData().getScale(i8));
                                            rowSetMetaDataImpl.setSchemaName(i8, this.crsInternal.getMetaData().getSchemaName(i8));
                                            rowSetMetaDataImpl.setSearchable(i8, this.crsInternal.getMetaData().isSearchable(i8));
                                            rowSetMetaDataImpl.setSigned(i8, this.crsInternal.getMetaData().isSigned(i8));
                                        }
                                        i8++;
                                    }
                                    for (int i10 = 1; i10 <= cachedRowSetImpl.getMetaData().getColumnCount(); i10++) {
                                        boolean z4 = false;
                                        int i11 = 0;
                                        while (true) {
                                            if (i11 >= i2) {
                                                break;
                                            }
                                            if (i10 != cachedRowSetImpl.getMatchColumnIndexes()[i11]) {
                                                i11++;
                                            } else {
                                                z4 = true;
                                                break;
                                            }
                                        }
                                        if (z4) {
                                            i8--;
                                        } else {
                                            i7++;
                                            cachedRowSetImpl2.updateObject(i7, cachedRowSetImpl.getObject(i10));
                                            rowSetMetaDataImpl.setColumnName(i7, cachedRowSetImpl.getMetaData().getColumnName(i10));
                                            rowSetMetaDataImpl.setTableName(i7, cachedRowSetImpl.getTableName());
                                            rowSetMetaDataImpl.setColumnType((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getColumnType(i10));
                                            rowSetMetaDataImpl.setAutoIncrement((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isAutoIncrement(i10));
                                            rowSetMetaDataImpl.setCaseSensitive((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isCaseSensitive(i10));
                                            rowSetMetaDataImpl.setCatalogName((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getCatalogName(i10));
                                            rowSetMetaDataImpl.setColumnDisplaySize((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getColumnDisplaySize(i10));
                                            rowSetMetaDataImpl.setColumnLabel((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getColumnLabel(i10));
                                            rowSetMetaDataImpl.setColumnType((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getColumnType(i10));
                                            rowSetMetaDataImpl.setColumnTypeName((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getColumnTypeName(i10));
                                            rowSetMetaDataImpl.setCurrency((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isCurrency(i10));
                                            rowSetMetaDataImpl.setNullable((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isNullable(i10));
                                            rowSetMetaDataImpl.setPrecision((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getPrecision(i10));
                                            rowSetMetaDataImpl.setScale((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getScale(i10));
                                            rowSetMetaDataImpl.setSchemaName((i8 + i10) - 1, cachedRowSetImpl.getMetaData().getSchemaName(i10));
                                            rowSetMetaDataImpl.setSearchable((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isSearchable(i10));
                                            rowSetMetaDataImpl.setSigned((i8 + i10) - 1, cachedRowSetImpl.getMetaData().isSigned(i10));
                                        }
                                    }
                                    cachedRowSetImpl2.insertRow();
                                    cachedRowSetImpl2.moveToCurrentRow();
                                }
                            }
                        }
                    }
                }
                cachedRowSetImpl2.setMetaData(rowSetMetaDataImpl);
                cachedRowSetImpl2.setOriginal();
                int[] iArr = new int[i2];
                for (int i12 = 0; i12 < i2; i12++) {
                    iArr[i12] = this.crsInternal.getMatchColumnIndexes()[i12];
                }
                this.crsInternal = (CachedRowSetImpl) cachedRowSetImpl2.createCopy();
                this.crsInternal.setMatchColumn(iArr);
                this.crsInternal.setMetaData(rowSetMetaDataImpl);
                this.vecRowSetsInJOIN.add(cachedRowSetImpl);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.initerror").toString() + ((Object) e2));
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new SQLException(this.resBundle.handleGetObject("joinrowsetimpl.genericerr").toString() + ((Object) e3));
        }
    }

    @Override // javax.sql.rowset.JoinRowSet
    public String getWhereClause() throws SQLException {
        String strConcat;
        String strConcat2 = "Select ";
        String strConcat3 = "";
        String strConcat4 = "";
        int size = this.vecRowSetsInJOIN.size();
        for (int i2 = 0; i2 < size; i2++) {
            CachedRowSetImpl cachedRowSetImpl = this.vecRowSetsInJOIN.get(i2);
            int columnCount = cachedRowSetImpl.getMetaData().getColumnCount();
            strConcat3 = strConcat3.concat(cachedRowSetImpl.getTableName());
            strConcat4 = strConcat4.concat(strConcat3 + ", ");
            int i3 = 1;
            while (i3 < columnCount) {
                int i4 = i3;
                i3++;
                strConcat2 = strConcat2.concat(strConcat3 + "." + cachedRowSetImpl.getMetaData().getColumnName(i4)).concat(", ");
            }
        }
        String strConcat5 = strConcat2.substring(0, strConcat2.lastIndexOf(",")).concat(" from ").concat(strConcat4);
        String strConcat6 = strConcat5.substring(0, strConcat5.lastIndexOf(",")).concat(" where ");
        for (int i5 = 0; i5 < size; i5++) {
            String strConcat7 = strConcat6.concat(this.vecRowSetsInJOIN.get(i5).getMatchColumnNames()[0]);
            if (i5 % 2 != 0) {
                strConcat = strConcat7.concat("=");
            } else {
                strConcat = strConcat7.concat(" and");
            }
            strConcat6 = strConcat.concat(" ");
        }
        return strConcat6;
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean next() throws SQLException {
        return this.crsInternal.next();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        this.crsInternal.close();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean wasNull() throws SQLException {
        return this.crsInternal.wasNull();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getString(int i2) throws SQLException {
        return this.crsInternal.getString(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean getBoolean(int i2) throws SQLException {
        return this.crsInternal.getBoolean(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte getByte(int i2) throws SQLException {
        return this.crsInternal.getByte(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public short getShort(int i2) throws SQLException {
        return this.crsInternal.getShort(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getInt(int i2) throws SQLException {
        return this.crsInternal.getInt(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public long getLong(int i2) throws SQLException {
        return this.crsInternal.getLong(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public float getFloat(int i2) throws SQLException {
        return this.crsInternal.getFloat(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public double getDouble(int i2) throws SQLException {
        return this.crsInternal.getDouble(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int i2, int i3) throws SQLException {
        return this.crsInternal.getBigDecimal(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte[] getBytes(int i2) throws SQLException {
        return this.crsInternal.getBytes(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(int i2) throws SQLException {
        return this.crsInternal.getDate(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(int i2) throws SQLException {
        return this.crsInternal.getTime(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(int i2) throws SQLException {
        return this.crsInternal.getTimestamp(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getAsciiStream(int i2) throws SQLException {
        return this.crsInternal.getAsciiStream(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(int i2) throws SQLException {
        return this.crsInternal.getUnicodeStream(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getBinaryStream(int i2) throws SQLException {
        return this.crsInternal.getBinaryStream(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getString(String str) throws SQLException {
        return this.crsInternal.getString(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean getBoolean(String str) throws SQLException {
        return this.crsInternal.getBoolean(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte getByte(String str) throws SQLException {
        return this.crsInternal.getByte(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public short getShort(String str) throws SQLException {
        return this.crsInternal.getShort(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getInt(String str) throws SQLException {
        return this.crsInternal.getInt(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public long getLong(String str) throws SQLException {
        return this.crsInternal.getLong(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public float getFloat(String str) throws SQLException {
        return this.crsInternal.getFloat(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public double getDouble(String str) throws SQLException {
        return this.crsInternal.getDouble(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String str, int i2) throws SQLException {
        return this.crsInternal.getBigDecimal(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public byte[] getBytes(String str) throws SQLException {
        return this.crsInternal.getBytes(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(String str) throws SQLException {
        return this.crsInternal.getDate(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(String str) throws SQLException {
        return this.crsInternal.getTime(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(String str) throws SQLException {
        return this.crsInternal.getTimestamp(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getAsciiStream(String str) throws SQLException {
        return this.crsInternal.getAsciiStream(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(String str) throws SQLException {
        return this.crsInternal.getUnicodeStream(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public InputStream getBinaryStream(String str) throws SQLException {
        return this.crsInternal.getBinaryStream(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public SQLWarning getWarnings() {
        return this.crsInternal.getWarnings();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void clearWarnings() {
        this.crsInternal.clearWarnings();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public String getCursorName() throws SQLException {
        return this.crsInternal.getCursorName();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.crsInternal.getMetaData();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(int i2) throws SQLException {
        return this.crsInternal.getObject(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(int i2, Map<String, Class<?>> map) throws SQLException {
        return this.crsInternal.getObject(i2, map);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(String str) throws SQLException {
        return this.crsInternal.getObject(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Object getObject(String str, Map<String, Class<?>> map) throws SQLException {
        return this.crsInternal.getObject(str, map);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Reader getCharacterStream(int i2) throws SQLException {
        return this.crsInternal.getCharacterStream(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Reader getCharacterStream(String str) throws SQLException {
        return this.crsInternal.getCharacterStream(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public BigDecimal getBigDecimal(int i2) throws SQLException {
        return this.crsInternal.getBigDecimal(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public BigDecimal getBigDecimal(String str) throws SQLException {
        return this.crsInternal.getBigDecimal(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public int size() {
        return this.crsInternal.size();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        return this.crsInternal.isBeforeFirst();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        return this.crsInternal.isAfterLast();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        return this.crsInternal.isFirst();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean isLast() throws SQLException {
        return this.crsInternal.isLast();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        this.crsInternal.beforeFirst();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void afterLast() throws SQLException {
        this.crsInternal.afterLast();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean first() throws SQLException {
        return this.crsInternal.first();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean last() throws SQLException {
        return this.crsInternal.last();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int getRow() throws SQLException {
        return this.crsInternal.getRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean absolute(int i2) throws SQLException {
        return this.crsInternal.absolute(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean relative(int i2) throws SQLException {
        return this.crsInternal.relative(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean previous() throws SQLException {
        return this.crsInternal.previous();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public int findColumn(String str) throws SQLException {
        return this.crsInternal.findColumn(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        return this.crsInternal.rowUpdated();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public boolean columnUpdated(int i2) throws SQLException {
        return this.crsInternal.columnUpdated(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        return this.crsInternal.rowInserted();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        return this.crsInternal.rowDeleted();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateNull(int i2) throws SQLException {
        this.crsInternal.updateNull(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(int i2, boolean z2) throws SQLException {
        this.crsInternal.updateBoolean(i2, z2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(int i2, byte b2) throws SQLException {
        this.crsInternal.updateByte(i2, b2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(int i2, short s2) throws SQLException {
        this.crsInternal.updateShort(i2, s2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(int i2, int i3) throws SQLException {
        this.crsInternal.updateInt(i2, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(int i2, long j2) throws SQLException {
        this.crsInternal.updateLong(i2, j2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(int i2, float f2) throws SQLException {
        this.crsInternal.updateFloat(i2, f2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(int i2, double d2) throws SQLException {
        this.crsInternal.updateDouble(i2, d2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        this.crsInternal.updateBigDecimal(i2, bigDecimal);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(int i2, String str) throws SQLException {
        this.crsInternal.updateString(i2, str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(int i2, byte[] bArr) throws SQLException {
        this.crsInternal.updateBytes(i2, bArr);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(int i2, Date date) throws SQLException {
        this.crsInternal.updateDate(i2, date);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(int i2, Time time) throws SQLException {
        this.crsInternal.updateTime(i2, time);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(int i2, Timestamp timestamp) throws SQLException {
        this.crsInternal.updateTimestamp(i2, timestamp);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        this.crsInternal.updateAsciiStream(i2, inputStream, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        this.crsInternal.updateBinaryStream(i2, inputStream, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        this.crsInternal.updateCharacterStream(i2, reader, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj, int i3) throws SQLException {
        this.crsInternal.updateObject(i2, obj, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj) throws SQLException {
        this.crsInternal.updateObject(i2, obj);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateNull(String str) throws SQLException {
        this.crsInternal.updateNull(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(String str, boolean z2) throws SQLException {
        this.crsInternal.updateBoolean(str, z2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(String str, byte b2) throws SQLException {
        this.crsInternal.updateByte(str, b2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(String str, short s2) throws SQLException {
        this.crsInternal.updateShort(str, s2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(String str, int i2) throws SQLException {
        this.crsInternal.updateInt(str, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(String str, long j2) throws SQLException {
        this.crsInternal.updateLong(str, j2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(String str, float f2) throws SQLException {
        this.crsInternal.updateFloat(str, f2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(String str, double d2) throws SQLException {
        this.crsInternal.updateDouble(str, d2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        this.crsInternal.updateBigDecimal(str, bigDecimal);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(String str, String str2) throws SQLException {
        this.crsInternal.updateString(str, str2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(String str, byte[] bArr) throws SQLException {
        this.crsInternal.updateBytes(str, bArr);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(String str, Date date) throws SQLException {
        this.crsInternal.updateDate(str, date);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(String str, Time time) throws SQLException {
        this.crsInternal.updateTime(str, time);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
        this.crsInternal.updateTimestamp(str, timestamp);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        this.crsInternal.updateAsciiStream(str, inputStream, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        this.crsInternal.updateBinaryStream(str, inputStream, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, int i2) throws SQLException {
        this.crsInternal.updateCharacterStream(str, reader, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj, int i2) throws SQLException {
        this.crsInternal.updateObject(str, obj, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj) throws SQLException {
        this.crsInternal.updateObject(str, obj);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void insertRow() throws SQLException {
        this.crsInternal.insertRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRow() throws SQLException {
        this.crsInternal.updateRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void deleteRow() throws SQLException {
        this.crsInternal.deleteRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void refreshRow() throws SQLException {
        this.crsInternal.refreshRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        this.crsInternal.cancelRowUpdates();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        this.crsInternal.moveToInsertRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        this.crsInternal.moveToCurrentRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Statement getStatement() throws SQLException {
        return this.crsInternal.getStatement();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Ref getRef(int i2) throws SQLException {
        return this.crsInternal.getRef(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Blob getBlob(int i2) throws SQLException {
        return this.crsInternal.getBlob(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Clob getClob(int i2) throws SQLException {
        return this.crsInternal.getClob(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Array getArray(int i2) throws SQLException {
        return this.crsInternal.getArray(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Ref getRef(String str) throws SQLException {
        return this.crsInternal.getRef(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Blob getBlob(String str) throws SQLException {
        return this.crsInternal.getBlob(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Clob getClob(String str) throws SQLException {
        return this.crsInternal.getClob(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Array getArray(String str) throws SQLException {
        return this.crsInternal.getArray(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(int i2, Calendar calendar) throws SQLException {
        return this.crsInternal.getDate(i2, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Date getDate(String str, Calendar calendar) throws SQLException {
        return this.crsInternal.getDate(str, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(int i2, Calendar calendar) throws SQLException {
        return this.crsInternal.getTime(i2, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Time getTime(String str, Calendar calendar) throws SQLException {
        return this.crsInternal.getTime(str, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(int i2, Calendar calendar) throws SQLException {
        return this.crsInternal.getTimestamp(i2, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public Timestamp getTimestamp(String str, Calendar calendar) throws SQLException {
        return this.crsInternal.getTimestamp(str, calendar);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public void setMetaData(RowSetMetaData rowSetMetaData) throws SQLException {
        this.crsInternal.setMetaData(rowSetMetaData);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginal() throws SQLException {
        return this.crsInternal.getOriginal();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSetInternal, javax.sql.rowset.CachedRowSet
    public ResultSet getOriginalRow() throws SQLException {
        return this.crsInternal.getOriginalRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setOriginalRow() throws SQLException {
        this.crsInternal.setOriginalRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public int[] getKeyColumns() throws SQLException {
        return this.crsInternal.getKeyColumns();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setKeyColumns(int[] iArr) throws SQLException {
        this.crsInternal.setKeyColumns(iArr);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRef(int i2, Ref ref) throws SQLException {
        this.crsInternal.updateRef(i2, ref);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateRef(String str, Ref ref) throws SQLException {
        this.crsInternal.updateRef(str, ref);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateClob(int i2, Clob clob) throws SQLException {
        this.crsInternal.updateClob(i2, clob);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateClob(String str, Clob clob) throws SQLException {
        this.crsInternal.updateClob(str, clob);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBlob(int i2, Blob blob) throws SQLException {
        this.crsInternal.updateBlob(i2, blob);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBlob(String str, Blob blob) throws SQLException {
        this.crsInternal.updateBlob(str, blob);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateArray(int i2, Array array) throws SQLException {
        this.crsInternal.updateArray(i2, array);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateArray(String str, Array array) throws SQLException {
        this.crsInternal.updateArray(str, array);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.RowSet
    public void execute() throws SQLException {
        this.crsInternal.execute();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void execute(Connection connection) throws SQLException {
        this.crsInternal.execute(connection);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public URL getURL(int i2) throws SQLException {
        return this.crsInternal.getURL(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public URL getURL(String str) throws SQLException {
        return this.crsInternal.getURL(str);
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void writeXml(ResultSet resultSet, Writer writer) throws SQLException {
        this.wrs = new WebRowSetImpl();
        this.wrs.populate(resultSet);
        this.wrs.writeXml(writer);
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void writeXml(Writer writer) throws SQLException {
        createWebRowSet().writeXml(writer);
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void readXml(Reader reader) throws SQLException {
        this.wrs = new WebRowSetImpl();
        this.wrs.readXml(reader);
        this.crsInternal = (CachedRowSetImpl) this.wrs;
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void readXml(InputStream inputStream) throws SQLException, IOException {
        this.wrs = new WebRowSetImpl();
        this.wrs.readXml(inputStream);
        this.crsInternal = (CachedRowSetImpl) this.wrs;
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void writeXml(OutputStream outputStream) throws SQLException, IOException {
        createWebRowSet().writeXml(outputStream);
    }

    @Override // com.sun.rowset.WebRowSetImpl, javax.sql.rowset.WebRowSet
    public void writeXml(ResultSet resultSet, OutputStream outputStream) throws SQLException, IOException {
        this.wrs = new WebRowSetImpl();
        this.wrs.populate(resultSet);
        this.wrs.writeXml(outputStream);
    }

    private WebRowSet createWebRowSet() throws SQLException {
        if (this.wrs != null) {
            return this.wrs;
        }
        this.wrs = new WebRowSetImpl();
        this.crsInternal.beforeFirst();
        this.wrs.populate(this.crsInternal);
        return this.wrs;
    }

    @Override // javax.sql.rowset.JoinRowSet
    public int getJoinType() throws SQLException {
        if (this.vecJoinType == null) {
            setJoinType(1);
        }
        return this.vecJoinType.get(this.vecJoinType.size() - 1).intValue();
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void addRowSetListener(RowSetListener rowSetListener) {
        this.crsInternal.addRowSetListener(rowSetListener);
    }

    @Override // javax.sql.rowset.BaseRowSet, javax.sql.RowSet
    public void removeRowSetListener(RowSetListener rowSetListener) {
        this.crsInternal.removeRowSetListener(rowSetListener);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection() throws SQLException {
        return this.crsInternal.toCollection();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection(int i2) throws SQLException {
        return this.crsInternal.toCollection(i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public Collection<?> toCollection(String str) throws SQLException {
        return this.crsInternal.toCollection(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public CachedRowSet createCopySchema() throws SQLException {
        return this.crsInternal.createCopySchema();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void setSyncProvider(String str) throws SQLException {
        this.crsInternal.setSyncProvider(str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public void acceptChanges() throws SyncProviderException {
        this.crsInternal.acceptChanges();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, javax.sql.rowset.CachedRowSet
    public SyncProvider getSyncProvider() throws SQLException {
        return this.crsInternal.getSyncProvider();
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

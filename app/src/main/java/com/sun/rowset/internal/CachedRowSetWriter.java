package com.sun.rowset.internal;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import javax.sql.RowSetInternal;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.serial.SQLInputImpl;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialStruct;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.TransactionalWriter;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/rowset/internal/CachedRowSetWriter.class */
public class CachedRowSetWriter implements TransactionalWriter, Serializable {
    private transient Connection con;
    private String selectCmd;
    private String updateCmd;
    private String updateWhere;
    private String deleteCmd;
    private String deleteWhere;
    private String insertCmd;
    private int[] keyCols;
    private Object[] params;
    private CachedRowSetReader reader;
    private ResultSetMetaData callerMd;
    private int callerColumnCount;
    private CachedRowSetImpl crsResolve;
    private ArrayList<Integer> status;
    private int iChangedValsInDbAndCRS;
    private int iChangedValsinDbOnly;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = -8506030970299413976L;

    public CachedRowSetWriter() {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.RowSetWriter
    public boolean writeData(RowSetInternal rowSetInternal) throws SQLException {
        long j2 = 0;
        PreparedStatement preparedStatementPrepareStatement = null;
        this.iChangedValsInDbAndCRS = 0;
        this.iChangedValsinDbOnly = 0;
        CachedRowSetImpl cachedRowSetImpl = (CachedRowSetImpl) rowSetInternal;
        this.crsResolve = new CachedRowSetImpl();
        this.con = this.reader.connect(rowSetInternal);
        if (this.con == null) {
            throw new SQLException(this.resBundle.handleGetObject("crswriter.connect").toString());
        }
        initSQLStatements(cachedRowSetImpl);
        RowSetMetaDataImpl rowSetMetaDataImpl = (RowSetMetaDataImpl) cachedRowSetImpl.getMetaData();
        RowSetMetaDataImpl rowSetMetaDataImpl2 = new RowSetMetaDataImpl();
        int columnCount = rowSetMetaDataImpl.getColumnCount();
        this.status = new ArrayList<>(cachedRowSetImpl.size() + 1);
        this.status.add(0, null);
        rowSetMetaDataImpl2.setColumnCount(columnCount);
        for (int i2 = 1; i2 <= columnCount; i2++) {
            rowSetMetaDataImpl2.setColumnType(i2, rowSetMetaDataImpl.getColumnType(i2));
            rowSetMetaDataImpl2.setColumnName(i2, rowSetMetaDataImpl.getColumnName(i2));
            rowSetMetaDataImpl2.setNullable(i2, 2);
        }
        this.crsResolve.setMetaData(rowSetMetaDataImpl2);
        if (this.callerColumnCount < 1) {
            if (this.reader.getCloseConnection()) {
                this.con.close();
                return true;
            }
            return true;
        }
        boolean showDeleted = cachedRowSetImpl.getShowDeleted();
        cachedRowSetImpl.setShowDeleted(true);
        cachedRowSetImpl.beforeFirst();
        int i3 = 1;
        while (cachedRowSetImpl.next()) {
            if (cachedRowSetImpl.rowDeleted()) {
                if (deleteOriginalRow(cachedRowSetImpl, this.crsResolve)) {
                    this.status.add(i3, 1);
                    j2++;
                } else {
                    this.status.add(i3, 3);
                }
            } else if (cachedRowSetImpl.rowInserted()) {
                preparedStatementPrepareStatement = this.con.prepareStatement(this.insertCmd);
                if (insertNewRow(cachedRowSetImpl, preparedStatementPrepareStatement, this.crsResolve)) {
                    this.status.add(i3, 2);
                    j2++;
                } else {
                    this.status.add(i3, 3);
                }
            } else if (cachedRowSetImpl.rowUpdated()) {
                if (updateOriginalRow(cachedRowSetImpl)) {
                    this.status.add(i3, 0);
                    j2++;
                } else {
                    this.status.add(i3, 3);
                }
            } else {
                cachedRowSetImpl.getMetaData().getColumnCount();
                this.status.add(i3, 3);
                this.crsResolve.moveToInsertRow();
                for (int i4 = 0; i4 < columnCount; i4++) {
                    this.crsResolve.updateNull(i4 + 1);
                }
                this.crsResolve.insertRow();
                this.crsResolve.moveToCurrentRow();
            }
            i3++;
        }
        if (preparedStatementPrepareStatement != null) {
            preparedStatementPrepareStatement.close();
        }
        cachedRowSetImpl.setShowDeleted(showDeleted);
        cachedRowSetImpl.beforeFirst();
        this.crsResolve.beforeFirst();
        if (j2 != 0) {
            SyncProviderException syncProviderException = new SyncProviderException(j2 + " " + this.resBundle.handleGetObject("crswriter.conflictsno").toString());
            SyncResolverImpl syncResolverImpl = (SyncResolverImpl) syncProviderException.getSyncResolver();
            syncResolverImpl.setCachedRowSet(cachedRowSetImpl);
            syncResolverImpl.setCachedRowSetResolver(this.crsResolve);
            syncResolverImpl.setStatus(this.status);
            syncResolverImpl.setCachedRowSetWriter(this);
            throw syncProviderException;
        }
        return true;
    }

    private boolean updateOriginalRow(CachedRowSet cachedRowSet) throws SQLException {
        int i2 = 0;
        ResultSet originalRow = cachedRowSet.getOriginalRow();
        originalRow.next();
        try {
            this.updateWhere = buildWhereClause(this.updateWhere, originalRow);
            int iIndexOf = this.selectCmd.toLowerCase().indexOf("where");
            if (iIndexOf != -1) {
                this.selectCmd = this.selectCmd.substring(0, iIndexOf);
            }
            PreparedStatement preparedStatementPrepareStatement = this.con.prepareStatement(this.selectCmd + this.updateWhere, 1005, 1007);
            for (int i3 = 0; i3 < this.keyCols.length; i3++) {
                if (this.params[i3] != null) {
                    i2++;
                    preparedStatementPrepareStatement.setObject(i2, this.params[i3]);
                }
            }
            try {
                preparedStatementPrepareStatement.setMaxRows(cachedRowSet.getMaxRows());
                preparedStatementPrepareStatement.setMaxFieldSize(cachedRowSet.getMaxFieldSize());
                preparedStatementPrepareStatement.setEscapeProcessing(cachedRowSet.getEscapeProcessing());
                preparedStatementPrepareStatement.setQueryTimeout(cachedRowSet.getQueryTimeout());
            } catch (Exception e2) {
            }
            ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
            resultSetExecuteQuery.getMetaData();
            if (!resultSetExecuteQuery.next() || resultSetExecuteQuery.next()) {
                return true;
            }
            resultSetExecuteQuery.first();
            int i4 = 0;
            Vector vector = new Vector();
            String str = this.updateCmd;
            Object obj = null;
            boolean z2 = true;
            boolean z3 = true;
            this.crsResolve.moveToInsertRow();
            for (int i5 = 1; i5 <= this.callerColumnCount; i5++) {
                Object object = originalRow.getObject(i5);
                Object object2 = cachedRowSet.getObject(i5);
                Object object3 = resultSetExecuteQuery.getObject(i5);
                Map<String, Class<?>> typeMap = cachedRowSet.getTypeMap() == null ? this.con.getTypeMap() : cachedRowSet.getTypeMap();
                if (object3 instanceof Struct) {
                    Struct struct = (Struct) object3;
                    Class<?> cls = typeMap.get(struct.getSQLTypeName());
                    if (cls != null) {
                        try {
                            SQLData sQLData = (SQLData) ReflectUtil.newInstance(cls);
                            sQLData.readSQL(new SQLInputImpl(struct.getAttributes(typeMap), typeMap), struct.getSQLTypeName());
                            object3 = sQLData;
                        } catch (Exception e3) {
                            throw new SQLException("Unable to Instantiate: ", e3);
                        }
                    }
                } else if (object3 instanceof SQLData) {
                    object3 = new SerialStruct((SQLData) object3, typeMap);
                } else if (object3 instanceof Blob) {
                    object3 = new SerialBlob((Blob) object3);
                } else if (object3 instanceof Clob) {
                    object3 = new SerialClob((Clob) object3);
                } else if (object3 instanceof Array) {
                    object3 = new SerialArray((Array) object3, typeMap);
                }
                boolean z4 = true;
                if (object3 == null && object != null) {
                    this.iChangedValsinDbOnly++;
                    z4 = false;
                    obj = object3;
                } else if (object3 != null && !object3.equals(object)) {
                    this.iChangedValsinDbOnly++;
                    z4 = false;
                    obj = object3;
                } else if (object == null || object2 == null) {
                    if (!z2 || !z3) {
                        str = str + ", ";
                    }
                    String str2 = str + cachedRowSet.getMetaData().getColumnName(i5);
                    vector.add(Integer.valueOf(i5));
                    str = str2 + " = ? ";
                    z2 = false;
                } else if (object.equals(object2)) {
                    i4++;
                } else if (!object.equals(object2) && cachedRowSet.columnUpdated(i5)) {
                    if (object3.equals(object)) {
                        if (!z3 || !z2) {
                            str = str + ", ";
                        }
                        String str3 = str + cachedRowSet.getMetaData().getColumnName(i5);
                        vector.add(Integer.valueOf(i5));
                        str = str3 + " = ? ";
                        z3 = false;
                    } else {
                        z4 = false;
                        obj = object3;
                        this.iChangedValsInDbAndCRS++;
                    }
                }
                if (!z4) {
                    this.crsResolve.updateObject(i5, obj);
                } else {
                    this.crsResolve.updateNull(i5);
                }
            }
            resultSetExecuteQuery.close();
            preparedStatementPrepareStatement.close();
            this.crsResolve.insertRow();
            this.crsResolve.moveToCurrentRow();
            if ((!z2 && vector.size() == 0) || i4 == this.callerColumnCount) {
                return false;
            }
            if (this.iChangedValsInDbAndCRS != 0 || this.iChangedValsinDbOnly != 0) {
                return true;
            }
            PreparedStatement preparedStatementPrepareStatement2 = this.con.prepareStatement(str + this.updateWhere);
            int i6 = 0;
            while (i6 < vector.size()) {
                Object object4 = cachedRowSet.getObject(((Integer) vector.get(i6)).intValue());
                if (object4 != null) {
                    preparedStatementPrepareStatement2.setObject(i6 + 1, object4);
                } else {
                    preparedStatementPrepareStatement2.setNull(i6 + 1, cachedRowSet.getMetaData().getColumnType(i6 + 1));
                }
                i6++;
            }
            int i7 = i6;
            for (int i8 = 0; i8 < this.keyCols.length; i8++) {
                if (this.params[i8] != null) {
                    i7++;
                    preparedStatementPrepareStatement2.setObject(i7, this.params[i8]);
                }
            }
            preparedStatementPrepareStatement2.executeUpdate();
            return false;
        } catch (SQLException e4) {
            e4.printStackTrace();
            this.crsResolve.moveToInsertRow();
            for (int i9 = 1; i9 <= this.callerColumnCount; i9++) {
                this.crsResolve.updateNull(i9);
            }
            this.crsResolve.insertRow();
            this.crsResolve.moveToCurrentRow();
            return true;
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r12v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0368: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:143:0x0368 */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x036d: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:145:0x036d */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x0333: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:129:0x0333 */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x0338: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:131:0x0338 */
    /* JADX WARN: Type inference failed for: r12v1, types: [java.sql.ResultSet] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.sql.ResultSet] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Throwable] */
    private boolean insertNewRow(CachedRowSet cachedRowSet, PreparedStatement preparedStatement, CachedRowSetImpl cachedRowSetImpl) throws SQLException {
        boolean z2 = false;
        PreparedStatement preparedStatementPrepareStatement = this.con.prepareStatement(this.selectCmd, 1005, 1007);
        Throwable th = null;
        try {
            try {
                ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
                Throwable th2 = null;
                try {
                    ResultSet primaryKeys = this.con.getMetaData().getPrimaryKeys(null, null, cachedRowSet.getTableName());
                    Throwable th3 = null;
                    ResultSetMetaData metaData = cachedRowSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    String[] strArr = new String[columnCount];
                    int i2 = 0;
                    while (primaryKeys.next()) {
                        strArr[i2] = primaryKeys.getString("COLUMN_NAME");
                        i2++;
                    }
                    if (resultSetExecuteQuery.next()) {
                        for (String str : strArr) {
                            if (isPKNameValid(str, metaData)) {
                                Object object = cachedRowSet.getObject(str);
                                if (object == null) {
                                    break;
                                }
                                String string = resultSetExecuteQuery.getObject(str).toString();
                                if (object.toString().equals(string)) {
                                    z2 = true;
                                    this.crsResolve.moveToInsertRow();
                                    for (int i3 = 1; i3 <= columnCount; i3++) {
                                        if (resultSetExecuteQuery.getMetaData().getColumnName(i3).equals(str)) {
                                            this.crsResolve.updateObject(i3, string);
                                        } else {
                                            this.crsResolve.updateNull(i3);
                                        }
                                    }
                                    this.crsResolve.insertRow();
                                    this.crsResolve.moveToCurrentRow();
                                }
                            }
                        }
                    }
                    if (z2) {
                        boolean z3 = z2;
                        if (primaryKeys != null) {
                            if (0 != 0) {
                                try {
                                    primaryKeys.close();
                                } catch (Throwable th4) {
                                    th3.addSuppressed(th4);
                                }
                            } else {
                                primaryKeys.close();
                            }
                        }
                        if (resultSetExecuteQuery != null) {
                            if (0 != 0) {
                                try {
                                    resultSetExecuteQuery.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                resultSetExecuteQuery.close();
                            }
                        }
                        return z3;
                    }
                    for (int i4 = 1; i4 <= columnCount; i4++) {
                        try {
                            Object object2 = cachedRowSet.getObject(i4);
                            if (object2 != null) {
                                preparedStatement.setObject(i4, object2);
                            } else {
                                preparedStatement.setNull(i4, cachedRowSet.getMetaData().getColumnType(i4));
                            }
                        } catch (SQLException e2) {
                            this.crsResolve.moveToInsertRow();
                            for (int i5 = 1; i5 <= columnCount; i5++) {
                                this.crsResolve.updateNull(i5);
                            }
                            this.crsResolve.insertRow();
                            this.crsResolve.moveToCurrentRow();
                            if (primaryKeys != null) {
                                if (0 != 0) {
                                    try {
                                        primaryKeys.close();
                                    } catch (Throwable th6) {
                                        th3.addSuppressed(th6);
                                    }
                                } else {
                                    primaryKeys.close();
                                }
                            }
                            if (resultSetExecuteQuery != null) {
                                if (0 != 0) {
                                    try {
                                        resultSetExecuteQuery.close();
                                    } catch (Throwable th7) {
                                        th2.addSuppressed(th7);
                                    }
                                } else {
                                    resultSetExecuteQuery.close();
                                }
                            }
                            if (preparedStatementPrepareStatement != null) {
                                if (0 != 0) {
                                    try {
                                        preparedStatementPrepareStatement.close();
                                    } catch (Throwable th8) {
                                        th.addSuppressed(th8);
                                    }
                                } else {
                                    preparedStatementPrepareStatement.close();
                                }
                            }
                            return true;
                        }
                    }
                    preparedStatement.executeUpdate();
                    if (primaryKeys != null) {
                        if (0 != 0) {
                            try {
                                primaryKeys.close();
                            } catch (Throwable th9) {
                                th3.addSuppressed(th9);
                            }
                        } else {
                            primaryKeys.close();
                        }
                    }
                    if (resultSetExecuteQuery != null) {
                        if (0 != 0) {
                            try {
                                resultSetExecuteQuery.close();
                            } catch (Throwable th10) {
                                th2.addSuppressed(th10);
                            }
                        } else {
                            resultSetExecuteQuery.close();
                        }
                    }
                    if (preparedStatementPrepareStatement != null) {
                        if (0 != 0) {
                            try {
                                preparedStatementPrepareStatement.close();
                            } catch (Throwable th11) {
                                th.addSuppressed(th11);
                            }
                        } else {
                            preparedStatementPrepareStatement.close();
                        }
                    }
                    return false;
                } finally {
                }
            } finally {
            }
        } finally {
            if (preparedStatementPrepareStatement != null) {
                if (0 != 0) {
                    try {
                        preparedStatementPrepareStatement.close();
                    } catch (Throwable th12) {
                        th.addSuppressed(th12);
                    }
                } else {
                    preparedStatementPrepareStatement.close();
                }
            }
        }
    }

    private boolean deleteOriginalRow(CachedRowSet cachedRowSet, CachedRowSetImpl cachedRowSetImpl) throws SQLException {
        int i2 = 0;
        ResultSet originalRow = cachedRowSet.getOriginalRow();
        originalRow.next();
        this.deleteWhere = buildWhereClause(this.deleteWhere, originalRow);
        PreparedStatement preparedStatementPrepareStatement = this.con.prepareStatement(this.selectCmd + this.deleteWhere, 1005, 1007);
        for (int i3 = 0; i3 < this.keyCols.length; i3++) {
            if (this.params[i3] != null) {
                i2++;
                preparedStatementPrepareStatement.setObject(i2, this.params[i3]);
            }
        }
        try {
            preparedStatementPrepareStatement.setMaxRows(cachedRowSet.getMaxRows());
            preparedStatementPrepareStatement.setMaxFieldSize(cachedRowSet.getMaxFieldSize());
            preparedStatementPrepareStatement.setEscapeProcessing(cachedRowSet.getEscapeProcessing());
            preparedStatementPrepareStatement.setQueryTimeout(cachedRowSet.getQueryTimeout());
        } catch (Exception e2) {
        }
        ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
        if (!resultSetExecuteQuery.next() || resultSetExecuteQuery.next()) {
            return true;
        }
        resultSetExecuteQuery.first();
        boolean z2 = false;
        cachedRowSetImpl.moveToInsertRow();
        for (int i4 = 1; i4 <= cachedRowSet.getMetaData().getColumnCount(); i4++) {
            Object object = originalRow.getObject(i4);
            Object object2 = resultSetExecuteQuery.getObject(i4);
            if (object != null && object2 != null) {
                if (!object.toString().equals(object2.toString())) {
                    z2 = true;
                    cachedRowSetImpl.updateObject(i4, originalRow.getObject(i4));
                }
            } else {
                cachedRowSetImpl.updateNull(i4);
            }
        }
        cachedRowSetImpl.insertRow();
        cachedRowSetImpl.moveToCurrentRow();
        if (z2) {
            return true;
        }
        PreparedStatement preparedStatementPrepareStatement2 = this.con.prepareStatement(this.deleteCmd + this.deleteWhere);
        int i5 = 0;
        for (int i6 = 0; i6 < this.keyCols.length; i6++) {
            if (this.params[i6] != null) {
                i5++;
                preparedStatementPrepareStatement2.setObject(i5, this.params[i6]);
            }
        }
        if (preparedStatementPrepareStatement2.executeUpdate() != 1) {
            return true;
        }
        preparedStatementPrepareStatement2.close();
        return false;
    }

    public void setReader(CachedRowSetReader cachedRowSetReader) throws SQLException {
        this.reader = cachedRowSetReader;
    }

    public CachedRowSetReader getReader() throws SQLException {
        return this.reader;
    }

    private void initSQLStatements(CachedRowSet cachedRowSet) throws SQLException {
        this.callerMd = cachedRowSet.getMetaData();
        this.callerColumnCount = this.callerMd.getColumnCount();
        if (this.callerColumnCount < 1) {
            return;
        }
        String tableName = cachedRowSet.getTableName();
        if (tableName == null) {
            tableName = this.callerMd.getTableName(1);
            if (tableName == null || tableName.length() == 0) {
                throw new SQLException(this.resBundle.handleGetObject("crswriter.tname").toString());
            }
        }
        String catalogName = this.callerMd.getCatalogName(1);
        String schemaName = this.callerMd.getSchemaName(1);
        DatabaseMetaData metaData = this.con.getMetaData();
        this.selectCmd = "SELECT ";
        for (int i2 = 1; i2 <= this.callerColumnCount; i2++) {
            this.selectCmd += this.callerMd.getColumnName(i2);
            if (i2 < this.callerMd.getColumnCount()) {
                this.selectCmd += ", ";
            } else {
                this.selectCmd += " ";
            }
        }
        this.selectCmd += "FROM " + buildTableName(metaData, catalogName, schemaName, tableName);
        this.updateCmd = "UPDATE " + buildTableName(metaData, catalogName, schemaName, tableName);
        int iIndexOf = this.updateCmd.toLowerCase().indexOf("where");
        if (iIndexOf != -1) {
            this.updateCmd = this.updateCmd.substring(0, iIndexOf);
        }
        this.updateCmd += "SET ";
        this.insertCmd = "INSERT INTO " + buildTableName(metaData, catalogName, schemaName, tableName);
        this.insertCmd += "(";
        for (int i3 = 1; i3 <= this.callerColumnCount; i3++) {
            this.insertCmd += this.callerMd.getColumnName(i3);
            if (i3 < this.callerMd.getColumnCount()) {
                this.insertCmd += ", ";
            } else {
                this.insertCmd += ") VALUES (";
            }
        }
        for (int i4 = 1; i4 <= this.callerColumnCount; i4++) {
            this.insertCmd += "?";
            if (i4 < this.callerColumnCount) {
                this.insertCmd += ", ";
            } else {
                this.insertCmd += ")";
            }
        }
        this.deleteCmd = "DELETE FROM " + buildTableName(metaData, catalogName, schemaName, tableName);
        buildKeyDesc(cachedRowSet);
    }

    private String buildTableName(DatabaseMetaData databaseMetaData, String str, String str2, String str3) throws SQLException {
        String str4;
        String str5 = "";
        String strTrim = str.trim();
        String strTrim2 = str2.trim();
        String strTrim3 = str3.trim();
        if (databaseMetaData.isCatalogAtStart()) {
            if (strTrim != null && strTrim.length() > 0) {
                str5 = str5 + strTrim + databaseMetaData.getCatalogSeparator();
            }
            if (strTrim2 != null && strTrim2.length() > 0) {
                str5 = str5 + strTrim2 + ".";
            }
            str4 = str5 + strTrim3;
        } else {
            if (strTrim2 != null && strTrim2.length() > 0) {
                str5 = str5 + strTrim2 + ".";
            }
            str4 = str5 + strTrim3;
            if (strTrim != null && strTrim.length() > 0) {
                str4 = str4 + databaseMetaData.getCatalogSeparator() + strTrim;
            }
        }
        return str4 + " ";
    }

    private void buildKeyDesc(CachedRowSet cachedRowSet) throws SQLException {
        this.keyCols = cachedRowSet.getKeyColumns();
        ResultSetMetaData metaData = cachedRowSet.getMetaData();
        if (this.keyCols == null || this.keyCols.length == 0) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.callerColumnCount; i2++) {
                if (metaData.getColumnType(i2 + 1) != 2005 && metaData.getColumnType(i2 + 1) != 2002 && metaData.getColumnType(i2 + 1) != 2009 && metaData.getColumnType(i2 + 1) != 2004 && metaData.getColumnType(i2 + 1) != 2003 && metaData.getColumnType(i2 + 1) != 1111) {
                    arrayList.add(Integer.valueOf(i2 + 1));
                }
            }
            this.keyCols = new int[arrayList.size()];
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                this.keyCols[i3] = ((Integer) arrayList.get(i3)).intValue();
            }
        }
        this.params = new Object[this.keyCols.length];
    }

    private String buildWhereClause(String str, ResultSet resultSet) throws SQLException {
        String str2;
        String str3 = "WHERE ";
        for (int i2 = 0; i2 < this.keyCols.length; i2++) {
            if (i2 > 0) {
                str3 = str3 + "AND ";
            }
            String str4 = str3 + this.callerMd.getColumnName(this.keyCols[i2]);
            this.params[i2] = resultSet.getObject(this.keyCols[i2]);
            if (resultSet.wasNull()) {
                str2 = str4 + " IS NULL ";
            } else {
                str2 = str4 + " = ? ";
            }
            str3 = str2;
        }
        return str3;
    }

    void updateResolvedConflictToDB(CachedRowSet cachedRowSet, Connection connection) throws SQLException {
        String str;
        int columnCount = cachedRowSet.getMetaData().getColumnCount();
        int[] keyColumns = cachedRowSet.getKeyColumns();
        String str2 = "";
        buildWhereClause("WHERE ", cachedRowSet);
        if (keyColumns == null || keyColumns.length == 0) {
            keyColumns = new int[columnCount];
            int i2 = 0;
            while (i2 < keyColumns.length) {
                int i3 = i2;
                i2++;
                keyColumns[i3] = i2;
            }
        }
        Object[] objArr = new Object[keyColumns.length];
        String str3 = ("UPDATE " + buildTableName(connection.getMetaData(), cachedRowSet.getMetaData().getCatalogName(1), cachedRowSet.getMetaData().getSchemaName(1), cachedRowSet.getTableName())) + "SET ";
        boolean z2 = true;
        for (int i4 = 1; i4 <= columnCount; i4++) {
            if (cachedRowSet.columnUpdated(i4)) {
                if (!z2) {
                    str2 = str2 + ", ";
                }
                str2 = (str2 + cachedRowSet.getMetaData().getColumnName(i4)) + " = ? ";
                z2 = false;
            }
        }
        String str4 = str3 + str2;
        String str5 = "WHERE ";
        for (int i5 = 0; i5 < keyColumns.length; i5++) {
            if (i5 > 0) {
                str5 = str5 + "AND ";
            }
            String str6 = str5 + cachedRowSet.getMetaData().getColumnName(keyColumns[i5]);
            objArr[i5] = cachedRowSet.getObject(keyColumns[i5]);
            if (cachedRowSet.wasNull()) {
                str = str6 + " IS NULL ";
            } else {
                str = str6 + " = ? ";
            }
            str5 = str;
        }
        PreparedStatement preparedStatementPrepareStatement = connection.prepareStatement(str4 + str5);
        int i6 = 0;
        for (int i7 = 0; i7 < columnCount; i7++) {
            if (cachedRowSet.columnUpdated(i7 + 1)) {
                Object object = cachedRowSet.getObject(i7 + 1);
                if (object != null) {
                    i6++;
                    preparedStatementPrepareStatement.setObject(i6, object);
                } else {
                    preparedStatementPrepareStatement.setNull(i7 + 1, cachedRowSet.getMetaData().getColumnType(i7 + 1));
                }
            }
        }
        for (int i8 = 0; i8 < keyColumns.length; i8++) {
            if (objArr[i8] != null) {
                i6++;
                preparedStatementPrepareStatement.setObject(i6, objArr[i8]);
            }
        }
        preparedStatementPrepareStatement.executeUpdate();
    }

    @Override // javax.sql.rowset.spi.TransactionalWriter
    public void commit() throws SQLException {
        this.con.commit();
        if (this.reader.getCloseConnection()) {
            this.con.close();
        }
    }

    public void commit(CachedRowSetImpl cachedRowSetImpl, boolean z2) throws SQLException {
        this.con.commit();
        if (z2 && cachedRowSetImpl.getCommand() != null) {
            cachedRowSetImpl.execute(this.con);
        }
        if (this.reader.getCloseConnection()) {
            this.con.close();
        }
    }

    @Override // javax.sql.rowset.spi.TransactionalWriter
    public void rollback() throws SQLException {
        this.con.rollback();
        if (this.reader.getCloseConnection()) {
            this.con.close();
        }
    }

    @Override // javax.sql.rowset.spi.TransactionalWriter
    public void rollback(Savepoint savepoint) throws SQLException {
        this.con.rollback(savepoint);
        if (this.reader.getCloseConnection()) {
            this.con.close();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private boolean isPKNameValid(String str, ResultSetMetaData resultSetMetaData) throws SQLException {
        boolean z2 = false;
        int columnCount = resultSetMetaData.getColumnCount();
        int i2 = 1;
        while (true) {
            if (i2 > columnCount) {
                break;
            }
            if (!resultSetMetaData.getColumnClassName(i2).equalsIgnoreCase(str)) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        return z2;
    }
}

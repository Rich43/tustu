package com.sun.rowset.internal;

import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.BitSet;
import javax.sql.RowSetMetaData;

/* loaded from: rt.jar:com/sun/rowset/internal/InsertRow.class */
public class InsertRow extends BaseRow implements Serializable, Cloneable {
    private BitSet colsInserted;
    private int cols;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = 1066099658102869344L;

    public InsertRow(int i2) {
        this.origVals = new Object[i2];
        this.colsInserted = new BitSet(i2);
        this.cols = i2;
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    protected void markColInserted(int i2) {
        this.colsInserted.set(i2);
    }

    public boolean isCompleteRow(RowSetMetaData rowSetMetaData) throws SQLException {
        for (int i2 = 0; i2 < this.cols; i2++) {
            if (!this.colsInserted.get(i2) && rowSetMetaData.isNullable(i2 + 1) == 0) {
                return false;
            }
        }
        return true;
    }

    public void initInsertRow() {
        for (int i2 = 0; i2 < this.cols; i2++) {
            this.colsInserted.clear(i2);
        }
    }

    @Override // com.sun.rowset.internal.BaseRow
    public Object getColumnObject(int i2) throws SQLException {
        if (!this.colsInserted.get(i2 - 1)) {
            throw new SQLException(this.resBundle.handleGetObject("insertrow.novalue").toString());
        }
        return this.origVals[i2 - 1];
    }

    @Override // com.sun.rowset.internal.BaseRow
    public void setColumnObject(int i2, Object obj) {
        this.origVals[i2 - 1] = obj;
        markColInserted(i2 - 1);
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

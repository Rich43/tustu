package com.sun.rowset.internal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.BitSet;

/* loaded from: rt.jar:com/sun/rowset/internal/Row.class */
public class Row extends BaseRow implements Serializable, Cloneable {
    static final long serialVersionUID = 5047859032611314762L;
    private Object[] currentVals;
    private BitSet colsChanged;
    private boolean deleted;
    private boolean updated;
    private boolean inserted;
    private int numCols;

    public Row(int i2) {
        this.origVals = new Object[i2];
        this.currentVals = new Object[i2];
        this.colsChanged = new BitSet(i2);
        this.numCols = i2;
    }

    public Row(int i2, Object[] objArr) {
        this.origVals = new Object[i2];
        System.arraycopy(objArr, 0, this.origVals, 0, i2);
        this.currentVals = new Object[i2];
        this.colsChanged = new BitSet(i2);
        this.numCols = i2;
    }

    public void initColumnObject(int i2, Object obj) {
        this.origVals[i2 - 1] = obj;
    }

    @Override // com.sun.rowset.internal.BaseRow
    public void setColumnObject(int i2, Object obj) {
        this.currentVals[i2 - 1] = obj;
        setColUpdated(i2 - 1);
    }

    @Override // com.sun.rowset.internal.BaseRow
    public Object getColumnObject(int i2) throws SQLException {
        if (getColUpdated(i2 - 1)) {
            return this.currentVals[i2 - 1];
        }
        return this.origVals[i2 - 1];
    }

    public boolean getColUpdated(int i2) {
        return this.colsChanged.get(i2);
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void clearDeleted() {
        this.deleted = false;
    }

    public void setInserted() {
        this.inserted = true;
    }

    public boolean getInserted() {
        return this.inserted;
    }

    public void clearInserted() {
        this.inserted = false;
    }

    public boolean getUpdated() {
        return this.updated;
    }

    public void setUpdated() {
        for (int i2 = 0; i2 < this.numCols; i2++) {
            if (getColUpdated(i2)) {
                this.updated = true;
                return;
            }
        }
    }

    private void setColUpdated(int i2) {
        this.colsChanged.set(i2);
    }

    public void clearUpdated() {
        this.updated = false;
        for (int i2 = 0; i2 < this.numCols; i2++) {
            this.currentVals[i2] = null;
            this.colsChanged.clear(i2);
        }
    }

    public void moveCurrentToOrig() {
        for (int i2 = 0; i2 < this.numCols; i2++) {
            if (getColUpdated(i2)) {
                this.origVals[i2] = this.currentVals[i2];
                this.currentVals[i2] = null;
                this.colsChanged.clear(i2);
            }
        }
        this.updated = false;
    }

    public BaseRow getCurrentRow() {
        return null;
    }
}

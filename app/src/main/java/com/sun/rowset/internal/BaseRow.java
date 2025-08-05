package com.sun.rowset.internal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;

/* loaded from: rt.jar:com/sun/rowset/internal/BaseRow.class */
public abstract class BaseRow implements Serializable, Cloneable {
    private static final long serialVersionUID = 4152013523511412238L;
    protected Object[] origVals;

    public abstract Object getColumnObject(int i2) throws SQLException;

    public abstract void setColumnObject(int i2, Object obj) throws SQLException;

    public Object[] getOrigRow() {
        Object[] objArr = this.origVals;
        if (objArr == null) {
            return null;
        }
        return Arrays.copyOf(objArr, objArr.length);
    }
}

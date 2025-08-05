package com.sun.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

/* loaded from: rt.jar:com/sun/rowset/FilteredRowSetImpl.class */
public class FilteredRowSetImpl extends WebRowSetImpl implements Serializable, Cloneable, FilteredRowSet {

    /* renamed from: p, reason: collision with root package name */
    private Predicate f12032p;
    private boolean onInsertRow;
    static final long serialVersionUID = 6178454588413509360L;

    public FilteredRowSetImpl() throws SQLException {
        this.onInsertRow = false;
    }

    public FilteredRowSetImpl(Hashtable hashtable) throws SQLException {
        super(hashtable);
        this.onInsertRow = false;
    }

    @Override // javax.sql.rowset.FilteredRowSet
    public void setFilter(Predicate predicate) throws SQLException {
        this.f12032p = predicate;
    }

    @Override // javax.sql.rowset.FilteredRowSet
    public Predicate getFilter() {
        return this.f12032p;
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalNext() throws SQLException {
        boolean zInternalNext = false;
        for (int row = getRow(); row <= size(); row++) {
            zInternalNext = super.internalNext();
            if (!zInternalNext || this.f12032p == null) {
                return zInternalNext;
            }
            if (this.f12032p.evaluate(this)) {
                break;
            }
        }
        return zInternalNext;
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalPrevious() throws SQLException {
        boolean zInternalPrevious = false;
        for (int row = getRow(); row > 0; row--) {
            zInternalPrevious = super.internalPrevious();
            if (this.f12032p == null) {
                return zInternalPrevious;
            }
            if (this.f12032p.evaluate(this)) {
                break;
            }
        }
        return zInternalPrevious;
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalFirst() throws SQLException {
        boolean zInternalFirst = super.internalFirst();
        if (this.f12032p == null) {
            return zInternalFirst;
        }
        while (zInternalFirst && !this.f12032p.evaluate(this)) {
            zInternalFirst = super.internalNext();
        }
        return zInternalFirst;
    }

    @Override // com.sun.rowset.CachedRowSetImpl
    protected boolean internalLast() throws SQLException {
        boolean zInternalLast = super.internalLast();
        if (this.f12032p == null) {
            return zInternalLast;
        }
        while (zInternalLast && !this.f12032p.evaluate(this)) {
            zInternalLast = super.internalPrevious();
        }
        return zInternalLast;
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean relative(int i2) throws SQLException {
        boolean z2;
        boolean zInternalNext = false;
        boolean zInternalPrevious = false;
        if (getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.relative").toString());
        }
        if (i2 > 0) {
            for (int i3 = 0; i3 < i2; i3++) {
                if (isAfterLast()) {
                    return false;
                }
                zInternalNext = internalNext();
            }
            z2 = zInternalNext;
        } else {
            for (int i4 = i2; i4 < 0; i4++) {
                if (isBeforeFirst()) {
                    return false;
                }
                zInternalPrevious = internalPrevious();
            }
            z2 = zInternalPrevious;
        }
        if (i2 != 0) {
            notifyCursorMoved();
        }
        return z2;
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public boolean absolute(int i2) throws SQLException {
        boolean z2;
        if (i2 == 0 || getType() == 1003) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.absolute").toString());
        }
        if (i2 > 0) {
            boolean zInternalFirst = internalFirst();
            for (int i3 = 0; i3 < i2 - 1; i3++) {
                if (isAfterLast()) {
                    return false;
                }
                zInternalFirst = internalNext();
            }
            z2 = zInternalFirst;
        } else {
            boolean zInternalLast = internalLast();
            for (int i4 = i2; i4 + 1 < 0; i4++) {
                if (isBeforeFirst()) {
                    return false;
                }
                zInternalLast = internalPrevious();
            }
            z2 = zInternalLast;
        }
        notifyCursorMoved();
        return z2;
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        this.onInsertRow = true;
        super.moveToInsertRow();
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(int i2, int i3) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Integer.valueOf(i3), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateInt(i2, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateInt(String str, int i2) throws SQLException {
        updateInt(findColumn(str), i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(int i2, boolean z2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Boolean.valueOf(z2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateBoolean(i2, z2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBoolean(String str, boolean z2) throws SQLException {
        updateBoolean(findColumn(str), z2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(int i2, byte b2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Byte.valueOf(b2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateByte(i2, b2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateByte(String str, byte b2) throws SQLException {
        updateByte(findColumn(str), b2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(int i2, short s2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Short.valueOf(s2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateShort(i2, s2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateShort(String str, short s2) throws SQLException {
        updateShort(findColumn(str), s2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(int i2, long j2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Long.valueOf(j2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateLong(i2, j2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateLong(String str, long j2) throws SQLException {
        updateLong(findColumn(str), j2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(int i2, float f2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Float.valueOf(f2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateFloat(i2, f2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateFloat(String str, float f2) throws SQLException {
        updateFloat(findColumn(str), f2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(int i2, double d2) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(Double.valueOf(d2), i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateDouble(i2, d2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDouble(String str, double d2) throws SQLException {
        updateDouble(findColumn(str), d2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(int i2, BigDecimal bigDecimal) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(bigDecimal, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateBigDecimal(i2, bigDecimal);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBigDecimal(String str, BigDecimal bigDecimal) throws SQLException {
        updateBigDecimal(findColumn(str), bigDecimal);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(int i2, String str) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(str, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateString(i2, str);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateString(String str, String str2) throws SQLException {
        updateString(findColumn(str), str2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(int i2, byte[] bArr) throws SQLException {
        String strConcat = "";
        Byte[] bArr2 = new Byte[bArr.length];
        for (int i3 = 0; i3 < bArr.length; i3++) {
            bArr2[i3] = Byte.valueOf(bArr[i3]);
            strConcat = strConcat.concat(bArr2[i3].toString());
        }
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(strConcat, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateBytes(i2, bArr);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBytes(String str, byte[] bArr) throws SQLException {
        updateBytes(findColumn(str), bArr);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(int i2, Date date) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(date, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateDate(i2, date);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateDate(String str, Date date) throws SQLException {
        updateDate(findColumn(str), date);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(int i2, Time time) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(time, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateTime(i2, time);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTime(String str, Time time) throws SQLException {
        updateTime(findColumn(str), time);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(int i2, Timestamp timestamp) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(timestamp, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateTimestamp(i2, timestamp);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateTimestamp(String str, Timestamp timestamp) throws SQLException {
        updateTimestamp(findColumn(str), timestamp);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(int i2, InputStream inputStream, int i3) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(inputStream, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateAsciiStream(i2, inputStream, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateAsciiStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateAsciiStream(findColumn(str), inputStream, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(int i2, Reader reader, int i3) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(reader, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateCharacterStream(i2, reader, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateCharacterStream(String str, Reader reader, int i2) throws SQLException {
        updateCharacterStream(findColumn(str), reader, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(int i2, InputStream inputStream, int i3) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(inputStream, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateBinaryStream(i2, inputStream, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateBinaryStream(String str, InputStream inputStream, int i2) throws SQLException {
        updateBinaryStream(findColumn(str), inputStream, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(obj, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateObject(i2, obj);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj) throws SQLException {
        updateObject(findColumn(str), obj);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(int i2, Object obj, int i3) throws SQLException {
        if (this.onInsertRow && this.f12032p != null && !this.f12032p.evaluate(obj, i2)) {
            throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
        }
        super.updateObject(i2, obj, i3);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void updateObject(String str, Object obj, int i2) throws SQLException {
        updateObject(findColumn(str), obj, i2);
    }

    @Override // com.sun.rowset.CachedRowSetImpl, java.sql.ResultSet
    public void insertRow() throws SQLException {
        this.onInsertRow = false;
        super.insertRow();
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

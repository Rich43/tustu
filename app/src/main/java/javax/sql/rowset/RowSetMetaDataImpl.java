package javax.sql.rowset;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import javax.sql.RowSetMetaData;

/* loaded from: rt.jar:javax/sql/rowset/RowSetMetaDataImpl.class */
public class RowSetMetaDataImpl implements RowSetMetaData, Serializable {
    private int colCount;
    private ColInfo[] colInfo;
    static final long serialVersionUID = 6893806403181801867L;

    private void checkColRange(int i2) throws SQLException {
        if (i2 <= 0 || i2 > this.colCount) {
            throw new SQLException("Invalid column index :" + i2);
        }
    }

    private void checkColType(int i2) throws SQLException {
        try {
            for (Field field : Types.class.getFields()) {
                if (field.getInt(Types.class) == i2) {
                    return;
                }
            }
            throw new SQLException("Invalid SQL type for column");
        } catch (Exception e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnCount(int i2) throws SQLException {
        if (i2 <= 0) {
            throw new SQLException("Invalid column count. Cannot be less or equal to zero");
        }
        this.colCount = i2;
        if (this.colCount != Integer.MAX_VALUE) {
            this.colInfo = new ColInfo[this.colCount + 1];
            for (int i3 = 1; i3 <= this.colCount; i3++) {
                this.colInfo[i3] = new ColInfo();
            }
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setAutoIncrement(int i2, boolean z2) throws SQLException {
        checkColRange(i2);
        this.colInfo[i2].autoIncrement = z2;
    }

    @Override // javax.sql.RowSetMetaData
    public void setCaseSensitive(int i2, boolean z2) throws SQLException {
        checkColRange(i2);
        this.colInfo[i2].caseSensitive = z2;
    }

    @Override // javax.sql.RowSetMetaData
    public void setSearchable(int i2, boolean z2) throws SQLException {
        checkColRange(i2);
        this.colInfo[i2].searchable = z2;
    }

    @Override // javax.sql.RowSetMetaData
    public void setCurrency(int i2, boolean z2) throws SQLException {
        checkColRange(i2);
        this.colInfo[i2].currency = z2;
    }

    @Override // javax.sql.RowSetMetaData
    public void setNullable(int i2, int i3) throws SQLException {
        if (i3 < 0 || i3 > 2) {
            throw new SQLException("Invalid nullable constant set. Must be either columnNoNulls, columnNullable or columnNullableUnknown");
        }
        checkColRange(i2);
        this.colInfo[i2].nullable = i3;
    }

    @Override // javax.sql.RowSetMetaData
    public void setSigned(int i2, boolean z2) throws SQLException {
        checkColRange(i2);
        this.colInfo[i2].signed = z2;
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnDisplaySize(int i2, int i3) throws SQLException {
        if (i3 < 0) {
            throw new SQLException("Invalid column display size. Cannot be less than zero");
        }
        checkColRange(i2);
        this.colInfo[i2].columnDisplaySize = i3;
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnLabel(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].columnLabel = str;
        } else {
            this.colInfo[i2].columnLabel = "";
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnName(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].columnName = str;
        } else {
            this.colInfo[i2].columnName = "";
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setSchemaName(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].schemaName = str;
        } else {
            this.colInfo[i2].schemaName = "";
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setPrecision(int i2, int i3) throws SQLException {
        if (i3 < 0) {
            throw new SQLException("Invalid precision value. Cannot be less than zero");
        }
        checkColRange(i2);
        this.colInfo[i2].colPrecision = i3;
    }

    @Override // javax.sql.RowSetMetaData
    public void setScale(int i2, int i3) throws SQLException {
        if (i3 < 0) {
            throw new SQLException("Invalid scale size. Cannot be less than zero");
        }
        checkColRange(i2);
        this.colInfo[i2].colScale = i3;
    }

    @Override // javax.sql.RowSetMetaData
    public void setTableName(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].tableName = str;
        } else {
            this.colInfo[i2].tableName = "";
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setCatalogName(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].catName = str;
        } else {
            this.colInfo[i2].catName = "";
        }
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnType(int i2, int i3) throws SQLException {
        checkColType(i3);
        checkColRange(i2);
        this.colInfo[i2].colType = i3;
    }

    @Override // javax.sql.RowSetMetaData
    public void setColumnTypeName(int i2, String str) throws SQLException {
        checkColRange(i2);
        if (str != null) {
            this.colInfo[i2].colTypeName = str;
        } else {
            this.colInfo[i2].colTypeName = "";
        }
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnCount() throws SQLException {
        return this.colCount;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isAutoIncrement(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].autoIncrement;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isCaseSensitive(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].caseSensitive;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isSearchable(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].searchable;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isCurrency(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].currency;
    }

    @Override // java.sql.ResultSetMetaData
    public int isNullable(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].nullable;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isSigned(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].signed;
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnDisplaySize(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].columnDisplaySize;
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnLabel(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].columnLabel;
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnName(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].columnName;
    }

    @Override // java.sql.ResultSetMetaData
    public String getSchemaName(int i2) throws SQLException {
        checkColRange(i2);
        String str = "";
        if (this.colInfo[i2].schemaName != null) {
            str = this.colInfo[i2].schemaName;
        }
        return str;
    }

    @Override // java.sql.ResultSetMetaData
    public int getPrecision(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].colPrecision;
    }

    @Override // java.sql.ResultSetMetaData
    public int getScale(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].colScale;
    }

    @Override // java.sql.ResultSetMetaData
    public String getTableName(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].tableName;
    }

    @Override // java.sql.ResultSetMetaData
    public String getCatalogName(int i2) throws SQLException {
        checkColRange(i2);
        String str = "";
        if (this.colInfo[i2].catName != null) {
            str = this.colInfo[i2].catName;
        }
        return str;
    }

    @Override // java.sql.ResultSetMetaData
    public int getColumnType(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].colType;
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnTypeName(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].colTypeName;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isReadOnly(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].readOnly;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isWritable(int i2) throws SQLException {
        checkColRange(i2);
        return this.colInfo[i2].writable;
    }

    @Override // java.sql.ResultSetMetaData
    public boolean isDefinitelyWritable(int i2) throws SQLException {
        checkColRange(i2);
        return true;
    }

    @Override // java.sql.ResultSetMetaData
    public String getColumnClassName(int i2) throws SQLException {
        String name = String.class.getName();
        switch (getColumnType(i2)) {
            case -7:
                name = Boolean.class.getName();
                break;
            case -6:
                name = Byte.class.getName();
                break;
            case -5:
                name = Long.class.getName();
                break;
            case -4:
            case -3:
            case -2:
                name = "byte[]";
                break;
            case 2:
            case 3:
                name = BigDecimal.class.getName();
                break;
            case 4:
                name = Integer.class.getName();
                break;
            case 5:
                name = Short.class.getName();
                break;
            case 6:
            case 8:
                name = Double.class.getName();
                break;
            case 7:
                name = Float.class.getName();
                break;
            case 91:
                name = Date.class.getName();
                break;
            case 92:
                name = Time.class.getName();
                break;
            case 93:
                name = Timestamp.class.getName();
                break;
            case Types.BLOB /* 2004 */:
                name = Blob.class.getName();
                break;
            case Types.CLOB /* 2005 */:
                name = Clob.class.getName();
                break;
        }
        return name;
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        if (isWrapperFor(cls)) {
            return cls.cast(this);
        }
        throw new SQLException("unwrap failed for:" + ((Object) cls));
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> cls) throws SQLException {
        return cls.isInstance(this);
    }

    /* loaded from: rt.jar:javax/sql/rowset/RowSetMetaDataImpl$ColInfo.class */
    private class ColInfo implements Serializable {
        public boolean autoIncrement;
        public boolean caseSensitive;
        public boolean currency;
        public int nullable;
        public boolean signed;
        public boolean searchable;
        public int columnDisplaySize;
        public String columnLabel;
        public String columnName;
        public String schemaName;
        public int colPrecision;
        public int colScale;
        public String tableName;
        public String catName;
        public int colType;
        public String colTypeName;
        public boolean readOnly;
        public boolean writable;
        static final long serialVersionUID = 5490834817919311283L;

        private ColInfo() {
            this.tableName = "";
            this.readOnly = false;
            this.writable = true;
        }
    }
}

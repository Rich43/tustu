package javax.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/RowSetMetaData.class */
public interface RowSetMetaData extends ResultSetMetaData {
    void setColumnCount(int i2) throws SQLException;

    void setAutoIncrement(int i2, boolean z2) throws SQLException;

    void setCaseSensitive(int i2, boolean z2) throws SQLException;

    void setSearchable(int i2, boolean z2) throws SQLException;

    void setCurrency(int i2, boolean z2) throws SQLException;

    void setNullable(int i2, int i3) throws SQLException;

    void setSigned(int i2, boolean z2) throws SQLException;

    void setColumnDisplaySize(int i2, int i3) throws SQLException;

    void setColumnLabel(int i2, String str) throws SQLException;

    void setColumnName(int i2, String str) throws SQLException;

    void setSchemaName(int i2, String str) throws SQLException;

    void setPrecision(int i2, int i3) throws SQLException;

    void setScale(int i2, int i3) throws SQLException;

    void setTableName(int i2, String str) throws SQLException;

    void setCatalogName(int i2, String str) throws SQLException;

    void setColumnType(int i2, int i3) throws SQLException;

    void setColumnTypeName(int i2, String str) throws SQLException;
}

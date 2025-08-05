package javax.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/RowSetInternal.class */
public interface RowSetInternal {
    Object[] getParams() throws SQLException;

    Connection getConnection() throws SQLException;

    void setMetaData(RowSetMetaData rowSetMetaData) throws SQLException;

    ResultSet getOriginal() throws SQLException;

    ResultSet getOriginalRow() throws SQLException;
}

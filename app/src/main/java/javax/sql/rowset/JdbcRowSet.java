package javax.sql.rowset;

import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.RowSet;

/* loaded from: rt.jar:javax/sql/rowset/JdbcRowSet.class */
public interface JdbcRowSet extends RowSet, Joinable {
    boolean getShowDeleted() throws SQLException;

    void setShowDeleted(boolean z2) throws SQLException;

    RowSetWarning getRowSetWarnings() throws SQLException;

    void commit() throws SQLException;

    boolean getAutoCommit() throws SQLException;

    void setAutoCommit(boolean z2) throws SQLException;

    void rollback() throws SQLException;

    void rollback(Savepoint savepoint) throws SQLException;
}

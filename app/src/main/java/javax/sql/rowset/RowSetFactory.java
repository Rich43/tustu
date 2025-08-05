package javax.sql.rowset;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/rowset/RowSetFactory.class */
public interface RowSetFactory {
    CachedRowSet createCachedRowSet() throws SQLException;

    FilteredRowSet createFilteredRowSet() throws SQLException;

    JdbcRowSet createJdbcRowSet() throws SQLException;

    JoinRowSet createJoinRowSet() throws SQLException;

    WebRowSet createWebRowSet() throws SQLException;
}

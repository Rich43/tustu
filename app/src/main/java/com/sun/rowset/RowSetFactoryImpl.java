package com.sun.rowset;

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.WebRowSet;

/* loaded from: rt.jar:com/sun/rowset/RowSetFactoryImpl.class */
public final class RowSetFactoryImpl implements RowSetFactory {
    @Override // javax.sql.rowset.RowSetFactory
    public CachedRowSet createCachedRowSet() throws SQLException {
        return new CachedRowSetImpl();
    }

    @Override // javax.sql.rowset.RowSetFactory
    public FilteredRowSet createFilteredRowSet() throws SQLException {
        return new FilteredRowSetImpl();
    }

    @Override // javax.sql.rowset.RowSetFactory
    public JdbcRowSet createJdbcRowSet() throws SQLException {
        return new JdbcRowSetImpl();
    }

    @Override // javax.sql.rowset.RowSetFactory
    public JoinRowSet createJoinRowSet() throws SQLException {
        return new JoinRowSetImpl();
    }

    @Override // javax.sql.rowset.RowSetFactory
    public WebRowSet createWebRowSet() throws SQLException {
        return new WebRowSetImpl();
    }
}

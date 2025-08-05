package javax.sql.rowset;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/rowset/FilteredRowSet.class */
public interface FilteredRowSet extends WebRowSet {
    void setFilter(Predicate predicate) throws SQLException;

    Predicate getFilter();
}

package javax.sql.rowset;

import java.sql.SQLException;
import javax.sql.RowSet;

/* loaded from: rt.jar:javax/sql/rowset/Predicate.class */
public interface Predicate {
    boolean evaluate(RowSet rowSet);

    boolean evaluate(Object obj, int i2) throws SQLException;

    boolean evaluate(Object obj, String str) throws SQLException;
}

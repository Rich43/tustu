package javax.sql;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/RowSetWriter.class */
public interface RowSetWriter {
    boolean writeData(RowSetInternal rowSetInternal) throws SQLException;
}

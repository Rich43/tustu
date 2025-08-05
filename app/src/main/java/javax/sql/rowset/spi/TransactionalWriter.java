package javax.sql.rowset.spi;

import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.RowSetWriter;

/* loaded from: rt.jar:javax/sql/rowset/spi/TransactionalWriter.class */
public interface TransactionalWriter extends RowSetWriter {
    void commit() throws SQLException;

    void rollback() throws SQLException;

    void rollback(Savepoint savepoint) throws SQLException;
}

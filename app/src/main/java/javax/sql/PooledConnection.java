package javax.sql;

import java.sql.Connection;
import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/PooledConnection.class */
public interface PooledConnection {
    Connection getConnection() throws SQLException;

    void close() throws SQLException;

    void addConnectionEventListener(ConnectionEventListener connectionEventListener);

    void removeConnectionEventListener(ConnectionEventListener connectionEventListener);

    void addStatementEventListener(StatementEventListener statementEventListener);

    void removeStatementEventListener(StatementEventListener statementEventListener);
}

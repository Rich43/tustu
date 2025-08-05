package javax.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EventObject;

/* loaded from: rt.jar:javax/sql/StatementEvent.class */
public class StatementEvent extends EventObject {
    static final long serialVersionUID = -8089573731826608315L;
    private SQLException exception;
    private PreparedStatement statement;

    public StatementEvent(PooledConnection pooledConnection, PreparedStatement preparedStatement) {
        super(pooledConnection);
        this.statement = preparedStatement;
        this.exception = null;
    }

    public StatementEvent(PooledConnection pooledConnection, PreparedStatement preparedStatement, SQLException sQLException) {
        super(pooledConnection);
        this.statement = preparedStatement;
        this.exception = sQLException;
    }

    public PreparedStatement getStatement() {
        return this.statement;
    }

    public SQLException getSQLException() {
        return this.exception;
    }
}

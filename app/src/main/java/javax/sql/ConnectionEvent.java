package javax.sql;

import java.sql.SQLException;
import java.util.EventObject;

/* loaded from: rt.jar:javax/sql/ConnectionEvent.class */
public class ConnectionEvent extends EventObject {
    private SQLException ex;
    static final long serialVersionUID = -4843217645290030002L;

    public ConnectionEvent(PooledConnection pooledConnection) {
        super(pooledConnection);
        this.ex = null;
    }

    public ConnectionEvent(PooledConnection pooledConnection, SQLException sQLException) {
        super(pooledConnection);
        this.ex = null;
        this.ex = sQLException;
    }

    public SQLException getSQLException() {
        return this.ex;
    }
}

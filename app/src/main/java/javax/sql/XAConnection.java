package javax.sql;

import java.sql.SQLException;
import javax.transaction.xa.XAResource;

/* loaded from: rt.jar:javax/sql/XAConnection.class */
public interface XAConnection extends PooledConnection {
    XAResource getXAResource() throws SQLException;
}

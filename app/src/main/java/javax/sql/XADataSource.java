package javax.sql;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/XADataSource.class */
public interface XADataSource extends CommonDataSource {
    XAConnection getXAConnection() throws SQLException;

    XAConnection getXAConnection(String str, String str2) throws SQLException;
}

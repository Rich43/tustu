package javax.sql;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/* loaded from: rt.jar:javax/sql/CommonDataSource.class */
public interface CommonDataSource {
    PrintWriter getLogWriter() throws SQLException;

    void setLogWriter(PrintWriter printWriter) throws SQLException;

    void setLoginTimeout(int i2) throws SQLException;

    int getLoginTimeout() throws SQLException;

    Logger getParentLogger() throws SQLFeatureNotSupportedException;
}

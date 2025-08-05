package java.sql;

import java.util.Properties;
import java.util.logging.Logger;

/* loaded from: rt.jar:java/sql/Driver.class */
public interface Driver {
    Connection connect(String str, Properties properties) throws SQLException;

    boolean acceptsURL(String str) throws SQLException;

    DriverPropertyInfo[] getPropertyInfo(String str, Properties properties) throws SQLException;

    int getMajorVersion();

    int getMinorVersion();

    boolean jdbcCompliant();

    Logger getParentLogger() throws SQLFeatureNotSupportedException;
}

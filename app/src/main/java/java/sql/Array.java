package java.sql;

import java.util.Map;

/* loaded from: rt.jar:java/sql/Array.class */
public interface Array {
    String getBaseTypeName() throws SQLException;

    int getBaseType() throws SQLException;

    Object getArray() throws SQLException;

    Object getArray(Map<String, Class<?>> map) throws SQLException;

    Object getArray(long j2, int i2) throws SQLException;

    Object getArray(long j2, int i2, Map<String, Class<?>> map) throws SQLException;

    ResultSet getResultSet() throws SQLException;

    ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException;

    ResultSet getResultSet(long j2, int i2) throws SQLException;

    ResultSet getResultSet(long j2, int i2, Map<String, Class<?>> map) throws SQLException;

    void free() throws SQLException;
}

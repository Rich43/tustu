package java.sql;

import java.util.Map;

/* loaded from: rt.jar:java/sql/Struct.class */
public interface Struct {
    String getSQLTypeName() throws SQLException;

    Object[] getAttributes() throws SQLException;

    Object[] getAttributes(Map<String, Class<?>> map) throws SQLException;
}

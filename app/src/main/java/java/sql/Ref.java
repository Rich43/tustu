package java.sql;

import java.util.Map;

/* loaded from: rt.jar:java/sql/Ref.class */
public interface Ref {
    String getBaseTypeName() throws SQLException;

    Object getObject(Map<String, Class<?>> map) throws SQLException;

    Object getObject() throws SQLException;

    void setObject(Object obj) throws SQLException;
}

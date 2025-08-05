package java.sql;

/* loaded from: rt.jar:java/sql/Wrapper.class */
public interface Wrapper {
    <T> T unwrap(Class<T> cls) throws SQLException;

    boolean isWrapperFor(Class<?> cls) throws SQLException;
}

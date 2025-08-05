package java.sql;

/* loaded from: rt.jar:java/sql/Savepoint.class */
public interface Savepoint {
    int getSavepointId() throws SQLException;

    String getSavepointName() throws SQLException;
}

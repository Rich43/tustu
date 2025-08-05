package java.sql;

/* loaded from: rt.jar:java/sql/RowId.class */
public interface RowId {
    boolean equals(Object obj);

    byte[] getBytes();

    String toString();

    int hashCode();
}

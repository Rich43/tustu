package java.sql;

/* loaded from: rt.jar:java/sql/SQLData.class */
public interface SQLData {
    String getSQLTypeName() throws SQLException;

    void readSQL(SQLInput sQLInput, String str) throws SQLException;

    void writeSQL(SQLOutput sQLOutput) throws SQLException;
}

package javax.sql.rowset;

import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/rowset/Joinable.class */
public interface Joinable {
    void setMatchColumn(int i2) throws SQLException;

    void setMatchColumn(int[] iArr) throws SQLException;

    void setMatchColumn(String str) throws SQLException;

    void setMatchColumn(String[] strArr) throws SQLException;

    int[] getMatchColumnIndexes() throws SQLException;

    String[] getMatchColumnNames() throws SQLException;

    void unsetMatchColumn(int i2) throws SQLException;

    void unsetMatchColumn(int[] iArr) throws SQLException;

    void unsetMatchColumn(String str) throws SQLException;

    void unsetMatchColumn(String[] strArr) throws SQLException;
}

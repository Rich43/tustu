package javax.sql.rowset;

import java.sql.SQLException;
import java.util.Collection;
import javax.sql.RowSet;

/* loaded from: rt.jar:javax/sql/rowset/JoinRowSet.class */
public interface JoinRowSet extends WebRowSet {
    public static final int CROSS_JOIN = 0;
    public static final int INNER_JOIN = 1;
    public static final int LEFT_OUTER_JOIN = 2;
    public static final int RIGHT_OUTER_JOIN = 3;
    public static final int FULL_JOIN = 4;

    void addRowSet(Joinable joinable) throws SQLException;

    void addRowSet(RowSet rowSet, int i2) throws SQLException;

    void addRowSet(RowSet rowSet, String str) throws SQLException;

    void addRowSet(RowSet[] rowSetArr, int[] iArr) throws SQLException;

    void addRowSet(RowSet[] rowSetArr, String[] strArr) throws SQLException;

    Collection<?> getRowSets() throws SQLException;

    String[] getRowSetNames() throws SQLException;

    CachedRowSet toCachedRowSet() throws SQLException;

    boolean supportsCrossJoin();

    boolean supportsInnerJoin();

    boolean supportsLeftOuterJoin();

    boolean supportsRightOuterJoin();

    boolean supportsFullJoin();

    void setJoinType(int i2) throws SQLException;

    String getWhereClause() throws SQLException;

    int getJoinType() throws SQLException;
}

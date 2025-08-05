package java.sql;

/* loaded from: rt.jar:java/sql/Statement.class */
public interface Statement extends Wrapper, AutoCloseable {
    public static final int CLOSE_CURRENT_RESULT = 1;
    public static final int KEEP_CURRENT_RESULT = 2;
    public static final int CLOSE_ALL_RESULTS = 3;
    public static final int SUCCESS_NO_INFO = -2;
    public static final int EXECUTE_FAILED = -3;
    public static final int RETURN_GENERATED_KEYS = 1;
    public static final int NO_GENERATED_KEYS = 2;

    ResultSet executeQuery(String str) throws SQLException;

    int executeUpdate(String str) throws SQLException;

    @Override // java.lang.AutoCloseable
    void close() throws SQLException;

    int getMaxFieldSize() throws SQLException;

    void setMaxFieldSize(int i2) throws SQLException;

    int getMaxRows() throws SQLException;

    void setMaxRows(int i2) throws SQLException;

    void setEscapeProcessing(boolean z2) throws SQLException;

    int getQueryTimeout() throws SQLException;

    void setQueryTimeout(int i2) throws SQLException;

    void cancel() throws SQLException;

    SQLWarning getWarnings() throws SQLException;

    void clearWarnings() throws SQLException;

    void setCursorName(String str) throws SQLException;

    boolean execute(String str) throws SQLException;

    ResultSet getResultSet() throws SQLException;

    int getUpdateCount() throws SQLException;

    boolean getMoreResults() throws SQLException;

    void setFetchDirection(int i2) throws SQLException;

    int getFetchDirection() throws SQLException;

    void setFetchSize(int i2) throws SQLException;

    int getFetchSize() throws SQLException;

    int getResultSetConcurrency() throws SQLException;

    int getResultSetType() throws SQLException;

    void addBatch(String str) throws SQLException;

    void clearBatch() throws SQLException;

    int[] executeBatch() throws SQLException;

    Connection getConnection() throws SQLException;

    boolean getMoreResults(int i2) throws SQLException;

    ResultSet getGeneratedKeys() throws SQLException;

    int executeUpdate(String str, int i2) throws SQLException;

    int executeUpdate(String str, int[] iArr) throws SQLException;

    int executeUpdate(String str, String[] strArr) throws SQLException;

    boolean execute(String str, int i2) throws SQLException;

    boolean execute(String str, int[] iArr) throws SQLException;

    boolean execute(String str, String[] strArr) throws SQLException;

    int getResultSetHoldability() throws SQLException;

    boolean isClosed() throws SQLException;

    void setPoolable(boolean z2) throws SQLException;

    boolean isPoolable() throws SQLException;

    void closeOnCompletion() throws SQLException;

    boolean isCloseOnCompletion() throws SQLException;

    default long getLargeUpdateCount() throws SQLException {
        throw new UnsupportedOperationException("getLargeUpdateCount not implemented");
    }

    default void setLargeMaxRows(long j2) throws SQLException {
        throw new UnsupportedOperationException("setLargeMaxRows not implemented");
    }

    default long getLargeMaxRows() throws SQLException {
        return 0L;
    }

    default long[] executeLargeBatch() throws SQLException {
        throw new UnsupportedOperationException("executeLargeBatch not implemented");
    }

    default long executeLargeUpdate(String str) throws SQLException {
        throw new UnsupportedOperationException("executeLargeUpdate not implemented");
    }

    default long executeLargeUpdate(String str, int i2) throws SQLException {
        throw new SQLFeatureNotSupportedException("executeLargeUpdate not implemented");
    }

    default long executeLargeUpdate(String str, int[] iArr) throws SQLException {
        throw new SQLFeatureNotSupportedException("executeLargeUpdate not implemented");
    }

    default long executeLargeUpdate(String str, String[] strArr) throws SQLException {
        throw new SQLFeatureNotSupportedException("executeLargeUpdate not implemented");
    }
}

package java.sql;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/* loaded from: rt.jar:java/sql/Connection.class */
public interface Connection extends Wrapper, AutoCloseable {
    public static final int TRANSACTION_NONE = 0;
    public static final int TRANSACTION_READ_UNCOMMITTED = 1;
    public static final int TRANSACTION_READ_COMMITTED = 2;
    public static final int TRANSACTION_REPEATABLE_READ = 4;
    public static final int TRANSACTION_SERIALIZABLE = 8;

    Statement createStatement() throws SQLException;

    PreparedStatement prepareStatement(String str) throws SQLException;

    CallableStatement prepareCall(String str) throws SQLException;

    String nativeSQL(String str) throws SQLException;

    void setAutoCommit(boolean z2) throws SQLException;

    boolean getAutoCommit() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    @Override // java.lang.AutoCloseable
    void close() throws SQLException;

    boolean isClosed() throws SQLException;

    DatabaseMetaData getMetaData() throws SQLException;

    void setReadOnly(boolean z2) throws SQLException;

    boolean isReadOnly() throws SQLException;

    void setCatalog(String str) throws SQLException;

    String getCatalog() throws SQLException;

    void setTransactionIsolation(int i2) throws SQLException;

    int getTransactionIsolation() throws SQLException;

    SQLWarning getWarnings() throws SQLException;

    void clearWarnings() throws SQLException;

    Statement createStatement(int i2, int i3) throws SQLException;

    PreparedStatement prepareStatement(String str, int i2, int i3) throws SQLException;

    CallableStatement prepareCall(String str, int i2, int i3) throws SQLException;

    Map<String, Class<?>> getTypeMap() throws SQLException;

    void setTypeMap(Map<String, Class<?>> map) throws SQLException;

    void setHoldability(int i2) throws SQLException;

    int getHoldability() throws SQLException;

    Savepoint setSavepoint() throws SQLException;

    Savepoint setSavepoint(String str) throws SQLException;

    void rollback(Savepoint savepoint) throws SQLException;

    void releaseSavepoint(Savepoint savepoint) throws SQLException;

    Statement createStatement(int i2, int i3, int i4) throws SQLException;

    PreparedStatement prepareStatement(String str, int i2, int i3, int i4) throws SQLException;

    CallableStatement prepareCall(String str, int i2, int i3, int i4) throws SQLException;

    PreparedStatement prepareStatement(String str, int i2) throws SQLException;

    PreparedStatement prepareStatement(String str, int[] iArr) throws SQLException;

    PreparedStatement prepareStatement(String str, String[] strArr) throws SQLException;

    Clob createClob() throws SQLException;

    Blob createBlob() throws SQLException;

    NClob createNClob() throws SQLException;

    SQLXML createSQLXML() throws SQLException;

    boolean isValid(int i2) throws SQLException;

    void setClientInfo(String str, String str2) throws SQLClientInfoException;

    void setClientInfo(Properties properties) throws SQLClientInfoException;

    String getClientInfo(String str) throws SQLException;

    Properties getClientInfo() throws SQLException;

    Array createArrayOf(String str, Object[] objArr) throws SQLException;

    Struct createStruct(String str, Object[] objArr) throws SQLException;

    void setSchema(String str) throws SQLException;

    String getSchema() throws SQLException;

    void abort(Executor executor) throws SQLException;

    void setNetworkTimeout(Executor executor, int i2) throws SQLException;

    int getNetworkTimeout() throws SQLException;
}

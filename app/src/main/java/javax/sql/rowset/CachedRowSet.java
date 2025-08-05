package javax.sql.rowset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;

/* loaded from: rt.jar:javax/sql/rowset/CachedRowSet.class */
public interface CachedRowSet extends RowSet, Joinable {

    @Deprecated
    public static final boolean COMMIT_ON_ACCEPT_CHANGES = true;

    void populate(ResultSet resultSet) throws SQLException;

    void execute(Connection connection) throws SQLException;

    void acceptChanges() throws SyncProviderException;

    void acceptChanges(Connection connection) throws SyncProviderException;

    void restoreOriginal() throws SQLException;

    void release() throws SQLException;

    void undoDelete() throws SQLException;

    void undoInsert() throws SQLException;

    void undoUpdate() throws SQLException;

    boolean columnUpdated(int i2) throws SQLException;

    boolean columnUpdated(String str) throws SQLException;

    Collection<?> toCollection() throws SQLException;

    Collection<?> toCollection(int i2) throws SQLException;

    Collection<?> toCollection(String str) throws SQLException;

    SyncProvider getSyncProvider() throws SQLException;

    void setSyncProvider(String str) throws SQLException;

    int size();

    void setMetaData(RowSetMetaData rowSetMetaData) throws SQLException;

    ResultSet getOriginal() throws SQLException;

    ResultSet getOriginalRow() throws SQLException;

    void setOriginalRow() throws SQLException;

    String getTableName() throws SQLException;

    void setTableName(String str) throws SQLException;

    int[] getKeyColumns() throws SQLException;

    void setKeyColumns(int[] iArr) throws SQLException;

    RowSet createShared() throws SQLException;

    CachedRowSet createCopy() throws SQLException;

    CachedRowSet createCopySchema() throws SQLException;

    CachedRowSet createCopyNoConstraints() throws SQLException;

    RowSetWarning getRowSetWarnings() throws SQLException;

    boolean getShowDeleted() throws SQLException;

    void setShowDeleted(boolean z2) throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void rollback(Savepoint savepoint) throws SQLException;

    void rowSetPopulated(RowSetEvent rowSetEvent, int i2) throws SQLException;

    void populate(ResultSet resultSet, int i2) throws SQLException;

    void setPageSize(int i2) throws SQLException;

    int getPageSize();

    boolean nextPage() throws SQLException;

    boolean previousPage() throws SQLException;
}

package javax.sql.rowset.spi;

import java.sql.SQLException;
import javax.sql.RowSet;

/* loaded from: rt.jar:javax/sql/rowset/spi/SyncResolver.class */
public interface SyncResolver extends RowSet {
    public static final int UPDATE_ROW_CONFLICT = 0;
    public static final int DELETE_ROW_CONFLICT = 1;
    public static final int INSERT_ROW_CONFLICT = 2;
    public static final int NO_ROW_CONFLICT = 3;

    int getStatus();

    Object getConflictValue(int i2) throws SQLException;

    Object getConflictValue(String str) throws SQLException;

    void setResolvedValue(int i2, Object obj) throws SQLException;

    void setResolvedValue(String str, Object obj) throws SQLException;

    boolean nextConflict() throws SQLException;

    boolean previousConflict() throws SQLException;
}

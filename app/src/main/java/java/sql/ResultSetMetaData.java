package java.sql;

/* loaded from: rt.jar:java/sql/ResultSetMetaData.class */
public interface ResultSetMetaData extends Wrapper {
    public static final int columnNoNulls = 0;
    public static final int columnNullable = 1;
    public static final int columnNullableUnknown = 2;

    int getColumnCount() throws SQLException;

    boolean isAutoIncrement(int i2) throws SQLException;

    boolean isCaseSensitive(int i2) throws SQLException;

    boolean isSearchable(int i2) throws SQLException;

    boolean isCurrency(int i2) throws SQLException;

    int isNullable(int i2) throws SQLException;

    boolean isSigned(int i2) throws SQLException;

    int getColumnDisplaySize(int i2) throws SQLException;

    String getColumnLabel(int i2) throws SQLException;

    String getColumnName(int i2) throws SQLException;

    String getSchemaName(int i2) throws SQLException;

    int getPrecision(int i2) throws SQLException;

    int getScale(int i2) throws SQLException;

    String getTableName(int i2) throws SQLException;

    String getCatalogName(int i2) throws SQLException;

    int getColumnType(int i2) throws SQLException;

    String getColumnTypeName(int i2) throws SQLException;

    boolean isReadOnly(int i2) throws SQLException;

    boolean isWritable(int i2) throws SQLException;

    boolean isDefinitelyWritable(int i2) throws SQLException;

    String getColumnClassName(int i2) throws SQLException;
}

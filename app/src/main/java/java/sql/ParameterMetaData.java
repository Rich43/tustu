package java.sql;

/* loaded from: rt.jar:java/sql/ParameterMetaData.class */
public interface ParameterMetaData extends Wrapper {
    public static final int parameterNoNulls = 0;
    public static final int parameterNullable = 1;
    public static final int parameterNullableUnknown = 2;
    public static final int parameterModeUnknown = 0;
    public static final int parameterModeIn = 1;
    public static final int parameterModeInOut = 2;
    public static final int parameterModeOut = 4;

    int getParameterCount() throws SQLException;

    int isNullable(int i2) throws SQLException;

    boolean isSigned(int i2) throws SQLException;

    int getPrecision(int i2) throws SQLException;

    int getScale(int i2) throws SQLException;

    int getParameterType(int i2) throws SQLException;

    String getParameterTypeName(int i2) throws SQLException;

    String getParameterClassName(int i2) throws SQLException;

    int getParameterMode(int i2) throws SQLException;
}

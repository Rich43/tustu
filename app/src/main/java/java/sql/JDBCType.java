package java.sql;

/* loaded from: rt.jar:java/sql/JDBCType.class */
public enum JDBCType implements SQLType {
    BIT(-7),
    TINYINT(-6),
    SMALLINT(5),
    INTEGER(4),
    BIGINT(-5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    NUMERIC(2),
    DECIMAL(3),
    CHAR(1),
    VARCHAR(12),
    LONGVARCHAR(-1),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    BINARY(-2),
    VARBINARY(-3),
    LONGVARBINARY(-4),
    NULL(0),
    OTHER(Integer.valueOf(Types.OTHER)),
    JAVA_OBJECT(2000),
    DISTINCT(Integer.valueOf(Types.DISTINCT)),
    STRUCT(Integer.valueOf(Types.STRUCT)),
    ARRAY(Integer.valueOf(Types.ARRAY)),
    BLOB(Integer.valueOf(Types.BLOB)),
    CLOB(Integer.valueOf(Types.CLOB)),
    REF(Integer.valueOf(Types.REF)),
    DATALINK(70),
    BOOLEAN(16),
    ROWID(-8),
    NCHAR(-15),
    NVARCHAR(-9),
    LONGNVARCHAR(-16),
    NCLOB(Integer.valueOf(Types.NCLOB)),
    SQLXML(Integer.valueOf(Types.SQLXML)),
    REF_CURSOR(Integer.valueOf(Types.REF_CURSOR)),
    TIME_WITH_TIMEZONE(Integer.valueOf(Types.TIME_WITH_TIMEZONE)),
    TIMESTAMP_WITH_TIMEZONE(Integer.valueOf(Types.TIMESTAMP_WITH_TIMEZONE));

    private Integer type;

    JDBCType(Integer num) {
        this.type = num;
    }

    @Override // java.sql.SQLType
    public String getName() {
        return name();
    }

    @Override // java.sql.SQLType
    public String getVendor() {
        return "java.sql";
    }

    @Override // java.sql.SQLType
    public Integer getVendorTypeNumber() {
        return this.type;
    }

    public static JDBCType valueOf(int i2) {
        for (JDBCType jDBCType : (JDBCType[]) JDBCType.class.getEnumConstants()) {
            if (i2 == jDBCType.type.intValue()) {
                return jDBCType;
            }
        }
        throw new IllegalArgumentException("Type:" + i2 + " is not a valid Types.java value.");
    }
}

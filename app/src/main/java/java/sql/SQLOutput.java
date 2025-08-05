package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;

/* loaded from: rt.jar:java/sql/SQLOutput.class */
public interface SQLOutput {
    void writeString(String str) throws SQLException;

    void writeBoolean(boolean z2) throws SQLException;

    void writeByte(byte b2) throws SQLException;

    void writeShort(short s2) throws SQLException;

    void writeInt(int i2) throws SQLException;

    void writeLong(long j2) throws SQLException;

    void writeFloat(float f2) throws SQLException;

    void writeDouble(double d2) throws SQLException;

    void writeBigDecimal(BigDecimal bigDecimal) throws SQLException;

    void writeBytes(byte[] bArr) throws SQLException;

    void writeDate(Date date) throws SQLException;

    void writeTime(Time time) throws SQLException;

    void writeTimestamp(Timestamp timestamp) throws SQLException;

    void writeCharacterStream(Reader reader) throws SQLException;

    void writeAsciiStream(InputStream inputStream) throws SQLException;

    void writeBinaryStream(InputStream inputStream) throws SQLException;

    void writeObject(SQLData sQLData) throws SQLException;

    void writeRef(Ref ref) throws SQLException;

    void writeBlob(Blob blob) throws SQLException;

    void writeClob(Clob clob) throws SQLException;

    void writeStruct(Struct struct) throws SQLException;

    void writeArray(Array array) throws SQLException;

    void writeURL(URL url) throws SQLException;

    void writeNString(String str) throws SQLException;

    void writeNClob(NClob nClob) throws SQLException;

    void writeRowId(RowId rowId) throws SQLException;

    void writeSQLXML(SQLXML sqlxml) throws SQLException;

    default void writeObject(Object obj, SQLType sQLType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}

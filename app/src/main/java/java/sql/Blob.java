package java.sql;

import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:java/sql/Blob.class */
public interface Blob {
    long length() throws SQLException;

    byte[] getBytes(long j2, int i2) throws SQLException;

    InputStream getBinaryStream() throws SQLException;

    long position(byte[] bArr, long j2) throws SQLException;

    long position(Blob blob, long j2) throws SQLException;

    int setBytes(long j2, byte[] bArr) throws SQLException;

    int setBytes(long j2, byte[] bArr, int i2, int i3) throws SQLException;

    OutputStream setBinaryStream(long j2) throws SQLException;

    void truncate(long j2) throws SQLException;

    void free() throws SQLException;

    InputStream getBinaryStream(long j2, long j3) throws SQLException;
}

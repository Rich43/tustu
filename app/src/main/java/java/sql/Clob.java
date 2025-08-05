package java.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/* loaded from: rt.jar:java/sql/Clob.class */
public interface Clob {
    long length() throws SQLException;

    String getSubString(long j2, int i2) throws SQLException;

    Reader getCharacterStream() throws SQLException;

    InputStream getAsciiStream() throws SQLException;

    long position(String str, long j2) throws SQLException;

    long position(Clob clob, long j2) throws SQLException;

    int setString(long j2, String str) throws SQLException;

    int setString(long j2, String str, int i2, int i3) throws SQLException;

    OutputStream setAsciiStream(long j2) throws SQLException;

    Writer setCharacterStream(long j2) throws SQLException;

    void truncate(long j2) throws SQLException;

    void free() throws SQLException;

    Reader getCharacterStream(long j2, long j3) throws SQLException;
}

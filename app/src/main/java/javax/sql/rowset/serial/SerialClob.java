package javax.sql.rowset.serial;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialClob.class */
public class SerialClob implements Clob, Serializable, Cloneable {
    private char[] buf;
    private Clob clob;
    private long len;
    private long origLen;
    static final long serialVersionUID = -1662519690087375313L;

    public SerialClob(char[] cArr) throws SQLException {
        this.len = cArr.length;
        this.buf = new char[(int) this.len];
        for (int i2 = 0; i2 < this.len; i2++) {
            this.buf[i2] = cArr[i2];
        }
        this.origLen = this.len;
        this.clob = null;
    }

    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x016c: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:76:0x016c */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0171: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:78:0x0171 */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.io.Reader] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    public SerialClob(Clob clob) throws SQLException {
        int i2;
        if (clob == null) {
            throw new SQLException("Cannot instantiate a SerialClob object with a null Clob object");
        }
        this.len = clob.length();
        this.clob = clob;
        this.buf = new char[(int) this.len];
        int i3 = 0;
        try {
            try {
                Reader characterStream = clob.getCharacterStream();
                Throwable th = null;
                if (characterStream == null) {
                    throw new SQLException("Invalid Clob object. The call to getCharacterStream returned null which cannot be serialized.");
                }
                InputStream asciiStream = clob.getAsciiStream();
                Throwable th2 = null;
                if (asciiStream == null) {
                    try {
                        try {
                            throw new SQLException("Invalid Clob object. The call to getAsciiStream returned null which cannot be serialized.");
                        } catch (Throwable th3) {
                            if (asciiStream != null) {
                                if (th2 != null) {
                                    try {
                                        asciiStream.close();
                                    } catch (Throwable th4) {
                                        th2.addSuppressed(th4);
                                    }
                                } else {
                                    asciiStream.close();
                                }
                            }
                            throw th3;
                        }
                    } finally {
                    }
                }
                if (asciiStream != null) {
                    if (0 != 0) {
                        try {
                            asciiStream.close();
                        } catch (Throwable th5) {
                            th2.addSuppressed(th5);
                        }
                    } else {
                        asciiStream.close();
                    }
                }
                BufferedReader bufferedReader = new BufferedReader(characterStream);
                Throwable th6 = null;
                do {
                    try {
                        try {
                            i2 = bufferedReader.read(this.buf, i3, (int) (this.len - i3));
                            i3 += i2;
                        } catch (Throwable th7) {
                            if (bufferedReader != null) {
                                if (th6 != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (Throwable th8) {
                                        th6.addSuppressed(th8);
                                    }
                                } else {
                                    bufferedReader.close();
                                }
                            }
                            throw th7;
                        }
                    } finally {
                    }
                } while (i2 > 0);
                if (bufferedReader != null) {
                    if (0 != 0) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th9) {
                            th6.addSuppressed(th9);
                        }
                    } else {
                        bufferedReader.close();
                    }
                }
                if (characterStream != null) {
                    if (0 != 0) {
                        try {
                            characterStream.close();
                        } catch (Throwable th10) {
                            th.addSuppressed(th10);
                        }
                    } else {
                        characterStream.close();
                    }
                }
                this.origLen = this.len;
            } finally {
            }
        } catch (IOException e2) {
            throw new SerialException("SerialClob: " + e2.getMessage());
        }
    }

    @Override // java.sql.Clob
    public long length() throws SerialException {
        isValid();
        return this.len;
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream() throws SerialException {
        isValid();
        return new CharArrayReader(this.buf);
    }

    @Override // java.sql.Clob
    public InputStream getAsciiStream() throws SQLException {
        isValid();
        if (this.clob != null) {
            return this.clob.getAsciiStream();
        }
        throw new SerialException("Unsupported operation. SerialClob cannot return a the CLOB value as an ascii stream, unless instantiated with a fully implemented Clob object.");
    }

    @Override // java.sql.Clob
    public String getSubString(long j2, int i2) throws SerialException {
        isValid();
        if (j2 < 1 || j2 > length()) {
            throw new SerialException("Invalid position in SerialClob object set");
        }
        if ((j2 - 1) + i2 > length()) {
            throw new SerialException("Invalid position and substring length");
        }
        try {
            return new String(this.buf, ((int) j2) - 1, i2);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new SerialException("StringIndexOutOfBoundsException: " + e2.getMessage());
        }
    }

    @Override // java.sql.Clob
    public long position(String str, long j2) throws SQLException {
        isValid();
        if (j2 < 1 || j2 > this.len) {
            return -1L;
        }
        char[] charArray = str.toCharArray();
        int i2 = ((int) j2) - 1;
        int i3 = 0;
        long length = charArray.length;
        while (i2 < this.len) {
            if (charArray[i3] == this.buf[i2]) {
                if (i3 + 1 == length) {
                    return (i2 + 1) - (length - 1);
                }
                i3++;
                i2++;
            } else if (charArray[i3] != this.buf[i2]) {
                i2++;
            }
        }
        return -1L;
    }

    @Override // java.sql.Clob
    public long position(Clob clob, long j2) throws SQLException {
        isValid();
        return position(clob.getSubString(1L, (int) clob.length()), j2);
    }

    @Override // java.sql.Clob
    public int setString(long j2, String str) throws SerialException {
        return setString(j2, str, 0, str.length());
    }

    @Override // java.sql.Clob
    public int setString(long j2, String str, int i2, int i3) throws SerialException {
        isValid();
        char[] charArray = str.substring(i2).toCharArray();
        if (i2 < 0 || i2 > str.length()) {
            throw new SerialException("Invalid offset in byte array set");
        }
        if (j2 < 1 || j2 > length()) {
            throw new SerialException("Invalid position in Clob object set");
        }
        if (i3 > this.origLen) {
            throw new SerialException("Buffer is not sufficient to hold the value");
        }
        if (i3 + i2 > str.length()) {
            throw new SerialException("Invalid OffSet. Cannot have combined offset  and length that is greater that the Blob buffer");
        }
        int i4 = 0;
        long j3 = j2 - 1;
        while (true) {
            if (i4 < i3 || i2 + i4 + 1 < str.length() - i2) {
                this.buf[((int) j3) + i4] = charArray[i2 + i4];
                i4++;
            } else {
                return i4;
            }
        }
    }

    @Override // java.sql.Clob
    public OutputStream setAsciiStream(long j2) throws SQLException {
        isValid();
        if (this.clob != null) {
            return this.clob.setAsciiStream(j2);
        }
        throw new SerialException("Unsupported operation. SerialClob cannot return a writable ascii stream\n unless instantiated with a Clob object that has a setAsciiStream() implementation");
    }

    @Override // java.sql.Clob
    public Writer setCharacterStream(long j2) throws SQLException {
        isValid();
        if (this.clob != null) {
            return this.clob.setCharacterStream(j2);
        }
        throw new SerialException("Unsupported operation. SerialClob cannot return a writable character stream\n unless instantiated with a Clob object that has a setCharacterStream implementation");
    }

    @Override // java.sql.Clob
    public void truncate(long j2) throws SerialException {
        isValid();
        if (j2 > this.len) {
            throw new SerialException("Length more than what can be truncated");
        }
        this.len = j2;
        if (this.len == 0) {
            this.buf = new char[0];
        } else {
            this.buf = getSubString(1L, (int) this.len).toCharArray();
        }
    }

    @Override // java.sql.Clob
    public Reader getCharacterStream(long j2, long j3) throws SQLException {
        isValid();
        if (j2 < 1 || j2 > this.len) {
            throw new SerialException("Invalid position in Clob object set");
        }
        if ((j2 - 1) + j3 > this.len) {
            throw new SerialException("Invalid position and substring length");
        }
        if (j3 <= 0) {
            throw new SerialException("Invalid length specified");
        }
        return new CharArrayReader(this.buf, (int) j2, (int) j3);
    }

    @Override // java.sql.Clob
    public void free() throws SQLException {
        if (this.buf != null) {
            this.buf = null;
            if (this.clob != null) {
                this.clob.free();
            }
            this.clob = null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialClob) {
            SerialClob serialClob = (SerialClob) obj;
            if (this.len == serialClob.len) {
                return Arrays.equals(this.buf, serialClob.buf);
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return ((((31 + Arrays.hashCode(this.buf)) * 31) + ((int) this.len)) * 31) + ((int) this.origLen);
    }

    public Object clone() {
        try {
            SerialClob serialClob = (SerialClob) super.clone();
            serialClob.buf = this.buf != null ? Arrays.copyOf(this.buf, (int) this.len) : null;
            serialClob.clob = null;
            return serialClob;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        char[] cArr = (char[]) fields.get("buf", (Object) null);
        if (cArr == null) {
            throw new InvalidObjectException("buf is null and should not be!");
        }
        this.buf = (char[]) cArr.clone();
        this.len = fields.get("len", 0L);
        if (this.buf.length != this.len) {
            throw new InvalidObjectException("buf is not the expected size");
        }
        this.origLen = fields.get("origLen", 0L);
        this.clob = (Clob) fields.get("clob", (Object) null);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("buf", this.buf);
        putFieldPutFields.put("len", this.len);
        putFieldPutFields.put("origLen", this.origLen);
        putFieldPutFields.put("clob", this.clob instanceof Serializable ? this.clob : null);
        objectOutputStream.writeFields();
    }

    private void isValid() throws SerialException {
        if (this.buf == null) {
            throw new SerialException("Error: You cannot call a method on a SerialClob instance once free() has been called.");
        }
    }
}

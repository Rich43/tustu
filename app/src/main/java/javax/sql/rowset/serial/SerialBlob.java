package javax.sql.rowset.serial;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialBlob.class */
public class SerialBlob implements Blob, Serializable, Cloneable {
    private byte[] buf;
    private Blob blob;
    private long len;
    private long origLen;
    static final long serialVersionUID = -8144641928112860441L;

    public SerialBlob(byte[] bArr) throws SQLException {
        this.len = bArr.length;
        this.buf = new byte[(int) this.len];
        for (int i2 = 0; i2 < this.len; i2++) {
            this.buf[i2] = bArr[i2];
        }
        this.origLen = this.len;
    }

    public SerialBlob(Blob blob) throws SQLException {
        if (blob == null) {
            throw new SQLException("Cannot instantiate a SerialBlob object with a null Blob object");
        }
        this.len = blob.length();
        this.buf = blob.getBytes(1L, (int) this.len);
        this.blob = blob;
        this.origLen = this.len;
    }

    @Override // java.sql.Blob
    public byte[] getBytes(long j2, int i2) throws SerialException {
        isValid();
        if (i2 > this.len) {
            i2 = (int) this.len;
        }
        if (j2 < 1 || this.len - j2 < 0) {
            throw new SerialException("Invalid arguments: position cannot be less than 1 or greater than the length of the SerialBlob");
        }
        long j3 = j2 - 1;
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr[i3] = this.buf[(int) j3];
            j3++;
        }
        return bArr;
    }

    @Override // java.sql.Blob
    public long length() throws SerialException {
        isValid();
        return this.len;
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream() throws SerialException {
        isValid();
        return new ByteArrayInputStream(this.buf);
    }

    @Override // java.sql.Blob
    public long position(byte[] bArr, long j2) throws SQLException {
        isValid();
        if (j2 < 1 || j2 > this.len) {
            return -1L;
        }
        int i2 = ((int) j2) - 1;
        int i3 = 0;
        long length = bArr.length;
        while (i2 < this.len) {
            if (bArr[i3] == this.buf[i2]) {
                if (i3 + 1 == length) {
                    return (i2 + 1) - (length - 1);
                }
                i3++;
                i2++;
            } else if (bArr[i3] != this.buf[i2]) {
                i2++;
            }
        }
        return -1L;
    }

    @Override // java.sql.Blob
    public long position(Blob blob, long j2) throws SQLException {
        isValid();
        return position(blob.getBytes(1L, (int) blob.length()), j2);
    }

    @Override // java.sql.Blob
    public int setBytes(long j2, byte[] bArr) throws SQLException {
        return setBytes(j2, bArr, 0, bArr.length);
    }

    @Override // java.sql.Blob
    public int setBytes(long j2, byte[] bArr, int i2, int i3) throws SQLException {
        isValid();
        if (i2 < 0 || i2 > bArr.length) {
            throw new SerialException("Invalid offset in byte array set");
        }
        if (j2 < 1 || j2 > length()) {
            throw new SerialException("Invalid position in BLOB object set");
        }
        if (i3 > this.origLen) {
            throw new SerialException("Buffer is not sufficient to hold the value");
        }
        if (i3 + i2 > bArr.length) {
            throw new SerialException("Invalid OffSet. Cannot have combined offset and length that is greater that the Blob buffer");
        }
        int i4 = 0;
        long j3 = j2 - 1;
        while (true) {
            if (i4 < i3 || i2 + i4 + 1 < bArr.length - i2) {
                this.buf[((int) j3) + i4] = bArr[i2 + i4];
                i4++;
            } else {
                return i4;
            }
        }
    }

    @Override // java.sql.Blob
    public OutputStream setBinaryStream(long j2) throws SQLException {
        isValid();
        if (this.blob != null) {
            return this.blob.setBinaryStream(j2);
        }
        throw new SerialException("Unsupported operation. SerialBlob cannot return a writable binary stream, unless instantiated with a Blob object that provides a setBinaryStream() implementation");
    }

    @Override // java.sql.Blob
    public void truncate(long j2) throws SerialException {
        isValid();
        if (j2 > this.len) {
            throw new SerialException("Length more than what can be truncated");
        }
        if (((int) j2) == 0) {
            this.buf = new byte[0];
            this.len = j2;
        } else {
            this.len = j2;
            this.buf = getBytes(1L, (int) this.len);
        }
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream(long j2, long j3) throws SQLException {
        isValid();
        if (j2 < 1 || j2 > length()) {
            throw new SerialException("Invalid position in BLOB object set");
        }
        if (j3 < 1 || j3 > (this.len - j2) + 1) {
            throw new SerialException("length is < 1 or pos + length > total number of bytes");
        }
        return new ByteArrayInputStream(this.buf, ((int) j2) - 1, (int) j3);
    }

    @Override // java.sql.Blob
    public void free() throws SQLException {
        if (this.buf != null) {
            this.buf = null;
            if (this.blob != null) {
                this.blob.free();
            }
            this.blob = null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialBlob) {
            SerialBlob serialBlob = (SerialBlob) obj;
            if (this.len == serialBlob.len) {
                return Arrays.equals(this.buf, serialBlob.buf);
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
            SerialBlob serialBlob = (SerialBlob) super.clone();
            serialBlob.buf = this.buf != null ? Arrays.copyOf(this.buf, (int) this.len) : null;
            serialBlob.blob = null;
            return serialBlob;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        byte[] bArr = (byte[]) fields.get("buf", (Object) null);
        if (bArr == null) {
            throw new InvalidObjectException("buf is null and should not be!");
        }
        this.buf = (byte[]) bArr.clone();
        this.len = fields.get("len", 0L);
        if (this.buf.length != this.len) {
            throw new InvalidObjectException("buf is not the expected size");
        }
        this.origLen = fields.get("origLen", 0L);
        this.blob = (Blob) fields.get("blob", (Object) null);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("buf", this.buf);
        putFieldPutFields.put("len", this.len);
        putFieldPutFields.put("origLen", this.origLen);
        putFieldPutFields.put("blob", this.blob instanceof Serializable ? this.blob : null);
        objectOutputStream.writeFields();
    }

    private void isValid() throws SerialException {
        if (this.buf == null) {
            throw new SerialException("Error: You cannot call a method on a SerialBlob instance once free() has been called.");
        }
    }
}

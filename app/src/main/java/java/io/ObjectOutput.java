package java.io;

/* loaded from: rt.jar:java/io/ObjectOutput.class */
public interface ObjectOutput extends DataOutput, AutoCloseable {
    void writeObject(Object obj) throws IOException;

    @Override // java.io.DataOutput
    void write(int i2) throws IOException;

    @Override // java.io.DataOutput
    void write(byte[] bArr) throws IOException;

    @Override // java.io.DataOutput
    void write(byte[] bArr, int i2, int i3) throws IOException;

    void flush() throws IOException;

    @Override // java.lang.AutoCloseable
    void close() throws IOException;
}

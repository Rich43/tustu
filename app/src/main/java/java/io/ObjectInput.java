package java.io;

/* loaded from: rt.jar:java/io/ObjectInput.class */
public interface ObjectInput extends DataInput, AutoCloseable {
    Object readObject() throws ClassNotFoundException, IOException;

    int read() throws IOException;

    int read(byte[] bArr) throws IOException;

    int read(byte[] bArr, int i2, int i3) throws IOException;

    long skip(long j2) throws IOException;

    int available() throws IOException;

    @Override // java.lang.AutoCloseable
    void close() throws IOException;
}

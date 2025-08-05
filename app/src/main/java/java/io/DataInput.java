package java.io;

/* loaded from: rt.jar:java/io/DataInput.class */
public interface DataInput {
    void readFully(byte[] bArr) throws IOException;

    void readFully(byte[] bArr, int i2, int i3) throws IOException;

    int skipBytes(int i2) throws IOException;

    boolean readBoolean() throws IOException;

    byte readByte() throws IOException;

    int readUnsignedByte() throws IOException;

    short readShort() throws IOException;

    int readUnsignedShort() throws IOException;

    char readChar() throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    float readFloat() throws IOException;

    double readDouble() throws IOException;

    String readLine() throws IOException;

    String readUTF() throws IOException;
}

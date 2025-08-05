package java.io;

/* loaded from: rt.jar:java/io/DataOutput.class */
public interface DataOutput {
    void write(int i2) throws IOException;

    void write(byte[] bArr) throws IOException;

    void write(byte[] bArr, int i2, int i3) throws IOException;

    void writeBoolean(boolean z2) throws IOException;

    void writeByte(int i2) throws IOException;

    void writeShort(int i2) throws IOException;

    void writeChar(int i2) throws IOException;

    void writeInt(int i2) throws IOException;

    void writeLong(long j2) throws IOException;

    void writeFloat(float f2) throws IOException;

    void writeDouble(double d2) throws IOException;

    void writeBytes(String str) throws IOException;

    void writeChars(String str) throws IOException;

    void writeUTF(String str) throws IOException;
}

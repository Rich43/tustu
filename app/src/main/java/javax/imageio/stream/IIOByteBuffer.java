package javax.imageio.stream;

/* loaded from: rt.jar:javax/imageio/stream/IIOByteBuffer.class */
public class IIOByteBuffer {
    private byte[] data;
    private int offset;
    private int length;

    public IIOByteBuffer(byte[] bArr, int i2, int i3) {
        this.data = bArr;
        this.offset = i2;
        this.length = i3;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i2) {
        this.offset = i2;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int i2) {
        this.length = i2;
    }
}

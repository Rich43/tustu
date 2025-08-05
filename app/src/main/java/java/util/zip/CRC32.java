package java.util.zip;

import java.nio.ByteBuffer;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/util/zip/CRC32.class */
public class CRC32 implements Checksum {
    private int crc;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native int update(int i2, int i3);

    private static native int updateBytes(int i2, byte[] bArr, int i3, int i4);

    private static native int updateByteBuffer(int i2, long j2, int i3, int i4);

    static {
        $assertionsDisabled = !CRC32.class.desiredAssertionStatus();
    }

    @Override // java.util.zip.Checksum
    public void update(int i2) {
        this.crc = update(this.crc, i2);
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.crc = updateBytes(this.crc, bArr, i2, i3);
    }

    public void update(byte[] bArr) {
        this.crc = updateBytes(this.crc, bArr, 0, bArr.length);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void update(ByteBuffer byteBuffer) {
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iLimit - iPosition;
        if (i2 <= 0) {
            return;
        }
        if (byteBuffer instanceof DirectBuffer) {
            this.crc = updateByteBuffer(this.crc, ((DirectBuffer) byteBuffer).address(), iPosition, i2);
        } else if (byteBuffer.hasArray()) {
            this.crc = updateBytes(this.crc, byteBuffer.array(), iPosition + byteBuffer.arrayOffset(), i2);
        } else {
            byte[] bArr = new byte[i2];
            byteBuffer.get(bArr);
            this.crc = updateBytes(this.crc, bArr, 0, bArr.length);
        }
        byteBuffer.position(iLimit);
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        this.crc = 0;
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        return this.crc & 4294967295L;
    }
}

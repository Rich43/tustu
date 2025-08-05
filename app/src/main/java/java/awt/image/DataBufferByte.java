package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferByte.class */
public final class DataBufferByte extends DataBuffer {
    byte[] data;
    byte[][] bankdata;

    /* JADX WARN: Type inference failed for: r1v4, types: [byte[], byte[][]] */
    public DataBufferByte(int i2) {
        super(StateTrackable.State.STABLE, 0, i2);
        this.data = new byte[i2];
        this.bankdata = new byte[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [byte[], byte[][]] */
    public DataBufferByte(int i2, int i3) {
        super(StateTrackable.State.STABLE, 0, i2, i3);
        this.bankdata = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new byte[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
    public DataBufferByte(byte[] bArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 0, i2);
        this.data = bArr;
        this.bankdata = new byte[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
    public DataBufferByte(byte[] bArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 0, i2, 1, i3);
        this.data = bArr;
        this.bankdata = new byte[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferByte(byte[][] bArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 0, i2, bArr.length);
        this.bankdata = (byte[][]) bArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferByte(byte[][] bArr, int i2, int[] iArr) {
        super(StateTrackable.State.UNTRACKABLE, 0, i2, bArr.length, iArr);
        this.bankdata = (byte[][]) bArr.clone();
        this.data = this.bankdata[0];
    }

    public byte[] getData() {
        this.theTrackable.setUntrackable();
        return this.data;
    }

    public byte[] getData(int i2) {
        this.theTrackable.setUntrackable();
        return this.bankdata[i2];
    }

    public byte[][] getBankData() {
        this.theTrackable.setUntrackable();
        return (byte[][]) this.bankdata.clone();
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2) {
        return this.data[i2 + this.offset] & 255;
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2, int i3) {
        return this.bankdata[i2][i3 + this.offsets[i2]] & 255;
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3) {
        this.data[i2 + this.offset] = (byte) i3;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3, int i4) {
        this.bankdata[i2][i3 + this.offsets[i2]] = (byte) i4;
        this.theTrackable.markDirty();
    }
}

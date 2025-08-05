package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferShort.class */
public final class DataBufferShort extends DataBuffer {
    short[] data;
    short[][] bankdata;

    /* JADX WARN: Type inference failed for: r1v4, types: [short[], short[][]] */
    public DataBufferShort(int i2) {
        super(StateTrackable.State.STABLE, 2, i2);
        this.data = new short[i2];
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [short[], short[][]] */
    public DataBufferShort(int i2, int i3) {
        super(StateTrackable.State.STABLE, 2, i2, i3);
        this.bankdata = new short[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new short[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [short[], short[][]] */
    public DataBufferShort(short[] sArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 2, i2);
        this.data = sArr;
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [short[], short[][]] */
    public DataBufferShort(short[] sArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 2, i2, 1, i3);
        this.data = sArr;
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferShort(short[][] sArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 2, i2, sArr.length);
        this.bankdata = (short[][]) sArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferShort(short[][] sArr, int i2, int[] iArr) {
        super(StateTrackable.State.UNTRACKABLE, 2, i2, sArr.length, iArr);
        this.bankdata = (short[][]) sArr.clone();
        this.data = this.bankdata[0];
    }

    public short[] getData() {
        this.theTrackable.setUntrackable();
        return this.data;
    }

    public short[] getData(int i2) {
        this.theTrackable.setUntrackable();
        return this.bankdata[i2];
    }

    public short[][] getBankData() {
        this.theTrackable.setUntrackable();
        return (short[][]) this.bankdata.clone();
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2) {
        return this.data[i2 + this.offset];
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2, int i3) {
        return this.bankdata[i2][i3 + this.offsets[i2]];
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3) {
        this.data[i2 + this.offset] = (short) i3;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3, int i4) {
        this.bankdata[i2][i3 + this.offsets[i2]] = (short) i4;
        this.theTrackable.markDirty();
    }
}

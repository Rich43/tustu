package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferUShort.class */
public final class DataBufferUShort extends DataBuffer {
    short[] data;
    short[][] bankdata;

    /* JADX WARN: Type inference failed for: r1v4, types: [short[], short[][]] */
    public DataBufferUShort(int i2) {
        super(StateTrackable.State.STABLE, 1, i2);
        this.data = new short[i2];
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [short[], short[][]] */
    public DataBufferUShort(int i2, int i3) {
        super(StateTrackable.State.STABLE, 1, i2, i3);
        this.bankdata = new short[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new short[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [short[], short[][]] */
    public DataBufferUShort(short[] sArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 1, i2);
        if (sArr == null) {
            throw new NullPointerException("dataArray is null");
        }
        this.data = sArr;
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [short[], short[][]] */
    public DataBufferUShort(short[] sArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 1, i2, 1, i3);
        if (sArr == null) {
            throw new NullPointerException("dataArray is null");
        }
        if (i2 + i3 > sArr.length) {
            throw new IllegalArgumentException("Length of dataArray is less  than size+offset.");
        }
        this.data = sArr;
        this.bankdata = new short[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferUShort(short[][] sArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 1, i2, sArr.length);
        if (sArr == null) {
            throw new NullPointerException("dataArray is null");
        }
        for (int i3 = 0; i3 < sArr.length; i3++) {
            if (sArr[i3] == null) {
                throw new NullPointerException("dataArray[" + i3 + "] is null");
            }
        }
        this.bankdata = (short[][]) sArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferUShort(short[][] sArr, int i2, int[] iArr) {
        super(StateTrackable.State.UNTRACKABLE, 1, i2, sArr.length, iArr);
        if (sArr == null) {
            throw new NullPointerException("dataArray is null");
        }
        for (int i3 = 0; i3 < sArr.length; i3++) {
            if (sArr[i3] == null) {
                throw new NullPointerException("dataArray[" + i3 + "] is null");
            }
            if (i2 + iArr[i3] > sArr[i3].length) {
                throw new IllegalArgumentException("Length of dataArray[" + i3 + "] is less than size+offsets[" + i3 + "].");
            }
        }
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
        return this.data[i2 + this.offset] & 65535;
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2, int i3) {
        return this.bankdata[i2][i3 + this.offsets[i2]] & 65535;
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3) {
        this.data[i2 + this.offset] = (short) (i3 & 65535);
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3, int i4) {
        this.bankdata[i2][i3 + this.offsets[i2]] = (short) (i4 & 65535);
        this.theTrackable.markDirty();
    }
}

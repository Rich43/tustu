package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferInt.class */
public final class DataBufferInt extends DataBuffer {
    int[] data;
    int[][] bankdata;

    /* JADX WARN: Type inference failed for: r1v4, types: [int[], int[][]] */
    public DataBufferInt(int i2) {
        super(StateTrackable.State.STABLE, 3, i2);
        this.data = new int[i2];
        this.bankdata = new int[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [int[], int[][]] */
    public DataBufferInt(int i2, int i3) {
        super(StateTrackable.State.STABLE, 3, i2, i3);
        this.bankdata = new int[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new int[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [int[], int[][]] */
    public DataBufferInt(int[] iArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 3, i2);
        this.data = iArr;
        this.bankdata = new int[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [int[], int[][]] */
    public DataBufferInt(int[] iArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 3, i2, 1, i3);
        this.data = iArr;
        this.bankdata = new int[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferInt(int[][] iArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 3, i2, iArr.length);
        this.bankdata = (int[][]) iArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferInt(int[][] iArr, int i2, int[] iArr2) {
        super(StateTrackable.State.UNTRACKABLE, 3, i2, iArr.length, iArr2);
        this.bankdata = (int[][]) iArr.clone();
        this.data = this.bankdata[0];
    }

    public int[] getData() {
        this.theTrackable.setUntrackable();
        return this.data;
    }

    public int[] getData(int i2) {
        this.theTrackable.setUntrackable();
        return this.bankdata[i2];
    }

    public int[][] getBankData() {
        this.theTrackable.setUntrackable();
        return (int[][]) this.bankdata.clone();
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
        this.data[i2 + this.offset] = i3;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3, int i4) {
        this.bankdata[i2][i3 + this.offsets[i2]] = i4;
        this.theTrackable.markDirty();
    }
}

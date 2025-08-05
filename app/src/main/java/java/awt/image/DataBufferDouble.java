package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferDouble.class */
public final class DataBufferDouble extends DataBuffer {
    double[][] bankdata;
    double[] data;

    /* JADX WARN: Type inference failed for: r1v4, types: [double[], double[][]] */
    public DataBufferDouble(int i2) {
        super(StateTrackable.State.STABLE, 5, i2);
        this.data = new double[i2];
        this.bankdata = new double[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [double[], double[][]] */
    public DataBufferDouble(int i2, int i3) {
        super(StateTrackable.State.STABLE, 5, i2, i3);
        this.bankdata = new double[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new double[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [double[], double[][]] */
    public DataBufferDouble(double[] dArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 5, i2);
        this.data = dArr;
        this.bankdata = new double[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [double[], double[][]] */
    public DataBufferDouble(double[] dArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 5, i2, 1, i3);
        this.data = dArr;
        this.bankdata = new double[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferDouble(double[][] dArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 5, i2, dArr.length);
        this.bankdata = (double[][]) dArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferDouble(double[][] dArr, int i2, int[] iArr) {
        super(StateTrackable.State.UNTRACKABLE, 5, i2, dArr.length, iArr);
        this.bankdata = (double[][]) dArr.clone();
        this.data = this.bankdata[0];
    }

    public double[] getData() {
        this.theTrackable.setUntrackable();
        return this.data;
    }

    public double[] getData(int i2) {
        this.theTrackable.setUntrackable();
        return this.bankdata[i2];
    }

    public double[][] getBankData() {
        this.theTrackable.setUntrackable();
        return (double[][]) this.bankdata.clone();
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2) {
        return (int) this.data[i2 + this.offset];
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2, int i3) {
        return (int) this.bankdata[i2][i3 + this.offsets[i2]];
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

    @Override // java.awt.image.DataBuffer
    public float getElemFloat(int i2) {
        return (float) this.data[i2 + this.offset];
    }

    @Override // java.awt.image.DataBuffer
    public float getElemFloat(int i2, int i3) {
        return (float) this.bankdata[i2][i3 + this.offsets[i2]];
    }

    @Override // java.awt.image.DataBuffer
    public void setElemFloat(int i2, float f2) {
        this.data[i2 + this.offset] = f2;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElemFloat(int i2, int i3, float f2) {
        this.bankdata[i2][i3 + this.offsets[i2]] = f2;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public double getElemDouble(int i2) {
        return this.data[i2 + this.offset];
    }

    @Override // java.awt.image.DataBuffer
    public double getElemDouble(int i2, int i3) {
        return this.bankdata[i2][i3 + this.offsets[i2]];
    }

    @Override // java.awt.image.DataBuffer
    public void setElemDouble(int i2, double d2) {
        this.data[i2 + this.offset] = d2;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElemDouble(int i2, int i3, double d2) {
        this.bankdata[i2][i3 + this.offsets[i2]] = d2;
        this.theTrackable.markDirty();
    }
}

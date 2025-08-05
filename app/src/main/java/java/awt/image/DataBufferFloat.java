package java.awt.image;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:java/awt/image/DataBufferFloat.class */
public final class DataBufferFloat extends DataBuffer {
    float[][] bankdata;
    float[] data;

    /* JADX WARN: Type inference failed for: r1v4, types: [float[], float[][]] */
    public DataBufferFloat(int i2) {
        super(StateTrackable.State.STABLE, 4, i2);
        this.data = new float[i2];
        this.bankdata = new float[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [float[], float[][]] */
    public DataBufferFloat(int i2, int i3) {
        super(StateTrackable.State.STABLE, 4, i2, i3);
        this.bankdata = new float[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.bankdata[i4] = new float[i2];
        }
        this.data = this.bankdata[0];
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [float[], float[][]] */
    public DataBufferFloat(float[] fArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 4, i2);
        this.data = fArr;
        this.bankdata = new float[1];
        this.bankdata[0] = this.data;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [float[], float[][]] */
    public DataBufferFloat(float[] fArr, int i2, int i3) {
        super(StateTrackable.State.UNTRACKABLE, 4, i2, 1, i3);
        this.data = fArr;
        this.bankdata = new float[1];
        this.bankdata[0] = this.data;
    }

    public DataBufferFloat(float[][] fArr, int i2) {
        super(StateTrackable.State.UNTRACKABLE, 4, i2, fArr.length);
        this.bankdata = (float[][]) fArr.clone();
        this.data = this.bankdata[0];
    }

    public DataBufferFloat(float[][] fArr, int i2, int[] iArr) {
        super(StateTrackable.State.UNTRACKABLE, 4, i2, fArr.length, iArr);
        this.bankdata = (float[][]) fArr.clone();
        this.data = this.bankdata[0];
    }

    public float[] getData() {
        this.theTrackable.setUntrackable();
        return this.data;
    }

    public float[] getData(int i2) {
        this.theTrackable.setUntrackable();
        return this.bankdata[i2];
    }

    public float[][] getBankData() {
        this.theTrackable.setUntrackable();
        return (float[][]) this.bankdata.clone();
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
        return this.data[i2 + this.offset];
    }

    @Override // java.awt.image.DataBuffer
    public float getElemFloat(int i2, int i3) {
        return this.bankdata[i2][i3 + this.offsets[i2]];
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
        this.data[i2 + this.offset] = (float) d2;
        this.theTrackable.markDirty();
    }

    @Override // java.awt.image.DataBuffer
    public void setElemDouble(int i2, int i3, double d2) {
        this.bankdata[i2][i3 + this.offsets[i2]] = (float) d2;
        this.theTrackable.markDirty();
    }
}

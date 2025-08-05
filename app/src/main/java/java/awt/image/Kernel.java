package java.awt.image;

/* loaded from: rt.jar:java/awt/image/Kernel.class */
public class Kernel implements Cloneable {
    private int width;
    private int height;
    private int xOrigin;
    private int yOrigin;
    private float[] data;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public Kernel(int i2, int i3, float[] fArr) {
        this.width = i2;
        this.height = i3;
        this.xOrigin = (i2 - 1) >> 1;
        this.yOrigin = (i3 - 1) >> 1;
        int i4 = i2 * i3;
        if (fArr.length < i4) {
            throw new IllegalArgumentException("Data array too small (is " + fArr.length + " and should be " + i4);
        }
        this.data = new float[i4];
        System.arraycopy(fArr, 0, this.data, 0, i4);
    }

    public final int getXOrigin() {
        return this.xOrigin;
    }

    public final int getYOrigin() {
        return this.yOrigin;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final float[] getKernelData(float[] fArr) {
        if (fArr == null) {
            fArr = new float[this.data.length];
        } else if (fArr.length < this.data.length) {
            throw new IllegalArgumentException("Data array too small (should be " + this.data.length + " but is " + fArr.length + " )");
        }
        System.arraycopy(this.data, 0, fArr, 0, this.data.length);
        return fArr;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}

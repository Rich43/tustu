package java.awt.image;

/* loaded from: rt.jar:java/awt/image/LookupTable.class */
public abstract class LookupTable {
    int numComponents;
    int offset;
    int numEntries;

    public abstract int[] lookupPixel(int[] iArr, int[] iArr2);

    protected LookupTable(int i2, int i3) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Offset must be greater than 0");
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("Number of components must  be at least 1");
        }
        this.numComponents = i3;
        this.offset = i2;
    }

    public int getNumComponents() {
        return this.numComponents;
    }

    public int getOffset() {
        return this.offset;
    }
}

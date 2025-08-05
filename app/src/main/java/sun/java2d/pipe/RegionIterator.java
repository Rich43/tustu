package sun.java2d.pipe;

/* loaded from: rt.jar:sun/java2d/pipe/RegionIterator.class */
public class RegionIterator {
    Region region;
    int curIndex;
    int numXbands;

    RegionIterator(Region region) {
        this.region = region;
    }

    public RegionIterator createCopy() {
        RegionIterator regionIterator = new RegionIterator(this.region);
        regionIterator.curIndex = this.curIndex;
        regionIterator.numXbands = this.numXbands;
        return regionIterator;
    }

    public void copyStateFrom(RegionIterator regionIterator) {
        if (this.region != regionIterator.region) {
            throw new InternalError("region mismatch");
        }
        this.curIndex = regionIterator.curIndex;
        this.numXbands = regionIterator.numXbands;
    }

    public boolean nextYRange(int[] iArr) {
        this.curIndex += this.numXbands * 2;
        this.numXbands = 0;
        if (this.curIndex >= this.region.endIndex) {
            return false;
        }
        int[] iArr2 = this.region.bands;
        int i2 = this.curIndex;
        this.curIndex = i2 + 1;
        iArr[1] = iArr2[i2];
        int[] iArr3 = this.region.bands;
        int i3 = this.curIndex;
        this.curIndex = i3 + 1;
        iArr[3] = iArr3[i3];
        int[] iArr4 = this.region.bands;
        int i4 = this.curIndex;
        this.curIndex = i4 + 1;
        this.numXbands = iArr4[i4];
        return true;
    }

    public boolean nextXBand(int[] iArr) {
        if (this.numXbands <= 0) {
            return false;
        }
        this.numXbands--;
        int[] iArr2 = this.region.bands;
        int i2 = this.curIndex;
        this.curIndex = i2 + 1;
        iArr[0] = iArr2[i2];
        int[] iArr3 = this.region.bands;
        int i3 = this.curIndex;
        this.curIndex = i3 + 1;
        iArr[2] = iArr3[i3];
        return true;
    }
}

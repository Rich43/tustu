package java.awt.image;

/* loaded from: rt.jar:java/awt/image/PixelInterleavedSampleModel.class */
public class PixelInterleavedSampleModel extends ComponentSampleModel {
    public PixelInterleavedSampleModel(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        super(i2, i3, i4, i5, i6, iArr);
        int iMin = this.bandOffsets[0];
        int iMax = this.bandOffsets[0];
        for (int i7 = 1; i7 < this.bandOffsets.length; i7++) {
            iMin = Math.min(iMin, this.bandOffsets[i7]);
            iMax = Math.max(iMax, this.bandOffsets[i7]);
        }
        int i8 = iMax - iMin;
        if (i8 > i6) {
            throw new IllegalArgumentException("Offsets between bands must be less than the scanline  stride");
        }
        if (i5 * i3 > i6) {
            throw new IllegalArgumentException("Pixel stride times width must be less than or equal to the scanline stride");
        }
        if (i5 < i8) {
            throw new IllegalArgumentException("Pixel stride must be greater than or equal to the offsets between bands");
        }
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        int[] iArr;
        int i4 = this.bandOffsets[0];
        int length = this.bandOffsets.length;
        for (int i5 = 1; i5 < length; i5++) {
            if (this.bandOffsets[i5] < i4) {
                i4 = this.bandOffsets[i5];
            }
        }
        if (i4 > 0) {
            iArr = new int[length];
            for (int i6 = 0; i6 < length; i6++) {
                iArr[i6] = this.bandOffsets[i6] - i4;
            }
        } else {
            iArr = this.bandOffsets;
        }
        return new PixelInterleavedSampleModel(this.dataType, i2, i3, this.pixelStride, this.pixelStride * i2, iArr);
    }

    @Override // java.awt.image.ComponentSampleModel, java.awt.image.SampleModel
    public SampleModel createSubsetSampleModel(int[] iArr) {
        int[] iArr2 = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr2[i2] = this.bandOffsets[iArr[i2]];
        }
        return new PixelInterleavedSampleModel(this.dataType, this.width, this.height, this.pixelStride, this.scanlineStride, iArr2);
    }

    @Override // java.awt.image.ComponentSampleModel
    public int hashCode() {
        return super.hashCode() ^ 1;
    }
}

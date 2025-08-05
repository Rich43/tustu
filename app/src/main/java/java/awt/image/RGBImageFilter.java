package java.awt.image;

/* loaded from: rt.jar:java/awt/image/RGBImageFilter.class */
public abstract class RGBImageFilter extends ImageFilter {
    protected ColorModel origmodel;
    protected ColorModel newmodel;
    protected boolean canFilterIndexColorModel;

    public abstract int filterRGB(int i2, int i3, int i4);

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setColorModel(ColorModel colorModel) {
        if (this.canFilterIndexColorModel && (colorModel instanceof IndexColorModel)) {
            IndexColorModel indexColorModelFilterIndexColorModel = filterIndexColorModel((IndexColorModel) colorModel);
            substituteColorModel(colorModel, indexColorModelFilterIndexColorModel);
            this.consumer.setColorModel(indexColorModelFilterIndexColorModel);
            return;
        }
        this.consumer.setColorModel(ColorModel.getRGBdefault());
    }

    public void substituteColorModel(ColorModel colorModel, ColorModel colorModel2) {
        this.origmodel = colorModel;
        this.newmodel = colorModel2;
    }

    public IndexColorModel filterIndexColorModel(IndexColorModel indexColorModel) {
        int mapSize = indexColorModel.getMapSize();
        byte[] bArr = new byte[mapSize];
        byte[] bArr2 = new byte[mapSize];
        byte[] bArr3 = new byte[mapSize];
        byte[] bArr4 = new byte[mapSize];
        indexColorModel.getReds(bArr);
        indexColorModel.getGreens(bArr2);
        indexColorModel.getBlues(bArr3);
        indexColorModel.getAlphas(bArr4);
        int transparentPixel = indexColorModel.getTransparentPixel();
        boolean z2 = false;
        for (int i2 = 0; i2 < mapSize; i2++) {
            int iFilterRGB = filterRGB(-1, -1, indexColorModel.getRGB(i2));
            bArr4[i2] = (byte) (iFilterRGB >> 24);
            if (bArr4[i2] != -1 && i2 != transparentPixel) {
                z2 = true;
            }
            bArr[i2] = (byte) (iFilterRGB >> 16);
            bArr2[i2] = (byte) (iFilterRGB >> 8);
            bArr3[i2] = (byte) (iFilterRGB >> 0);
        }
        if (z2) {
            return new IndexColorModel(indexColorModel.getPixelSize(), mapSize, bArr, bArr2, bArr3, bArr4);
        }
        return new IndexColorModel(indexColorModel.getPixelSize(), mapSize, bArr, bArr2, bArr3, transparentPixel);
    }

    public void filterRGBPixels(int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        int i8 = i6;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < i4; i10++) {
                iArr[i8] = filterRGB(i2 + i10, i3 + i9, iArr[i8]);
                i8++;
            }
            i8 += i7 - i4;
        }
        this.consumer.setPixels(i2, i3, i4, i5, ColorModel.getRGBdefault(), iArr, i6, i7);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        if (colorModel == this.origmodel) {
            this.consumer.setPixels(i2, i3, i4, i5, this.newmodel, bArr, i6, i7);
            return;
        }
        int[] iArr = new int[i4];
        int i8 = i6;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < i4; i10++) {
                iArr[i10] = colorModel.getRGB(bArr[i8] & 255);
                i8++;
            }
            i8 += i7 - i4;
            filterRGBPixels(i2, i3 + i9, i4, 1, iArr, 0, i4);
        }
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        if (colorModel == this.origmodel) {
            this.consumer.setPixels(i2, i3, i4, i5, this.newmodel, iArr, i6, i7);
            return;
        }
        int[] iArr2 = new int[i4];
        int i8 = i6;
        for (int i9 = 0; i9 < i5; i9++) {
            for (int i10 = 0; i10 < i4; i10++) {
                iArr2[i10] = colorModel.getRGB(iArr[i8]);
                i8++;
            }
            i8 += i7 - i4;
            filterRGBPixels(i2, i3 + i9, i4, 1, iArr2, 0, i4);
        }
    }
}

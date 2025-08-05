package java.awt.image;

import java.awt.Point;

/* loaded from: rt.jar:java/awt/image/BufferedImageFilter.class */
public class BufferedImageFilter extends ImageFilter implements Cloneable {
    BufferedImageOp bufferedImageOp;
    ColorModel model;
    int width;
    int height;
    byte[] bytePixels;
    int[] intPixels;

    public BufferedImageFilter(BufferedImageOp bufferedImageOp) {
        if (bufferedImageOp == null) {
            throw new NullPointerException("Operation cannot be null");
        }
        this.bufferedImageOp = bufferedImageOp;
    }

    public BufferedImageOp getBufferedImageOp() {
        return this.bufferedImageOp;
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            imageComplete(3);
        } else {
            this.width = i2;
            this.height = i3;
        }
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setColorModel(ColorModel colorModel) {
        this.model = colorModel;
    }

    private void convertToRGB() {
        int i2 = this.width * this.height;
        int[] iArr = new int[i2];
        if (this.bytePixels != null) {
            for (int i3 = 0; i3 < i2; i3++) {
                iArr[i3] = this.model.getRGB(this.bytePixels[i3] & 255);
            }
        } else if (this.intPixels != null) {
            for (int i4 = 0; i4 < i2; i4++) {
                iArr[i4] = this.model.getRGB(this.intPixels[i4]);
            }
        }
        this.bytePixels = null;
        this.intPixels = iArr;
        this.model = ColorModel.getRGBdefault();
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        if (i4 < 0 || i5 < 0) {
            throw new IllegalArgumentException("Width (" + i4 + ") and height (" + i5 + ") must be > 0");
        }
        if (i4 == 0 || i5 == 0) {
            return;
        }
        if (i3 < 0) {
            int i8 = -i3;
            if (i8 >= i5) {
                return;
            }
            i6 += i7 * i8;
            i3 += i8;
            i5 -= i8;
        }
        if (i3 + i5 > this.height) {
            i5 = this.height - i3;
            if (i5 <= 0) {
                return;
            }
        }
        if (i2 < 0) {
            int i9 = -i2;
            if (i9 >= i4) {
                return;
            }
            i6 += i9;
            i2 += i9;
            i4 -= i9;
        }
        if (i2 + i4 > this.width) {
            i4 = this.width - i2;
            if (i4 <= 0) {
                return;
            }
        }
        int i10 = (i3 * this.width) + i2;
        if (this.intPixels == null) {
            if (this.bytePixels == null) {
                this.bytePixels = new byte[this.width * this.height];
                this.model = colorModel;
            } else if (this.model != colorModel) {
                convertToRGB();
            }
            if (this.bytePixels != null) {
                for (int i11 = i5; i11 > 0; i11--) {
                    System.arraycopy(bArr, i6, this.bytePixels, i10, i4);
                    i6 += i7;
                    i10 += this.width;
                }
            }
        }
        if (this.intPixels != null) {
            int i12 = this.width - i4;
            int i13 = i7 - i4;
            for (int i14 = i5; i14 > 0; i14--) {
                for (int i15 = i4; i15 > 0; i15--) {
                    int i16 = i10;
                    i10++;
                    int i17 = i6;
                    i6++;
                    this.intPixels[i16] = colorModel.getRGB(bArr[i17] & 255);
                }
                i6 += i13;
                i10 += i12;
            }
        }
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        if (i4 < 0 || i5 < 0) {
            throw new IllegalArgumentException("Width (" + i4 + ") and height (" + i5 + ") must be > 0");
        }
        if (i4 == 0 || i5 == 0) {
            return;
        }
        if (i3 < 0) {
            int i8 = -i3;
            if (i8 >= i5) {
                return;
            }
            i6 += i7 * i8;
            i3 += i8;
            i5 -= i8;
        }
        if (i3 + i5 > this.height) {
            i5 = this.height - i3;
            if (i5 <= 0) {
                return;
            }
        }
        if (i2 < 0) {
            int i9 = -i2;
            if (i9 >= i4) {
                return;
            }
            i6 += i9;
            i2 += i9;
            i4 -= i9;
        }
        if (i2 + i4 > this.width) {
            i4 = this.width - i2;
            if (i4 <= 0) {
                return;
            }
        }
        if (this.intPixels == null) {
            if (this.bytePixels == null) {
                this.intPixels = new int[this.width * this.height];
                this.model = colorModel;
            } else {
                convertToRGB();
            }
        }
        int i10 = (i3 * this.width) + i2;
        if (this.model == colorModel) {
            for (int i11 = i5; i11 > 0; i11--) {
                System.arraycopy(iArr, i6, this.intPixels, i10, i4);
                i6 += i7;
                i10 += this.width;
            }
            return;
        }
        if (this.model != ColorModel.getRGBdefault()) {
            convertToRGB();
        }
        int i12 = this.width - i4;
        int i13 = i7 - i4;
        for (int i14 = i5; i14 > 0; i14--) {
            for (int i15 = i4; i15 > 0; i15--) {
                int i16 = i10;
                i10++;
                int i17 = i6;
                i6++;
                this.intPixels[i16] = colorModel.getRGB(iArr[i17]);
            }
            i6 += i13;
            i10 += i12;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0147  */
    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void imageComplete(int r11) {
        /*
            Method dump skipped, instructions count: 412
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.image.BufferedImageFilter.imageComplete(int):void");
    }

    private final WritableRaster createDCMraster() {
        DirectColorModel directColorModel = (DirectColorModel) this.model;
        boolean zHasAlpha = this.model.hasAlpha();
        int[] iArr = new int[3 + (zHasAlpha ? 1 : 0)];
        iArr[0] = directColorModel.getRedMask();
        iArr[1] = directColorModel.getGreenMask();
        iArr[2] = directColorModel.getBlueMask();
        if (zHasAlpha) {
            iArr[3] = directColorModel.getAlphaMask();
        }
        return Raster.createPackedRaster(new DataBufferInt(this.intPixels, this.width * this.height), this.width, this.height, this.width, iArr, (Point) null);
    }
}

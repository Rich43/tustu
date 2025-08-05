package java.awt.image;

/* loaded from: rt.jar:java/awt/image/AreaAveragingScaleFilter.class */
public class AreaAveragingScaleFilter extends ReplicateScaleFilter {
    private static final ColorModel rgbmodel = ColorModel.getRGBdefault();
    private static final int neededHints = 6;
    private boolean passthrough;
    private float[] reds;
    private float[] greens;
    private float[] blues;
    private float[] alphas;
    private int savedy;
    private int savedyrem;

    public AreaAveragingScaleFilter(int i2, int i3) {
        super(i2, i3);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setHints(int i2) {
        this.passthrough = (i2 & 6) != 6;
        super.setHints(i2);
    }

    private void makeAccumBuffers() {
        this.reds = new float[this.destWidth];
        this.greens = new float[this.destWidth];
        this.blues = new float[this.destWidth];
        this.alphas = new float[this.destWidth];
    }

    private int[] calcRow() {
        float f2 = this.srcWidth * this.srcHeight;
        if (this.outpixbuf == null || !(this.outpixbuf instanceof int[])) {
            this.outpixbuf = new int[this.destWidth];
        }
        int[] iArr = (int[]) this.outpixbuf;
        for (int i2 = 0; i2 < this.destWidth; i2++) {
            float f3 = f2;
            int iRound = Math.round(this.alphas[i2] / f3);
            if (iRound <= 0) {
                iRound = 0;
            } else if (iRound >= 255) {
                iRound = 255;
            } else {
                f3 = this.alphas[i2] / 255.0f;
            }
            int iRound2 = Math.round(this.reds[i2] / f3);
            int iRound3 = Math.round(this.greens[i2] / f3);
            int iRound4 = Math.round(this.blues[i2] / f3);
            if (iRound2 < 0) {
                iRound2 = 0;
            } else if (iRound2 > 255) {
                iRound2 = 255;
            }
            if (iRound3 < 0) {
                iRound3 = 0;
            } else if (iRound3 > 255) {
                iRound3 = 255;
            }
            if (iRound4 < 0) {
                iRound4 = 0;
            } else if (iRound4 > 255) {
                iRound4 = 255;
            }
            iArr[i2] = (iRound << 24) | (iRound2 << 16) | (iRound3 << 8) | iRound4;
        }
        return iArr;
    }

    private void accumPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, Object obj, int i6, int i7) {
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        if (this.reds == null) {
            makeAccumBuffers();
        }
        int i13 = i3;
        int i14 = this.destHeight;
        if (i13 == 0) {
            i8 = 0;
            i9 = 0;
        } else {
            i8 = this.savedy;
            i9 = this.savedyrem;
        }
        while (i13 < i3 + i5) {
            if (i9 == 0) {
                for (int i15 = 0; i15 < this.destWidth; i15++) {
                    this.blues[i15] = 0.0f;
                    this.greens[i15] = 0.0f;
                    this.reds[i15] = 0.0f;
                    this.alphas[i15] = 0.0f;
                }
                i9 = this.srcHeight;
            }
            if (i14 < i9) {
                i10 = i14;
            } else {
                i10 = i9;
            }
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = this.srcWidth;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            while (i16 < i4) {
                if (i18 == 0) {
                    i18 = this.destWidth;
                    if (obj instanceof byte[]) {
                        i12 = ((byte[]) obj)[i6 + i16] & 255;
                    } else {
                        i12 = ((int[]) obj)[i6 + i16];
                    }
                    int rgb = colorModel.getRGB(i12);
                    f2 = rgb >>> 24;
                    f3 = (rgb >> 16) & 255;
                    f4 = (rgb >> 8) & 255;
                    f5 = rgb & 255;
                    if (f2 != 255.0f) {
                        float f6 = f2 / 255.0f;
                        f3 *= f6;
                        f4 *= f6;
                        f5 *= f6;
                    }
                }
                if (i18 < i19) {
                    i11 = i18;
                } else {
                    i11 = i19;
                }
                float f7 = i11 * i10;
                float[] fArr = this.alphas;
                int i20 = i17;
                fArr[i20] = fArr[i20] + (f7 * f2);
                float[] fArr2 = this.reds;
                int i21 = i17;
                fArr2[i21] = fArr2[i21] + (f7 * f3);
                float[] fArr3 = this.greens;
                int i22 = i17;
                fArr3[i22] = fArr3[i22] + (f7 * f4);
                float[] fArr4 = this.blues;
                int i23 = i17;
                fArr4[i23] = fArr4[i23] + (f7 * f5);
                int i24 = i18 - i11;
                i18 = i24;
                if (i24 == 0) {
                    i16++;
                }
                int i25 = i19 - i11;
                i19 = i25;
                if (i25 == 0) {
                    i17++;
                    i19 = this.srcWidth;
                }
            }
            int i26 = i9 - i10;
            i9 = i26;
            if (i26 == 0) {
                int[] iArrCalcRow = calcRow();
                do {
                    this.consumer.setPixels(0, i8, this.destWidth, 1, rgbmodel, iArrCalcRow, 0, this.destWidth);
                    i8++;
                    int i27 = i14 - i10;
                    i14 = i27;
                    if (i27 < i10) {
                        break;
                    }
                } while (i10 == this.srcHeight);
            } else {
                i14 -= i10;
            }
            if (i14 == 0) {
                i14 = this.destHeight;
                i13++;
                i6 += i7;
            }
        }
        this.savedyrem = i9;
        this.savedy = i8;
    }

    @Override // java.awt.image.ReplicateScaleFilter, java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        if (this.passthrough) {
            super.setPixels(i2, i3, i4, i5, colorModel, bArr, i6, i7);
        } else {
            accumPixels(i2, i3, i4, i5, colorModel, bArr, i6, i7);
        }
    }

    @Override // java.awt.image.ReplicateScaleFilter, java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        if (this.passthrough) {
            super.setPixels(i2, i3, i4, i5, colorModel, iArr, i6, i7);
        } else {
            accumPixels(i2, i3, i4, i5, colorModel, iArr, i6, i7);
        }
    }
}

package java.awt.image;

import java.util.Hashtable;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/awt/image/ReplicateScaleFilter.class */
public class ReplicateScaleFilter extends ImageFilter {
    protected int srcWidth;
    protected int srcHeight;
    protected int destWidth;
    protected int destHeight;
    protected int[] srcrows;
    protected int[] srccols;
    protected Object outpixbuf;

    public ReplicateScaleFilter(int i2, int i3) {
        if (i2 == 0 || i3 == 0) {
            throw new IllegalArgumentException("Width (" + i2 + ") and height (" + i3 + ") must be non-zero");
        }
        this.destWidth = i2;
        this.destHeight = i3;
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setProperties(Hashtable<?, ?> hashtable) {
        Hashtable<?, ?> hashtable2 = (Hashtable) hashtable.clone();
        String str = this.destWidth + LanguageTag.PRIVATEUSE + this.destHeight;
        Object obj = hashtable2.get("rescale");
        if (obj != null && (obj instanceof String)) {
            str = ((String) obj) + ", " + str;
        }
        hashtable2.put("rescale", str);
        super.setProperties(hashtable2);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        this.srcWidth = i2;
        this.srcHeight = i3;
        if (this.destWidth < 0) {
            if (this.destHeight < 0) {
                this.destWidth = this.srcWidth;
                this.destHeight = this.srcHeight;
            } else {
                this.destWidth = (this.srcWidth * this.destHeight) / this.srcHeight;
            }
        } else if (this.destHeight < 0) {
            this.destHeight = (this.srcHeight * this.destWidth) / this.srcWidth;
        }
        this.consumer.setDimensions(this.destWidth, this.destHeight);
    }

    private void calculateMaps() {
        this.srcrows = new int[this.destHeight + 1];
        for (int i2 = 0; i2 <= this.destHeight; i2++) {
            this.srcrows[i2] = (((2 * i2) * this.srcHeight) + this.srcHeight) / (2 * this.destHeight);
        }
        this.srccols = new int[this.destWidth + 1];
        for (int i3 = 0; i3 <= this.destWidth; i3++) {
            this.srccols[i3] = (((2 * i3) * this.srcWidth) + this.srcWidth) / (2 * this.destWidth);
        }
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        byte[] bArr2;
        if (this.srcrows == null || this.srccols == null) {
            calculateMaps();
        }
        int i8 = ((((2 * i2) * this.destWidth) + this.srcWidth) - 1) / (2 * this.srcWidth);
        int i9 = ((((2 * i3) * this.destHeight) + this.srcHeight) - 1) / (2 * this.srcHeight);
        if (this.outpixbuf != null && (this.outpixbuf instanceof byte[])) {
            bArr2 = (byte[]) this.outpixbuf;
        } else {
            bArr2 = new byte[this.destWidth];
            this.outpixbuf = bArr2;
        }
        int i10 = i9;
        while (true) {
            int i11 = this.srcrows[i10];
            if (i11 < i3 + i5) {
                int i12 = i6 + (i7 * (i11 - i3));
                int i13 = i8;
                while (true) {
                    int i14 = this.srccols[i13];
                    if (i14 >= i2 + i4) {
                        break;
                    }
                    bArr2[i13] = bArr[(i12 + i14) - i2];
                    i13++;
                }
                if (i13 > i8) {
                    this.consumer.setPixels(i8, i10, i13 - i8, 1, colorModel, bArr2, i8, this.destWidth);
                }
                i10++;
            } else {
                return;
            }
        }
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        int[] iArr2;
        if (this.srcrows == null || this.srccols == null) {
            calculateMaps();
        }
        int i8 = ((((2 * i2) * this.destWidth) + this.srcWidth) - 1) / (2 * this.srcWidth);
        int i9 = ((((2 * i3) * this.destHeight) + this.srcHeight) - 1) / (2 * this.srcHeight);
        if (this.outpixbuf != null && (this.outpixbuf instanceof int[])) {
            iArr2 = (int[]) this.outpixbuf;
        } else {
            iArr2 = new int[this.destWidth];
            this.outpixbuf = iArr2;
        }
        int i10 = i9;
        while (true) {
            int i11 = this.srcrows[i10];
            if (i11 < i3 + i5) {
                int i12 = i6 + (i7 * (i11 - i3));
                int i13 = i8;
                while (true) {
                    int i14 = this.srccols[i13];
                    if (i14 >= i2 + i4) {
                        break;
                    }
                    iArr2[i13] = iArr[(i12 + i14) - i2];
                    i13++;
                }
                if (i13 > i8) {
                    this.consumer.setPixels(i8, i10, i13 - i8, 1, colorModel, iArr2, i8, this.destWidth);
                }
                i10++;
            } else {
                return;
            }
        }
    }
}

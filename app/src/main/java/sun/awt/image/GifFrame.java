package sun.awt.image;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

/* compiled from: GifImageDecoder.java */
/* loaded from: rt.jar:sun/awt/image/GifFrame.class */
class GifFrame {
    private static final boolean verbose = false;
    private static IndexColorModel trans_model;
    static final int DISPOSAL_NONE = 0;
    static final int DISPOSAL_SAVE = 1;
    static final int DISPOSAL_BGCOLOR = 2;
    static final int DISPOSAL_PREVIOUS = 3;
    GifImageDecoder decoder;
    int disposal_method;
    int delay;
    IndexColorModel model;

    /* renamed from: x, reason: collision with root package name */
    int f13545x;

    /* renamed from: y, reason: collision with root package name */
    int f13546y;
    int width;
    int height;
    boolean initialframe;

    public GifFrame(GifImageDecoder gifImageDecoder, int i2, int i3, boolean z2, IndexColorModel indexColorModel, int i4, int i5, int i6, int i7) {
        this.decoder = gifImageDecoder;
        this.disposal_method = i2;
        this.delay = i3;
        this.model = indexColorModel;
        this.initialframe = z2;
        this.f13545x = i4;
        this.f13546y = i5;
        this.width = i6;
        this.height = i7;
    }

    private void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        this.decoder.setPixels(i2, i3, i4, i5, colorModel, bArr, i6, i7);
    }

    public boolean dispose() {
        byte transparentPixel;
        if (this.decoder.imageComplete(2, false) == 0) {
            return false;
        }
        if (this.delay > 0) {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e2) {
                return false;
            }
        } else {
            Thread.yield();
        }
        int i2 = this.decoder.global_width;
        int i3 = this.decoder.global_height;
        if (this.f13545x < 0) {
            this.width += this.f13545x;
            this.f13545x = 0;
        }
        if (this.f13545x + this.width > i2) {
            this.width = i2 - this.f13545x;
        }
        if (this.width <= 0) {
            this.disposal_method = 0;
        } else {
            if (this.f13546y < 0) {
                this.height += this.f13546y;
                this.f13546y = 0;
            }
            if (this.f13546y + this.height > i3) {
                this.height = i3 - this.f13546y;
            }
            if (this.height <= 0) {
                this.disposal_method = 0;
            }
        }
        switch (this.disposal_method) {
            case 1:
                this.decoder.saved_model = this.model;
                return true;
            case 2:
                if (this.model.getTransparentPixel() < 0) {
                    this.model = trans_model;
                    if (this.model == null) {
                        this.model = new IndexColorModel(8, 1, new byte[4], 0, true);
                        trans_model = this.model;
                    }
                    transparentPixel = 0;
                } else {
                    transparentPixel = (byte) this.model.getTransparentPixel();
                }
                byte[] bArr = new byte[this.width];
                if (transparentPixel != 0) {
                    for (int i4 = 0; i4 < this.width; i4++) {
                        bArr[i4] = transparentPixel;
                    }
                }
                if (this.decoder.saved_image != null) {
                    for (int i5 = 0; i5 < i2 * i3; i5++) {
                        this.decoder.saved_image[i5] = transparentPixel;
                    }
                }
                setPixels(this.f13545x, this.f13546y, this.width, this.height, this.model, bArr, 0, 0);
                return true;
            case 3:
                byte[] bArr2 = this.decoder.saved_image;
                IndexColorModel indexColorModel = this.decoder.saved_model;
                if (bArr2 != null) {
                    setPixels(this.f13545x, this.f13546y, this.width, this.height, indexColorModel, bArr2, (this.f13546y * i2) + this.f13545x, i2);
                    return true;
                }
                return true;
            default:
                return true;
        }
    }
}

package java.awt.image;

import java.awt.Image;
import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/image/PixelGrabber.class */
public class PixelGrabber implements ImageConsumer {
    ImageProducer producer;
    int dstX;
    int dstY;
    int dstW;
    int dstH;
    ColorModel imageModel;
    byte[] bytePixels;
    int[] intPixels;
    int dstOff;
    int dstScan;
    private boolean grabbing;
    private int flags;
    private static final int GRABBEDBITS = 48;
    private static final int DONEBITS = 112;

    public PixelGrabber(Image image, int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        this(image.getSource(), i2, i3, i4, i5, iArr, i6, i7);
    }

    public PixelGrabber(ImageProducer imageProducer, int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7) {
        this.producer = imageProducer;
        this.dstX = i2;
        this.dstY = i3;
        this.dstW = i4;
        this.dstH = i5;
        this.dstOff = i6;
        this.dstScan = i7;
        this.intPixels = iArr;
        this.imageModel = ColorModel.getRGBdefault();
    }

    public PixelGrabber(Image image, int i2, int i3, int i4, int i5, boolean z2) {
        this.producer = image.getSource();
        this.dstX = i2;
        this.dstY = i3;
        this.dstW = i4;
        this.dstH = i5;
        if (z2) {
            this.imageModel = ColorModel.getRGBdefault();
        }
    }

    public synchronized void startGrabbing() {
        if ((this.flags & 112) == 0 && !this.grabbing) {
            this.grabbing = true;
            this.flags &= -129;
            this.producer.startProduction(this);
        }
    }

    public synchronized void abortGrabbing() {
        imageComplete(4);
    }

    public boolean grabPixels() throws InterruptedException {
        return grabPixels(0L);
    }

    public synchronized boolean grabPixels(long j2) throws InterruptedException {
        long jCurrentTimeMillis;
        if ((this.flags & 112) != 0) {
            return (this.flags & 48) != 0;
        }
        long jCurrentTimeMillis2 = j2 + System.currentTimeMillis();
        if (!this.grabbing) {
            this.grabbing = true;
            this.flags &= -129;
            this.producer.startProduction(this);
        }
        while (this.grabbing) {
            if (j2 == 0) {
                jCurrentTimeMillis = 0;
            } else {
                jCurrentTimeMillis = jCurrentTimeMillis2 - System.currentTimeMillis();
                if (jCurrentTimeMillis <= 0) {
                    break;
                }
            }
            wait(jCurrentTimeMillis);
        }
        return (this.flags & 48) != 0;
    }

    public synchronized int getStatus() {
        return this.flags;
    }

    public synchronized int getWidth() {
        if (this.dstW < 0) {
            return -1;
        }
        return this.dstW;
    }

    public synchronized int getHeight() {
        if (this.dstH < 0) {
            return -1;
        }
        return this.dstH;
    }

    public synchronized Object getPixels() {
        return this.bytePixels == null ? this.intPixels : this.bytePixels;
    }

    public synchronized ColorModel getColorModel() {
        return this.imageModel;
    }

    @Override // java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        if (this.dstW < 0) {
            this.dstW = i2 - this.dstX;
        }
        if (this.dstH < 0) {
            this.dstH = i3 - this.dstY;
        }
        if (this.dstW <= 0 || this.dstH <= 0) {
            imageComplete(3);
        } else if (this.intPixels == null && this.imageModel == ColorModel.getRGBdefault()) {
            this.intPixels = new int[this.dstW * this.dstH];
            this.dstScan = this.dstW;
            this.dstOff = 0;
        }
        this.flags |= 3;
    }

    @Override // java.awt.image.ImageConsumer
    public void setHints(int i2) {
    }

    @Override // java.awt.image.ImageConsumer
    public void setProperties(Hashtable<?, ?> hashtable) {
    }

    @Override // java.awt.image.ImageConsumer
    public void setColorModel(ColorModel colorModel) {
    }

    private void convertToRGB() {
        int i2 = this.dstW * this.dstH;
        int[] iArr = new int[i2];
        if (this.bytePixels != null) {
            for (int i3 = 0; i3 < i2; i3++) {
                iArr[i3] = this.imageModel.getRGB(this.bytePixels[i3] & 255);
            }
        } else if (this.intPixels != null) {
            for (int i4 = 0; i4 < i2; i4++) {
                iArr[i4] = this.imageModel.getRGB(this.intPixels[i4]);
            }
        }
        this.bytePixels = null;
        this.intPixels = iArr;
        this.dstScan = this.dstW;
        this.dstOff = 0;
        this.imageModel = ColorModel.getRGBdefault();
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        if (i3 < this.dstY) {
            int i8 = this.dstY - i3;
            if (i8 >= i5) {
                return;
            }
            i6 += i7 * i8;
            i3 += i8;
            i5 -= i8;
        }
        if (i3 + i5 > this.dstY + this.dstH) {
            i5 = (this.dstY + this.dstH) - i3;
            if (i5 <= 0) {
                return;
            }
        }
        if (i2 < this.dstX) {
            int i9 = this.dstX - i2;
            if (i9 >= i4) {
                return;
            }
            i6 += i9;
            i2 += i9;
            i4 -= i9;
        }
        if (i2 + i4 > this.dstX + this.dstW) {
            i4 = (this.dstX + this.dstW) - i2;
            if (i4 <= 0) {
                return;
            }
        }
        int i10 = this.dstOff + ((i3 - this.dstY) * this.dstScan) + (i2 - this.dstX);
        if (this.intPixels == null) {
            if (this.bytePixels == null) {
                this.bytePixels = new byte[this.dstW * this.dstH];
                this.dstScan = this.dstW;
                this.dstOff = 0;
                this.imageModel = colorModel;
            } else if (this.imageModel != colorModel) {
                convertToRGB();
            }
            if (this.bytePixels != null) {
                for (int i11 = i5; i11 > 0; i11--) {
                    System.arraycopy(bArr, i6, this.bytePixels, i10, i4);
                    i6 += i7;
                    i10 += this.dstScan;
                }
            }
        }
        if (this.intPixels != null) {
            int i12 = this.dstScan - i4;
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
        this.flags |= 8;
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        if (i3 < this.dstY) {
            int i8 = this.dstY - i3;
            if (i8 >= i5) {
                return;
            }
            i6 += i7 * i8;
            i3 += i8;
            i5 -= i8;
        }
        if (i3 + i5 > this.dstY + this.dstH) {
            i5 = (this.dstY + this.dstH) - i3;
            if (i5 <= 0) {
                return;
            }
        }
        if (i2 < this.dstX) {
            int i9 = this.dstX - i2;
            if (i9 >= i4) {
                return;
            }
            i6 += i9;
            i2 += i9;
            i4 -= i9;
        }
        if (i2 + i4 > this.dstX + this.dstW) {
            i4 = (this.dstX + this.dstW) - i2;
            if (i4 <= 0) {
                return;
            }
        }
        if (this.intPixels == null) {
            if (this.bytePixels == null) {
                this.intPixels = new int[this.dstW * this.dstH];
                this.dstScan = this.dstW;
                this.dstOff = 0;
                this.imageModel = colorModel;
            } else {
                convertToRGB();
            }
        }
        int i10 = this.dstOff + ((i3 - this.dstY) * this.dstScan) + (i2 - this.dstX);
        if (this.imageModel == colorModel) {
            for (int i11 = i5; i11 > 0; i11--) {
                System.arraycopy(iArr, i6, this.intPixels, i10, i4);
                i6 += i7;
                i10 += this.dstScan;
            }
        } else {
            if (this.imageModel != ColorModel.getRGBdefault()) {
                convertToRGB();
            }
            int i12 = this.dstScan - i4;
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
        this.flags |= 8;
    }

    @Override // java.awt.image.ImageConsumer
    public synchronized void imageComplete(int i2) {
        this.grabbing = false;
        switch (i2) {
            case 1:
            default:
                this.flags |= 192;
                break;
            case 2:
                this.flags |= 16;
                break;
            case 3:
                this.flags |= 32;
                break;
            case 4:
                this.flags |= 128;
                break;
        }
        this.producer.removeConsumer(this);
        notifyAll();
    }

    public synchronized int status() {
        return this.flags;
    }
}

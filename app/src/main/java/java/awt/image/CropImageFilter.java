package java.awt.image;

import java.awt.Rectangle;
import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/image/CropImageFilter.class */
public class CropImageFilter extends ImageFilter {
    int cropX;
    int cropY;
    int cropW;
    int cropH;

    public CropImageFilter(int i2, int i3, int i4, int i5) {
        this.cropX = i2;
        this.cropY = i3;
        this.cropW = i4;
        this.cropH = i5;
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setProperties(Hashtable<?, ?> hashtable) {
        Hashtable<?, ?> hashtable2 = (Hashtable) hashtable.clone();
        hashtable2.put("croprect", new Rectangle(this.cropX, this.cropY, this.cropW, this.cropH));
        super.setProperties(hashtable2);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        this.consumer.setDimensions(this.cropW, this.cropH);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        int i8 = i2;
        if (i8 < this.cropX) {
            i8 = this.cropX;
        }
        int iAddWithoutOverflow = addWithoutOverflow(i2, i4);
        if (iAddWithoutOverflow > this.cropX + this.cropW) {
            iAddWithoutOverflow = this.cropX + this.cropW;
        }
        int i9 = i3;
        if (i9 < this.cropY) {
            i9 = this.cropY;
        }
        int iAddWithoutOverflow2 = addWithoutOverflow(i3, i5);
        if (iAddWithoutOverflow2 > this.cropY + this.cropH) {
            iAddWithoutOverflow2 = this.cropY + this.cropH;
        }
        if (i8 >= iAddWithoutOverflow || i9 >= iAddWithoutOverflow2) {
            return;
        }
        this.consumer.setPixels(i8 - this.cropX, i9 - this.cropY, iAddWithoutOverflow - i8, iAddWithoutOverflow2 - i9, colorModel, bArr, i6 + ((i9 - i3) * i7) + (i8 - i2), i7);
    }

    @Override // java.awt.image.ImageFilter, java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        int i8 = i2;
        if (i8 < this.cropX) {
            i8 = this.cropX;
        }
        int iAddWithoutOverflow = addWithoutOverflow(i2, i4);
        if (iAddWithoutOverflow > this.cropX + this.cropW) {
            iAddWithoutOverflow = this.cropX + this.cropW;
        }
        int i9 = i3;
        if (i9 < this.cropY) {
            i9 = this.cropY;
        }
        int iAddWithoutOverflow2 = addWithoutOverflow(i3, i5);
        if (iAddWithoutOverflow2 > this.cropY + this.cropH) {
            iAddWithoutOverflow2 = this.cropY + this.cropH;
        }
        if (i8 >= iAddWithoutOverflow || i9 >= iAddWithoutOverflow2) {
            return;
        }
        this.consumer.setPixels(i8 - this.cropX, i9 - this.cropY, iAddWithoutOverflow - i8, iAddWithoutOverflow2 - i9, colorModel, iArr, i6 + ((i9 - i3) * i7) + (i8 - i2), i7);
    }

    private int addWithoutOverflow(int i2, int i3) {
        int i4 = i2 + i3;
        if (i2 > 0 && i3 > 0 && i4 < 0) {
            i4 = Integer.MAX_VALUE;
        } else if (i2 < 0 && i3 < 0 && i4 > 0) {
            i4 = Integer.MIN_VALUE;
        }
        return i4;
    }
}

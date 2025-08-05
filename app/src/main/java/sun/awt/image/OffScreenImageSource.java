package sun.awt.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/awt/image/OffScreenImageSource.class */
public class OffScreenImageSource implements ImageProducer {
    BufferedImage image;
    int width;
    int height;
    Hashtable properties;
    private ImageConsumer theConsumer;

    public OffScreenImageSource(BufferedImage bufferedImage, Hashtable hashtable) {
        this.image = bufferedImage;
        if (hashtable != null) {
            this.properties = hashtable;
        } else {
            this.properties = new Hashtable();
        }
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }

    public OffScreenImageSource(BufferedImage bufferedImage) {
        this(bufferedImage, null);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        this.theConsumer = imageConsumer;
        produce();
    }

    @Override // java.awt.image.ImageProducer
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return imageConsumer == this.theConsumer;
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        if (this.theConsumer == imageConsumer) {
            this.theConsumer = null;
        }
    }

    @Override // java.awt.image.ImageProducer
    public void startProduction(ImageConsumer imageConsumer) {
        addConsumer(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }

    private void sendPixels() {
        ColorModel colorModel = this.image.getColorModel();
        WritableRaster raster = this.image.getRaster();
        int numDataElements = raster.getNumDataElements();
        int dataType = raster.getDataBuffer().getDataType();
        int[] iArr = new int[this.width * numDataElements];
        boolean z2 = true;
        if (colorModel instanceof IndexColorModel) {
            byte[] bArr = new byte[this.width];
            this.theConsumer.setColorModel(colorModel);
            if (raster instanceof ByteComponentRaster) {
                z2 = false;
                for (int i2 = 0; i2 < this.height; i2++) {
                    raster.getDataElements(0, i2, this.width, 1, bArr);
                    this.theConsumer.setPixels(0, i2, this.width, 1, colorModel, bArr, 0, this.width);
                }
            } else if (raster instanceof BytePackedRaster) {
                z2 = false;
                for (int i3 = 0; i3 < this.height; i3++) {
                    raster.getPixels(0, i3, this.width, 1, iArr);
                    for (int i4 = 0; i4 < this.width; i4++) {
                        bArr[i4] = (byte) iArr[i4];
                    }
                    this.theConsumer.setPixels(0, i3, this.width, 1, colorModel, bArr, 0, this.width);
                }
            } else if (dataType == 2 || dataType == 3) {
                z2 = false;
                for (int i5 = 0; i5 < this.height; i5++) {
                    raster.getPixels(0, i5, this.width, 1, iArr);
                    this.theConsumer.setPixels(0, i5, this.width, 1, colorModel, iArr, 0, this.width);
                }
            }
        } else if (colorModel instanceof DirectColorModel) {
            this.theConsumer.setColorModel(colorModel);
            z2 = false;
            switch (dataType) {
                case 0:
                    byte[] bArr2 = new byte[this.width];
                    for (int i6 = 0; i6 < this.height; i6++) {
                        raster.getDataElements(0, i6, this.width, 1, bArr2);
                        for (int i7 = 0; i7 < this.width; i7++) {
                            iArr[i7] = bArr2[i7] & 255;
                        }
                        this.theConsumer.setPixels(0, i6, this.width, 1, colorModel, iArr, 0, this.width);
                    }
                    break;
                case 1:
                    short[] sArr = new short[this.width];
                    for (int i8 = 0; i8 < this.height; i8++) {
                        raster.getDataElements(0, i8, this.width, 1, sArr);
                        for (int i9 = 0; i9 < this.width; i9++) {
                            iArr[i9] = sArr[i9] & 65535;
                        }
                        this.theConsumer.setPixels(0, i8, this.width, 1, colorModel, iArr, 0, this.width);
                    }
                    break;
                case 2:
                default:
                    z2 = true;
                    break;
                case 3:
                    for (int i10 = 0; i10 < this.height; i10++) {
                        raster.getDataElements(0, i10, this.width, 1, iArr);
                        this.theConsumer.setPixels(0, i10, this.width, 1, colorModel, iArr, 0, this.width);
                    }
                    break;
            }
        }
        if (z2) {
            ColorModel rGBdefault = ColorModel.getRGBdefault();
            this.theConsumer.setColorModel(rGBdefault);
            for (int i11 = 0; i11 < this.height; i11++) {
                for (int i12 = 0; i12 < this.width; i12++) {
                    iArr[i12] = this.image.getRGB(i12, i11);
                }
                this.theConsumer.setPixels(0, i11, this.width, 1, rGBdefault, iArr, 0, this.width);
            }
        }
    }

    private void produce() {
        try {
            this.theConsumer.setDimensions(this.image.getWidth(), this.image.getHeight());
            this.theConsumer.setProperties(this.properties);
            sendPixels();
            this.theConsumer.imageComplete(2);
            this.theConsumer.imageComplete(3);
        } catch (NullPointerException e2) {
            if (this.theConsumer != null) {
                this.theConsumer.imageComplete(1);
            }
        }
    }
}

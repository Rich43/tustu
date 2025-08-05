package java.awt.image;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/MemoryImageSource.class */
public class MemoryImageSource implements ImageProducer {
    int width;
    int height;
    ColorModel model;
    Object pixels;
    int pixeloffset;
    int pixelscan;
    Hashtable properties;
    Vector theConsumers = new Vector();
    boolean animating;
    boolean fullbuffers;

    public MemoryImageSource(int i2, int i3, ColorModel colorModel, byte[] bArr, int i4, int i5) {
        initialize(i2, i3, colorModel, bArr, i4, i5, null);
    }

    public MemoryImageSource(int i2, int i3, ColorModel colorModel, byte[] bArr, int i4, int i5, Hashtable<?, ?> hashtable) {
        initialize(i2, i3, colorModel, bArr, i4, i5, hashtable);
    }

    public MemoryImageSource(int i2, int i3, ColorModel colorModel, int[] iArr, int i4, int i5) {
        initialize(i2, i3, colorModel, iArr, i4, i5, null);
    }

    public MemoryImageSource(int i2, int i3, ColorModel colorModel, int[] iArr, int i4, int i5, Hashtable<?, ?> hashtable) {
        initialize(i2, i3, colorModel, iArr, i4, i5, hashtable);
    }

    private void initialize(int i2, int i3, ColorModel colorModel, Object obj, int i4, int i5, Hashtable hashtable) {
        this.width = i2;
        this.height = i3;
        this.model = colorModel;
        this.pixels = obj;
        this.pixeloffset = i4;
        this.pixelscan = i5;
        if (hashtable == null) {
            hashtable = new Hashtable();
        }
        this.properties = hashtable;
    }

    public MemoryImageSource(int i2, int i3, int[] iArr, int i4, int i5) {
        initialize(i2, i3, ColorModel.getRGBdefault(), iArr, i4, i5, null);
    }

    public MemoryImageSource(int i2, int i3, int[] iArr, int i4, int i5, Hashtable<?, ?> hashtable) {
        initialize(i2, i3, ColorModel.getRGBdefault(), iArr, i4, i5, hashtable);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        if (this.theConsumers.contains(imageConsumer)) {
            return;
        }
        this.theConsumers.addElement(imageConsumer);
        try {
            initConsumer(imageConsumer);
            sendPixels(imageConsumer, 0, 0, this.width, this.height);
            if (isConsumer(imageConsumer)) {
                imageConsumer.imageComplete(this.animating ? 2 : 3);
                if (!this.animating && isConsumer(imageConsumer)) {
                    imageConsumer.imageComplete(1);
                    removeConsumer(imageConsumer);
                }
            }
        } catch (Exception e2) {
            if (isConsumer(imageConsumer)) {
                imageConsumer.imageComplete(1);
            }
        }
    }

    @Override // java.awt.image.ImageProducer
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return this.theConsumers.contains(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        this.theConsumers.removeElement(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public void startProduction(ImageConsumer imageConsumer) {
        addConsumer(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }

    public synchronized void setAnimated(boolean z2) {
        this.animating = z2;
        if (!this.animating) {
            Enumeration enumerationElements = this.theConsumers.elements();
            while (enumerationElements.hasMoreElements()) {
                ImageConsumer imageConsumer = (ImageConsumer) enumerationElements.nextElement2();
                imageConsumer.imageComplete(3);
                if (isConsumer(imageConsumer)) {
                    imageConsumer.imageComplete(1);
                }
            }
            this.theConsumers.removeAllElements();
        }
    }

    public synchronized void setFullBufferUpdates(boolean z2) {
        if (this.fullbuffers == z2) {
            return;
        }
        this.fullbuffers = z2;
        if (this.animating) {
            Enumeration enumerationElements = this.theConsumers.elements();
            while (enumerationElements.hasMoreElements()) {
                ((ImageConsumer) enumerationElements.nextElement2()).setHints(z2 ? 6 : 1);
            }
        }
    }

    public void newPixels() {
        newPixels(0, 0, this.width, this.height, true);
    }

    public synchronized void newPixels(int i2, int i3, int i4, int i5) {
        newPixels(i2, i3, i4, i5, true);
    }

    public synchronized void newPixels(int i2, int i3, int i4, int i5, boolean z2) {
        if (this.animating) {
            if (this.fullbuffers) {
                i3 = 0;
                i2 = 0;
                i4 = this.width;
                i5 = this.height;
            } else {
                if (i2 < 0) {
                    i4 += i2;
                    i2 = 0;
                }
                if (i2 + i4 > this.width) {
                    i4 = this.width - i2;
                }
                if (i3 < 0) {
                    i5 += i3;
                    i3 = 0;
                }
                if (i3 + i5 > this.height) {
                    i5 = this.height - i3;
                }
            }
            if ((i4 <= 0 || i5 <= 0) && !z2) {
                return;
            }
            Enumeration enumerationElements = this.theConsumers.elements();
            while (enumerationElements.hasMoreElements()) {
                ImageConsumer imageConsumer = (ImageConsumer) enumerationElements.nextElement2();
                if (i4 > 0 && i5 > 0) {
                    sendPixels(imageConsumer, i2, i3, i4, i5);
                }
                if (z2 && isConsumer(imageConsumer)) {
                    imageConsumer.imageComplete(2);
                }
            }
        }
    }

    public synchronized void newPixels(byte[] bArr, ColorModel colorModel, int i2, int i3) {
        this.pixels = bArr;
        this.model = colorModel;
        this.pixeloffset = i2;
        this.pixelscan = i3;
        newPixels();
    }

    public synchronized void newPixels(int[] iArr, ColorModel colorModel, int i2, int i3) {
        this.pixels = iArr;
        this.model = colorModel;
        this.pixeloffset = i2;
        this.pixelscan = i3;
        newPixels();
    }

    private void initConsumer(ImageConsumer imageConsumer) {
        if (isConsumer(imageConsumer)) {
            imageConsumer.setDimensions(this.width, this.height);
        }
        if (isConsumer(imageConsumer)) {
            imageConsumer.setProperties(this.properties);
        }
        if (isConsumer(imageConsumer)) {
            imageConsumer.setColorModel(this.model);
        }
        if (isConsumer(imageConsumer)) {
            imageConsumer.setHints(this.animating ? this.fullbuffers ? 6 : 1 : 30);
        }
    }

    private void sendPixels(ImageConsumer imageConsumer, int i2, int i3, int i4, int i5) {
        int i6 = this.pixeloffset + (this.pixelscan * i3) + i2;
        if (isConsumer(imageConsumer)) {
            if (this.pixels instanceof byte[]) {
                imageConsumer.setPixels(i2, i3, i4, i5, this.model, (byte[]) this.pixels, i6, this.pixelscan);
            } else {
                imageConsumer.setPixels(i2, i3, i4, i5, this.model, (int[]) this.pixels, i6, this.pixelscan);
            }
        }
    }
}

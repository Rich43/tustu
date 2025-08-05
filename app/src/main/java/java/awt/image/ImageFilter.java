package java.awt.image;

import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/image/ImageFilter.class */
public class ImageFilter implements ImageConsumer, Cloneable {
    protected ImageConsumer consumer;

    public ImageFilter getFilterInstance(ImageConsumer imageConsumer) {
        ImageFilter imageFilter = (ImageFilter) clone();
        imageFilter.consumer = imageConsumer;
        return imageFilter;
    }

    @Override // java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        this.consumer.setDimensions(i2, i3);
    }

    @Override // java.awt.image.ImageConsumer
    public void setProperties(Hashtable<?, ?> hashtable) {
        Hashtable<?, ?> hashtable2 = (Hashtable) hashtable.clone();
        Object obj = hashtable2.get("filters");
        if (obj == null) {
            hashtable2.put("filters", toString());
        } else if (obj instanceof String) {
            hashtable2.put("filters", ((String) obj) + toString());
        }
        this.consumer.setProperties(hashtable2);
    }

    @Override // java.awt.image.ImageConsumer
    public void setColorModel(ColorModel colorModel) {
        this.consumer.setColorModel(colorModel);
    }

    @Override // java.awt.image.ImageConsumer
    public void setHints(int i2) {
        this.consumer.setHints(i2);
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        this.consumer.setPixels(i2, i3, i4, i5, colorModel, bArr, i6, i7);
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        this.consumer.setPixels(i2, i3, i4, i5, colorModel, iArr, i6, i7);
    }

    @Override // java.awt.image.ImageConsumer
    public void imageComplete(int i2) {
        this.consumer.imageComplete(i2);
    }

    public void resendTopDownLeftRight(ImageProducer imageProducer) {
        imageProducer.requestTopDownLeftRightResend(this);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}

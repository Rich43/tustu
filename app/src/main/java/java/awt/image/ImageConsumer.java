package java.awt.image;

import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/image/ImageConsumer.class */
public interface ImageConsumer {
    public static final int RANDOMPIXELORDER = 1;
    public static final int TOPDOWNLEFTRIGHT = 2;
    public static final int COMPLETESCANLINES = 4;
    public static final int SINGLEPASS = 8;
    public static final int SINGLEFRAME = 16;
    public static final int IMAGEERROR = 1;
    public static final int SINGLEFRAMEDONE = 2;
    public static final int STATICIMAGEDONE = 3;
    public static final int IMAGEABORTED = 4;

    void setDimensions(int i2, int i3);

    void setProperties(Hashtable<?, ?> hashtable);

    void setColorModel(ColorModel colorModel);

    void setHints(int i2);

    void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7);

    void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7);

    void imageComplete(int i2);
}

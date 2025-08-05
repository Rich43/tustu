package sun.awt.image;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.List;
import sun.misc.SoftCache;

/* loaded from: rt.jar:sun/awt/image/MultiResolutionToolkitImage.class */
public class MultiResolutionToolkitImage extends ToolkitImage implements MultiResolutionImage {
    Image resolutionVariant;
    private static final int BITS_INFO = 56;

    public MultiResolutionToolkitImage(Image image, Image image2) {
        super(image.getSource());
        this.resolutionVariant = image2;
    }

    @Override // sun.awt.image.MultiResolutionImage
    public Image getResolutionVariant(int i2, int i3) {
        return (i2 > getWidth() || i3 > getHeight()) ? this.resolutionVariant : this;
    }

    public Image getResolutionVariant() {
        return this.resolutionVariant;
    }

    @Override // sun.awt.image.MultiResolutionImage
    public List<Image> getResolutionVariants() {
        return Arrays.asList(this, this.resolutionVariant);
    }

    /* loaded from: rt.jar:sun/awt/image/MultiResolutionToolkitImage$ObserverCache.class */
    private static class ObserverCache {
        static final SoftCache INSTANCE = new SoftCache();

        private ObserverCache() {
        }
    }

    public static ImageObserver getResolutionVariantObserver(Image image, ImageObserver imageObserver, int i2, int i3, int i4, int i5) {
        return getResolutionVariantObserver(image, imageObserver, i2, i3, i4, i5, false);
    }

    public static ImageObserver getResolutionVariantObserver(Image image, ImageObserver imageObserver, int i2, int i3, int i4, int i5, boolean z2) {
        ImageObserver imageObserver2;
        if (imageObserver == null) {
            return null;
        }
        synchronized (ObserverCache.INSTANCE) {
            ImageObserver imageObserver3 = (ImageObserver) ObserverCache.INSTANCE.get(imageObserver);
            if (imageObserver3 == null) {
                imageObserver3 = (image2, i6, i7, i8, i9, i10) -> {
                    if ((i6 & 57) != 0) {
                        i9 = (i9 + 1) / 2;
                    }
                    if ((i6 & 58) != 0) {
                        i10 = (i10 + 1) / 2;
                    }
                    if ((i6 & 56) != 0) {
                        i7 /= 2;
                        i8 /= 2;
                    }
                    if (z2) {
                        i6 &= ((ToolkitImage) image).getImageRep().check(null);
                    }
                    return imageObserver.imageUpdate(image, i6, i7, i8, i9, i10);
                };
                ObserverCache.INSTANCE.put(imageObserver, imageObserver3);
            }
            imageObserver2 = imageObserver3;
        }
        return imageObserver2;
    }
}

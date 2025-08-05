package sun.awt.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/* loaded from: rt.jar:sun/awt/image/AbstractMultiResolutionImage.class */
public abstract class AbstractMultiResolutionImage extends Image implements MultiResolutionImage {
    protected abstract Image getBaseImage();

    @Override // java.awt.Image
    public int getWidth(ImageObserver imageObserver) {
        return getBaseImage().getWidth(null);
    }

    @Override // java.awt.Image
    public int getHeight(ImageObserver imageObserver) {
        return getBaseImage().getHeight(null);
    }

    @Override // java.awt.Image
    public ImageProducer getSource() {
        return getBaseImage().getSource();
    }

    @Override // java.awt.Image
    public Graphics getGraphics() {
        return getBaseImage().getGraphics();
    }

    @Override // java.awt.Image
    public Object getProperty(String str, ImageObserver imageObserver) {
        return getBaseImage().getProperty(str, imageObserver);
    }
}

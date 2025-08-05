package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/InlineImageStreamReference.class */
public class InlineImageStreamReference extends ImageReference {
    private static final Logger logger = Logger.getLogger(InlineImageStreamReference.class.toString());

    public InlineImageStreamReference(ImageStream imageStream, Color fillColor, Resources resources, int iamgeIndex, Page page) {
        super(imageStream, fillColor, resources, iamgeIndex, page);
        ImagePool imagePool = imageStream.getLibrary().getImagePool();
        if (useProxy && imagePool.get(this.reference) == null) {
            this.futureTask = new FutureTask<>(this);
            Library.executeImage(this.futureTask);
        } else if (!useProxy && imagePool.get(this.reference) == null) {
            this.image = call();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getWidth() {
        return this.imageStream.getWidth();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getHeight() {
        return this.imageStream.getHeight();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public BufferedImage getImage() {
        if (this.image == null && useProxy) {
            this.image = createImage();
        } else {
            this.image = call();
        }
        return this.image;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public BufferedImage call() {
        BufferedImage image = null;
        long start = System.nanoTime();
        try {
            image = this.imageStream.getImage(this.fillColor, this.resources);
        } catch (Throwable e2) {
            logger.log(Level.WARNING, "Error loading image: " + ((Object) this.imageStream.getPObjectReference()) + " " + this.imageStream.toString(), e2);
        }
        long end = System.nanoTime();
        notifyImagePageEvents(end - start);
        return image;
    }
}

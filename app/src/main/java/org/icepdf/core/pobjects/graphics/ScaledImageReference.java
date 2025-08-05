package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.ImageUtility;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ScaledImageReference.class */
public class ScaledImageReference extends CachedImageReference {
    private static final Logger logger = Logger.getLogger(ScaledImageReference.class.toString());
    private int width;
    private int height;

    protected ScaledImageReference(ImageStream imageStream, Color fillColor, Resources resources, int imageIndex, Page page) {
        super(imageStream, fillColor, resources, imageIndex, page);
        this.width = imageStream.getWidth();
        this.height = imageStream.getHeight();
        ImagePool imagePool = imageStream.getLibrary().getImagePool();
        if (useProxy && imagePool.get(this.reference) == null) {
            this.futureTask = new FutureTask<>(this);
            Library.executeImage(this.futureTask);
        } else if (!useProxy && imagePool.get(this.reference) == null) {
            this.image = call();
        }
    }

    public ScaledImageReference(ImageReference imageReference, Color fillColor, Resources resources, int width, int height, int imageIndex, Page page) {
        super(imageReference.getImageStream(), fillColor, resources, imageIndex, page);
        this.width = width;
        this.height = height;
        if (imageReference.isImage()) {
            this.image = imageReference.getImage();
        }
        ImagePool imagePool = this.imageStream.getLibrary().getImagePool();
        if (useProxy && imagePool.get(this.reference) == null) {
            this.futureTask = new FutureTask<>(this);
            Library.executeImage(this.futureTask);
        } else if (!useProxy && imagePool.get(this.reference) == null) {
            this.image = call();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getWidth() {
        return this.width;
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getHeight() {
        return this.height;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public BufferedImage call() {
        BufferedImage scaled;
        BufferedImage image = null;
        long start = System.nanoTime();
        try {
            image = this.imageStream.getImage(this.fillColor, this.resources);
            if (image != null) {
                int width = this.imageStream.getWidth();
                int height = this.imageStream.getHeight();
                double scaleFactor = 1.0d;
                if (width > 1000 && width < 1500) {
                    scaleFactor = 0.75d;
                } else if (width > 1500) {
                    scaleFactor = 0.5d;
                }
                if (scaleFactor < 1.0d) {
                    int width2 = (int) Math.ceil(width * scaleFactor);
                    int height2 = (int) Math.ceil(height * scaleFactor);
                    if (ImageUtility.hasAlpha(image)) {
                        scaled = new BufferedImage(width2, height2, 2);
                    } else {
                        scaled = new BufferedImage(width2, height2, 1);
                    }
                    Graphics2D g2 = scaled.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.drawImage(image, 0, 0, width2, height2, null);
                    g2.dispose();
                    image.flush();
                    image = scaled;
                }
            }
        } catch (Throwable th) {
            logger.warning("Error loading image: " + ((Object) this.imageStream.getPObjectReference()) + " " + this.imageStream.toString());
        }
        long end = System.nanoTime();
        notifyImagePageEvents(end - start);
        return image;
    }
}

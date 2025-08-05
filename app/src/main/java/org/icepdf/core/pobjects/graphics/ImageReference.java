package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.events.PageImageEvent;
import org.icepdf.core.events.PageLoadingEvent;
import org.icepdf.core.events.PageLoadingListener;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.ImageUtility;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Defs;
import sun.util.locale.LanguageTag;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ImageReference.class */
public abstract class ImageReference implements Callable<BufferedImage> {
    private static final Logger logger = Logger.getLogger(ImageReference.class.toString());
    protected static boolean useProxy = Defs.booleanProperty("org.icepdf.core.imageProxy", true);
    protected FutureTask<BufferedImage> futureTask;
    protected ImageStream imageStream;
    protected Color fillColor;
    protected Resources resources;
    protected BufferedImage image;
    protected Reference reference;
    protected int imageIndex;
    protected Page parentPage;

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract BufferedImage getImage();

    protected ImageReference(ImageStream imageStream, Color fillColor, Resources resources, int imageIndex, Page parentPage) {
        this.imageStream = imageStream;
        this.fillColor = fillColor;
        this.resources = resources;
        this.imageIndex = imageIndex;
        this.parentPage = parentPage;
    }

    public void drawImage(Graphics2D aG2, int aX2, int aY2, int aW2, int aH2) {
        BufferedImage image = getImage();
        if (image != null) {
            try {
                aG2.drawImage(image, aX2, aY2, aW2, aH2, null);
            } catch (Throwable th) {
                logger.warning("There was a problem painting image, falling back to scaled instance " + ((Object) this.imageStream.getPObjectReference()) + "(" + this.imageStream.getWidth() + LanguageTag.PRIVATEUSE + this.imageStream.getHeight() + ")");
                int width = image.getWidth(null);
                if (width > 1000 && width < 2000) {
                    width = 1000;
                } else if (width > 2000) {
                    width = 2000;
                }
                Image scaledImage = image.getScaledInstance(width, -1, 4);
                image.flush();
                aG2.drawImage(scaledImage, aX2, aY2, aW2, aH2, null);
                this.image = ImageUtility.createBufferedImage(scaledImage);
            }
        }
    }

    protected BufferedImage createImage() {
        try {
            if (this.futureTask != null) {
                this.image = this.futureTask.get();
            }
            if (this.image == null) {
                this.image = call();
            }
        } catch (InterruptedException e2) {
            logger.warning("Image loading interrupted");
        } catch (Exception e3) {
            logger.log(Level.FINE, "Image loading execution exception", (Throwable) e3);
        }
        return this.image;
    }

    public ImageStream getImageStream() {
        return this.imageStream;
    }

    public boolean isImage() {
        return this.image != null;
    }

    protected void notifyPageImageLoadedEvent(long duration, boolean interrupted) {
        if (this.parentPage != null) {
            PageImageEvent pageLoadingEvent = new PageImageEvent(this.parentPage, this.imageIndex, this.parentPage.getImageCount(), duration, interrupted);
            List<PageLoadingListener> pageLoadingListeners = this.parentPage.getPageLoadingListeners();
            for (int i2 = pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
                PageLoadingListener client = pageLoadingListeners.get(i2);
                client.pageImageLoaded(pageLoadingEvent);
            }
        }
    }

    protected void notifyImagePageEvents(long duration) {
        notifyPageImageLoadedEvent(duration, this.image == null);
        if (this.parentPage != null && this.imageIndex == this.parentPage.getImageCount() && this.parentPage.isPageInitialized() && this.parentPage.isPagePainted()) {
            notifyPageLoadingEnded();
        }
    }

    protected void notifyPageLoadingEnded() {
        if (this.parentPage != null) {
            PageLoadingEvent pageLoadingEvent = new PageLoadingEvent(this.parentPage, this.parentPage.isInitiated());
            List<PageLoadingListener> pageLoadingListeners = this.parentPage.getPageLoadingListeners();
            for (int i2 = pageLoadingListeners.size() - 1; i2 >= 0; i2--) {
                PageLoadingListener client = pageLoadingListeners.get(i2);
                client.pageLoadingEnded(pageLoadingEvent);
            }
        }
    }
}

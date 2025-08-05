package org.icepdf.core.pobjects.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/SmoothScaledImageReference.class */
public class SmoothScaledImageReference extends CachedImageReference {
    private static final Logger logger = Logger.getLogger(ScaledImageReference.class.toString());
    private static int maxImageWidth;
    private static int maxImageHeight;
    private int width;
    private int height;

    static {
        maxImageWidth = 7000;
        maxImageHeight = 7000;
        try {
            maxImageWidth = Integer.parseInt(Defs.sysProperty("org.icepdf.core.imageReference.smoothscaled.maxwidth", String.valueOf(maxImageWidth)));
            maxImageHeight = Integer.parseInt(Defs.sysProperty("org.icepdf.core.imageReference.smoothscaled.maxheight", String.valueOf(maxImageHeight)));
        } catch (NumberFormatException e2) {
            logger.warning("Error reading buffered scale factor");
        }
    }

    protected SmoothScaledImageReference(ImageStream imageStream, Color fillColor, Resources resources, int imageIndex, Page page) {
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

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getWidth() {
        return this.width;
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getHeight() {
        return this.height;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0034 A[Catch: Throwable -> 0x01d8, TryCatch #0 {Throwable -> 0x01d8, blocks: (B:5:0x000a, B:7:0x0024, B:13:0x0034, B:15:0x004d, B:25:0x007c, B:87:0x01a7), top: B:93:0x000a }] */
    @Override // java.util.concurrent.Callable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.image.BufferedImage call() {
        /*
            Method dump skipped, instructions count: 535
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.graphics.SmoothScaledImageReference.call():java.awt.image.BufferedImage");
    }

    private static Image getTrilinearScaledInstance(BufferedImage img, int targetWidth, int targetHeight) {
        int iw = img.getWidth();
        int ih = img.getHeight();
        Object hint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        int type = img.getTransparency() == 1 ? 1 : 2;
        while (true) {
            if (iw <= targetWidth * 2 && ih <= targetHeight * 2) {
                break;
            }
            iw = iw > targetWidth * 2 ? iw / 2 : iw;
            ih = ih > targetHeight * 2 ? ih / 2 : ih;
            img = scaleImage(img, type, hint, iw, ih);
        }
        if (iw > targetWidth) {
            int iw2 = iw / 2;
            BufferedImage img2 = scaleImage(img, type, hint, iw2, ih);
            if (iw2 < targetWidth) {
                BufferedImage img3 = scaleImage(img, type, hint, targetWidth, ih);
                img2 = scaleImage(img2, type, hint, targetWidth, ih);
                interpolate(img2, img3, iw - targetWidth, targetWidth - iw2);
            }
            img = img2;
            iw = targetWidth;
        }
        if (ih > targetHeight) {
            int ih2 = ih / 2;
            BufferedImage img22 = scaleImage(img, type, hint, iw, ih2);
            if (ih2 < targetHeight) {
                BufferedImage img4 = scaleImage(img, type, hint, iw, targetHeight);
                img22 = scaleImage(img22, type, hint, iw, targetHeight);
                interpolate(img22, img4, ih - targetHeight, targetHeight - ih2);
            }
            img = img22;
            ih = targetHeight;
        }
        if (iw < targetWidth && ih < targetHeight) {
            img = scaleImage(img, type, hint, targetWidth, targetHeight);
        }
        return img;
    }

    private static void interpolate(BufferedImage img1, BufferedImage img2, int weight1, int weight2) {
        float alpha = weight1;
        float alpha2 = alpha / (weight1 + weight2);
        Graphics2D g2 = img1.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(3, alpha2));
        g2.drawImage(img2, 0, 0, (ImageObserver) null);
        g2.dispose();
    }

    private static BufferedImage scaleImage(BufferedImage orig, int type, Object hint, int w2, int h2) {
        BufferedImage tmp = new BufferedImage(w2, h2, type);
        Graphics2D g2 = tmp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
        g2.drawImage(orig, 0, 0, w2, h2, null);
        g2.dispose();
        return tmp;
    }
}

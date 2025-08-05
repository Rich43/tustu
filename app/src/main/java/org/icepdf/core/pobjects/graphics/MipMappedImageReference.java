package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/MipMappedImageReference.class */
class MipMappedImageReference extends ImageReference {
    private ArrayList<ImageReference> images;

    protected MipMappedImageReference(ImageStream imageStream, Color fillColor, Resources resources, int imageIndex, Page page) {
        super(imageStream, fillColor, resources, imageIndex, page);
        this.images = new ArrayList<>();
        ImageReference imageReference = new ImageStreamReference(imageStream, fillColor, resources, imageIndex, page);
        this.images.add(imageReference);
        int width = imageReference.getWidth();
        int height = imageReference.getHeight();
        useProxy = false;
        while (width > 20 && height > 20) {
            width /= 2;
            height /= 2;
            imageReference = new ScaledImageReference(imageReference, fillColor, resources, width, height, imageIndex, page);
            this.images.add(imageReference);
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getWidth() {
        return this.images.get(0).getWidth();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public int getHeight() {
        return this.images.get(0).getHeight();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public BufferedImage getImage() {
        return this.images.get(0).getImage();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public void drawImage(Graphics2D aG2, int aX2, int aY2, int aW2, int aH2) {
        ImageReference imageReference = chooseImage(aG2, aX2, aY2, aW2, aH2);
        imageReference.drawImage(aG2, aX2, aY2, aW2, aH2);
    }

    private ImageReference chooseImage(Graphics2D aG2, int aX2, int aY2, int aW2, int aH2) {
        Point2D.Double in = new Point2D.Double(aX2, aY2);
        Point2D.Double p1 = new Point2D.Double();
        Point2D.Double p2 = new Point2D.Double();
        aG2.getTransform().transform(in, p1);
        in.f12394x = aW2;
        aG2.getTransform().transform(in, p2);
        int distSq1 = (int) Math.round(p1.distanceSq(p2));
        in.f12394x = aX2;
        in.f12395y = aH2;
        aG2.getTransform().transform(in, p2);
        int distSq2 = (int) Math.round(p1.distanceSq(p2));
        int maxDistSq = Math.max(distSq1, distSq2);
        int level = 0;
        ImageReference image = this.images.get(0);
        int width = image.getWidth();
        int height = image.getHeight();
        while (true) {
            int height2 = height;
            if (level >= this.images.size() - 1 || (width * width) / 4 <= maxDistSq || (height2 * height2) / 4 <= maxDistSq) {
                break;
            }
            int i2 = level;
            level++;
            image = this.images.get(i2);
            width = image.getWidth();
            height = image.getHeight();
        }
        return image;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public BufferedImage call() {
        return null;
    }
}

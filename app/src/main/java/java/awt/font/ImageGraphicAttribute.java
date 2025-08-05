package java.awt.font;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

/* loaded from: rt.jar:java/awt/font/ImageGraphicAttribute.class */
public final class ImageGraphicAttribute extends GraphicAttribute {
    private Image fImage;
    private float fImageWidth;
    private float fImageHeight;
    private float fOriginX;
    private float fOriginY;

    public ImageGraphicAttribute(Image image, int i2) {
        this(image, i2, 0.0f, 0.0f);
    }

    public ImageGraphicAttribute(Image image, int i2, float f2, float f3) {
        super(i2);
        this.fImage = image;
        this.fImageWidth = image.getWidth(null);
        this.fImageHeight = image.getHeight(null);
        this.fOriginX = f2;
        this.fOriginY = f3;
    }

    @Override // java.awt.font.GraphicAttribute
    public float getAscent() {
        return Math.max(0.0f, this.fOriginY);
    }

    @Override // java.awt.font.GraphicAttribute
    public float getDescent() {
        return Math.max(0.0f, this.fImageHeight - this.fOriginY);
    }

    @Override // java.awt.font.GraphicAttribute
    public float getAdvance() {
        return Math.max(0.0f, this.fImageWidth - this.fOriginX);
    }

    @Override // java.awt.font.GraphicAttribute
    public Rectangle2D getBounds() {
        return new Rectangle2D.Float(-this.fOriginX, -this.fOriginY, this.fImageWidth, this.fImageHeight);
    }

    @Override // java.awt.font.GraphicAttribute
    public void draw(Graphics2D graphics2D, float f2, float f3) {
        graphics2D.drawImage(this.fImage, (int) (f2 - this.fOriginX), (int) (f3 - this.fOriginY), (ImageObserver) null);
    }

    public int hashCode() {
        return this.fImage.hashCode();
    }

    public boolean equals(Object obj) {
        try {
            return equals((ImageGraphicAttribute) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public boolean equals(ImageGraphicAttribute imageGraphicAttribute) {
        if (imageGraphicAttribute == null) {
            return false;
        }
        if (this == imageGraphicAttribute) {
            return true;
        }
        if (this.fOriginX != imageGraphicAttribute.fOriginX || this.fOriginY != imageGraphicAttribute.fOriginY || getAlignment() != imageGraphicAttribute.getAlignment() || !this.fImage.equals(imageGraphicAttribute.fImage)) {
            return false;
        }
        return true;
    }
}

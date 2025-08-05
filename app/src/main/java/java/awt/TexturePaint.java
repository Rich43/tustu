package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/* loaded from: rt.jar:java/awt/TexturePaint.class */
public class TexturePaint implements Paint {
    BufferedImage bufImg;
    double tx;
    double ty;
    double sx;
    double sy;

    public TexturePaint(BufferedImage bufferedImage, Rectangle2D rectangle2D) {
        this.bufImg = bufferedImage;
        this.tx = rectangle2D.getX();
        this.ty = rectangle2D.getY();
        this.sx = rectangle2D.getWidth() / this.bufImg.getWidth();
        this.sy = rectangle2D.getHeight() / this.bufImg.getHeight();
    }

    public BufferedImage getImage() {
        return this.bufImg;
    }

    public Rectangle2D getAnchorRect() {
        return new Rectangle2D.Double(this.tx, this.ty, this.sx * this.bufImg.getWidth(), this.sy * this.bufImg.getHeight());
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints) {
        AffineTransform affineTransform2;
        if (affineTransform == null) {
            affineTransform2 = new AffineTransform();
        } else {
            affineTransform2 = (AffineTransform) affineTransform.clone();
        }
        affineTransform2.translate(this.tx, this.ty);
        affineTransform2.scale(this.sx, this.sy);
        return TexturePaintContext.getContext(this.bufImg, affineTransform2, renderingHints, rectangle);
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return this.bufImg.getColorModel().getTransparency();
    }
}

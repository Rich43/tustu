package java.awt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/font/ShapeGraphicAttribute.class */
public final class ShapeGraphicAttribute extends GraphicAttribute {
    private Shape fShape;
    private boolean fStroke;
    public static final boolean STROKE = true;
    public static final boolean FILL = false;
    private Rectangle2D fShapeBounds;

    public ShapeGraphicAttribute(Shape shape, int i2, boolean z2) {
        super(i2);
        this.fShape = shape;
        this.fStroke = z2;
        this.fShapeBounds = this.fShape.getBounds2D();
    }

    @Override // java.awt.font.GraphicAttribute
    public float getAscent() {
        return (float) Math.max(0.0d, -this.fShapeBounds.getMinY());
    }

    @Override // java.awt.font.GraphicAttribute
    public float getDescent() {
        return (float) Math.max(0.0d, this.fShapeBounds.getMaxY());
    }

    @Override // java.awt.font.GraphicAttribute
    public float getAdvance() {
        return (float) Math.max(0.0d, this.fShapeBounds.getMaxX());
    }

    @Override // java.awt.font.GraphicAttribute
    public void draw(Graphics2D graphics2D, float f2, float f3) {
        graphics2D.translate((int) f2, (int) f3);
        try {
            if (this.fStroke) {
                graphics2D.draw(this.fShape);
            } else {
                graphics2D.fill(this.fShape);
            }
        } finally {
            graphics2D.translate(-((int) f2), -((int) f3));
        }
    }

    @Override // java.awt.font.GraphicAttribute
    public Rectangle2D getBounds() {
        Rectangle2D.Float r0 = new Rectangle2D.Float();
        r0.setRect(this.fShapeBounds);
        if (this.fStroke) {
            r0.width += 1.0f;
            r0.height += 1.0f;
        }
        return r0;
    }

    @Override // java.awt.font.GraphicAttribute
    public Shape getOutline(AffineTransform affineTransform) {
        return affineTransform == null ? this.fShape : affineTransform.createTransformedShape(this.fShape);
    }

    public int hashCode() {
        return this.fShape.hashCode();
    }

    public boolean equals(Object obj) {
        try {
            return equals((ShapeGraphicAttribute) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public boolean equals(ShapeGraphicAttribute shapeGraphicAttribute) {
        if (shapeGraphicAttribute == null) {
            return false;
        }
        if (this == shapeGraphicAttribute) {
            return true;
        }
        if (this.fStroke != shapeGraphicAttribute.fStroke || getAlignment() != shapeGraphicAttribute.getAlignment() || !this.fShape.equals(shapeGraphicAttribute.fShape)) {
            return false;
        }
        return true;
    }
}

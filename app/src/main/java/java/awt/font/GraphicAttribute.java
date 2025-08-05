package java.awt.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/font/GraphicAttribute.class */
public abstract class GraphicAttribute {
    private int fAlignment;
    public static final int TOP_ALIGNMENT = -1;
    public static final int BOTTOM_ALIGNMENT = -2;
    public static final int ROMAN_BASELINE = 0;
    public static final int CENTER_BASELINE = 1;
    public static final int HANGING_BASELINE = 2;

    public abstract float getAscent();

    public abstract float getDescent();

    public abstract float getAdvance();

    public abstract void draw(Graphics2D graphics2D, float f2, float f3);

    protected GraphicAttribute(int i2) {
        if (i2 < -2 || i2 > 2) {
            throw new IllegalArgumentException("bad alignment");
        }
        this.fAlignment = i2;
    }

    public Rectangle2D getBounds() {
        float ascent = getAscent();
        return new Rectangle2D.Float(0.0f, -ascent, getAdvance(), ascent + getDescent());
    }

    public Shape getOutline(AffineTransform affineTransform) {
        Shape bounds = getBounds();
        if (affineTransform != null) {
            bounds = affineTransform.createTransformedShape(bounds);
        }
        return bounds;
    }

    public final int getAlignment() {
        return this.fAlignment;
    }

    public GlyphJustificationInfo getJustificationInfo() {
        float advance = getAdvance();
        return new GlyphJustificationInfo(advance, false, 2, advance / 3.0f, advance / 3.0f, false, 1, 0.0f, 0.0f);
    }
}

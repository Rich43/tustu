package java.awt.font;

import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/font/GlyphMetrics.class */
public final class GlyphMetrics {
    private boolean horizontal;
    private float advanceX;
    private float advanceY;
    private Rectangle2D.Float bounds;
    private byte glyphType;
    public static final byte STANDARD = 0;
    public static final byte LIGATURE = 1;
    public static final byte COMBINING = 2;
    public static final byte COMPONENT = 3;
    public static final byte WHITESPACE = 4;

    public GlyphMetrics(float f2, Rectangle2D rectangle2D, byte b2) {
        this.horizontal = true;
        this.advanceX = f2;
        this.advanceY = 0.0f;
        this.bounds = new Rectangle2D.Float();
        this.bounds.setRect(rectangle2D);
        this.glyphType = b2;
    }

    public GlyphMetrics(boolean z2, float f2, float f3, Rectangle2D rectangle2D, byte b2) {
        this.horizontal = z2;
        this.advanceX = f2;
        this.advanceY = f3;
        this.bounds = new Rectangle2D.Float();
        this.bounds.setRect(rectangle2D);
        this.glyphType = b2;
    }

    public float getAdvance() {
        return this.horizontal ? this.advanceX : this.advanceY;
    }

    public float getAdvanceX() {
        return this.advanceX;
    }

    public float getAdvanceY() {
        return this.advanceY;
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(this.bounds.f12404x, this.bounds.f12405y, this.bounds.width, this.bounds.height);
    }

    public float getLSB() {
        return this.horizontal ? this.bounds.f12404x : this.bounds.f12405y;
    }

    public float getRSB() {
        return this.horizontal ? (this.advanceX - this.bounds.f12404x) - this.bounds.width : (this.advanceY - this.bounds.f12405y) - this.bounds.height;
    }

    public int getType() {
        return this.glyphType;
    }

    public boolean isStandard() {
        return (this.glyphType & 3) == 0;
    }

    public boolean isLigature() {
        return (this.glyphType & 3) == 1;
    }

    public boolean isCombining() {
        return (this.glyphType & 3) == 2;
    }

    public boolean isComponent() {
        return (this.glyphType & 3) == 3;
    }

    public boolean isWhitespace() {
        return (this.glyphType & 4) == 4;
    }
}

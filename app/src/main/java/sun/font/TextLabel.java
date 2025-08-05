package sun.font;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/TextLabel.class */
public abstract class TextLabel {
    public abstract Rectangle2D getVisualBounds(float f2, float f3);

    public abstract Rectangle2D getLogicalBounds(float f2, float f3);

    public abstract Rectangle2D getAlignBounds(float f2, float f3);

    public abstract Rectangle2D getItalicBounds(float f2, float f3);

    public abstract Shape getOutline(float f2, float f3);

    public abstract void draw(Graphics2D graphics2D, float f2, float f3);

    public Rectangle2D getVisualBounds() {
        return getVisualBounds(0.0f, 0.0f);
    }

    public Rectangle2D getLogicalBounds() {
        return getLogicalBounds(0.0f, 0.0f);
    }

    public Rectangle2D getAlignBounds() {
        return getAlignBounds(0.0f, 0.0f);
    }

    public Rectangle2D getItalicBounds() {
        return getItalicBounds(0.0f, 0.0f);
    }

    public Shape getOutline() {
        return getOutline(0.0f, 0.0f);
    }

    public void draw(Graphics2D graphics2D) {
        draw(graphics2D, 0.0f, 0.0f);
    }
}

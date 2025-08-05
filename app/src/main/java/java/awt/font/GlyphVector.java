package java.awt.font;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/font/GlyphVector.class */
public abstract class GlyphVector implements Cloneable {
    public static final int FLAG_HAS_TRANSFORMS = 1;
    public static final int FLAG_HAS_POSITION_ADJUSTMENTS = 2;
    public static final int FLAG_RUN_RTL = 4;
    public static final int FLAG_COMPLEX_GLYPHS = 8;
    public static final int FLAG_MASK = 15;

    public abstract Font getFont();

    public abstract FontRenderContext getFontRenderContext();

    public abstract void performDefaultLayout();

    public abstract int getNumGlyphs();

    public abstract int getGlyphCode(int i2);

    public abstract int[] getGlyphCodes(int i2, int i3, int[] iArr);

    public abstract Rectangle2D getLogicalBounds();

    public abstract Rectangle2D getVisualBounds();

    public abstract Shape getOutline();

    public abstract Shape getOutline(float f2, float f3);

    public abstract Shape getGlyphOutline(int i2);

    public abstract Point2D getGlyphPosition(int i2);

    public abstract void setGlyphPosition(int i2, Point2D point2D);

    public abstract AffineTransform getGlyphTransform(int i2);

    public abstract void setGlyphTransform(int i2, AffineTransform affineTransform);

    public abstract float[] getGlyphPositions(int i2, int i3, float[] fArr);

    public abstract Shape getGlyphLogicalBounds(int i2);

    public abstract Shape getGlyphVisualBounds(int i2);

    public abstract GlyphMetrics getGlyphMetrics(int i2);

    public abstract GlyphJustificationInfo getGlyphJustificationInfo(int i2);

    public abstract boolean equals(GlyphVector glyphVector);

    public int getGlyphCharIndex(int i2) {
        return i2;
    }

    public int[] getGlyphCharIndices(int i2, int i3, int[] iArr) {
        if (iArr == null) {
            iArr = new int[i3];
        }
        int i4 = 0;
        int i5 = i2;
        while (i4 < i3) {
            iArr[i4] = getGlyphCharIndex(i5);
            i4++;
            i5++;
        }
        return iArr;
    }

    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        Rectangle2D visualBounds = getVisualBounds();
        int iFloor = (int) Math.floor(visualBounds.getX() + f2);
        int iFloor2 = (int) Math.floor(visualBounds.getY() + f3);
        return new Rectangle(iFloor, iFloor2, ((int) Math.ceil(visualBounds.getMaxX() + f2)) - iFloor, ((int) Math.ceil(visualBounds.getMaxY() + f3)) - iFloor2);
    }

    public Shape getGlyphOutline(int i2, float f2, float f3) {
        return AffineTransform.getTranslateInstance(f2, f3).createTransformedShape(getGlyphOutline(i2));
    }

    public int getLayoutFlags() {
        return 0;
    }

    public Rectangle getGlyphPixelBounds(int i2, FontRenderContext fontRenderContext, float f2, float f3) {
        Rectangle2D bounds2D = getGlyphVisualBounds(i2).getBounds2D();
        int iFloor = (int) Math.floor(bounds2D.getX() + f2);
        int iFloor2 = (int) Math.floor(bounds2D.getY() + f3);
        return new Rectangle(iFloor, iFloor2, ((int) Math.ceil(bounds2D.getMaxX() + f2)) - iFloor, ((int) Math.ceil(bounds2D.getMaxY() + f3)) - iFloor2);
    }
}

package sun.font;

import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/FontStrike.class */
public abstract class FontStrike {
    protected FontStrikeDisposer disposer;
    protected FontStrikeDesc desc;
    protected StrikeMetrics strikeMetrics;
    protected boolean algoStyle = false;
    protected float boldness = 1.0f;
    protected float italic = 0.0f;

    public abstract int getNumGlyphs();

    abstract StrikeMetrics getFontMetrics();

    abstract void getGlyphImagePtrs(int[] iArr, long[] jArr, int i2);

    abstract long getGlyphImagePtr(int i2);

    abstract void getGlyphImageBounds(int i2, Point2D.Float r2, Rectangle rectangle);

    abstract Point2D.Float getGlyphMetrics(int i2);

    abstract Point2D.Float getCharMetrics(char c2);

    abstract float getGlyphAdvance(int i2);

    abstract float getCodePointAdvance(int i2);

    abstract Rectangle2D.Float getGlyphOutlineBounds(int i2);

    abstract GeneralPath getGlyphOutline(int i2, float f2, float f3);

    abstract GeneralPath getGlyphVectorOutline(int[] iArr, float f2, float f3);
}

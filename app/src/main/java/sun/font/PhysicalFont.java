package sun.font;

import java.awt.FontFormatException;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/PhysicalFont.class */
public abstract class PhysicalFont extends Font2D {
    protected String platName;
    protected Object nativeNames;

    abstract StrikeMetrics getFontMetrics(long j2);

    abstract float getGlyphAdvance(long j2, int i2);

    abstract void getGlyphMetrics(long j2, int i2, Point2D.Float r4);

    abstract long getGlyphImage(long j2, int i2);

    abstract Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2);

    abstract GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3);

    abstract GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3);

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && ((Font2D) obj).fullName.equals(this.fullName);
    }

    public int hashCode() {
        return this.fullName.hashCode();
    }

    PhysicalFont(String str, Object obj) throws FontFormatException {
        this.handle = new Font2DHandle(this);
        this.platName = str;
        this.nativeNames = obj;
    }

    protected PhysicalFont() {
        this.handle = new Font2DHandle(this);
    }

    Point2D.Float getGlyphPoint(long j2, int i2, int i3) {
        return new Point2D.Float();
    }
}

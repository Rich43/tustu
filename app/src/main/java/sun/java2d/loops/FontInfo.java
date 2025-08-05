package sun.java2d.loops;

import java.awt.Font;
import sun.font.Font2D;
import sun.font.FontStrike;

/* loaded from: rt.jar:sun/java2d/loops/FontInfo.class */
public class FontInfo implements Cloneable {
    public Font font;
    public Font2D font2D;
    public FontStrike fontStrike;
    public double[] devTx;
    public double[] glyphTx;
    public int pixelHeight;
    public float originX;
    public float originY;
    public int aaHint;
    public boolean lcdRGBOrder;
    public boolean lcdSubPixPos;

    public String mtx(double[] dArr) {
        return "[" + dArr[0] + ", " + dArr[1] + ", " + dArr[2] + ", " + dArr[3] + "]";
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String toString() {
        return "FontInfo[font=" + ((Object) this.font) + ", devTx=" + mtx(this.devTx) + ", glyphTx=" + mtx(this.glyphTx) + ", pixelHeight=" + this.pixelHeight + ", origin=(" + this.originX + "," + this.originY + "), aaHint=" + this.aaHint + ", lcdRGBOrder=" + (this.lcdRGBOrder ? "RGB" : "BGR") + "lcdSubPixPos=" + this.lcdSubPixPos + "]";
    }
}

package java.awt;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.CharacterIterator;

/* loaded from: rt.jar:java/awt/FontMetrics.class */
public abstract class FontMetrics implements Serializable {
    private static final FontRenderContext DEFAULT_FRC;
    protected Font font;
    private static final long serialVersionUID = 1681126225205050147L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        DEFAULT_FRC = new FontRenderContext((AffineTransform) null, false, false);
    }

    protected FontMetrics(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    public FontRenderContext getFontRenderContext() {
        return DEFAULT_FRC;
    }

    public int getLeading() {
        return 0;
    }

    public int getAscent() {
        return this.font.getSize();
    }

    public int getDescent() {
        return 0;
    }

    public int getHeight() {
        return getLeading() + getAscent() + getDescent();
    }

    public int getMaxAscent() {
        return getAscent();
    }

    public int getMaxDescent() {
        return getDescent();
    }

    @Deprecated
    public int getMaxDecent() {
        return getMaxDescent();
    }

    public int getMaxAdvance() {
        return -1;
    }

    public int charWidth(int i2) {
        if (!Character.isValidCodePoint(i2)) {
            i2 = 65535;
        }
        if (i2 < 256) {
            return getWidths()[i2];
        }
        char[] cArr = new char[2];
        return charsWidth(cArr, 0, Character.toChars(i2, cArr, 0));
    }

    public int charWidth(char c2) {
        if (c2 < 256) {
            return getWidths()[c2];
        }
        return charsWidth(new char[]{c2}, 0, 1);
    }

    public int stringWidth(String str) {
        int length = str.length();
        char[] cArr = new char[length];
        str.getChars(0, length, cArr, 0);
        return charsWidth(cArr, 0, length);
    }

    public int charsWidth(char[] cArr, int i2, int i3) {
        return stringWidth(new String(cArr, i2, i3));
    }

    public int bytesWidth(byte[] bArr, int i2, int i3) {
        return stringWidth(new String(bArr, 0, i2, i3));
    }

    public int[] getWidths() {
        int[] iArr = new int[256];
        char c2 = 0;
        while (true) {
            char c3 = c2;
            if (c3 < 256) {
                iArr[c3] = charWidth(c3);
                c2 = (char) (c3 + 1);
            } else {
                return iArr;
            }
        }
    }

    public boolean hasUniformLineMetrics() {
        return this.font.hasUniformLineMetrics();
    }

    public LineMetrics getLineMetrics(String str, Graphics graphics) {
        return this.font.getLineMetrics(str, myFRC(graphics));
    }

    public LineMetrics getLineMetrics(String str, int i2, int i3, Graphics graphics) {
        return this.font.getLineMetrics(str, i2, i3, myFRC(graphics));
    }

    public LineMetrics getLineMetrics(char[] cArr, int i2, int i3, Graphics graphics) {
        return this.font.getLineMetrics(cArr, i2, i3, myFRC(graphics));
    }

    public LineMetrics getLineMetrics(CharacterIterator characterIterator, int i2, int i3, Graphics graphics) {
        return this.font.getLineMetrics(characterIterator, i2, i3, myFRC(graphics));
    }

    public Rectangle2D getStringBounds(String str, Graphics graphics) {
        return this.font.getStringBounds(str, myFRC(graphics));
    }

    public Rectangle2D getStringBounds(String str, int i2, int i3, Graphics graphics) {
        return this.font.getStringBounds(str, i2, i3, myFRC(graphics));
    }

    public Rectangle2D getStringBounds(char[] cArr, int i2, int i3, Graphics graphics) {
        return this.font.getStringBounds(cArr, i2, i3, myFRC(graphics));
    }

    public Rectangle2D getStringBounds(CharacterIterator characterIterator, int i2, int i3, Graphics graphics) {
        return this.font.getStringBounds(characterIterator, i2, i3, myFRC(graphics));
    }

    public Rectangle2D getMaxCharBounds(Graphics graphics) {
        return this.font.getMaxCharBounds(myFRC(graphics));
    }

    private FontRenderContext myFRC(Graphics graphics) {
        if (graphics instanceof Graphics2D) {
            return ((Graphics2D) graphics).getFontRenderContext();
        }
        return DEFAULT_FRC;
    }

    public String toString() {
        return getClass().getName() + "[font=" + ((Object) getFont()) + "ascent=" + getAscent() + ", descent=" + getDescent() + ", height=" + getHeight() + "]";
    }
}

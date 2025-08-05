package sun.awt.windows;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/awt/windows/WFontMetrics.class */
final class WFontMetrics extends FontMetrics {
    int[] widths;
    int ascent;
    int descent;
    int leading;
    int height;
    int maxAscent;
    int maxDescent;
    int maxHeight;
    int maxAdvance;
    static Hashtable table;

    @Override // java.awt.FontMetrics
    public native int stringWidth(String str);

    @Override // java.awt.FontMetrics
    public native int charsWidth(char[] cArr, int i2, int i3);

    @Override // java.awt.FontMetrics
    public native int bytesWidth(byte[] bArr, int i2, int i3);

    native void init();

    private static native void initIDs();

    static {
        initIDs();
        table = new Hashtable();
    }

    public WFontMetrics(Font font) {
        super(font);
        init();
    }

    @Override // java.awt.FontMetrics
    public int getLeading() {
        return this.leading;
    }

    @Override // java.awt.FontMetrics
    public int getAscent() {
        return this.ascent;
    }

    @Override // java.awt.FontMetrics
    public int getDescent() {
        return this.descent;
    }

    @Override // java.awt.FontMetrics
    public int getHeight() {
        return this.height;
    }

    @Override // java.awt.FontMetrics
    public int getMaxAscent() {
        return this.maxAscent;
    }

    @Override // java.awt.FontMetrics
    public int getMaxDescent() {
        return this.maxDescent;
    }

    @Override // java.awt.FontMetrics
    public int getMaxAdvance() {
        return this.maxAdvance;
    }

    @Override // java.awt.FontMetrics
    public int[] getWidths() {
        return this.widths;
    }

    static FontMetrics getFontMetrics(Font font) {
        FontMetrics fontMetrics = (FontMetrics) table.get(font);
        if (fontMetrics == null) {
            Hashtable hashtable = table;
            WFontMetrics wFontMetrics = new WFontMetrics(font);
            fontMetrics = wFontMetrics;
            hashtable.put(font, wFontMetrics);
        }
        return fontMetrics;
    }
}
